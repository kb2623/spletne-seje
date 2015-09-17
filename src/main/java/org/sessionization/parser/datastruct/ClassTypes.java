package org.sessionization.parser.datastruct;

import javax.persistence.*;

public class ClassTypes {

	public static String IntegerType = getType(Integer.class.getName());

	public static String EntityType = getType(Entity.class.getName());

	public static String IdType = getType(Id.class.getName());
	public static String GeneratedValueType = getType(GeneratedValue.class.getName());
	public static String GenerationTypeType = getType(GenerationType.class.getName());

	public static String CascadeTypeType = getType(CascadeType.class.getName());
	public static String OneToOneType = getType(OneToOne.class.getName());
	public static String OneToManyType = getType(OneToMany.class.getName());

	public static String WebPageRequestAbsClass = WebPageRequestAbs.class.getName().replaceAll(".", "/");

	public static String getType(String className) {
		return "L" + className.replaceAll(".", "/") + ";";
	}
}
