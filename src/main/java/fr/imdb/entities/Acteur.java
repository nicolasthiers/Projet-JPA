package fr.imdb.entities;

import java.time.LocalDate;

public class Acteur extends Personne{

    private double taille;
    private String url;


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
