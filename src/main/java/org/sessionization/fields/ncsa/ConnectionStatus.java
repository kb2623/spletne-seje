package org.sessionization.fields.ncsa;

import org.oosqljet.SqlMapping;
import org.oosqljet.annotation.Entry;
import org.oosqljet.annotation.Table;
import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

@Table public enum ConnectionStatus implements Field, SqlMapping<ConnectionStatus, Integer> {

    KeepAliveResponse {
        @Override
        public Integer inMaping(ConnectionStatus connectionStatus) {
            // TODO
            return null;
        }

        @Override
        public String izpis() {
            return "+";
        }
    },
    CloseResponse {
        @Override
        public Integer inMaping(ConnectionStatus connectionStatus) {
            // TODO
            return null;
        }

        @Override
        public String izpis() {
            return "-";
        }
    },
    Aborted {
        @Override
        public Integer inMaping(ConnectionStatus connectionStatus) {
            // TODO
            return null;
        }

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
    public String toString() {
        return izpis();
    }

    @Override
    public FieldType getFieldType() {
        return FieldType.ConnectionStatus;
    }

    @Override
    public ConnectionStatus outMaping(Integer in) {
        // TODO
        return null;
    }
}
