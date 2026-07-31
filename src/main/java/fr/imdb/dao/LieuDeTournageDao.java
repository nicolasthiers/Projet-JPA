package fr.imdb.dao;

import fr.imdb.entities.LieuDeTournage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

/**
 * DAO d'accès aux {@link LieuDeTournage}.
 */
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

    /**
     * Persiste un nouveau lieu de tournage dans une transaction dédiée.
     * En cas d'erreur, la transaction est annulée (rollback) puis l'exception est propagée.
     *
     * @param lieuDeTournage lieu de tournage à sauvegarder
     */
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

    /**
     * @param nomRecherche libellé exact du lieu recherché
     * @return le lieu de tournage correspondant, ou {@code null} si aucun ne porte ce libellé
     */
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
