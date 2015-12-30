package org.sessionization.parser;

public enum LogType {
	NCSA {
		@Override
		public String parseCooki(String data) {
			return data.replace(';', ' ');
		}

		@Override
		public String parseUserAgent(String data) {
			return data;
		}
	},
	W3C {
		@Override
		public String parseCooki(String data) {
			data = data.replace(';', ' ');
			return data.replaceAll("\\+", "");
		}

		@Override
		public String parseUserAgent(String data) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < data.length(); i++) {
				if (data.charAt(i) == '+') {
					if (i > 0 && data.charAt(i - 1) != '+') builder.append(' ');
					else builder.append('+');
				} else builder.append(data.charAt(i));
			}
			return builder.toString();
		}
	};

	/**
	 * @param data
	 * @return
	 */
	public abstract String parseCooki(String data);

	/**
	 * @param data
	 * @return
	 */
	public abstract String parseUserAgent(String data);
}
