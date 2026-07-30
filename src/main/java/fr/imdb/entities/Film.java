package fr.imdb.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "film")
public class Film {

    @Id
    @Column(name = "id_imdb", length = 20)
    private String idImdb;
    @Column(name = "nom", nullable = false, length = 50)
    private String nom;
    @Column(name = "annee", nullable = false, length = 20)
    private String annee;
    @Column(name = "rating", precision = 3, scale = 1)
    private BigDecimal rating;
    @Column(name = "url", unique = true, length = 60)
    private String url;
    @Column(name = "resume", length = 1000)
    private String resume;

    @ElementCollection(targetClass = Genre.class)
    @CollectionTable(
            name = "film_genre",
            joinColumns = @JoinColumn(name = "id_film")
    )
    @Column(name = "id_genre")
    @Convert(converter = GenreConverter.class)
    private List<Genre> genres = new ArrayList<Genre>();

    @OneToMany(mappedBy = "film")
    private List<Role> roles = new ArrayList<Role>();

    @ManyToOne
    @JoinColumn(name = "id_lieu_de_tournage")
    private LieuDeTournage lieuDeTournage;

    @ManyToOne
    @JoinColumn(name = "id_langue")
    private Langue langue;

    @ManyToOne
    @JoinColumn(name = "id_pays")
    private Pays pays;

    @ManyToMany
    @JoinTable(name = "realisateur_film",
                joinColumns = @JoinColumn(name = "id_film", referencedColumnName = "id_imdb"),
                inverseJoinColumns = @JoinColumn(name = "id_realisateur", referencedColumnName = "id_imdb")
    )
    private Set<Realisateur> realisateurs = new HashSet<>();

    public Film() {
    }

    public Film(String idImdb, String nom, String annee, BigDecimal rating, String url, LieuDeTournage lieuDeTournage, Langue langue, String resume, Pays pays) {
        this.idImdb = idImdb;
        this.nom = nom;
        this.annee = annee;
        this.rating = rating;
        this.url = url;
        this.lieuDeTournage = lieuDeTournage;
        this.langue = langue;
        this.resume = resume;
        this.pays = pays;
    }

    public String getIdImdb() {
        return idImdb;
    }

    public void setIdImdb(String idImdb) {
        this.idImdb = idImdb;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LieuDeTournage getLieuDeTournage() {
        return lieuDeTournage;
    }

    public void setLieuDeTournage(LieuDeTournage lieuDeTournage) {
        this.lieuDeTournage = lieuDeTournage;
    }

    public Langue getLangue() {
        return langue;
    }

    public void setLangue(Langue langue) {
        this.langue = langue;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public Pays getPays() {
        return pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Set<Realisateur> getRealisateurs() {
        return realisateurs;
    }

    public void setRealisateurs(Set<Realisateur> realisateurs) {
        this.realisateurs = realisateurs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film film)) return false;
        return Objects.equals(getIdImdb(), film.getIdImdb());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdImdb());
    }

    @Override
    public String toString() {
        return "Film{" +
                "idImdb='" + idImdb + '\'' +
                ", nom='" + nom + '\'' +
                ", annee='" + annee + '\'' +
                ", rating=" + rating +
                ", url='" + url + '\'' +
                ", lieuDeTournage=" + lieuDeTournage +
                ", langue=" + langue +
                ", resume='" + resume + '\'' +
                ", pays=" + pays +
                '}';
    }
}
