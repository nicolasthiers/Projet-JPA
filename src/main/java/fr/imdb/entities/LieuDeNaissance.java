package fr.imdb.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "lieu_de_naissance")
public class LieuDeNaissance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "localisation", unique = true, length = 100)
    private String localisation;

    @OneToMany(mappedBy = "lieuDeNaissance")
    private List<Acteur> acteurs = new ArrayList<Acteur>();

    @OneToMany(mappedBy = "lieuDeNaissance")
    private List<Realisateur> realisateurs = new ArrayList<Realisateur>();

    public LieuDeNaissance() {
    }

    public LieuDeNaissance(String localisation) {
        this.localisation = localisation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LieuDeNaissance that)) return false;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LieuDeNaissance{" +
                "id=" + id +
                ", localisation='" + localisation + '\'' +
                '}';
    }
}
