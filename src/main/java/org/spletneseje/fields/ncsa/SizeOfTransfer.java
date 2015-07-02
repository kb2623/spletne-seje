package org.spletneseje.fields.ncsa;

import org.spletneseje.database.annotation.Entry;
import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;

public class SizeOfTransfer implements Field {

    @Entry private int size;

    public SizeOfTransfer(String niz) {
        size = Integer.valueOf(niz);
    }

    public int getSize() {
        return size;
    }

    @Override
    public String izpis() {
        return String.valueOf(size);
    }

    @Override
    public FieldType getFieldType() {
        return FieldType.SizeOfTransfer;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SizeOfTransfer && size == ((SizeOfTransfer) o).getSize();
    }

    @Override
    public String toString() {
        return String.valueOf(size);
    }
}
