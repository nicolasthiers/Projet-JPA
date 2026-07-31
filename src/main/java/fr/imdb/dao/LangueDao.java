package fr.imdb.dao;

import fr.imdb.entities.Langue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

public class LangueDao {

    private EntityManager entityManager;

    /**
     * Crée un DAO utilisant l'EntityManager fourni.
     *
     * @param entityManager gestionnaire d'entités JPA
     */
    public LangueDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void sauvegarder(Langue langue) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(langue);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Langue trouverParLibelle(String nomRecherche) {
        try {
            return entityManager.createQuery("SELECT l FROM Langue l WHERE l.nomLangue = :nomEntre", Langue.class)
                    .setParameter("nomEntre", nomRecherche)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
