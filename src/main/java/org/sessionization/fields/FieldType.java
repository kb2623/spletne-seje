package org.sessionization.fields;

import org.sessionization.fields.ncsa.*;
import org.sessionization.fields.w3c.*;

import static org.sessionization.fields.FieldType.ProtocolVersion;

public enum FieldType {
	/** Apache atribut, lahko je IP ali pa niz */
	RemoteHost {
		@Override
		public boolean isKey() {
			return true;
		}

		@Override
		public Class getClassType() {
			return org.sessionization.fields.ncsa.RemoteHost.class;
		}
	},
	/** RFC 1413 */
	RemoteLogname {
		@Override
		public boolean isKey() {
			return true;
		}

		@Override
		public Class getClassType() {
			return org.sessionization.fields.ncsa.RemoteHost.class;
		}
	},
	/** Oseba ki zahteva resurs */
	RemoteUser {
		@Override
		public boolean isKey() {
			return true;
		}

		@Override
		public Class getClassType() {
			return org.sessionization.fields.RemoteUser.class;
		}
	},
	/** Datum in čas */
	DateTime {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.ncsa.DateTime.class;
		}
	},
	/** Metoda, url in protokol */
	RequestLine {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.ncsa.RequestLine.class;
		}
	},
	/** Status zahteve */
	StatusCode {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.StatusCode.class;
		}
	},
	/** Preneseno število bytov od strežnika do klienta */
	SizeOfResponse {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.SizeOfResponse.class;
		}
	},
	/** Preneseno število bytov od klienta do strežnika */
	SizeOfRequest {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.SizeOfRequest.class;
		}
	},
	/** Stran s katere je prišel na našo stran */
	Referer {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.Referer.class;
		}
	},
	/** Identifikacija brskalnika in botov */
	UserAgent {
		@Override
		public boolean isKey() {
			return true;
		}

		@Override
		public Class getClassType() {
			return org.sessionization.fields.UserAgent.class;
		}
	},
	/** Pi�kot (name = value;) */
	Cookie {
		@Override
		public boolean isKey() {
			return true;
		}

		@Override
		public Class getClassType() {
			return org.sessionization.fields.Cookie.class;
		}
	},
	/** W3C metoda */
	Method {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.Method.class;
		}
	},
	/** W3C datum */
	Date {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.w3c.Date.class;
		}
	},
	/** W3C �as */
	Time {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.w3c.Time.class;
		}
	},
	/** �tevilka port na strežniku */
	ServerPort {
		@Override
		public Class getClassType() {
			return Port.class;
		}
	},
	/** �tevilka port na klientu */
	ClientPort {
		@Override
		public boolean isKey() {
			return true;
		}

		@Override
		public Class getClassType() {
			return Port.class;
		}
	},
	/** IP strežnika */
	ServerIP {
		@Override
		public Class getClassType() {
			return Address.class;
		}
	},
	/** IP klienta */
	ClientIP {
		@Override
		public boolean isKey() {
			return true;
		}

		@Override
		public Class getClassType() {
			return Address.class;
		}
	},
	/**	Čas porabljen za obdelavo zahteve (pri W3C v sekundah, pri Apache v milisekunah) */
	TimeTaken {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.TimeTaken.class;
		}
	},
	/** Pod status protokola */
	SubStatus {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.w3c.SubStatus.class;
		}
	},
	/** Status akcije, vezan na Windows */
	Win32Status {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.w3c.Win32Status.class;
		}
	},
	/** Ime gostovanja */
	Host {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.Host.class;
		}
	},
	/** Verija uporabljenega protokola in ime protokola */
	ProtocolVersion {
		@Override
		public Class getClassType() {
			return Protocol.class;
		}
	},
	/** Ime strani */
	SiteName {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.w3c.SiteName.class;
		}
	},
	/** Ime Računalnika */
	ComputerName {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.w3c.ComputerName.class;
		}
	},
	/** Atributi zahteve */
	UriQuery {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.UriQuery.class;
		}
	},
	/** Ime zahtevanega resursa */
	UriSteam {
		@Override
		public Class getClassType() {
			return UriSteam.class;
		}
	},
	/**
	 * NCSA
	 * Bytes transferred (received and sent), including request and headers, cannot be zero. This is the combination of %I and %O. You need to enable mod_logio to use this.
	 */
	SizeOfTransfer {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.ncsa.SizeOfTransfer.class;
		}
	},
	/**
	 * NCSA
	 * Number of keepalive requests handled on this connection. Interesting if KeepAlive is being used, so that, for example, a '1' means the first keepalive request after the initial one, '2' the second, etc...; otherwise this is always 0 (indicating the initial request). Available in versions 2.2.11 and later.
	 */
	KeepAliveNumber {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.ncsa.KeepAliveNumber.class;
		}
	},
	/**
	 * NCSA
	 * Connection status when response is completed:
	 * 		X = connection aborted before the response completed.
	 * 		+ =	connection may be kept alive after the response is sent.
	 * 		- =	connection will be closed after the response is sent.
	 */
	ConnectionStatus {
		@Override
		public Class getClassType() {
			return org.sessionization.fields.ncsa.ConnectionStatus.class;
		}
	},
	/** ID procesa, ki je obdelal zahtevo */
	ProcessID {
		@Override
		public Class getClassType() {
			return ProcessID.class;
		}
	},
	/** Neznano polje */
	Unknown,
	/** Tip polja ki predstavlja opis polja v W3C formatu */
	MetaData;

	public boolean isKey() {
		return false;
	}

	public Class getClassType() {
		return null;
	}

	public String getType() {
		Class c = getClassType();
		if (c != null) {
			return "L" + c.getName().replaceAll(".", "/") + ";";
		} else {
			return "";
		}
	}
}
