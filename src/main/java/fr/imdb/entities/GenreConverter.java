package fr.imdb.entities;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convertit un {@link Genre} vers/depuis son libellé texte pour la persistance JPA.
 * <p>
 * Appliqué automatiquement ({@code autoApply = true}) à tout attribut de type
 * {@link Genre}, notamment la collection {@code Film.genres}.
 */
@Converter(autoApply = true)
public class GenreConverter implements AttributeConverter<Genre, String> {

    @Override
    public String convertToDatabaseColumn(Genre genre) {
        return (genre != null) ? genre.getLabel() : null;
    }

    @Override
    public Genre convertToEntityAttribute(String dbData) {
        return (dbData != null) ? Genre.fromLabel(dbData) : null;
    }
}