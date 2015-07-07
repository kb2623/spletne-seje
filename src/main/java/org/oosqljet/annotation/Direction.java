package org.oosqljet.annotation;

public enum Direction {
    ASC {
		@Override
		public String toString() {
			return "ASC";
		}
	},
    DESC {
		@Override
		public String toString() {
			return "DESC";
		}
	}
}
