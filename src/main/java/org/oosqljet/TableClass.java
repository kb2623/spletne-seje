package org.oosqljet;

import org.oosqljet.annotation.Table;

public class TableClass {

    private Class c;

    public TableClass(Class c) {
        this.c = c;
    }

    public String getName() {
        return getAnnotation().name().isEmpty() ? c.getSimpleName() : getAnnotation().name();
    }

    public Table getAnnotation() {
        return (Table) c.getAnnotation(Table.class);
    }

}
