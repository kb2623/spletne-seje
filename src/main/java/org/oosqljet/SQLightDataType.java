package org.oosqljet;

public enum SQLightDataType {
	INTEGER {
		@Override
		public String toString() {
			return "INTEGER";
		}
	},
	REAL {
		@Override
		public String toString() {
			return "REAL";
		}
	},
	TEXT {
		@Override
		public String toString() {
			return "TEXT";
		}
	},
	NUMERIC {
		@Override
		public String toString() {
			return "NUMERIC";
		}
	},
	BLOB {
		@Override
		public String toString() {
			return "BLOB";
		}
	};

	public static SQLightDataType makeDataType(String typeName) {
		switch (typeName) {
			case "boolean":
			case "Boolean":
				return NUMERIC;
			case "int":
			case "Integer":
			case "long":
			case "Long":
			case "byte":
			case "Byte":
			case "short":
			case "Short":
				return INTEGER;
			case "float":
			case "Float":
			case "double":
			case "Double":
				return REAL;
			case "char":
			case "Character":
			case "String":
				return TEXT;
			case "blob":
				return BLOB;
			default:
				return null;
		}
	}
}
