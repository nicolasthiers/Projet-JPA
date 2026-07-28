package fr.imdb.entities;

import java.util.Objects;

public class Langue {

    private int id;
    private String nomLangue;

    public Langue() {
    }

    public Langue(int id, String nomLangue) {
        this.id = id;
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
