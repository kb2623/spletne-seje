package parser;

import java.io.*;
import java.text.ParseException;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Razred za parsanje log datoteke
 *
 * @author klemen
 */
public abstract class AbsParser implements Iterable<ParsedLine> {
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
		file = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
	}
	/**
	 * Metoda za nstavljanje ze odprte datoteke
	 *
	 * @param reader
	 * @throws FileNotFoundException
	 */
	public void openFile(BufferedReader reader) throws FileNotFoundException {
		file = reader;
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
	 * @return Obdelano vrstico
	 * @throws ParseException Napaka pri obdelavi datoteke
	 * @throws IOException Napaka pri branju datoeke
	 * @throws NullPointerException Lastnosti niso pravilno nstavljene
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
    /**
     * Metoda, ki vrne stevilko vrstice v kateri se nahaja parser
     *
     * @return Vrstica v kateri se nahaja parser
     */
	public int getPos() {
		return pos;
	}
	/**
	 * Foreach zanka z lambda izrazom
	 *
	 * @param consumer Akcija zapisana z lambda izrazom, ki se bo izvrsila nad vrstico
	 */
	@Override
	public void forEach(Consumer<? super ParsedLine> consumer) {
		try {
			for (ParsedLine line = parseLine(); line != null; line = parseLine()) consumer.accept(line);
		} catch (IOException | NullPointerException | ParseException e) {}
	}
	/**
     * Metoda, ki ustvari iterator za sprehod po datoteki
     *
     * @return Iterator za sprehod po datoteki
     */
    @Override
    public abstract Iterator<ParsedLine> iterator();

}
