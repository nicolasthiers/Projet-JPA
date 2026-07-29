package fr.imdb.dao;

import fr.imdb.entities.Pays;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

public class PaysDao {

    private EntityManager entityManager;


    /**
     * Crée un DAO utilisant l'EntityManager fourni.
     *
     * @param entityManager gestionnaire d'entités JPA
     */
    public PaysDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void sauvegarder(Pays pays) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(pays);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }

    }

    public Pays trouveParNom(String nomRecherche) {
        try {
            return entityManager.createQuery("SELECT p FROM Pays p WHERE p.nom = :nomEntre", Pays.class)
                    .setParameter("nomEntre", nomRecherche.trim())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
