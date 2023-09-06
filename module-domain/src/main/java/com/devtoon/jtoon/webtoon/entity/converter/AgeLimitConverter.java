package com.devtoon.jtoon.webtoon.entity.converter;

import static java.util.Objects.*;

import com.devtoon.jtoon.webtoon.entity.AgeLimit;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AgeLimitConverter implements AttributeConverter<AgeLimit, String> {

	@Override
	public String convertToDatabaseColumn(AgeLimit ageLimit) {
		if (isNull(ageLimit)) {
			return null;
		}

		return ageLimit.getValue();
	}

	@Override
	public AgeLimit convertToEntityAttribute(String dbData) {
		return AgeLimit.from(dbData);
	}
}
