package fr.imdb.dao;

import fr.imdb.entities.Langue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

/**
 * DAO d'accès aux {@link Langue}.
 */
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

    /**
     * Persiste une nouvelle langue dans une transaction dédiée.
     * En cas d'erreur, la transaction est annulée (rollback) puis l'exception est propagée.
     *
     * @param langue langue à sauvegarder
     */
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

    /**
     * @param nomRecherche libellé exact de la langue recherchée
     * @return la langue correspondante, ou {@code null} si aucune langue ne porte ce libellé
     */
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
