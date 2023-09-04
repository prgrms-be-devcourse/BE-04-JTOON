package com.devtoon.jtoon.webtoon.entity.converter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.devtoon.jtoon.webtoon.entity.DayOfWeek;

import jakarta.persistence.AttributeConverter;

public class DayOfWeekConverter implements AttributeConverter<Set<DayOfWeek>, String> {

	private static final String DELIMITER = ",";

	@Override
	public String convertToDatabaseColumn(Set<DayOfWeek> dayOfWeeks) {
		if (dayOfWeeks.isEmpty()) {
			throw new RuntimeException("attribute is empty");
		}

		return dayOfWeeks.stream()
			.map(DayOfWeek::getName)
			.collect(Collectors.joining(DELIMITER));
	}

	@Override
	public Set<DayOfWeek> convertToEntityAttribute(String dbData) {
		String[] values = dbData.split(DELIMITER);

		return Arrays.stream(values)
			.map(DayOfWeek::from)
			.collect(Collectors.toSet());
	}
}
