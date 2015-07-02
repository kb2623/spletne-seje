package org.spletneseje.fields;

import org.spletneseje.database.annotation.Table;

@Table public abstract class File implements Field {
    /**
     * Metoda, ki vrne koncnico zahtevanega resursa.
     *
     * @return Koncnica zahtevanega resursa.
     */
    public abstract String getExtension();
    /**
     * Metoda, ki preverja ali je zahteva po spletni strani ali po resursu za spletno stran.
     *
     * @return Zahteva resurs ali spletna stran.
     */
    public boolean isResource() {
        String extension = getExtension();
        switch ((extension != null) ? extension : "") {
        case "php": case "png": case "css": case "js": case "jpg": case "txt": case "gif": case "ico": case "xml": case "csv":
            return true;
        default:
            return false;
        }
    }
    /**
     * Metoda vrne query
     *
     * @return vrne <code>UriQuery</code>
     * @see UriQuery
     */
    public UriQuery getQuery() {
        return null;
    }
    /**
     *
     * @return
     */
    public abstract String file();

}
