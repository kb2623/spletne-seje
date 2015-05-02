package org.kohsuke.args4j;

import java.util.Locale;
import org.kohsuke.args4j.spi.OptionHandler;

/**
 * Signals an error in the user input.
 *
 * @author Kohsuke Kawaguchi
 */
public class CmdLineException extends Exception {
	private static final long serialVersionUID = -8574071211991372980L;

    private CmdLineParser parser = null;
    
    /** 
     * The optional localized message.
     * @see Throwable#getLocalizedMessage() 
     */
    private String localizedMessage = null;
    
    /**
	 * @param message
     * @deprecated
     *      Use {@link #CmdLineException(CmdLineParser, String)}
     */
    public CmdLineException(String message) {
        super(message);
    }

    /**
	 * @param message
	 * @param cause
     * @deprecated
     *      Use {@link #CmdLineException(CmdLineParser, String, Throwable)}
     */
    public CmdLineException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * @param cause
     * @deprecated
     *      Use {@link #CmdLineException(CmdLineParser, Throwable)}
     */
    public CmdLineException(Throwable cause) {
        super(cause);
    }
    
    public CmdLineException(CmdLineParser parser, Localizable message, String... args) {
        super(message.formatWithLocale(Locale.ENGLISH, (Object[])args));
        this.localizedMessage = message.format((Object[])args);
        this.parser = parser;
    }
    
    /**
	 * @param parser
	 * @param message
     * @deprecated
     *      Use {@link #CmdLineException(org.kohsuke.args4j.CmdLineParser, Localizable, java.lang.String...) }
     */
    public CmdLineException(CmdLineParser parser, String message) {
        super(message);
        this.parser = parser;
    }

    public CmdLineException(CmdLineParser parser, String message, Throwable cause) {
        super(message, cause);
        this.parser = parser;
    }

    public CmdLineException(CmdLineParser parser, Throwable cause) {
        super(cause);
        this.parser = parser;
    }

    @Override
    public String getLocalizedMessage() {
        if (localizedMessage != null) {
            return localizedMessage;
        } else {
            return getMessage();
        }
    }
    
    /**
     * Obtains the {@link CmdLineParser} that triggered an exception.
     *
     * <p>
     * Unless you have legacy {@link OptionHandler} that doesn't pass in this information
     * when it throws an exception, this method should always return a non-null value.
	 * @return 
     */
    public CmdLineParser getParser() {
        return parser;
    }
}
