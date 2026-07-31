package fr.imdb.dao;

import fr.imdb.entities.Realisateur;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

/**
 * DAO d'accès aux {@link Realisateur}.
 */
public class RealisateurDao {

    private EntityManager entityManager;

    /**
     * Crée un DAO utilisant l'EntityManager fourni.
     *
     * @param entityManager gestionnaire d'entités JPA
     */
    public RealisateurDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Persiste un nouveau réalisateur dans une transaction dédiée.
     * En cas d'erreur, la transaction est annulée (rollback) puis l'exception est propagée.
     *
     * @param realisateur réalisateur à sauvegarder
     */
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

    /**
     * @param idImdb identifiant IMDb du réalisateur recherché
     * @return le réalisateur correspondant, ou {@code null} si aucun réalisateur ne porte cet identifiant
     */
    public Realisateur trouverParIdImdb(String idImdb) {
        try {
            return entityManager.createQuery("SELECT r FROM Realisateur r WHERE r.idImdb = :idImdb", Realisateur.class)
                    .setParameter("idImdb", idImdb.trim())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * @param identite identité (nom) du réalisateur recherché
     * @return le réalisateur correspondant, ou {@code null} si aucun réalisateur ne porte cette identité
     */
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
