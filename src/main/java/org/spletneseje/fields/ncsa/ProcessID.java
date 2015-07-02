package org.spletneseje.fields.ncsa;

import org.spletneseje.database.annotation.Entry;
import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;

public class ProcessID implements Field{

    @Entry private int id;

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
