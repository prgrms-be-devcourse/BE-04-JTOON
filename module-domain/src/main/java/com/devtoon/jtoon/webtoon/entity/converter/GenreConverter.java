package com.devtoon.jtoon.webtoon.entity.converter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.devtoon.jtoon.webtoon.entity.Genre;

import jakarta.persistence.AttributeConverter;

public class GenreConverter implements AttributeConverter<Set<Genre>, String> {

	private static final String DELIMITER = ",";

	@Override
	public String convertToDatabaseColumn(Set<Genre> genres) {
		if (genres.isEmpty()) {
			throw new RuntimeException("attribute is empty");
		}

		return genres.stream()
			.map(Genre::getName)
			.collect(Collectors.joining(DELIMITER));
	}

	@Override
	public Set<Genre> convertToEntityAttribute(String dbData) {
		String[] values = dbData.split(DELIMITER);

		return Arrays.stream(values)
			.map(Genre::from)
			.collect(Collectors.toSet());
	}
}
