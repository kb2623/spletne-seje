package org.sessionization.database;

import org.sessionization.fields.ncsa.ConnectionStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ConnectionStatusConverter implements AttributeConverter<ConnectionStatus, String> {
	@Override
	public String convertToDatabaseColumn(ConnectionStatus attribute) {
		return attribute.name();
	}

	@Override
	public ConnectionStatus convertToEntityAttribute(String dbData) {
		return Enum.valueOf(ConnectionStatus.class, dbData);
	}
}
