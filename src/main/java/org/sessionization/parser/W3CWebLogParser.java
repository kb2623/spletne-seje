package org.sessionization.parser;

import org.sessionization.parser.datastruct.ParsedLine;
import org.sessionization.parser.fields.w3c.MetaData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Parser za formate: Extended Log Format
 *
 * @author klemen
 */
public class W3CWebLogParser extends WebLogParserW3C {

	public W3CWebLogParser() {
		super();
	}

	public W3CWebLogParser(Locale locale, File[] file) throws FileNotFoundException {
		super(locale, file);
	}

	public W3CWebLogParser(Locale locale, File[] file, List<LogFieldType> ignore) throws FileNotFoundException {
		super(locale, file, ignore);
	}

	@Override
	public ParsedLine parseLine() throws ParseException {
		try {
			Scanner tokens = getLine();
			if (tokens.hasNext(LogFieldTypeImp.MetaData.getPattern())) {
				MetaData metadata = (MetaData) LogFieldTypeImp.MetaData.parse(tokens, this);
				if (metadata.getMetaData().equals("Fields")) {
					super.setFieldType(LogFormats.ParseCmdArgs.create(metadata.getValues()));
				}
				return new ParsedLine(new LogField[]{metadata});
			} else {
				return super.parseLine(tokens);
			}
		} catch (IOException e) {
			throw new ParseException("Bad line!!!", getPos());
		}
	}
}
