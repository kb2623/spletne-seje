package org.sessionization.parser.datastruct;

import javax.persistence.*;
import java.util.List;

public class ClassTypes {

	public static String IntegerType = getType(Integer.class);
	public static String ListType = getType(List.class);

	public static String EntityType = getType(Entity.class);

	public static String IdType = getType(Id.class);
	public static String GeneratedValueType = getType(GeneratedValue.class);
	public static String GenerationTypeType = getType(GenerationType.class);

	public static String CascadeTypeType = getType(CascadeType.class);
	public static String OneToOneType = getType(OneToOne.class);
	public static String OneToManyType = getType(OneToMany.class);

	public static String EmbenddedType = getType(Embedded.class);

	public static String WebPageRequestAbsClass = WebPageRequestAbs.class.getName().replace(".", "/");

	public static String ListRequestsGType = "L" + List.class.getName().replace(".", "/") + "<" + RequestDump.getClassType() + ">;";

	public static String getType(Class c) {
		return "L" + c.getName().replace(".", "/") + ";";
	}
}
