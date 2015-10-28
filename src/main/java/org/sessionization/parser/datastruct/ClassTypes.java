package org.sessionization.parser.datastruct;

import org.sessionization.fields.LogField;

import javax.persistence.*;
import java.util.Iterator;
import java.util.List;

public class ClassTypes {

	public static String ObjectClass = Object.class.getName().replace(".", "/");
	public static String ObjectType = getType(Object.class);
	public static String ClassType = getType(Class.class);
	public static String StringType = getType(String.class);
	public static String IntegerType = getType(Integer.class);
	public static String IntegerClass = Integer.class.getName().replace(".", "/");
	public static String ListType = getType(List.class);
	public static String ListClass = List.class.getName().replace(".", "/");
	public static String IteratorType = getType(Iterator.class);
	public static String IteratorClass = Iterator.class.getName().replace(".", "/");
	public static String StringBuilderClass = StringBuilder.class.getName().replace(".", "/");
	public static String StringBuilderType = getType(StringBuilder.class);

	public static String EntityType = getType(Entity.class);

	public static String IdType = getType(Id.class);
	public static String GeneratedValueType = getType(GeneratedValue.class);
	public static String GenerationTypeType = getType(GenerationType.class);

	public static String CascadeTypeType = getType(CascadeType.class);
	public static String OneToOneType = getType(OneToOne.class);
	public static String OneToManyType = getType(OneToMany.class);

	public static String EmbenddedType = getType(Embedded.class);

	public static String ListResoucesGType = "L" + List.class.getName().replace(".", "/") + "<" + ResourceDump.getClassType() + ">;";

	public static String PageViewAbsType = PageViewAbs.class.getName().replace(".", "/");
	public static String ResourceAbsClass = ResourceAbs.class.getName().replace(".", "/");

	public static String ParsedLineType = getType(ParsedLine.class);
	public static String ParsedLineClass = ParsedLine.class.getName().replace(".", "/");

	public static String FieldClass = LogField.class.getName().replace(".", "/");

	public static String getType(Class c) {
		return "L" + c.getName().replace(".", "/") + ";";
	}
}
