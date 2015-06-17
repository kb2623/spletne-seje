package spletneseje.parser;

import spletneseje.fields.Field;
import spletneseje.parser.datastruct.ParsedLine;

import java.io.*;
import java.text.ParseException;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Razred za parsanje log datoteke
 *
 * @author klemen
 */
public abstract class AbsParser implements Iterable<ParsedLine>, AutoCloseable {
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
		pos = -1;
		file = null;
	}
	/**
	 * Konstruktor ki tudi opre datoteko
	 *
	 * @param path Pot do datoteke, predstavljena z nizom
	 * @throws FileNotFoundException Datoteka ne obstaja
	 */
	public AbsParser(String path) throws FileNotFoundException {
		file = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		pos = 0;
	}
	/**
	 * Konstruktor namenjen testiranju
	 *
	 * @param input Vhodni tok predstavljen z nizem
	 */
	@Deprecated
	public AbsParser(StringReader input) {
		file = new BufferedReader(input);
		pos = 0;
	}
	/**
	 * konstruktor ki uporabe že odprt tok
	 *
	 * @param file Datoteka predstavljena z vhodnim tokom
	 */
	public AbsParser(BufferedReader file) {
		this.file = file;
		pos = 0;
	}
	/**
	 * Metoda za odpiranje datoteke
	 *
	 * @param path Pot do datoteke, predstavljena z nizom
	 * @throws FileNotFoundException Datoteka ne obstaja
	 */
	public void openFile(String path) throws FileNotFoundException {
		file = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		pos = 0;
	}
	/**
	 * Metoda za nstavljanje že odprte datoteke
	 *
	 * @param reader Vhodni tok
	 */
	public void openFile(BufferedReader reader) {
		file = reader;
		pos = 0;
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
		pos = 0;
	}
	/**
	 * Metoda ki vrne celotno vrstico, ki jo je potrebno še parsati
	 *
	 * @return List of <code>Strings</code>
	 * @throws IOException Napaka pri branju vrstice
	 * @throws EOFException Konec datoteke ali strima
	 * @see String
	 */
	public String getLine() throws IOException {
		String line = file.readLine();
		if (line == null) throw new EOFException();
		pos++;
		return line;
	}
	/**
	 * Metoda za obdelavo vrstice do take mere da se vsi nizi shranjeni v instancah razredov
	 *
	 * @return Obdelano vrstico
	 * @throws ParseException Napaka pri obdelavi datoteke
	 * @throws IOException Napaka pri branju datoeke
	 * @throws NullPointerException Lastnosti niso pravilno nstavljene
	 * @see Field
	 * @see ParsedLine
	 */
	public abstract ParsedLine parseLine() throws ParseException, NullPointerException, IOException;
	/**
	 * Metoda za zapiranje datoteke
	 *
	 * @throws IOException Napaka pri zapiranju datoteke
	 */
	public void closeFile() throws IOException {
		file.close();
		pos = -1;
	}
    /**
     * Metoda, ki vrne stevilko vrstice v kateri se nahaja parser
     *
     * @return Vrstica v kateri se nahaja parser
     */
	public int getPos() {
		return pos;
	}

	@Override
	public void forEach(Consumer<? super ParsedLine> consumer) {
		try {
			for (ParsedLine line = parseLine(); line != null; line = parseLine()) consumer.accept(line);
		} catch (IOException | NullPointerException | ParseException ignored) {}
	}

    @Override
    public abstract Iterator<ParsedLine> iterator();

	@Override
	public void close() {
		try {
			file.close();
		} catch (IOException ignore) {}
		pos = -1;
	}
}
