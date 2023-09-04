package com.devtoon.jtoon.webtoon.entity.converter;

import static java.util.Objects.*;

import com.devtoon.jtoon.webtoon.entity.AgeLimit;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AgeLimitConverter implements AttributeConverter<AgeLimit, Integer> {

	@Override
	public Integer convertToDatabaseColumn(AgeLimit ageLimit) {
		if (isNull(ageLimit)) {
			return null;
		}

		return ageLimit.getAge();
	}

	@Override
	public AgeLimit convertToEntityAttribute(Integer dbData) {
		return AgeLimit.from(dbData);
	}
}
