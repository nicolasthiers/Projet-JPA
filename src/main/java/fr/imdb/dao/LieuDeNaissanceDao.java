package fr.imdb.dao;

import fr.imdb.entities.LieuDeNaissance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

/**
 * DAO d'accès aux {@link LieuDeNaissance}.
 */
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

    /**
     * Persiste un nouveau lieu de naissance dans une transaction dédiée.
     * En cas d'erreur, la transaction est annulée (rollback) puis l'exception est propagée.
     *
     * @param lieuDeNaissance lieu de naissance à sauvegarder
     */
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

    /**
     * @param nomRecherche libellé exact du lieu recherché
     * @return le lieu de naissance correspondant, ou {@code null} si aucun ne porte ce libellé
     */
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
