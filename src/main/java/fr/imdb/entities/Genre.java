package fr.imdb.entities;

enum Genre {
        Drama,
        Horror,
        Thriller,
        Adventure,
        Sci_Fi("Sci-fi"),
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
       for (Genre g : Genre.values()) {
           if(g.label.equalsIgnoreCase(text.trim())) {
               return g;
           }
       }
       throw new IllegalArgumentException("Genre inconnu : " + text);
   }
}
