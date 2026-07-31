package fr.imdb.dao;

import fr.imdb.entities.Pays;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

/**
 * DAO d'accès aux {@link Pays}.
 */
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

    /**
     * Persiste un nouveau pays dans une transaction dédiée.
     * En cas d'erreur, la transaction est annulée (rollback) puis l'exception est propagée.
     *
     * @param pays pays à sauvegarder
     */
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

    /**
     * @param nomRecherche nom exact du pays recherché
     * @return le pays correspondant, ou {@code null} si aucun pays ne porte ce nom
     */
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
