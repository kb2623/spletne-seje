package org.sessionization.parser;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;
import org.sessionization.parser.datastruct.ParsedLine;

import java.io.*;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Razred za parsanje log datoteke
 *
 * @author klemen
 */
public abstract class AbsParser implements Iterable<ParsedLine>, AutoCloseable {
	/**
	 * Vrstica v datotekah
	 */
	private int pos = 0;
	/**
	 * Datoteke za branje
	 *
	 * @see BufferedReader
	 */
	private BufferedReader[] readers;
	/**
	 * Tabela, ki vsebuje vrste polji v log datoteki
	 *
	 * @see FieldType
	 */
	protected List<FieldType> fieldType = null;
	/**
	 * Osnovni konstruktor, za prevzete nastavitve:<p>
	 * pozicija = 0<p>
	 * datoteka = null
	 */
	public AbsParser() {
		readers = null;
	}
	/**
	 * Konstruktor ki tudi opre datoteko
	 *
	 * @param path Pot do datoteke, predstavljena z nizom
	 * @throws FileNotFoundException Datoteka ne obstaja
	 */
	public AbsParser(String path) throws FileNotFoundException {
		readers = new BufferedReader[] {
				new BufferedReader(new InputStreamReader(new FileInputStream(path)))
		};
	}
	/**
	 *
	 * @param file
	 * @throws FileNotFoundException
	 */
	public AbsParser(File[] file) throws FileNotFoundException {
		openFile(file);
	}
	/**
	 * Konstruktor namenjen testiranju
	 *
	 * @param input Vhodni tok predstavljen z nizem
	 */
	@Deprecated
	public AbsParser(StringReader input) {
		readers = new BufferedReader[] {
				new BufferedReader(input)
		};
	}
	/**
	 * konstruktor ki uporabe že odprt tok
	 *
	 * @param file Datoteka predstavljena z vhodnim tokom
	 */
	public AbsParser(BufferedReader file) {
		readers = new BufferedReader[] {
				file
		};
	}
	/**
	 *
	 * @param path
	 * @throws FileNotFoundException
	 */
	public void openFile(File[] files) throws FileNotFoundException {
		readers = new BufferedReader[files.length];
		for (int i = 0; i < files.length; i++) {
			readers[i] = new BufferedReader(new InputStreamReader(new FileInputStream(files[i])));
		}
	}
	/**
	 * Metoda za nstavljanje že odprte datoteke
	 *
	 * @param reader Vhodni tok
	 */
	public void openFile(BufferedReader reader) {
		readers = new BufferedReader[] {
				reader
		};
	}
	/**
	 * Metoda namenjena testiranju, ki spremeni niz
	 * v vhodni tok
	 *
	 * @param input
	 * @throws FileNotFoundException
	 */
	@Deprecated
	public void openFile(StringReader input) {
		readers = new BufferedReader[] {
				new BufferedReader(input)
		};
		if (pos > 0) pos = 0;
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
		StringBuilder builder = new StringBuilder();
		for (BufferedReader br : readers) {
			String tmp = br.readLine();
			if (tmp != null) {
				builder.append(br.readLine());
			} else {
				throw new EOFException();
			}
		}
		pos++;
		return builder.toString();
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
	public abstract ParsedLine parseLine() throws ParseException, NullPointerException, IOException, URISyntaxException;
	/**
	 * Metoda za zapiranje datoteke
	 *
	 * @throws IOException Napaka pri zapiranju datoteke
	 */
	public void closeFile() throws IOException {
		for (BufferedReader br : readers) br.close();
		pos = 0;
	}
	/**
	 * Metoda za nastavljanje tipov polji v log datoteki.
	 *
	 * @see FieldType
	 * @param fields Tipi polji v log datoteki
	 * @throws NullPointerException Ko je parameter <code>fields</code> enak null
	 */
	public void setFieldType(List<FieldType> fields) throws NullPointerException {
		if(fields == null) throw new NullPointerException();
		this.fieldType = fields;
	}
	/**
	 * Geter za seznam tipov polji v log datoteki
	 *
	 * @return Seznam tipov polji v datoteki
	 */
	public List<FieldType> getFieldType() {
		return fieldType;
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
		} catch (IOException | NullPointerException | ParseException | URISyntaxException ignored) {}
	}

	@Override
	public abstract Iterator<ParsedLine> iterator();

	@Override
	public void close() {
		try {
			closeFile();
		} catch (IOException ignore) {}
		pos = 0;
	}
}