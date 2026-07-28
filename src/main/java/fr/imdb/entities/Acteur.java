package fr.imdb.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "acteur")
public class Acteur extends Personne{

    @Column(name = "taille", precision = 3, scale = 2)
    private double taille;
    @Column(name = "url", nullable = false, unique = true, length = 60)
    private String url;

    @OneToMany(mappedBy = "acteur")
    private List<Role> roles = new ArrayList<Role>();


    public Acteur() {
        super();
    }

    public Acteur(String idImdb, String identite, LocalDate dateDeNaissance, LieuDeNaissance lieuDeNaissance, double taille, String url) {
        super(idImdb, identite, dateDeNaissance, lieuDeNaissance);
        this.taille = taille;
        this.url = url;
    }

    public double getTaille() {
        return taille;
    }

    public void setTaille(double taille) {
        this.taille = taille;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Acteur{" +
                "idImdb='" + getIdImdb() + '\'' +
                ", identite='" + getIdentite() + '\'' +
                ", dateDeNaissance=" + getDateDeNaissance() +
                ", lieuDeNaissance=" + getLieuDeNaissance() +
                ", taille=" + taille +
                ", url='" + url + '\'' +
                '}';
    }
}
