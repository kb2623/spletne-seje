package org.sessionization.parser.fields.ncsa;

import org.sessionization.parser.LogField;

public enum ConnectionStatus implements LogField {

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
			case "X":
				return Aborted;
			case "+":
				return KeepAliveResponse;
			case "-":
				return CloseResponse;
			default:
				return null;
		}
	}

	@Override
	public abstract String izpis();

	@Override
	public String toString() {
		return izpis();
	}
}
