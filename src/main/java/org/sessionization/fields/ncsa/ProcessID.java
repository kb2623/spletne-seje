package org.sessionization.fields.ncsa;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;
import org.oosql.annotation.Column;

public class ProcessID implements Field {

    @Column 
	 private int id;

    public ProcessID(String niz) {
        id = Integer.valueOf(niz);
    }

    public int getId() {
        return id;
    }

    @Override
    public String izpis() {
        return id + "";
    }

    @Override
    public FieldType getFieldType() {
        return FieldType.ProcesID;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ProcessID && id == ((ProcessID) o).getId();
    }

    @Override
    public String toString() {
        return id + "";
    }
}
