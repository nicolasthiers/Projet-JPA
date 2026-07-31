package fr.imdb.entities;

/**
 * Genres cinématographiques possibles pour un {@link Film}.
 * <p>
 * Chaque valeur porte un libellé ({@link #getLabel()}) utilisé pour la persistance
 * (voir {@link GenreConverter}), car certains genres contiennent un caractère non
 * valide dans un nom de constante Java (ex. {@code Sci_Fi} → {@code "Sci-Fi"}).
 */
public enum Genre {
        Drama,
        Horror,
        Thriller,
        Adventure,
        Sci_Fi("Sci-Fi"),
        Comedy,
        Family,
        Crime,
        Western,
        Fantasy,
        Musical,
        Short,
        Romance,
        Animation,
        Mystery,
        History,
        War,
        Biography,
        Action;

   private final String label;

   Genre(String label) {
       this.label = label;
   }

   Genre() {
       this.label = this.name();
   }

   /**
    * @return le libellé du genre tel qu'utilisé en base et dans les fichiers d'import
    */
   public String getLabel() {
       return label;
   }

    /**
     * Retrouve un {@link Genre} à partir de son libellé texte.
     * <p>
     * La comparaison est insensible à la casse et tolère les espaces à la place des tirets
     * (ex. {@code "Sci Fi"} est reconnu comme {@code Sci_Fi}).
     *
     * @param text libellé à interpréter, peut être {@code null}
     * @return le {@link Genre} correspondant, ou {@code null} si {@code text} est {@code null}
     * @throws IllegalArgumentException si {@code text} ne correspond à aucun genre connu
     */
    public static Genre fromLabel(String text) {
        if (text == null) {
            return null;
        }
        String cleanedText = text.trim().replace(" ", "-");

        for (Genre g : Genre.values()) {
            if (g.label.equalsIgnoreCase(cleanedText) || g.name().equalsIgnoreCase(cleanedText.replace("-", "_"))) {
                return g;
            }
        }
        throw new IllegalArgumentException("Genre inconnu : " + text);
    }
}
