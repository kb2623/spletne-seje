package org.sessionization.fields.ncsa;

import org.sessionization.fields.FieldType;
import org.oosql.annotation.Column;
import org.sessionization.fields.Field;

public class KeepAliveNumber implements Field {

    @Column 
	 private int number;

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
