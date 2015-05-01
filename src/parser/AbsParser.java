package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
/**
 * Razred za parsanje log datoteke
 *
 * @author klemen
 */
public abstract class AbsParser {
	/**
	 * Vrstica v datoteki
	 */
	private int pos;
	/**
	 * Datoteka za branje
	 *
	 * @see BufferedReader
	 */
	private BufferedReader file;
	/**
	 * Osnovni konstruktor, za prevzete nastavitve:<p>
	 * pozicija = 0<p>
	 * datoteka = null
	 */
	public AbsParser() {
		pos = 0;
		file = null;
	}
	/**
	 * Metoda za odpiranje datoteke
	 *
	 * @param path Pot do datoteke, predstavljena z nizom
	 * @throws FileNotFoundException Napaka datoteka ne obstaja
	 */
	public void openFile(String path) throws FileNotFoundException {
		file = new BufferedReader(new FileReader(path));
	}
	/**
	 * Metoda namenjena testiranju, ki spremeni niz
	 * v vhodni tok
	 *
	 * @param input
	 * @throws FileNotFoundException
	 */
	@Deprecated
	public void openFile(StringReader input) throws FileNotFoundException {
		file = new BufferedReader(input);
	}
	/**
	 * Metoda ki vrne celotno vrstico, ki jo je potrebno še parsati
	 *
	 * @return List of <code>Strings</code>
	 * @throws IOException Napaka pri branju vrstice
	 * @see String
	 */
	public String getLine() throws IOException {
		pos++;
		return file.readLine();
	}
	/**
	 * Metoda za parsanje datoteke
	 *
	 * @return
	 * @throws ParseException Napaka pri branju datoteke
	 * @throws IOException
	 * @throws NullPointerException
	 * @see fields.Field
	 */
	public abstract ParsedLine parseLine() throws ParseException, NullPointerException, IOException;
	/**
	 * Metoda za zapiranje datoteke
	 *
	 * @throws IOException Napaka pri zapiranju datoteke
	 */
	public void closeFile() throws IOException {
		file.close();
	}

	public int getPos() {
		return pos;
	}
}
