package fields;
/**
 * Razred sharanjuje podatke o uporabljeni metodi
 * 
 * @author klemen
 */
public enum Method implements Field {

	// Vse HTTP metode
	GET("GET"),
	HEAD("HEAD"),
	POST("POST"),
	PUT("PUT"),
	DELETE("DELETE"),
	TRACE("TRACE"),
	OPTIONS("OPTIONS"),
	CONNECT("CONNECT"),
	PATCH("PATCH"),
	// Vse FTP metode
	ABOR("ABOR"), // Abort an active file transfer.
	ACCT("ACCT"), // Account information.
	ADAT("ADAT"), // Authentication/Security Data
	ALLO("ALLO"), // Allocate sufficient disk space to receive a file.
	APPE("APPE"), // Append.
	AUTH("AUTH"), // Authentication/Security Mechanism.
	CCC ("CCC"),  // Clear Command Channel.
	CDUP("CDUP"), // Change to Parent Directory.
	CONF("CONF"), // Confidentiality Protection Command.
	CWD ("CWD"),  // Change working directory.
	DELE("DELE"), // Delete file.
	ENC ("ENC"),  // Privacy Protected Channel.
	EPRT("EPRT"), // Specifies an extended address and port to which the server should connect.
	EPSV("EPSV"), // Enter extended passive mode.
	FEAT("FEAT"), // Get the feature list implemented by the server.
	HELP("HELP"), // Returns usage documentation on a command if specified, else a general help document is returned.
	LANG("LANG"), // Language Negotiation.
	LIST("LIST"), // Returns information of a file or directory if specified, else information of the current working directory is returned. If the server supports the '-R' command (e.g. 'LIST -R') then a recursive directory listing will be returned.
	LPRT("LPRT"), // Specifies a long address and port to which the server should connect.
	LPSV("LPSV"), // Enter long passive mode.
	MDMT("MDMT"), // Return the last-modified time of a specified file.
	MIC ("MIC"),  // Integrity Protected Command
	MKD ("MKD"),  // Make directory.
	MLSD("MLSD"), // Lists the contents of a directory if a directory is named.
	MLST("MLST"), // Provides data about exactly the object named on its command line and no others.
	MODE("MODE"), // Sets the transfer mode (Stream, Block, or Compressed).
	NLST("NLST"), // Returns a list of file names in a specified directory.
	NOOP("NOOP"), // No operation (dummy packet; used mostly as keepalives).
	OPTS("OPTS"), // Select options for a feature.
	PASS("PASS"), // Authentication password.
	PASV("PASV"), // Enter passive mode.
	PBSZ("PBSZ"), // Protection Buffer Size.
	PORT("PORT"), // Specifies an address and port to which the server should connect.
	PROT("PROT"), // Data Channel Protection Level.
	PWD ("PWD"),  // Print working directory. Returns the current directory of the host.
	QUIT("QUIT"), // Disconnect.
	REIN("REIN"), // Re-initialize the connection.
	REST("REST"), // Restart transfer from the specified point.
	RETR("RETR"), // Retrieve a copy of the file.
	RMD ("RMD"),  // Remove a directory.
	RNFR("RNFR"), // Rename from.
	RNTO("RNTO"), // Rename to.
	SITF("SITF"), // Sends site specific commands to remote server.
	SIZE("SIZE"), // Return the size of a file.
	SMNT("SMNT"), // Mount file structure.
	STAT("STAT"), // Returns the current status.
	STOR("STOR"), // Accept data and store data as a file at the server site.
	STOU("SOUT"), // Store file uniquely.
	STRU("STRU"), // Set file transfer structure.
	SYST("SYST"), // Return system type.
	TYPE("TYPE"), // Sets the transfer mode (ASCII/binary).
	USER("USER"), // Authentication username.
	XCUP("XCUP"), // Change to the parent of the current working directory.
	XMKD("XMKD"), // Make directory.
	XPWD("XPWD"), // Print current working directory.
	YRCP("XRCP"),
	XRMD("XRMD"), // Remove directory.
	XRSQ("XRSQ"),
	XSEM("XSEM"), // Send, mail if cannot.
	XSEN("XSEN"); // Send to terminal.
	
	private String method;
	
	private Method(String method) {
		this.method = method;
	}
	/**
	 * Staticna metoda, ki ustvari nov objekt tipa Method.
	 *
	 * @param protocol Niz, ki vsebuje protokol
	 * @param method Niz, ki predstavlja uporabljeno metodo protokola
	 * @return Objekt, ki predstavlja metodo
	 * @throws IllegalArgumentException
	 * @see Method
	 */
	public static Method setMethod(String protocol, String method) throws IllegalArgumentException {
		if(protocol.equalsIgnoreCase("HTTP")) {
			switch (method.toUpperCase()) {
			case "GET":		return GET;
			case "HEAD":	return HEAD;
			case "POST":	return POST;
			case "PUT": 	return PUT;
			case "DELETE":	return DELETE;
			case "TRACE": 	return TRACE;
			case "OPTIONS": return OPTIONS;
			case "CONNECT": return CONNECT;
			case "PATCH": 	return PATCH;
			default: 		throw new IllegalArgumentException("Neznana metoda");
			}
		} else if(protocol.equals("FTP")) {
			switch (method.toUpperCase()) {
			case "ABOR": return ABOR;
			case "ACCT": return ACCT;
			case "ADAT": return ADAT;
			case "ALLO": return ALLO;
			case "APPE": return APPE;
			case "AUTH": return AUTH;
			case "CCC" : return CCC;
			case "CDUP": return CDUP;
			case "CONF": return CONF;
			case "CWD" : return CWD;
			case "DELE": return DELE;
			case "ENC" : return ENC;
			case "EPRT": return EPRT;
			case "EPSV": return EPSV;
			case "FEAT": return FEAT;
			case "HELP": return HELP;
			case "LANG": return LANG;
			case "LIST": return LIST;
			case "LPRT": return LPRT;
			case "LPSV": return LPSV;
			case "MDMT": return MDMT;
			case "MIC" : return MIC;
			case "MKD" : return MKD;
			case "MLSD": return MLSD;
			case "MLST": return MLST;
			case "MODE": return MODE;
			case "NLST": return NLST;
			case "NOOP": return NOOP;
			case "OPTS": return OPTS;
			case "PASS": return PASS;
			case "PASV": return PASV;
			case "PORT": return PORT;
			case "PROT": return PROT;
			case "PWD" : return PWD;
			case "QUIT": return QUIT;
			case "REIN": return REIN;
			case "REST": return REST;
			case "RETR": return RETR;
			case "RMD" : return RMD;
			case "RNFR": return RNFR;
			case "RNTO": return RNTO;
			case "SITF": return SITF;
			case "SIZE": return SIZE;
			case "SMNT": return SMNT;
			case "STAT": return STAT;
			case "STOR": return STOR;
			case "STOU": return STOU;
			case "STRU": return STRU;
			case "SYST": return SYST;
			case "TYPE": return TYPE;
			case "USER": return USER;
			case "XCUP": return XCUP;
			case "XMKD": return XMKD;
			case "XPWD": return XPWD;
			case "YRCP": return YRCP;
			case "XRMD": return XRMD;
			case "XRSQ": return XRSQ;
			case "XSEM": return XSEM;
			case "XSEN": return XSEN;
			default: throw new IllegalArgumentException("Ni še implementirano");
			}
		} else {
			throw new IllegalArgumentException("Neznan protokol");
		}
	}

	public String getMethod() {
		return method;
	}

	public String getProtocol() {
		switch (this) {
		case GET: case HEAD: case POST: case PUT: case DELETE: case TRACE: case OPTIONS: case CONNECT: 	case PATCH:
			return "HTTP";
		case ABOR: case ACCT: case ADAT: case ALLO: case APPE: case AUTH: case CCC : case CDUP: case CONF: case CWD : case DELE: case ENC : case EPRT: case EPSV: case FEAT: case HELP: case LANG: case LIST: case LPRT: case LPSV: case MDMT: case MIC : case MKD : case MLSD: case MLST: case MODE: case NLST: case NOOP: case OPTS: case PASS: case PASV: case PORT: case PROT: case PWD : case QUIT: case REIN: case REST: case RETR: case RMD : case RNFR: case RNTO: case SITF: case SIZE: case SMNT: case STAT: case STOR: case STOU: case STRU: case SYST: case TYPE: case USER: case XCUP: 	case XMKD: case XPWD: case YRCP: case XRMD: case XRSQ: case XSEM: case XSEN:
			return "FTP";

		}
	}
	
	@Override
	public String izpis() {
		return method;
	}
	@Override
	public String getKey() {
		return null;
	}
}
