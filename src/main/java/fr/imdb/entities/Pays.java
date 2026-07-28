package fr.imdb.entities;

import java.util.Objects;

public class Pays {

    private int id;
    private String nom;
    private String url;

    public Pays() {
    }

    public Pays(int id, String nom, String url) {
        this.id = id;
        this.nom = nom;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pays pays)) return false;
        return getId() == pays.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pays{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
