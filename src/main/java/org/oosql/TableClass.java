package org.oosql;

import org.oosql.annotation.Table;

public class TableClass {

    private Class c;

    public TableClass(Class c) {
        this.c = c;
    }

    public String getName() {
        return getAnno().name().isEmpty() ? c.getSimpleName() : getAnno().name();
    }

    public Table getAnno() {
        return (Table) c.getAnnotation(Table.class);
    }

}
