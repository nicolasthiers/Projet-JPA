package fr.imdb.entities;

import java.util.Objects;

public class LieuDeTournage {

    private int id;
    private String localisation;

    public LieuDeTournage() {
    }

    public LieuDeTournage(int id, String localisation) {
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
