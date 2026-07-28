package fr.imdb.entities;

import java.util.Objects;

public class LieuDeNaissance {

    private int id;
    private String localisation;

    public LieuDeNaissance() {
    }

    public LieuDeNaissance(int id, String localisation) {
        this.id = id;
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
