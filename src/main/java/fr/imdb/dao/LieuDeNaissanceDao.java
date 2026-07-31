package fr.imdb.dao;

import fr.imdb.entities.LieuDeNaissance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

public class LieuDeNaissanceDao {

    private EntityManager entityManager;


    /**
     * Crée un DAO utilisant l'EntityManager fourni.
     *
     * @param entityManager gestionnaire d'entités JPA
     */
    public LieuDeNaissanceDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void sauvegarder(LieuDeNaissance lieuDeNaissance) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(lieuDeNaissance);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }

    }

    public LieuDeNaissance trouveParNom(String nomRecherche) {
        try {
            return entityManager.createQuery("SELECT ldn FROM LieuDeNaissance ldn WHERE ldn.localisation = :nomEntre", LieuDeNaissance.class)
                    .setParameter("nomEntre", nomRecherche.trim())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
