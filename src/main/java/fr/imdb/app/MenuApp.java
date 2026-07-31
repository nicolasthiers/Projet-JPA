package fr.imdb.app;

import fr.imdb.dao.ActeurDao;
import fr.imdb.dao.FilmDao;
import fr.imdb.entities.Acteur;
import fr.imdb.entities.Film;
import fr.imdb.entities.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Scanner;

/**
 * Point d'entrée du menu console permettant d'interroger la base de données de films
 * (filmographie d'un acteur, casting d'un film, films par période, films/acteurs en
 * commun, etc.) via {@link FilmDao} et {@link ActeurDao}.
 */
public class MenuApp {

    /**
     * @param args non utilisé
     */
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("projet-jpa");
        EntityManager em = emf.createEntityManager();

        FilmDao filmDao = new FilmDao(em);
        ActeurDao acteurDao = new ActeurDao(em);

        Scanner scanner = new Scanner(System.in);
        boolean continuer = true;

        while (continuer) {
            affichermenu();
            int choix = lireEntier(scanner, "votre choix : ");

            switch (choix) {
                case 1 -> optionFilmographie(scanner, filmDao);
                case 2 -> optionCasting(scanner, filmDao);
                case 3 -> optionFilmEntreAnnee(scanner, filmDao);
                case 4 -> optionFilmCommuns(scanner, filmDao);
                case 5 -> optionActeursCommuns(scanner, acteurDao);
                case 6 -> optionFilmsAnneesActeur(scanner, filmDao);
                case 7 -> continuer = false;
                default -> System.out.println("Choix invalide, réessayez.");
            }
        }

        System.out.println("Au revoir !");
        em.close();
        emf.close();

    }
    /** Affiche les options du menu principal. */
    private static void affichermenu() {
        System.out.println("""
                
                ===== MENU =====
                1. Filmographie d'un acteur
                2. Casting d'un film
                3. Films sortis entre 2 années
                4. Films communs à 2 acteurs
                5. Acteurs communs à 2 films
                6. Films entre 2 années avec un acteur donné
                7. Quitter
                =================""");
    }

    /**
     * Lit un entier saisi au clavier, en reboucle tant que la saisie n'est pas un nombre.
     *
     * @param scanner scanner de lecture de l'entrée standard
     * @param message message affiché à l'utilisateur
     * @return l'entier saisi
     */
    private static int lireEntier(Scanner scanner, String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.println("Merci d'entrer un nombre." + message);
            scanner.next();
        }
        int valeur = scanner.nextInt();
        scanner.nextLine();
        return valeur;
    }

    /** Option de menu : affiche la filmographie d'un acteur saisi au clavier. */
    private static void optionFilmographie(Scanner scanner, FilmDao filmDao) {
        System.out.print("Nom de l'acteur : ");
        String identite = scanner.nextLine();

        List<Film> films = filmDao.trouverFilmographieActeur(identite);
        if (films.isEmpty()) {
            System.out.println("Aucun film trouvé pour cet acteur.");
            return;
        }
        System.out.println("Filmographie de " + identite + " :");
        for (Film f : films) {
            System.out.println(" - "  + f.getNom() + " (" + f.getAnnee() + ")");
        }
    }

    /** Option de menu : affiche le casting d'un film saisi au clavier. */
    private static void optionCasting(Scanner scanner, FilmDao filmDao) {
        System.out.print("Nom du film : ");
        String nomFilm = scanner.nextLine();

        Film film = filmDao.trouverFilmAvecCasting(nomFilm);
        if (film == null){
            System.out.println("Film Introuvable.");
            return;
        }
        System.out.println("Casting de " + film.getNom() + " :");
        for (Role r : film.getRoles()) {
            String principal = r.isEstCastingPrincipal() ? "[Principal]" : "";
            System.out.println(" - " + r.getActeur().getIdentite() + " dans le role de " + r.getPersonnage() + principal);
        }
    }

    /** Option de menu : affiche les films sortis entre deux années saisies au clavier. */
    private static void optionFilmEntreAnnee(Scanner scanner, FilmDao filmDao) {
        System.out.print("Année de début : ");
        String debut = scanner.nextLine();
        System.out.print("Année de fin : ");
        String fin = scanner.nextLine();

        List<Film> films = filmDao.trouverFilmParAnnees(debut, fin);
        if (films.isEmpty()){
            System.out.println("Aucun film trouvé pour cette période.");
            return;
        }
        System.out.println("Voici la liste des films entre " + debut + " et " + fin + " :");
        for (Film f : films) {
            System.out.println(" - " + f.getNom() + " (" + f.getAnnee() + ")");
        }
    }

    /** Option de menu : affiche les films communs à deux acteurs saisis au clavier. */
    private static void optionFilmCommuns(Scanner scanner, FilmDao filmDao) {
        System.out.print("Nom du premier acteur : ");
        String premierActeur = scanner.nextLine();
        System.out.print("Nom du deuxième acteur : ");
        String deuxiemeActeur = scanner.nextLine();

        List<Film> films = filmDao.trouverFilmsCommunsActeurs(premierActeur, deuxiemeActeur);
        if (films.isEmpty()) {
            System.out.println("Aucun film commun trouvé.");
            return;
        }
        System.out.println("Voici la liste des films communs à " + premierActeur + " et " + deuxiemeActeur + " : ");
        for (Film f : films) {
            System.out.println(" - " + f.getNom() + " (" + f.getAnnee() + ")");
        }
    }

    /** Option de menu : affiche les acteurs communs à deux films saisis au clavier. */
    private static void optionActeursCommuns(Scanner scanner, ActeurDao acteurDao) {
        System.out.print("Nom du premier film : ");
        String premierFilm = scanner.nextLine();
        System.out.print("Nom du deuxième film : ");
        String deuxièmeFIlm = scanner.nextLine();

        List<Acteur> acteurs = acteurDao.trouverActeursCommuns(premierFilm, deuxièmeFIlm);
        if (acteurs.isEmpty()) {
            System.out.println("Aucun acteur en commun entre " + premierFilm + " et " + deuxièmeFIlm + ".");
            return;
        }
        System.out.println("Acteur communs à " + premierFilm + " et " + deuxièmeFIlm + " :");
        for (Acteur a : acteurs) {
            System.out.println(" - " + a.getIdentite());
        }
    }

    /** Option de menu : affiche les films d'un acteur sortis entre deux années, tous saisis au clavier. */
    private static void optionFilmsAnneesActeur(Scanner scanner, FilmDao filmDao) {
        System.out.print("Nom de l'acteur : ");
        String acteur = scanner.nextLine();
        System.out.print("Année de début : ");
        String debut = scanner.nextLine();
        System.out.print("Année de fin : ");
        String fin = scanner.nextLine();

        List<Film> films = filmDao.trouverFilmParActeurEtAnnee(acteur, debut, fin);
        if (films.isEmpty()) {
            System.out.println("Aucun film trouvé pour ces critères.");
            return;
        }
        System.out.println("Films de " + acteur + " entre " + debut + " et " + fin + " :");
        for (Film f : films) {
            System.out.println(" - " + f.getNom() + " (" + f.getAnnee() + ")");
        }

    }


}
