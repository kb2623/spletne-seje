package parser;

import java.util.regex.Pattern;

public class Format {
	
	public enum Field {
		remote_host,
		remote_port,
		local_host,
		rfc931,
		username,
		date,
		time,
		timezone,
		request_method,
		request_uri,
		request_protocol,
		request_query_string,
		response_status_code,
		connection_status,
		bytes_recd,
		bytes_sent,
		response_time_micros,
		referer,
		user_agent,
		cookie,
		pid,
		tid,
	}
	
	public static final Format COMMON_LOG_FORMAT = new Format (
		"([a-z0-9.]+) " +
			"([a-z0-9_-]+) " +
			"([a-z0-9_-]+) " +
			"\\[(\\d{1,2}/\\w{3}/\\d{4}):" +
			"(\\d{1,2}:\\d{1,2}:\\d{1,2}) " +
			"([+-]\\d{4})\\] " +
			"\"([A-Z]{3,5}) " +
			"(/[^ ]+) " +
			"([A-Z]+/\\d\\.\\d)\" " +
			"(\\d+) " +
			"(-|\\d+)",
		new Field[] {
			Field.remote_host,
			Field.rfc931,
			Field.username,
			Field.date,
			Field.time,
			Field.timezone,
			Field.request_method,
			Field.request_uri,
			Field.request_protocol,
			Field.response_status_code,
			Field.bytes_recd
		}
	);
	
	public static final Format COMBINED_LOG_FORMAT = new Format(
		"([a-z0-9.]+) " +
			"([a-z0-9_-]+) " +
			"([a-z0-9_-]+) " +
			"\\[(\\d{1,2}/\\w{3}/\\d{4}):" +
			"(\\d{1,2}:\\d{1,2}:\\d{1,2}) " +
			"([+-]\\d{4})\\] " +
			"\"([A-Z]{3,5}) " +
			"(/[^ ]+) " +
			"([A-Z]+/\\d\\.\\d)\" " +
			"(\\d+) " +
			"(-|\\d+) " +
			"\"([^\"]+)\" " +
			"\"([^\"]+)\" " +
			"\"([^\"]+)\"",
		new Field[] {
			Field.remote_host,
			Field.rfc931,
			Field.username,
			Field.date,
			Field.time,
			Field.timezone,
			Field.request_method,
			Field.request_uri,
			Field.request_protocol,
			Field.response_status_code,
			Field.bytes_recd,
			Field.referer,
			Field.user_agent,
			Field.cookie
		}
	);
	
	private final Pattern pattern;
	private final Field[] fieldNames;
	
	public Format(String patteren, Field[] fieldNames) {
		this.pattern = Pattern.compile(patteren);
		this.fieldNames = fieldNames;
	}
	
	public Pattern getPattern() {
		return pattern;
	}
	
	public Field[] getFields() {
		return fieldNames;
	}
	
}
