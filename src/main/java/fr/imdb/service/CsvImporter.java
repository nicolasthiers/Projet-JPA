package fr.imdb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * Lit les fichiers CSV du jeu de données (encodage UTF-8, séparateur {@code ;},
 * première ligne d'en-tête ignorée) et délègue la création des entités à
 * {@link ImportService}, ligne par ligne.
 * <p>
 * Chaque méthode {@code importerXxx} est tolérante aux erreurs individuelles : une
 * ligne en échec est journalisée sur {@code System.err} sans interrompre la lecture
 * du fichier. L'ordre d'appel attendu (respecter les dépendances entre entités) est
 * illustré par {@link fr.imdb.app.ImportApp}.
 */
public class CsvImporter {

    private final ImportService importService;

    /**
     * @param importService service utilisé pour persister les entités importées
     */
    public CsvImporter(ImportService importService) {
        this.importService = importService;
    }

    /**
     * Importe les pays depuis un CSV au format {@code NOM;URL}.
     *
     * @param cheminFichier chemin du fichier CSV
     * @throws IOException si le fichier ne peut pas être lu
     */
    public void importerPays(String cheminFichier) throws IOException {
        Path path = Path.of(cheminFichier);

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String ligne = reader.readLine();

            while ((ligne = reader.readLine()) != null) {
                if (ligne.isBlank()) continue;

                String[] champs = ligne.split(";", -1);

                String nom = champs[0].trim();
                String url = champs[1].trim();

                try {
                    importService.importerPays(nom, url);
                } catch (Exception e) {
                    System.err.println("Erreur import pays " + nom + " : " + e.getMessage());
                }

            }
        }
    }

    /**
     * Importe les acteurs depuis un CSV au format
     * {@code ID IMDB;IDENTITE;DATE NAISSANCE;LIEU NAISSANCE;TAILLE;URL}.
     *
     * @param cheminFichier chemin du fichier CSV
     * @throws IOException si le fichier ne peut pas être lu
     */
    public void importerActeur(String cheminFichier) throws IOException {
        Path path = Path.of(cheminFichier);

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String ligne = reader.readLine();

            while ((ligne = reader.readLine()) != null) {
                if (ligne.isBlank()) continue;

                String[] champs = ligne.split(";", -1);

                String idImdb = champs[0].trim();
                String identite = champs[1].trim();
                String dateDeNaissance = champs[2].trim();
                String lieuDeNaissance = champs[3].trim();
                String taille = champs[4].trim();
                String url = champs[5].trim();

                try {
                    importService.importerActeur(idImdb, identite, dateDeNaissance, lieuDeNaissance, taille, url);
                } catch (Exception e) {
                    System.err.println("Erreur import acteur " + idImdb + " : " + e.getMessage());
                }
            }
        }
    }

    /**
     * Importe les réalisateurs depuis un CSV au format
     * {@code ID;IDENTITE;DATE NAISSANCE;LIEU NAISSANCE;URL}.
     *
     * @param cheminFichier chemin du fichier CSV
     * @throws IOException si le fichier ne peut pas être lu
     */
    public void importerRealisateur(String cheminFichier) throws IOException {
        Path path = Path.of(cheminFichier);

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String ligne = reader.readLine();

            while ((ligne = reader.readLine()) != null) {
                if (ligne.isBlank()) continue;

                String[] champs = ligne.split(";", -1);

                String idImdb = champs[0].trim();
                String identite = champs[1].trim();
                String dateDeNaissance = champs[2].trim();
                String lieuDeNaissance = champs[3].trim();
                String url = champs[4].trim();


                try {
                    importService.importerRealisateur(idImdb, identite, dateDeNaissance, lieuDeNaissance, url);
                } catch (Exception e) {
                    System.err.println("Erreur import realisateur " + idImdb + " : " + e.getMessage());
                }
            }
        }
    }

    /**
     * Importe les films depuis un CSV au format
     * {@code ID IMDB;NOM;ANNEE;RATING;URL;LIEU TOURNAGE;GENRES;LANGUE;RESUME;...;PAYS}.
     * <p>
     * Le pays est toujours le dernier champ de la ligne et le résumé correspond à tous
     * les champs restants entre l'index 8 (inclus) et le pays (exclu), rejoints par
     * {@code ";"} — ceci afin de tolérer des points-virgules à l'intérieur même du résumé.
     *
     * @param cheminFichier chemin du fichier CSV
     * @throws IOException si le fichier ne peut pas être lu
     */
    public void importerFilm(String cheminFichier) throws IOException {
        Path path = Path.of(cheminFichier);

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String ligne = reader.readLine();

            while ((ligne = reader.readLine()) != null) {
                if (ligne.isBlank()) continue;

                String[] champs = ligne.split(";", -1);

                String idImdb = champs[0].trim();
                String nom = champs[1].trim();
                String annee = champs[2].trim();
                String rating = champs[3].trim();
                String url = champs[4].trim();
                String lieuDeTournage = champs[5].trim();
                String genre = champs[6].trim();
                String langue = champs[7].trim();

                // pays = dernier champ du tableau
                String pays = champs[champs.length - 1].trim();

                // resume = tout ce qui reste entre l'index 8 et pays (exclu), rejoint avec ";"
                StringBuilder resumeBuilder = new StringBuilder();
                for (int i = 8; i < champs.length - 1; i++) {
                    if (i > 8) resumeBuilder.append(";");
                    resumeBuilder.append(champs[i]);
                }
                String resume = resumeBuilder.toString().trim();

                try {
                    importService.importerFilm(idImdb, nom, annee, rating, url, lieuDeTournage, genre, langue, resume, pays);
                } catch (Exception e) {
                    System.err.println("Erreur import film " + idImdb + " : " + e.getMessage());
                }
            }
        }
    }


    /**
     * Rattache des réalisateurs à des films depuis un CSV au format
     * {@code FILM;ID REALISATEUR}. Le film et le réalisateur doivent déjà avoir été
     * importés (voir {@link #importerFilm} et {@link #importerRealisateur}).
     *
     * @param cheminFichier chemin du fichier CSV
     * @throws IOException si le fichier ne peut pas être lu
     */
    public void importerFilmRealisateur(String cheminFichier) throws IOException {
        Path path = Path.of(cheminFichier);

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String ligne = reader.readLine();

            while ((ligne = reader.readLine()) != null) {
                if (ligne.isBlank()) continue;

                String[] champs = ligne.split(";", -1);

                String film = champs[0].trim();
                String realisateur = champs[1].trim();


                try {
                    importService.ajouterRealisateurAuFilm(film, realisateur);
                } catch (Exception e) {
                    System.err.println("Erreur import film_realisateur " + film + " : " + e.getMessage());
                }
            }
        }
    }

    /**
     * Importe les rôles (interprétations d'acteurs) depuis deux CSV :
     * <ul>
     *     <li>{@code cheminCastingPrincipal}, format {@code FILM;ID ACTEUR}, qui liste les
     *     couples film/acteur faisant partie du casting principal ;</li>
     *     <li>{@code cheminRoles}, format {@code FILM;ID ACTEUR;PERSONNAGE}, qui liste tous
     *     les rôles à importer.</li>
     * </ul>
     * Le fichier de casting principal est chargé intégralement en mémoire au préalable
     * afin de déterminer, pour chaque rôle, la valeur de {@code estCastingPrincipal}.
     * Le film et l'acteur référencés doivent déjà avoir été importés. Un message de
     * progression est affiché tous les 500 rôles traités.
     *
     * @param cheminRoles chemin du CSV listant tous les rôles
     * @param cheminCastingPrincipal chemin du CSV listant les couples film/acteur en casting principal
     * @throws IOException si l'un des fichiers ne peut pas être lu
     */
    public void importerRoles(String cheminRoles, String cheminCastingPrincipal) throws IOException {

        Set<String> castingsPrincipaux = new HashSet<>();

        Path pathCasting = Path.of(cheminCastingPrincipal);
        try (BufferedReader reader = Files.newBufferedReader(pathCasting, StandardCharsets.UTF_8)) {
            String ligne = reader.readLine();

            while ((ligne = reader.readLine()) != null) {
                if (ligne.isBlank()) continue;

                String[] champs = ligne.split(";", -1);
                String idFilm = champs[0].trim();
                String idActeur = champs[1].trim();

                String cle = idFilm + "|" + idActeur;
                castingsPrincipaux.add(cle);
            }
        }


        Path pathRoles = Path.of(cheminRoles);
        try (BufferedReader reader = Files.newBufferedReader(pathRoles, StandardCharsets.UTF_8)) {
            String ligne = reader.readLine();
            int compteur = 0;

            while ((ligne = reader.readLine()) != null) {
                if (ligne.isBlank()) continue;

                String[] champs = ligne.split(";", -1);
                String idFilm = champs[0].trim();
                String idActeur = champs[1].trim();
                String personnage = champs[2].trim();

                String cle = idFilm + "|" + idActeur;
                boolean estCastingPrincipal = castingsPrincipaux.contains(cle);


                try {
                    importService.ajouterRole(idFilm, idActeur, personnage, estCastingPrincipal);
                } catch (Exception e) {
                    System.err.println("Erreur import role " + idFilm + " : " + e.getMessage());
                }
                compteur++;
                if (compteur % 500 == 0) {
                    System.out.println(">>> Rôles traités : " + compteur);
                }
            }
        }
    }

}
