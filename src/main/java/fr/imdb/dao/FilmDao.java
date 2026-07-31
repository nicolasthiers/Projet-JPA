package fr.imdb.dao;

import fr.imdb.entities.Film;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.List;

/**
 * DAO d'accès aux {@link Film} et de recherche multi-critères (filmographie,
 * casting, période, films/acteurs en commun) utilisées par {@link fr.imdb.app.MenuApp}.
 */
public class FilmDao {

    private EntityManager entityManager;

    /**
     * Crée un DAO utilisant l'EntityManager fourni.
     *
     * @param entityManager gestionnaire d'entités JPA
     */
    public FilmDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Persiste un nouveau film dans une transaction dédiée.
     * En cas d'erreur, la transaction est annulée (rollback) puis l'exception est propagée.
     *
     * @param film film à sauvegarder
     */
    public void sauvegarder(Film film){
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(film);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }
    }

    /**
     * @param idImdb identifiant IMDb du film recherché
     * @return le film correspondant, ou {@code null} si aucun film ne porte cet identifiant
     */
    public Film trouverParIdImdb(String idImdb) {
        try {
            return entityManager.createQuery("SELECT f FROM Film f WHERE f.idImdb = :idImdb", Film.class)
                    .setParameter("idImdb", idImdb)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Recherche tous les films dans lesquels l'acteur donné a un rôle.
     *
     * @param identiteActeur identité (nom) de l'acteur
     * @return la filmographie de l'acteur, éventuellement vide
     */
    public List<Film> trouverFilmographieActeur(String identiteActeur) {
        String jpql = "SELECT f FROM Film f JOIN f.roles r WHERE r.acteur.identite = :identite";

        return entityManager.createQuery(jpql, Film.class)
                .setParameter("identite", identiteActeur.trim())
                .getResultList();
    }

    /**
     * Recherche un film par son nom en chargeant en une seule requête son casting
     * (rôles et acteurs associés), via {@code JOIN FETCH}.
     *
     * @param nomFilm titre du film recherché
     * @return le film avec son casting initialisé, ou {@code null} si introuvable
     */
    public Film trouverFilmAvecCasting(String nomFilm) {
        try {
            String jpql = "SELECT DISTINCT f FROM Film f JOIN FETCH f.roles r LEFT JOIN FETCH r.acteur WHERE f.nom = :nom";

            return entityManager.createQuery(jpql, Film.class)
                    .setParameter("nom", nomFilm.trim())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Recherche les films sortis entre deux années (bornes incluses), triés par année croissante.
     *
     * @param anneeDebut année de début de la période
     * @param anneeFin année de fin de la période
     * @return la liste des films correspondants, éventuellement vide
     */
    public List<Film> trouverFilmParAnnees(String anneeDebut, String anneeFin) {
        String jpql = "SELECT f FROM Film f WHERE f.annee BETWEEN :debut AND :fin ORDER BY f.annee ASC";

        return entityManager.createQuery(jpql, Film.class)
                .setParameter("debut", anneeDebut.trim())
                .setParameter("fin", anneeFin.trim())
                .getResultList();
    }

    /**
     * Recherche les films où ont joué ensemble deux acteurs donnés.
     *
     * @param identiteActeur1 identité du premier acteur
     * @param identiteActeur2 identité du second acteur
     * @return la liste des films communs aux deux acteurs, éventuellement vide
     */
    public List<Film> trouverFilmsCommunsActeurs(String identiteActeur1, String identiteActeur2) {
        String jpql = "SELECT f FROM Film f JOIN f.roles r1 JOIN f.roles r2 WHERE r1.acteur.identite = :acteur1 AND r2.acteur.identite = :acteur2";

        return entityManager.createQuery(jpql, Film.class)
                .setParameter("acteur1", identiteActeur1.trim())
                .setParameter("acteur2", identiteActeur2.trim())
                .getResultList();
    }

    /**
     * Recherche les films d'un acteur sortis entre deux années (bornes incluses),
     * triés par année croissante.
     *
     * @param identiteActeur identité de l'acteur
     * @param anneeDebut année de début de la période
     * @param anneeFIn année de fin de la période
     * @return la liste des films correspondants, éventuellement vide
     */
    public List<Film> trouverFilmParActeurEtAnnee(String identiteActeur, String anneeDebut, String anneeFIn){
        String jpql = "SELECT f FROM Film f JOIN f.roles r WHERE r.acteur.identite = :identite AND f.annee BETWEEN :debut AND :fin ORDER BY f.annee ASC";

        return entityManager.createQuery(jpql, Film.class)
                .setParameter("identite", identiteActeur.trim())
                .setParameter("debut", anneeDebut.trim())
                .setParameter("fin", anneeFIn.trim())
                .getResultList();
    }


}
