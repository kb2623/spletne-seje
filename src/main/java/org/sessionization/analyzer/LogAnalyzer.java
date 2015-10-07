package org.sessionization.analyzer;

import org.sessionization.fields.Cookie;
import org.sessionization.fields.FieldType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LogAnalyzer {

	private FileInputStream stream;
	private BufferedReader reader;
	private LogFileType logFileType = LogFileType.UNKNOWN;
	private List<FieldType> fields;
	/**
	 *
	 * @param path
	 * @throws FileNotFoundException
	 */
	public LogAnalyzer(File[] file) throws IOException {
		// TODO dobimo lahko vec datotek, sedaj upostavamo samo eno
		stream = new FileInputStream(file[0]);
		reader = new BufferedReader(new InputStreamReader(stream));
		if (reader.readLine().charAt(0) == '#') {
			logFileType = LogFileType.W3C;
		} else {
			// TODO preveri kaksna locila uporablja datoteka, ce so ".., .., ....." je to IIS, ce so "... ... ...." je to NCSA, v nasprotnem primeru formata log datoteke ne poznamo
		}
		fields = new ArrayList<>();
	}
	/**
	 *
	 * @return
	 */
	public List<FieldType> getFields() {
		return fields;
	}
	/**
	 * Metoda preveri ali se v combined log frmatu nahaja
	 * polje s piskotkom
	 *
	 * @return Ali je prisotno polje s piskotkom
	 */
	public static String[] hasCombinedCookie() {
		// FIXME metoda mora uporabiti metodo isCookie(), kjer preverimo ali ima Combine format na koncu podatke o piskotku
		// Zacasna implementacija
		return null;
	}
	/**
	 *
	 * @param field
	 * @return
	 * @throws IOException
	 */
	public boolean isCookie(String field) throws IOException {
		return Pattern.compile(Cookie.patteren()).matcher(field).find();
	}
	/**
	 *
	 * @param input
	 * @return
	 */
	public static boolean isIP(String input) {
		// FIXME Implementiraj preverjanje niza za IP stevilko. Tukaj bodi pozoren saj imamo lahko IPv4 ali pa IPv6
		return false;
	}
	/**
	 *
	 * @param input
	 * @return
	 */
	public static boolean isNumber(String input) {
		// FIXME Implementiraj preverjanje niza za numericno vrednost
		return false;
	}
	/**
	 *
	 * @param input
	 * @return
	 */
	public static boolean isAlfa(String input) {
		return !isNumber(input);
	}
	/**
	 * Metoda, ki vrne razred za branje datoteke, pred tem
	 * pa vrne pozicijo za branje na zacetek datoteke
	 *
	 * @return Razred za branje datoteke
	 */
	public BufferedReader getOpendFile() throws IOException {
		stream.getChannel().position(0);
		return new BufferedReader(new InputStreamReader(stream));
	}
	/**
	 *
	 * @return
	 */
	public LogFileType getLogFileType() {
		return logFileType;
	}

	public enum LogFileType {
		UNKNOWN,
		NCSA,
		W3C,
		IIS
	}

}