package org.sessionization.parser.datastruct;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;
import org.sessionization.fields.ncsa.RequestLine;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class WebPageRequestAbs {

	public abstract boolean add(ParsedLine line);

	public abstract String getKey();
}
