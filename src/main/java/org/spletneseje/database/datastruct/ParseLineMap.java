package org.spletneseje.database.datastruct;

import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;
import org.spletneseje.parser.datastruct.ParsedLine;

import java.util.EnumMap;

public class ParseLineMap extends EnumMap<FieldType, Field> {

    public ParseLineMap(ParsedLine line) {
        super(FieldType.class);
        line.forEach(e -> super.put(e.getFieldType(), e));
    }
}
