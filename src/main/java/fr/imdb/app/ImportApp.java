package fr.imdb.app;

import fr.imdb.service.CsvImporter;
import fr.imdb.service.ImportService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.IOException;

/**
 * Point d'entrée pour importer le jeu de données CSV (dossier {@code src/main/resources})
 * dans la base configurée par l'unité de persistance {@code projet-jpa}.
 * <p>
 * Lance les imports dans un ordre respectant les dépendances entre entités : pays,
 * acteurs, réalisateurs, films, puis les associations film-réalisateur et les rôles.
 * Les erreurs de lecture des fichiers CSV sont journalisées ; les sorties d'erreur
 * standard sont redirigées vers {@code erreurs_import.log} pour la durée de l'import.
 */
public class ImportApp {

    /**
     * @param args non utilisé
     */
    public static void main(String[] args) {

        try {
            System.setErr(new java.io.PrintStream("erreurs_import.log", "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("projet-jpa");

        EntityManager em = emf.createEntityManager();

        ImportService importService = new ImportService(em);
        CsvImporter csvImporter = new CsvImporter(importService);

        try {
            csvImporter.importerPays("src/main/resources/pays.csv");
            csvImporter.importerActeur("src/main/resources/acteurs.csv");
            csvImporter.importerRealisateur("src/main/resources/realisateurs.csv");
            csvImporter.importerFilm("src/main/resources/films.csv");
            csvImporter.importerFilmRealisateur("src/main/resources/film_realisateurs.csv");
            csvImporter.importerRoles("src/main/resources/roles.csv", "src/main/resources/castingPrincipal.csv");

            System.out.println("Import terminé avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture des fichiers CSV : " + e.getMessage());
        } finally {
            em.close();
            emf.close();
        }




    }
}
