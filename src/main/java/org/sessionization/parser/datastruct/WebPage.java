package org.sessionization.parser.datastruct;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

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
        if (!line.isResource()) return false;
        for (Field entry : line) {
            if (valeList.containsKey(entry.getFieldType())) {
                List<Field> list = valeList.get(entry.getFieldType());
                list.add(entry);
            } else if (!keyList.containsKey(entry.getFieldType())) {
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
        builder.append("Keys: ").append('\n').append('\t').append('[');
        for (Iterator<Field> it = iteratorKey(); it.hasNext(); ) builder.append(it.next().toString()).append(' ');
        builder.deleteCharAt(builder.length() - 1);
        builder.append(']').append('\n');
        builder.append("Values: ").append('\n');
        for (Iterator<List<Field>> lIt = iteratorValue(); lIt.hasNext(); ) {
            builder.append('\t').append('[');
            lIt.next().forEach(e -> builder.append(e.toString()).append(' '));
            builder.deleteCharAt(builder.length() - 1);
            builder.append(']').append('\n');
        }
        return builder.toString();
    }

    public Iterator<Field> iteratorKey() {
        return keyList.values().iterator();
    }

    public Iterator<List<Field>> iteratorValue() {
        return new Iterator<List<Field>>() {

            private List<Iterator<Field>> listIt;

            {
                listIt = new ArrayList<>(valeList.size());
                valeList.values().forEach(e -> listIt.add(e.iterator()));
            }

            @Override
            public boolean hasNext() {
                return listIt.size() > 0 && listIt.get(0).hasNext();
            }

            @Override
            public List<Field> next() {
                List<Field> list = new ArrayList<>(listIt.size());
                listIt.forEach(i -> list.add(i.next()));
                return list;
            }
        };
    }
}
