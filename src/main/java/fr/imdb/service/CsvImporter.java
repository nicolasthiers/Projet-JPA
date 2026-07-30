package fr.imdb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class CsvImporter {

    private final ImportService importService;

    public CsvImporter(ImportService importService) {
        this.importService = importService;
    }

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
                String resume = champs[8].trim();
                String pays = champs[9].trim();


                try {
                    importService.importerFilm(idImdb, nom, annee, rating, url, lieuDeTournage, genre, langue, resume, pays);
                } catch (Exception e) {
                    System.err.println("Erreur import film " + idImdb + " : " + e.getMessage());
                }
            }
        }
    }


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
            }
        }
    }

}
