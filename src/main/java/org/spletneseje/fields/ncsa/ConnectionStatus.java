package org.spletneseje.fields.ncsa;

import org.spletneseje.database.annotation.Entry;
import org.spletneseje.database.annotation.Table;
import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;

@Table public enum ConnectionStatus implements Field {

    KeepAliveResponse {
        @Override
        public String izpis() {
            return "+";
        }
    },
    CloseResponse {
        @Override
        public String izpis() {
            return "-";
        }
    },
    Aborted {
        @Override
        public String izpis() {
            return "X";
        }
    };

    public static ConnectionStatus getConnectionStatus(String niz) {
        switch (niz != null ? niz : "") {
        case "X": return Aborted;
        case "+": return KeepAliveResponse;
        case "-": return CloseResponse;
        default: return null;
        }
    }

    @Override
    public abstract String izpis();

    @Override
    @Entry public String toString() {
        return izpis();
    }

    @Override
    public FieldType getFieldType() {
        return FieldType.ConnectionStatus;
    }

}
