package fr.imdb.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Superclasse commune à {@link Acteur} et {@link Realisateur}.
 * <p>
 * Regroupe les attributs partagés par toute personne du domaine : identifiant IMDb,
 * identité, date et lieu de naissance. N'étant pas une entité à part entière
 * ({@code @MappedSuperclass}), elle ne possède pas de table dédiée : ses colonnes
 * sont reportées sur la table de chaque sous-classe.
 */
@MappedSuperclass
public abstract class Personne {

    @Id
    @Column(name = "id_imdb", length = 20)
    private String idImdb;

    @Column(name = "identite", nullable = false, length = 50)
    private String identite;
    @Column(name = "date_de_naissance")
    private LocalDate dateDeNaissance;

    @ManyToOne
    @JoinColumn(name = "id_lieu_de_naissance")
    private LieuDeNaissance lieuDeNaissance;

    public Personne() {
    }

    /**
     * @param idImdb identifiant IMDb de la personne (ex. {@code nm0000123})
     * @param identite nom complet de la personne
     * @param dateDeNaissance date de naissance, peut être {@code null} si inconnue
     * @param lieuDeNaissance lieu de naissance, peut être {@code null} si inconnu
     */
    public Personne(String idImdb, String identite, LocalDate dateDeNaissance, LieuDeNaissance lieuDeNaissance) {
        this.idImdb = idImdb;
        this.identite = identite;
        this.dateDeNaissance = dateDeNaissance;
        this.lieuDeNaissance = lieuDeNaissance;
    }

    public String getIdImdb() {
        return idImdb;
    }

    public String getIdentite() {
        return identite;
    }

    public LocalDate getDateDeNaissance() {
        return dateDeNaissance;
    }

    public LieuDeNaissance getLieuDeNaissance() {
        return lieuDeNaissance;
    }

    public void setIdImdb(String idImdb) {
        this.idImdb = idImdb;
    }

    public void setIdentite(String identite) {
        this.identite = identite;
    }

    public void setDateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public void setLieuDeNaissance(LieuDeNaissance lieuDeNaissance) {
        this.lieuDeNaissance = lieuDeNaissance;
    }

    @Override
    public String toString() {
        return "Personne{" +
                "idImdb='" + idImdb + '\'' +
                ", identite='" + identite + '\'' +
                ", dateDeNaissance=" + dateDeNaissance +
                ", lieuDeNaissance=" + lieuDeNaissance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Personne personne)) return false;
        return Objects.equals(getIdImdb(), personne.getIdImdb());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdImdb());
    }
}
