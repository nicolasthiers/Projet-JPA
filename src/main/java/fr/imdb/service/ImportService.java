package fr.imdb.service;

import fr.imdb.dao.*;
import fr.imdb.entities.*;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportService {

    private final PaysDao paysDao;
    private final LangueDao langueDao;
    private final LieuDeNaissanceDao lieuDeNaissanceDao;
    private final LieuDeTournageDao lieuDeTournageDao;
    private final ActeurDao acteurDao;
    private final RealisateurDao realisateurDao;
    private final FilmDao filmDao;

    private final EntityManager entityManager;
    private final DateTimeFormatter dateTimeFormatter;

    public ImportService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.paysDao = new PaysDao(entityManager);
        this.langueDao = new LangueDao(entityManager);
        this.lieuDeNaissanceDao = new LieuDeNaissanceDao(entityManager);
        this.lieuDeTournageDao = new LieuDeTournageDao(entityManager);
        this.acteurDao = new ActeurDao(entityManager);
        this.realisateurDao = new RealisateurDao(entityManager);
        this.filmDao = new FilmDao(entityManager);
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH);
    }

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

    public void importerActeur(String idImdb, String identite, String dateDeNaissance, String lieuDeNaissance, String taille, String url) {
        if (idImdb == null || idImdb.isBlank()) return;
        String idNettoye = idImdb.trim();

        if (acteurDao.trouverParIdImdb(idNettoye) != null) return;

        Acteur acteur = new Acteur();
        acteur.setIdImdb(idNettoye);
        acteur.setIdentite(identite.trim());

        if (taille != null && !taille.isBlank()) {
            try {
                String tailleNettoye = taille.replaceAll("[^0-9.]", "");
                if (!tailleNettoye.isEmpty()) {
                    acteur.setTaille(new BigDecimal(tailleNettoye));
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
                String ratingNettoye = ratingStr.replaceAll("[^0-9.]", "");
                if (!ratingNettoye.isEmpty()) {
                    film.setRating(Double.parseDouble(ratingNettoye));
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

    public void ajouterRole(String idFilm, String idActeur, String personnage, boolean estCastingPrincipal) {
        if (idFilm == null || idActeur == null) return;

        Film film = filmDao.trouverParIdImdb(idFilm.trim());
        Acteur acteur = acteurDao.trouverParIdImdb(idActeur.trim());

        if (film != null && acteur != null) {
            entityManager.getTransaction().begin();

            Role role = new Role(film, acteur, personnage != null ? personnage.trim() : "", estCastingPrincipal);
            film.getRoles().add(role);

            entityManager.persist(role);
            entityManager.getTransaction().commit();
        }
    }

    public void ajouterRealisateurAuFilm(String idFilm, String idRealisateur) {
        if (idFilm == null || idRealisateur == null) return;

        Film film = filmDao.trouverParIdImdb(idFilm.trim());
        Realisateur realisateur = realisateurDao.trouverParIdImdb(idRealisateur.trim());

        if (film != null && realisateur != null) {
            entityManager.getTransaction().begin();
            film.getRealisateurs().add(realisateur);
            entityManager.getTransaction().commit();
        }
    }

    private static final Pattern ANNEE_PATTERN = Pattern.compile("\\d{4}");

    public String extraireAnnee(String anneeBrute) {
        if (anneeBrute == null) return null;

        Matcher matcher = ANNEE_PATTERN.matcher(anneeBrute);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
