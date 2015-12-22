package org.sessionization.database;

import org.sessionization.parser.fields.Method;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MethodConverter implements AttributeConverter<Method, String> {
	@Override
	public String convertToDatabaseColumn(Method attribute) {
		return attribute.name();
	}

	@Override
	public Method convertToEntityAttribute(String dbData) {
		return Enum.valueOf(Method.class, dbData);
	}
}
