package fr.imdb.dao;

import fr.imdb.entities.Acteur;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.List;

public class ActeurDao {

    private EntityManager entityManager;

    public ActeurDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void sauvegarder(Acteur acteur){
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(acteur);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Acteur trouverParIdImdb(String idImdb) {
        try {
            return entityManager.createQuery("SELECT a FROM Acteur a WHERE a.idImdb = :idImdb", Acteur.class)
                    .setParameter("idImdb", idImdb)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Acteur trouverParIdentite(String identite) {
        try {
            return entityManager.createQuery("SELECT a FROM Acteur a WHERE a.identite = :identite", Acteur.class)
                    .setParameter("identite", identite)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Acteur> trouverActeursCommuns(String titreFilm1, String titreFilm2) {
        String jpql = "SELECT a FROM Acteur a JOIN a.roles r1 JOIN a.roles r2 WHERE r1.film.nom = :nom1 AND r2.film.nom = :nom2";

        return entityManager.createQuery(jpql, Acteur.class)
                .setParameter("nom1", titreFilm1.trim())
                .setParameter("nom2", titreFilm2.trim())
                .getResultList();
    }



}
