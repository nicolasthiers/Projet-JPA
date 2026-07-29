package fr.imdb.entities;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

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