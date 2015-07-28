package org.oosql.exception;

public class ColumnAnnotationException extends OosqlException {
    public ColumnAnnotationException(String message) {
        super("Column annotation " + message);
    }
}
