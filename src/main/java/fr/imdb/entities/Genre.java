package fr.imdb.entities;

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

   public String getLabel() {
       return label;
   }

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
