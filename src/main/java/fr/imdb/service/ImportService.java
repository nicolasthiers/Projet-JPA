package fr.imdb.service;

import fr.imdb.dao.*;
import fr.imdb.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service métier de l'import de données CSV vers la base.
 * <p>
 * Chaque méthode {@code importerXxx} implémente une logique "chercher ou créer" :
 * si l'entité existe déjà (par clé métier), elle est réutilisée sans doublon ;
 * sinon elle est créée, nettoyée/validée (dates, nombres, genres) puis persistée.
 * Les erreurs de parsing individuelles (date invalide, taille aberrante, ...) sont
 * journalisées sur {@code System.err} sans interrompre l'import global.
 * <p>
 * Consommé par {@link CsvImporter}, qui lit les fichiers CSV et appelle ce service
 * ligne par ligne.
 */
public class ImportService {

    private final PaysDao paysDao;
    private final LangueDao langueDao;
    private final LieuDeNaissanceDao lieuDeNaissanceDao;
    private final LieuDeTournageDao lieuDeTournageDao;
    private final ActeurDao acteurDao;
    private final RealisateurDao realisateurDao;
    private final FilmDao filmDao;
    private final RoleDao roleDao;

    private final EntityManager entityManager;
    private final DateTimeFormatter dateTimeFormatter;

    /**
     * Crée le service et ses DAOs internes à partir de l'EntityManager fourni.
     *
     * @param entityManager gestionnaire d'entités JPA partagé avec les DAOs
     */
    public ImportService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.paysDao = new PaysDao(entityManager);
        this.langueDao = new LangueDao(entityManager);
        this.lieuDeNaissanceDao = new LieuDeNaissanceDao(entityManager);
        this.lieuDeTournageDao = new LieuDeTournageDao(entityManager);
        this.acteurDao = new ActeurDao(entityManager);
        this.realisateurDao = new RealisateurDao(entityManager);
        this.filmDao = new FilmDao(entityManager);
        this.roleDao = new RoleDao(entityManager);
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH);
    }

    /**
     * Recherche un pays par son nom, ou le crée s'il n'existe pas encore.
     *
     * @param nomPays nom du pays, peut être {@code null} ou vide (auquel cas rien n'est fait)
     * @param urlPays URL IMDb associée, utilisée uniquement à la création
     * @return le pays existant ou nouvellement créé, {@code null} si {@code nomPays} est vide/null
     */
    public Pays importerPays(String nomPays, String urlPays) {
        if (nomPays == null || nomPays.isBlank()) return null;
        String nomNettoye = nomPays.trim();

        Pays pays = paysDao.trouveParNom(nomNettoye);
        if (pays == null) {
            pays = new Pays(nomNettoye, urlPays != null ? urlPays : null);
            paysDao.sauvegarder(pays);
        }
        return pays;
    }

    /**
     * Recherche un lieu de tournage par son nom, ou le crée s'il n'existe pas encore.
     *
     * @param nomLieu libellé du lieu, peut être {@code null} ou vide (auquel cas rien n'est fait)
     * @return le lieu existant ou nouvellement créé, {@code null} si {@code nomLieu} est vide/null
     */
    public LieuDeTournage importerLieuDeTournage(String nomLieu) {
        if (nomLieu == null || nomLieu.isBlank()) return null;
        String lieuNettoye = nomLieu.trim();

        LieuDeTournage lieuDeTournage = lieuDeTournageDao.trouverParNom(lieuNettoye);
        if (lieuDeTournage == null) {
            lieuDeTournage = new LieuDeTournage(lieuNettoye);
            lieuDeTournageDao.sauvegarder(lieuDeTournage);
        }
        return lieuDeTournage;
    }

    /**
     * Recherche une langue par son libellé, ou la crée si elle n'existe pas encore.
     *
     * @param libelleLangue libellé de la langue, peut être {@code null} ou vide (auquel cas rien n'est fait)
     * @return la langue existante ou nouvellement créée, {@code null} si {@code libelleLangue} est vide/null
     */
    public Langue importerLangue(String libelleLangue) {
        if (libelleLangue == null || libelleLangue.isBlank()) return null;
        String libelleNettoye = libelleLangue.trim();

        Langue langue = langueDao.trouverParLibelle(libelleNettoye);
        if (langue == null) {
            langue = new Langue(libelleNettoye);
            langueDao.sauvegarder(langue);
        }
        return langue;
    }

    /**
     * Recherche un lieu de naissance par son nom, ou le crée s'il n'existe pas encore.
     *
     * @param nomLieu libellé du lieu, peut être {@code null} ou vide (auquel cas rien n'est fait)
     * @return le lieu existant ou nouvellement créé, {@code null} si {@code nomLieu} est vide/null
     */
    public LieuDeNaissance importerLieuDeNaissance(String nomLieu) {
        if (nomLieu == null || nomLieu.isBlank()) return null;
        String lieuNettoye = nomLieu.trim();

        LieuDeNaissance lieuDeNaissance = lieuDeNaissanceDao.trouveParNom(lieuNettoye);
        if (lieuDeNaissance == null) {
            lieuDeNaissance = new LieuDeNaissance(lieuNettoye);
            lieuDeNaissanceDao.sauvegarder(lieuDeNaissance);
        }
        return lieuDeNaissance;
    }

    /**
     * Importe un acteur s'il n'existe pas déjà (déduplication par {@code idImdb}).
     * <p>
     * La date de naissance et la taille sont parsées de façon tolérante : toute valeur
     * invalide (format de date incorrect, taille non numérique ou aberrante {@literal (>= 3m)})
     * est ignorée et journalisée sur {@code System.err}, sans bloquer l'import de l'acteur.
     *
     * @param idImdb identifiant IMDb de l'acteur ; si {@code null}/vide, aucun import n'a lieu
     * @param identite nom complet de l'acteur
     * @param dateDeNaissance date de naissance au format {@code "MMMM d yyyy"} (ex. {@code "March 15 1954"}), peut être vide
     * @param lieuDeNaissance libellé du lieu de naissance, peut être vide
     * @param taille taille brute telle que lue dans le CSV (ex. {@code "1.70 m"}), peut être vide
     * @param url URL de la fiche IMDb de l'acteur
     */
    public void importerActeur(String idImdb, String identite, String dateDeNaissance, String lieuDeNaissance, String taille, String url) {
        if (idImdb == null || idImdb.isBlank()) return;
        String idNettoye = idImdb.trim();

        if (acteurDao.trouverParIdImdb(idNettoye) != null) return;

        Acteur acteur = new Acteur();
        acteur.setIdImdb(idNettoye);
        acteur.setIdentite(identite.trim());

        if (taille != null && !taille.isBlank()) {
            try {
                String tailleNettoye = taille.replace(",", ".").replaceAll("[^0-9.]", "");
                if (!tailleNettoye.isEmpty()) {
                    BigDecimal tailleParsed = new BigDecimal(tailleNettoye);
                    if (tailleParsed.compareTo(BigDecimal.valueOf(3)) < 0) { // aucune taille humaine ne dépasse 3m
                        acteur.setTaille(tailleParsed);
                    } else {
                        System.err.println("Taille aberrante ignorée : '" + taille + "' pour " + identite);
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Impossible de parser la taille : '" + taille + "' pour " + identite);
            }
        }

        if (dateDeNaissance != null && !dateDeNaissance.isBlank()) {
            try {
                acteur.setDateDeNaissance(LocalDate.parse(dateDeNaissance.trim(), dateTimeFormatter));
            } catch (DateTimeParseException e) {
                System.err.println("Erreur format date Acteur : " + dateDeNaissance);
            }
        }

        if (lieuDeNaissance != null && !lieuDeNaissance.isBlank()) {
            acteur.setLieuDeNaissance(importerLieuDeNaissance(lieuDeNaissance));
        }

        acteur.setUrl(url != null ? url.trim() : null);

        acteurDao.sauvegarder(acteur);

    }

    /**
     * Importe un réalisateur s'il n'existe pas déjà (déduplication par {@code idImdb}).
     * <p>
     * La date de naissance est parsée de façon tolérante : un format invalide est
     * ignoré et journalisé sur {@code System.err}, sans bloquer l'import.
     *
     * @param idImdb identifiant IMDb du réalisateur ; si {@code null}/vide, aucun import n'a lieu
     * @param identite nom complet du réalisateur
     * @param dateDeNaissance date de naissance au format {@code "MMMM d yyyy"}, peut être vide
     * @param lieuDeNaissance libellé du lieu de naissance, peut être vide
     * @param url URL de la fiche IMDb du réalisateur
     */
    public void importerRealisateur(String idImdb, String identite, String dateDeNaissance, String lieuDeNaissance, String url) {
        if (idImdb == null || idImdb.isBlank()) return;
        String idNettoye = idImdb.trim();

        if (realisateurDao.trouverParIdImdb(idNettoye) != null) return;

        Realisateur realisateur = new Realisateur();
        realisateur.setIdImdb(idNettoye);
        realisateur.setIdentite(identite.trim());

        if (dateDeNaissance != null && !dateDeNaissance.isBlank()) {
            try {
                realisateur.setDateDeNaissance(LocalDate.parse(dateDeNaissance.trim(), dateTimeFormatter));
            } catch (DateTimeParseException e) {
                System.err.println("Erreur format date Réalisateur : " + dateDeNaissance);
            }
        }

        if (lieuDeNaissance != null && !lieuDeNaissance.isBlank()) {
            realisateur.setLieuDeNaissance(importerLieuDeNaissance(lieuDeNaissance));
        }

        realisateur.setUrl(url != null ? url.trim() : null);

        realisateurDao.sauvegarder(realisateur);
    }

    /**
     * Importe un film s'il n'existe pas déjà (déduplication par {@code idImdb}), en
     * important/rattachant au passage son lieu de tournage, pays, langue et genres.
     * <p>
     * L'année est extraite de {@code annee} via {@link #extraireAnnee(String)} et le
     * rating est parsé de façon tolérante (valeur invalide ignorée et journalisée sur
     * {@code System.err}). Chaque genre inconnu de {@code genresStr} est silencieusement ignoré.
     *
     * @param idImdb identifiant IMDb du film ; si {@code null}/vide, aucun import n'a lieu
     * @param nom titre du film
     * @param annee année brute (peut contenir du texte autour des 4 chiffres, ex. {@code "(1981)"})
     * @param ratingStr note brute telle que lue dans le CSV, peut être vide
     * @param url URL de la fiche IMDb du film
     * @param lieuDeTournage libellé du lieu de tournage, peut être vide
     * @param genresStr genres séparés par des virgules (ex. {@code "Drama,Horror"}), peut être vide
     * @param libelleLangue libellé de la langue, peut être vide
     * @param resume résumé du film, peut être vide
     * @param nomPays nom du pays d'origine, peut être vide
     */
    public void importerFilm(String idImdb, String nom, String annee, String ratingStr, String url, String lieuDeTournage, String genresStr, String libelleLangue, String resume, String nomPays) {
        if (idImdb == null || idImdb.isBlank()) return;
        String idNettoye = idImdb.trim();

        if (filmDao.trouverParIdImdb(idNettoye) != null) return;

        Film film = new Film();
        film.setIdImdb(idNettoye);
        film.setNom(nom.trim());
        film.setAnnee(extraireAnnee(annee));
        film.setUrl(url != null ? url.trim() : null);
        film.setLieuDeTournage(importerLieuDeTournage(lieuDeTournage));
        film.setResume(resume != null ? resume.trim() : null);

        if (ratingStr != null && !ratingStr.isBlank())
            try {
                String ratingNettoye = ratingStr.replace(",", ".").replaceAll("[^0-9.]", "");
                if (!ratingNettoye.isEmpty()) {
                    film.setRating(new BigDecimal(ratingNettoye));
                }
            } catch (NumberFormatException e) {
                System.err.println("Impossible de parser le rating : '" + ratingStr + "' pour " + nom);
            }

        film.setPays(importerPays(nomPays, null));
        film.setLangue(importerLangue(libelleLangue));

        if (genresStr != null && !genresStr.isBlank()) {
            String[] pieces = genresStr.split(",");
            for (String p : pieces) {
                try {
                    Genre genre = Genre.fromLabel(p.trim());
                    film.getGenres().add(genre);
                } catch (IllegalArgumentException e) {

                }
            }
        }
        filmDao.sauvegarder(film);
    }

    /**
     * Ajoute un rôle (interprétation d'un acteur dans un film) si le film et l'acteur
     * existent déjà en base et que ce rôle n'a pas déjà été importé (déduplication par
     * triplet film/acteur/personnage, voir {@link fr.imdb.dao.RoleDao#trouverParFilmActeurPersonnage}).
     * Ne fait rien si le film ou l'acteur référencé est introuvable.
     *
     * @param idFilm identifiant IMDb du film
     * @param idActeur identifiant IMDb de l'acteur
     * @param personnage nom du personnage joué, peut être {@code null} (traité comme vide)
     * @param estCastingPrincipal {@code true} si ce rôle fait partie du casting principal
     */
    public void ajouterRole(String idFilm, String idActeur, String personnage, boolean estCastingPrincipal) {
        if (idFilm == null || idActeur == null) return;

        Film film = filmDao.trouverParIdImdb(idFilm.trim());
        Acteur acteur = acteurDao.trouverParIdImdb(idActeur.trim());

        if (film != null && acteur != null) {

            String personnageNettoye = personnage != null ? personnage.trim() : "";

            if (roleDao.trouverParFilmActeurPersonnage(idFilm.trim(), idActeur.trim(), personnageNettoye) != null) {
                return; // le rôle existe déjà, on ne le recrée pas
            }

            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();

                Role role = new Role(film, acteur, personnage != null ? personnage.trim() : "", estCastingPrincipal);
                film.getRoles().add(role);

                entityManager.persist(role);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                entityManager.clear();
                throw e;
            }
        }
    }

    /**
     * Rattache un réalisateur à un film si les deux existent déjà en base.
     * Ne fait rien si le film ou le réalisateur référencé est introuvable.
     *
     * @param idFilm identifiant IMDb du film
     * @param idRealisateur identifiant IMDb du réalisateur
     */
    public void ajouterRealisateurAuFilm(String idFilm, String idRealisateur) {
        if (idFilm == null || idRealisateur == null) return;

        Film film = filmDao.trouverParIdImdb(idFilm.trim());
        Realisateur realisateur = realisateurDao.trouverParIdImdb(idRealisateur.trim());

        if (film != null && realisateur != null) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                film.getRealisateurs().add(realisateur);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                entityManager.clear();
                throw e;
            }
        }
    }

    private static final Pattern ANNEE_PATTERN = Pattern.compile("\\d{4}");

    /**
     * Extrait les 4 premiers chiffres consécutifs trouvés dans la chaîne, correspondant
     * à une année (utile lorsque le champ CSV contient du texte additionnel).
     *
     * @param anneeBrute texte contenant potentiellement une année, peut être {@code null}
     * @return l'année sous forme de 4 chiffres, ou {@code null} si {@code anneeBrute} est
     *         {@code null} ou ne contient aucune séquence de 4 chiffres
     */
    public String extraireAnnee(String anneeBrute) {
        if (anneeBrute == null) return null;

        Matcher matcher = ANNEE_PATTERN.matcher(anneeBrute);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
