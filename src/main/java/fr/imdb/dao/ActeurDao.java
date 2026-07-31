package fr.imdb.dao;

import fr.imdb.entities.Acteur;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.List;

/**
 * DAO d'accès aux {@link Acteur}.
 */
public class ActeurDao {

    private EntityManager entityManager;

    /**
     * Crée un DAO utilisant l'EntityManager fourni.
     *
     * @param entityManager gestionnaire d'entités JPA
     */
    public ActeurDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Persiste un nouvel acteur dans une transaction dédiée.
     * En cas d'erreur, la transaction est annulée (rollback) puis l'exception est propagée.
     *
     * @param acteur acteur à sauvegarder
     */
    public void sauvegarder(Acteur acteur){
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(acteur);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    /**
     * @param idImdb identifiant IMDb de l'acteur recherché
     * @return l'acteur correspondant, ou {@code null} si aucun acteur ne porte cet identifiant
     */
    public Acteur trouverParIdImdb(String idImdb) {
        try {
            return entityManager.createQuery("SELECT a FROM Acteur a WHERE a.idImdb = :idImdb", Acteur.class)
                    .setParameter("idImdb", idImdb)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * @param identite identité (nom) de l'acteur recherché
     * @return l'acteur correspondant, ou {@code null} si aucun acteur ne porte cette identité
     */
    public Acteur trouverParIdentite(String identite) {
        try {
            return entityManager.createQuery("SELECT a FROM Acteur a WHERE a.identite = :identite", Acteur.class)
                    .setParameter("identite", identite)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Recherche les acteurs ayant joué à la fois dans les deux films donnés.
     *
     * @param titreFilm1 titre du premier film
     * @param titreFilm2 titre du second film
     * @return la liste des acteurs communs aux deux films, éventuellement vide
     */
    public List<Acteur> trouverActeursCommuns(String titreFilm1, String titreFilm2) {
        String jpql = "SELECT a FROM Acteur a JOIN a.roles r1 JOIN a.roles r2 WHERE r1.film.nom = :nom1 AND r2.film.nom = :nom2";

        return entityManager.createQuery(jpql, Acteur.class)
                .setParameter("nom1", titreFilm1.trim())
                .setParameter("nom2", titreFilm2.trim())
                .getResultList();
    }



}
