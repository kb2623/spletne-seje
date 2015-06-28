package org.spletneseje.parser.datastruct;

import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;

import java.util.*;

public class WebPage {

    private EnumMap<FieldType, Field> keyList;
    private EnumMap<FieldType, List<Field>> valeList;

    public WebPage(ParsedLine line) throws NullPointerException {
        if (line == null) throw new NullPointerException();
        keyList = new EnumMap<>(FieldType.class);
        valeList = new EnumMap<>(FieldType.class);
        for (Field field : line) {
            if (field.getKey() == null) {
                List<Field> list = new ArrayList<>();
                list.add(field);
                valeList.put(field.getFieldType(), list);
            } else {
                keyList.put(field.getFieldType(), field);
            }
        }
    }

    public boolean add(ParsedLine line) throws NoSuchElementException {
        if (!line.isResurse()) return false;
        for (Field entry : line) {
            if (keyList.containsKey(entry.getFieldType())) {
                if (!keyList.get(entry.getFieldType()).equals(entry)) System.err.println("Error in comparison [" + keyList.get(entry.getFieldType()).toString() + "] == [" + entry.toString() + "]!!!");
            } else if (valeList.containsKey(
                    entry.getFieldType())) {
                List<Field> list = valeList.get(entry.getFieldType());
                list.add(entry);
            } else {
                throw new NoSuchElementException(entry.toString());
            }
        }
        return true;
    }

    public int size() {
        return valeList.get(0).size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Keys: ").append('\n');
        keyList.values().forEach(e -> builder.append('\t').append('[').append(e.toString()).append(']'));
        builder.append('\n');
        builder.append("Values: ").append('\n');
        List<Iterator<Field>> its = new ArrayList<>();
        valeList.values().forEach(e -> its.add(e.iterator()));
        while (its.get(0).hasNext()) {
            its.forEach(e -> builder.append('\t').append('[').append(e.next().toString()).append(']'));
            builder.append('\n');
        }
        return builder.toString();
    }
}
