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

    public Class getType() {
        return c;
    }

    public Table getAnnotation() {
        return (Table) c.getAnnotation(Table.class);
    }

}
