package org.sessionization.fields;

public enum LogType {
	NCSA {
		@Override
		protected String parseCooki(String data) {
			return data.replace(';', ' ');
		}

		@Override
		protected String parseUserAgent(String data) {
			return data;
		}
	},
	W3C {
		@Override
		protected String parseCooki(String data) {
			data = data.replace(';', ' ');
			return data.replaceAll("\\+", "");
		}

		@Override
		protected String parseUserAgent(String data) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < data.length(); i++) {
				if (data.charAt(i) == '+') {
					if (i > 0 && data.charAt(i - 1) != '+') builder.append(' ');
					else builder.append('+');
				}
				else builder.append(data.charAt(i));
			}
			return builder.toString();
		}
	};
	/**
	 *
	 * @param data
	 * @return
	 */
	protected abstract String parseCooki(String data);
	/**
	 *
	 * @param data
	 * @return
	 */
	protected abstract String parseUserAgent(String data);
}
