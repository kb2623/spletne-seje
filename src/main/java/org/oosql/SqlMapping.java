package org.oosql;

import org.oosql.exception.MappingException;

/**
 * @param <I> Tip objekta, ki ga želimo zapisati v podatkovno bazo. Tip je omejen na primitivne podatkovne tipe, ter String. v nasprotnem primeru dobimo napako.
 * @param <O> Tip obkekta, ki ga zapišemo v podatkovno bazo
 */
public interface SqlMapping<I, O> {
	/**
	 * Metoda, ki pretvori objekt <code>in</code> v obliko za zapis v podatkovno bazo.
	 *
	 * @param in Objekt ki za želimo zapisati v podatkovno bazo
	 * @return Objekt v obliki za zapis v podatkovno bazo
	 */
	O inMapping(I in);
	/**
	 * Metoda, ki pretvori objekt <code>in</code> v objekt, ki ga imamo v razredu.
	 *
	 * @param in Objekt iz podatkovne baze
	 * @return Objekt, ki se nahaja v razredu
	 */
	I outMapping(O in);
}
