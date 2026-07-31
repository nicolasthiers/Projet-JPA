package fr.imdb.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Représente un réalisateur de cinéma.
 * <p>
 * Un {@code Realisateur} peut avoir réalisé plusieurs {@link Film films}, et un
 * film peut avoir plusieurs réalisateurs (relation many-to-many via la table
 * {@code realisateur_film}).
 */
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

    /**
     * @param idImdb identifiant IMDb du réalisateur
     * @param identite nom complet du réalisateur
     * @param dateDeNaissance date de naissance, peut être {@code null}
     * @param lieuDeNaissance lieu de naissance, peut être {@code null}
     * @param url URL de la fiche IMDb du réalisateur
     */
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
