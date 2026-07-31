package fr.imdb.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Représente le pays d'origine d'un {@link Film}.
 */
@Entity
@Table(name = "pays")
public class Pays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "nom", unique = true, nullable = false, length = 100)
    private String nom;
    @Column(name = "url", unique = true, length = 60)
    private String url;

    @OneToMany(mappedBy = "pays")
    private List<Film> films = new ArrayList<Film>();

    public Pays() {
    }

    /**
     * @param nom nom du pays
     * @param url URL IMDb associée au pays (page de recherche par pays d'origine), peut être {@code null}
     */
    public Pays(String nom, String url) {
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
