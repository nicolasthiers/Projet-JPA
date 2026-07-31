package fr.imdb.dao;

import fr.imdb.entities.LieuDeTournage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

public class LieuDeTournageDao {

    private EntityManager entityManager;

    /**
     * Crée un DAO utilisant l'EntityManager fourni.
     *
     * @param entityManager gestionnaire d'entités JPA
     */
    public LieuDeTournageDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void sauvegarder(LieuDeTournage lieuDeTournage) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(lieuDeTournage);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public LieuDeTournage trouverParNom(String nomRecherche){
        try {
            return entityManager.createQuery("SELECT l FROM LieuDeTournage l WHERE l.localisation = :nomRecherche", LieuDeTournage.class)
                    .setParameter("nomRecherche", nomRecherche)
                    .getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }


}
