package org.spletneseje.fields.ncsa;


import org.spletneseje.database.annotation.Entry;
import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;

public class KeepAliveNumber implements Field {

    @Entry private int number;

    public KeepAliveNumber(String niz) {
        number = Integer.valueOf(niz);
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String izpis() {
        return number + "";
    }

    @Override
    public FieldType getFieldType() {
        return FieldType.KeepAliveNumber;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof KeepAliveNumber && number == ((KeepAliveNumber) o).getNumber();
    }

    @Override
    public String toString() {
        return number + "";
    }
}
