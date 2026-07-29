package fr.imdb.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

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
