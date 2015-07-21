package org.sessionization.fields.ncsa;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;
import org.oosqljet.annotation.Column;

public class SizeOfTransfer implements Field {

    @Column private int size;

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
