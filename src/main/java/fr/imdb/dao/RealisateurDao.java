package fr.imdb.dao;

import fr.imdb.entities.Realisateur;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

public class RealisateurDao {

    private EntityManager entityManager;

    public RealisateurDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void sauvegarder(Realisateur realisateur) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(realisateur);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Realisateur trouverParIdImdb(String idImdb) {
        try {
            return entityManager.createQuery("SELECT r FROM Realisateur r WHERE r.idImdb = :idImdb", Realisateur.class)
                    .setParameter("idImdb", idImdb.trim())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Realisateur trouverParIdentite(String identite) {
        try {
            return entityManager.createQuery("SELECT r FROM Realisateur r WHERE r.identite = :identite", Realisateur.class)
                    .setParameter("identite", identite.trim())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
