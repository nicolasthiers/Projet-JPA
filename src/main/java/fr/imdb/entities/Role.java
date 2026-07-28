package fr.imdb.entities;

import java.util.Objects;

public class Role {

    private Film film;
    private Acteur acteur;
    private String personnage;
    private boolean estCastingPrincipal;

    public Role() {
    }

    public Role(Film film, Acteur acteur, String personnage, boolean estCastingPrincipal) {
        this.film = film;
        this.acteur = acteur;
        this.personnage = personnage;
        this.estCastingPrincipal = estCastingPrincipal;
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
        return Objects.equals(getFilm(), role.getFilm()) && Objects.equals(getActeur(), role.getActeur());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFilm(), getActeur());
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
