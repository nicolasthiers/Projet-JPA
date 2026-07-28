package fr.imdb.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "realisateur")
public class Realisateur extends Personne{

    @Column(name = "url", nullable = false, unique = true, length = 60)
    private String url;

    @ManyToMany
    @JoinTable(name = "realisateur_film",
                joinColumns = @JoinColumn(name = "id_realisateur", referencedColumnName = "id_imdb"),
                inverseJoinColumns = @JoinColumn(name = "id_film", referencedColumnName = "id_imdb")
    )
    private Set<Film> films = new HashSet<>();



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
