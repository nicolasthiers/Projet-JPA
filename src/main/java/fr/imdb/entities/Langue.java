package fr.imdb.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "langue")
public class Langue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "nom_langue", unique = true, length = 20)
    private String nomLangue;

    @OneToMany(mappedBy = "langue")
    private List<Film> films = new ArrayList<Film>();

    public Langue() {
    }

    public Langue(String nomLangue) {
        this.nomLangue = nomLangue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomLangue() {
        return nomLangue;
    }

    public void setNomLangue(String nomLangue) {
        this.nomLangue = nomLangue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Langue langue)) return false;
        return getId() == langue.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Langue{" +
                "id=" + id +
                ", nomLangue='" + nomLangue + '\'' +
                '}';
    }
}
