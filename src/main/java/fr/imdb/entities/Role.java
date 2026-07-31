package fr.imdb.entities;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Représente l'interprétation d'un {@link Acteur} dans un {@link Film} : le
 * personnage joué et le fait qu'il s'agisse ou non d'un rôle principal.
 * <p>
 * Sert de table d'association enrichie entre {@code Film} et {@code Acteur}
 * (une entité {@link Role} est créée par personnage joué).
 */
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "personnage", length = 255)
    private String personnage;
    @Column(name = "est_casting_principal")
    private boolean estCastingPrincipal;

    @ManyToOne
    @JoinColumn(name = "id_film")
    private Film film;

    @ManyToOne
    @JoinColumn(name = "id_acteur")
    private Acteur acteur;


    public Role() {
    }

    /**
     * @param film film concerné
     * @param acteur acteur qui interprète le rôle
     * @param personnage nom du personnage joué
     * @param estCastingPrincipal {@code true} si ce rôle fait partie du casting principal du film
     */
    public Role(Film film, Acteur acteur, String personnage, boolean estCastingPrincipal) {
        this.film = film;
        this.acteur = acteur;
        this.personnage = personnage;
        this.estCastingPrincipal = estCastingPrincipal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Acteur getActeur() {
        return acteur;
    }

    public void setActeur(Acteur acteur) {
        this.acteur = acteur;
    }

    public String getPersonnage() {
        return personnage;
    }

    public void setPersonnage(String personnage) {
        this.personnage = personnage;
    }

    public boolean isEstCastingPrincipal() {
        return estCastingPrincipal;
    }

    public void setEstCastingPrincipal(boolean estCastingPrincipal) {
        this.estCastingPrincipal = estCastingPrincipal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        return getId() == role.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Role{" +
                "film=" + (film != null ? film.getNom() : "null") + // Évite d'afficher tout le film pour ne pas saturer la console
                ", acteur=" + (acteur != null ? acteur.getIdentite() : "null") +
                ", personnage='" + personnage + '\'' +
                ", estCastingPrincipal=" + estCastingPrincipal +
                '}';
    }
}
