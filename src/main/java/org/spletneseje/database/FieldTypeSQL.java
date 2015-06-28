package org.spletneseje.database;

public interface FieldTypeSQL {
    /**
     * Metoda vrne niz za izdelavo tabele
     *
     * @return
     *      <code>null</code> -> Ko polje ne potrebuje dodatne tabele za shranjevanje.
     *      <code>String[]</code> -> Enega ali več nizov, ki povejo kako se tabela izdela
     */
    default String[] createTableQuery() {
        return null;
    }
    /**
     * Metoda vrne niz za izdelavo indeksa, ki se uporablja za iskanje po tabeli
     *
     * @return
     *      <code>null</code> -> Ko polje ne potrebuje dodatne tabele za shranjevanje.
     *      <code>String[]</code> -> Enega ali več nizov, ki povejo kako se idekes izdela.
     */
    default String[] createIndexQuery() {
        return null;
    }
    /**
     * Metoda vrne niz za ustvarjanje vrstice v glavni tabeli
     *
     * @return
     *      <code>null</code> -> Ko polje ne potrebuje vrstice v glavni tabeli
     *      <code>String</code> -> Niz za ustvarjanje vrstice v glavni tabeli.
     */
    default String createTableConstraint() {
        return null;
    }
    /**
     *
     * @param level
     * @return
     */
    default String getTableName(int level) {
        return null;
    }
    /**
     *
     * @param level
     * @return
     */
    default String getIndexName(int level) {
        return null;
    }
}
