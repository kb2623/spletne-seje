package org.sessionization.database;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Converter(autoApply = true)
public class InetAddressConverter implements AttributeConverter<InetAddress, String> {
	public static String getInetAddressString(InetAddress address) {
		return address.toString();
	}

	@Override
	public String convertToDatabaseColumn(InetAddress attribute) {
		return attribute.toString();
	}

	@Override
	public InetAddress convertToEntityAttribute(String dbData) {
		try {
			return InetAddress.getByName(dbData);
		} catch (UnknownHostException e) {
			return null;
		}
	}
}
