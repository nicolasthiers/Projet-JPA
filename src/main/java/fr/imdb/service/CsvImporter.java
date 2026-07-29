package fr.imdb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

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

                importService.importerPays(nom, url);
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

                importService.importerActeur(idImdb, identite, dateDeNaissance, lieuDeNaissance, taille, url);
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

                importService.importerRealisateur(idImdb, identite, dateDeNaissance, lieuDeNaissance, url);
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

                importService.importerFilm(idImdb, nom, annee, rating, url, lieuDeTournage, genre, langue, resume, pays);
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

                importService.ajouterRealisateurAuFilm(film, realisateur);
            }
        }
    }

}
