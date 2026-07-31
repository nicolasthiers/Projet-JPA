package fr.imdb.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "lieu_de_tournage")
public class LieuDeTournage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "localisation", unique = true, length = 255)
    private String localisation;

    @OneToMany(mappedBy = "lieuDeTournage")
    private List<Film> films = new ArrayList<Film>();

    public LieuDeTournage() {
    }

    public LieuDeTournage(String localisation) {
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
        if (!(o instanceof LieuDeTournage that)) return false;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LieuDeTournage{" +
                "id=" + id +
                ", localisation='" + localisation + '\'' +
                '}';
    }
}
