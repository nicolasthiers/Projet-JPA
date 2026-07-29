package fr.imdb.dao;

import fr.imdb.entities.Film;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.List;

public class FilmDao {

    private EntityManager entityManager;

    public FilmDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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

    public Film trouverParIdImdb(String idImdb) {
        try {
            return entityManager.createQuery("SELECT f FROM Film f WHERE f.idImdb = :idImdb", Film.class)
                    .setParameter("idImdb", idImdb)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Film> trouverFilmographieActeur(String identiteActeur) {
        String jpql = "SELECT f FROM Film f JOIN f.roles r WHERE r.acteur.identite = :identite";

        return entityManager.createQuery(jpql, Film.class)
                .setParameter("identite", identiteActeur.trim())
                .getResultList();
    }

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

    public List<Film> trouverFilmParAnnees(String anneeDebut, String anneeFin) {
        String jpql = "SELECT f FROM Film f WHERE f.annee BETWEEN :debut AND :fin ORDER BY f.annee ASC";

        return entityManager.createQuery(jpql, Film.class)
                .setParameter("debut", anneeDebut.trim())
                .setParameter("fin", anneeFin.trim())
                .getResultList();
    }

    public List<Film> trouverFilmsCommunsActeurs(String identiteActeur1, String identiteActeur2) {
        String jpql = "SELECT f FROM Film f JOIN f.roles r1 JOIN f.roles r2 WHERE r1.acteur.identite = :acteur1 AND r2.acteur.identite = :acteur2";

        return entityManager.createQuery(jpql, Film.class)
                .setParameter("acteur1", identiteActeur1.trim())
                .setParameter("acteur2", identiteActeur2.trim())
                .getResultList();
    }

    public List<Film> trouverFilmParActeurEtAnnee(String identiteActeur, String anneeDebut, String anneeFIn){
        String jpql = "SELECT f FROM Film f JOIN f.roles r WHERE r.acteur.identite = :identite AND f.annee BETWEEN :debut AND :fin ORDER BY f.annee ASC";

        return entityManager.createQuery(jpql, Film.class)
                .setParameter("identite", identiteActeur.trim())
                .setParameter("debut", anneeDebut.trim())
                .setParameter("fin", anneeFIn.trim())
                .getResultList();
    }


}
