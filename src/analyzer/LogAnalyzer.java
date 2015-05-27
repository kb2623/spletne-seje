package analyzer;

import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.regex.Pattern;

import parser.FieldType;

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
            // TODO preveri kaksna locila uporablja datoteka, èe so ".., .., ....." je to IIS, èe so "... ... ...." je to NCSA, v nasprotnem primeru formata log datoteke ne poznamo
        }
    }
    /**
     * Metoda preveri ali se v combined log frmatu nahaja
     * polje s piskotkom
     *
     * @return Ali je prisotno polje s piskotkom
     */
    public boolean hasCookie() throws IOException {
        // FIXME Razdeli vrstico in poslji zadnji del vrstice v spodnjo metodo
        return Regex.isCookie(null);
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

    private static class Regex {
        /**
         *
         * @param input
         * @return
         */
        public static boolean isCookie(String input) {
            Pattern pattern = Pattern.compile("([^ \\\"\\[\\{\\(\\]\\}\\)<>/\\\\?=@,;:]+=[\\x21\\x23-\\x2B\\x2D-\\x3A\\x3C-\\x5B\\x5D-\\x7E]*;)*([^ \\\"\\[\\{\\(\\]\\}\\)<>/\\\\?=@,;:]+=[\\x21\\x23-\\x2B\\x2D-\\x3A\\x3C-\\x5B\\x5D-\\x7E]*){1}");
            return pattern.matcher(input).find();
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
    }

}
