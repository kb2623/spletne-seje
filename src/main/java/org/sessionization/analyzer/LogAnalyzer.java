package org.sessionization.analyzer;

import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.List;
import java.util.regex.Pattern;

import org.sessionization.fields.Cookie;

public class LogAnalyzer {

	private FileInputStream stream;
	private BufferedReader reader;
	private LogFileType logFileType = LogFileType.UNKNOWN;
	/**
	 *
	 * @param path
	 * @throws FileNotFoundException
	 */
	public LogAnalyzer(String path) throws IOException {
		stream = new FileInputStream(path);
		reader = new BufferedReader(new InputStreamReader(stream));
		if (reader.readLine().charAt(0) == '#') {
			logFileType = LogFileType.W3C;
		} else {
			// TODO preveri kaksna locila uporablja datoteka, ce so ".., .., ....." je to IIS, ce so "... ... ...." je to NCSA, v nasprotnem primeru formata log datoteke ne poznamo
		}
	}
	/**
	 * Metoda preveri ali se v combined log frmatu nahaja
	 * polje s piskotkom
	 *
	 * @return Ali je prisotno polje s piskotkom
	 */
	public static List<String> hasCombinedCookie() {
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