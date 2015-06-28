package org.spletneseje.fields;

import org.spletneseje.database.FieldTypeSQL;

public enum FieldType implements FieldTypeSQL {
	/** Apache atribut, lahko je IP ali pa niz */
	RemoteHost {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE remote_host(id INTEGER PRIMARY KEY ASC, name TEXT NOT NULL);"};
		}

		@Override
		public String[] createIndexQuery() {
			return new String[]{"CREATE INDEX remote_host_index ON remote_host(name);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(remote_host_id) REFERENCES remote_host(id)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "remote_host";
			default: return null;
			}
		}

		@Override
		public String getIndexName(int level) {
			switch (level) {
			case 0: return "remote_host_index";
			default: return null;
			}
		}
	},
	/** RFC 1413 */
	RemoteLogname {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE remote_logname(id INTEGER PRIMARY KEY ASC, name TEXT NOT NULL);"};
		}

		@Override
		public String[] createIndexQuery() {
			return new String[]{"CREATE INDEX remote_logname_index ON remote_logname(name);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(remote_logname_name) REFERENCES remote_logname(id)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "remote_logname";
			default: return null;
			}
		}

		@Override
		public String getIndexName(int level) {
			switch (level) {
			case 0: return "remote_logname_index";
			default: return null;
			}
		}
	},
	/** Oseba ki zahteva resurs */
	RemoteUser {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE remote_user(id INTEGER PRIMARY KEY ASC, name TEXT NOT NULL);"};
		}

		@Override
		public String[] createIndexQuery() {
			return new String[]{"CREATE INDEX remote_user_index ON remote_user(name);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(remote_user_name) REFERENCES remote_user(id)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "remote_user";
			default: return null;
			}
		}

		@Override
		public String getIndexName(int level) {
			switch (level) {
			case 0: return "remote_user_index";
			default: return null;
			}
		}
	},
	/** Datum in čas */
	DateTime {
		@Override
		public String createTableConstraint() {
			return "date_time NUMERIC";
		}
	},
	/** Metoda, url in protokol */
	RequestLine,
	/** Status zahteve */
	StatusCode {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE status_code(id INTEGER PRIMARY KEY ASC, code TEXT NOT NULL);"};
		}

		@Override
		public String[] createIndexQuery() {
			return new String[]{"CREATE INDEX status_code_index ON status_code(code);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(status_code_id) REFERENCES remote_user(id)";
		}

		@Override
		public String getTableName(int level) {
			return "status_code";
		}

		@Override
		public String getIndexName(int level) {
			return "status_code_index";
		}
	},
	/** Preneseno število bytov od strežnika do klienta */
	SizeOfResponse {
		@Override
		public String createTableConstraint() {
			return "size_of_response INTEGER";
		}
	},
	/** Preneseno število bytov od klienta do strežnika */
	SizeOfRequest {
		@Override
		public String createTableConstraint() {
			return "size_of_request INTEGER";
		}
	},
	/** Stran s katere je prišel na našo stran */
	Referer {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE referer(id INTEGER PRIMARY KEY ASC, value TEXT NOT NULL);"};
		}

		@Override
		public String[] createIndexQuery() {
			return new String[]{"CREATE INDEX referer_index ON referer(value);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(referer_id) REFERENCES referer(id)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "referer";
			default: return null;
			}
		}

		@Override
		public String getIndexName(int level) {
			switch (level) {
			case 0: return "referer_index";
			default: return null;
			}
		}
	},
	/** Identifikacija brskalnika in botov */
	UserAgent {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE user_agent(id INTEGER PRIMARY KEY ASC, value TEXT NOT NULL);"};
		}

		@Override
		public String[] createIndexQuery() {
			return new String[]{"CREATE INDEX user_agent_index ON user_agent(value);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(user_agent_id) REFERENCES user_agent(id)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "user_agent";
			default: return null;
			}
		}

		@Override
		public String getIndexName(int level) {
			switch (level) {
			case 0: return "user_agent_index";
			default: return null;
			}
		}
	},
	/** Pi�kot (name = value;) */
	Cookie {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE cookie_key(id INTEGER PRIMARY KEY ASC, key TEXT NOT NULL);",
					"CREATE TABLE cookie(id INTEGER PRIMARY KEY ASC, FOREIGN KEY(cookie_key_id) REFERENCES cookie_key(id), value TEXT NOT NULL);"};
		}

		@Override
		public String[] createIndexQuery() {
			return new String[]{"CREATE INDEX cookie_key_index ON cookie_key(key);", "CREATE INDEX cookie_index ON cookie(value);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(cookie_id) REFERENCES cookie(id)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "cookie_key";
			case 1: return "cookie";
			default: return null;
			}
		}

		@Override
		public String getIndexName(int level) {
			switch (level) {
			case 0: return "cookie_key_index";
			case 1: return "cookie_index";
			default: return null;
			}
		}
	},
	/** W3C metoda */
	Method {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE method(id INTEGER PRIMARY KEY ASC, name TEXT NOT NULL);"};
		}

		@Override
		public String[] createIndexQuery() {
			return new String[]{"CREATE INDEX method_index ON method(name);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(method_id) REFERENCES method(id)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "method";
			default: return null;
			}
		}

		@Override
		public String getIndexName(int level) {
			switch (level) {
			case 0: return "method_index";
			default: return null;
			}
		}
	},
	/** W3C datum */
	Date {
		@Override
		public String createTableConstraint() {
			return "date NUMERIC";
		}
	},
	/** W3C �as */
	Time {
		@Override
		public String createTableConstraint() {
			return "time NUMERIC";
		}
	},
	/** �tevilka port na strežniku */
	ServerPort {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE server_port(port INTEGER PRIMARY KEY NOT NULL);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(server_port_number) REFERENCES server_port(port)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "server_port";
			default: return null;
			}
		}
	},
	/** �tevilka port na klientu */
	ClientPort {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE client_port(port INTEGER PRIMARY KEY NOT NULL);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(client_port_number) REFERENCES client_port(port)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "client_port";
			default: return null;
			}
		}
	},
	/** W3C IP strežnika */
	ServerIP {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE server_ip(id INTEGER PRIMARY KEY ASC, ip TEXT NOT NULL);"};
		}

		@Override
		public String[] createIndexQuery() {
			return new String[]{"CREATE INDEX server_ip_index ON server_ip(ip);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(server_ip_id) REFERENCES server_ip(id)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "server_ip";
			default: return null;
			}
		}

		@Override
		public String getIndexName(int level) {
			switch (level) {
			case 0: return "server_ip_index";
			default: return null;
			}
		}
	},
	/** W3C IP klienta */
	ClientIP {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE client_ip(id INTEGER PRIMARY KEY ASC, ip TEXT NOT NULL);"};
		}

		@Override
		public String[] createIndexQuery() {
			return new String[]{"CREATE INDEX client_ip_index ON client_ip(ip);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(client_ip_id) REFERENCES client_ip(id)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "client_ip";
			default: return null;
			}
		}

		@Override
		public String getIndexName(int level) {
			switch (level) {
			case 0: return "client_ip_index";
			default: return null;
			}
		}
	},
	/**	Čas porabljen za obdelavo zahteve (pri W3C v sekundah, pri Apache v milisekunah) */
	TimeTaken {
		@Override
		public String createTableConstraint() {
			return "time_taken INTEGER";
		}
	},
	/** Pod status protokola */
	SubStatus {
		@Override
		public String createTableConstraint() {
			return "sub_status INTEGER";
		}
	},
	/** Status akcije, vezan na Windows */
	Win32Status {
		@Override
		public String createTableConstraint() {
			return "win_32_status INTEGER";
		}
	},
	/** Ime gostovanja */
	Host {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE host(id INTEGER PRIMARY KEY ASC, host TEXT NOT NULL);"};
		}

		@Override
		public String[] createIndexQuery() {
			return new String[]{"CREATE INDEX host_index ON host(host);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(host_id) REFERENCES host(id)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "host";
			default: return null;
			}
		}

		@Override
		public String getIndexName(int level) {
			switch (level) {
			case 0: return "host_index";
			default: return null;
			}
		}
	},
	/** Verija uporabljenega protokola in ime protokola */
	ProtocolVersion {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE protocol_version(id INTEGER PRIMARY KEY ASC, version REAL NOT NULL);"};
		}

		@Override
		public String[] createIndexQuery() {
			return new String[]{"CREATE INDEX protocol_version_index ON server_ip(version);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(protocol_version_id) REFERENCES protocol_version(id)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "protocol_version";
			default: return null;
			}
		}

		@Override
		public String getIndexName(int level) {
			switch (level) {
			case 0: return "protocol_version_index";
			default: return null;
			}
		}
	},
	/** Ime strani */
	SiteName {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE site_name(id INTEGER PRIMARY KEY ASC, name TEXT NOT NULL);"};
		}

		@Override
		public String[] createIndexQuery() {
			return new String[]{"CREATE INDEX site_name_index ON site_name(name);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(site_name_id) REFERENCES site_name(id)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "site_name";
			default: return null;
			}
		}

		@Override
		public String getIndexName(int level) {
			switch (level) {
			case 0: return "site_name_index";
			default: return null;
			}
		}
	},
	/** Ime Računalnika */
	ComputerName {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE computer_name(id INTEGER PRIMARY KEY ASC, name TEXT NOT NULL);"};
		}

		@Override
		public String[] createIndexQuery() {
			return new String[]{"CREATE INDEX computer_name_index ON computer_name(name);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(computer_name_id) REFERENCES computer_name(id)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "computer_name";
			default: return null;
			}
		}

		@Override
		public String getIndexName(int level) {
			switch (level) {
			case 0: return "computer_name_index";
			default: return null;
			}
		}
	},
	/** Atributi zahteve */
	UriQuery {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE query_key(id INTEGER PRIMARY KEY ASC, key TEXT NOT NULL);",
					"CREATE TABLE query(id INTEGER PRIMARY KEY ASC, FOREIGN KEY(query_key_id) REFERENCES query_key(id), value TEXT NOT NULL);"};
		}

		@Override
		public String[] createIndexQuery() {
			return new String[]{"CREATE INDEX query_key_index ON query_key(key);", "CREATE INDEX query_index ON query(value);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(uri_query_id) REFERENCES query(id)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "query_key";
			case 1: return "query";
			default: return null;
			}
		}

		@Override
		public String getIndexName(int level) {
			switch (level) {
			case 0: return "query_key_index";
			case 1: return "query_index";
			default: return null;
			}
		}
	},
	/** Ime zahtevanega resursa */
	UriStem {
		@Override
		public String[] createTableQuery() {
			return new String[]{"CREATE TABLE uri_stem(id INTEGER PRIMARY KEY ASC, name TEXT NOT NULL);"};
		}

		@Override
		public String[] createIndexQuery() {
			return new String[]{"CREATE INDEX uri_stem_index ON uri_stem(name);"};
		}

		@Override
		public String createTableConstraint() {
			return "FOREIGN KEY(uri_stem_id) REFERENCES uri_stem(id)";
		}

		@Override
		public String getTableName(int level) {
			switch (level) {
			case 0: return "uri_stem";
			default: return null;
			}
		}

		@Override
		public String getIndexName(int level) {
			switch (level) {
			case 0: return "uri_stem_index";
			default: return null;
			}
		}
	},
	/** Neznano polje */
	Unknown,
	/** Tip polja ki predstavlja opis polja v W3C formatu */
	MetaData

}
