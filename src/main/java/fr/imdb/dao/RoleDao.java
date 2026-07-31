package fr.imdb.dao;

import fr.imdb.entities.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

/**
 * DAO d'accès aux {@link Role}.
 */
public class RoleDao {

    private EntityManager entityManager;

    /**
     * Crée un DAO utilisant l'EntityManager fourni.
     *
     * @param entityManager gestionnaire d'entités JPA
     */
    public RoleDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Recherche un rôle existant pour un triplet (film, acteur, personnage), utilisé
     * par {@link fr.imdb.service.ImportService#ajouterRole} pour éviter les doublons à l'import.
     *
     * @param idFilm identifiant IMDb du film
     * @param idActeur identifiant IMDb de l'acteur
     * @param personnage nom du personnage joué
     * @return le rôle correspondant, ou {@code null} s'il n'existe pas encore
     */
    public Role trouverParFilmActeurPersonnage(String idFilm, String idActeur, String personnage) {
        try {
            String jpql = "SELECT r FROM Role r WHERE r.film.idImdb = :idFilm AND r.acteur.idImdb = :idActeur AND r.personnage = :personnage";
            return entityManager.createQuery(jpql, Role.class)
                    .setParameter("idFilm", idFilm)
                    .setParameter("idActeur", idActeur)
                    .setParameter("personnage", personnage)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}