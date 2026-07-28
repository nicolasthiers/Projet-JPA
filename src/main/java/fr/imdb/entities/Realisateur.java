package fr.imdb.entities;

import java.time.LocalDate;

public class Realisateur extends Personne{

    private String url;


    public Realisateur() {
        super();
    }

    public Realisateur(String idImdb, String identite, LocalDate dateDeNaissance, LieuDeNaissance lieuDeNaissance, String url) {
        super(idImdb, identite, dateDeNaissance, lieuDeNaissance);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Realisateur{" +
                "idImdb='" + getIdImdb() + '\'' +
                ", identite='" + getIdentite() + '\'' +
                ", dateDeNaissance=" + getDateDeNaissance() +
                ", lieuDeNaissance=" + getLieuDeNaissance() +
                ", url='" + url + '\'' +
                '}';
    }
}
