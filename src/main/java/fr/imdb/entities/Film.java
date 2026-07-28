package fr.imdb.entities;

import java.util.Objects;

public class Film {

    private String idImdb;
    private String nom;
    private String annee;
    private double rating;
    private String url;
    private LieuDeTournage lieuDeTournage;
    private Langue langue;
    private String resume;
    private Pays pays;

    public Film() {
    }

    public Film(String idImdb, String nom, String annee, double rating, String url, LieuDeTournage lieuDeTournage, Langue langue, String resume, Pays pays) {
        this.idImdb = idImdb;
        this.nom = nom;
        this.annee = annee;
        this.rating = rating;
        this.url = url;
        this.lieuDeTournage = lieuDeTournage;
        this.langue = langue;
        this.resume = resume;
        this.pays = pays;
    }

    public String getIdImdb() {
        return idImdb;
    }

    public void setIdImdb(String idImdb) {
        this.idImdb = idImdb;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LieuDeTournage getLieuDeTournage() {
        return lieuDeTournage;
    }

    public void setLieuDeTournage(LieuDeTournage lieuDeTournage) {
        this.lieuDeTournage = lieuDeTournage;
    }

    public Langue getLangue() {
        return langue;
    }

    public void setLangue(Langue langue) {
        this.langue = langue;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public Pays getPays() {
        return pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film film)) return false;
        return Objects.equals(getIdImdb(), film.getIdImdb());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdImdb());
    }

    @Override
    public String toString() {
        return "Film{" +
                "idImdb='" + idImdb + '\'' +
                ", nom='" + nom + '\'' +
                ", annee='" + annee + '\'' +
                ", rating=" + rating +
                ", url='" + url + '\'' +
                ", lieuDeTournage=" + lieuDeTournage +
                ", langue=" + langue +
                ", resume='" + resume + '\'' +
                ", pays=" + pays +
                '}';
    }
}
