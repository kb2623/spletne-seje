package org.sessionization.fields;

public enum FieldType {
	/** Apache atribut, lahko je IP ali pa niz */
	RemoteHost,
	/** RFC 1413 */
	RemoteLogname,
	/** Oseba ki zahteva resurs */
	RemoteUser,
	/** Datum in čas */
	DateTime,
	/** Metoda, url in protokol */
	RequestLine,
	/** Status zahteve */
	StatusCode,
	/** Preneseno število bytov od strežnika do klienta */
	SizeOfResponse,
	/** Preneseno število bytov od klienta do strežnika */
	SizeOfRequest,
	/** Stran s katere je prišel na našo stran */
	Referer,
	/** Identifikacija brskalnika in botov */
	UserAgent,
	/** Pi�kot (name = value;) */
	Cookie,
	/** W3C metoda */
	Method,
	/** W3C datum */
	Date,
	/** W3C �as */
	Time,
	/** �tevilka port na strežniku */
	ServerPort,
	/** �tevilka port na klientu */
	ClientPort,
	/** IP strežnika */
	ServerIP,
	/** IP klienta */
	ClientIP,
	/**	Čas porabljen za obdelavo zahteve (pri W3C v sekundah, pri Apache v milisekunah) */
	TimeTaken,
	/** Pod status protokola */
	SubStatus,
	/** Status akcije, vezan na Windows */
	Win32Status,
	/** Ime gostovanja */
	Host,
	/** Verija uporabljenega protokola in ime protokola */
	ProtocolVersion,
	/** Ime strani */
	SiteName,
	/** Ime Računalnika */
	ComputerName,
	/** Atributi zahteve */
	UriQuery,
	/** Ime zahtevanega resursa */
	UriStem,
	/**
	 * NCSA
	 * Bytes transferred (received and sent), including request and headers, cannot be zero. This is the combination of %I and %O. You need to enable mod_logio to use this.
	 */
	SizeOfTransfer,
	/**
	 * NCSA
	 * Number of keepalive requests handled on this connection. Interesting if KeepAlive is being used, so that, for example, a '1' means the first keepalive request after the initial one, '2' the second, etc...; otherwise this is always 0 (indicating the initial request). Available in versions 2.2.11 and later.
	 */
	KeepAliveNumber,
	/**
	 * NCSA
	 * Connection status when response is completed:
	 * 		X = connection aborted before the response completed.
	 * 		+ =	connection may be kept alive after the response is sent.
	 * 		- =	connection will be closed after the response is sent.
	 */
	ConnectionStatus,
	/** ID procesa, ki je obdelal zahtevo */
	ProcesID,
	/** Neznano polje */
	Unknown,
	/** Tip polja ki predstavlja opis polja v W3C formatu */
	MetaData

}
