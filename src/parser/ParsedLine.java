package parser;

import java.util.EnumMap;

import fields.Field;
import fields.ncsa.RequestLine;
import fields.w3c.UriStem;

public class ParsedLine {

	private EnumMap<FieldType, Field> map;
	/**
	 * 
	 * @param map 
	 */
	public ParsedLine(EnumMap<FieldType, Field> map) {
		this.map = map;
	}
	/**
	 * 
	 * @return 
	 */
	public String getKey() {
		StringBuilder builder = new StringBuilder();
//		for(Field f : map.values()) {
//			if(f.getKey() != null) {
//				builder.append(f.getKey());
//			}
//		}
		map.values().stream().filter((f) -> (f.getKey() != null)).forEach((f) -> builder.append(f.getKey()));
		return builder.toString();
	}
	/**
	 *
	 * @return
	 */
	public EnumMap<FieldType, Field> getMap() {
		return map;
	}
	/**
	 *
	 * @return
	 */
	public boolean isCrawler() {
		//TODO Tukaj moraš preveriti zahtevan resurs in/ali User Agent String
		return false;
	}

	public boolean isResurse() {
		String extension = getExtension();
		switch ((extension != null) ? extension : "") {
		case "php": case "png": case "css": case "js": case "jpg": case "txt": case "gif": case "ico": case "xml": case "csv":
			return true;
		default:
			return false;
		}
	}

	private String getExtension() {
		Field f = map.get(FieldType.RequestLine);
		if(f != null) {
			return ((RequestLine) f).getExtension();
		} else {
			f = map.get(FieldType.UriStem);
			return ((UriStem) f).getExtension();
		}
	}

}
