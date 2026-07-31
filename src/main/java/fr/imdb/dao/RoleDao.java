package fr.imdb.dao;

import fr.imdb.entities.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class RoleDao {

    private EntityManager entityManager;

    public RoleDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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