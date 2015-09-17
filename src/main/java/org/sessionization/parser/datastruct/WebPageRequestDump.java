package org.sessionization.parser.datastruct;

import org.objectweb.asm.*;
import org.sessionization.fields.FieldType;

import java.util.ArrayList;
import java.util.List;

public class WebPageRequestDump implements Opcodes {

	private static String CLASSNAME = "org/sessionization/fields/WebPageRequest";
	private static String FILENAME = "WebPageRequest.java";

	public static byte[] dump(List<FieldType> fieldTypes) {
		List<FieldType> fieldList = getFields(fieldTypes);

		ClassWriter cw = new ClassWriter(0);
		FieldVisitor fv;
		MethodVisitor mv;
		AnnotationVisitor av0;
		// Inicializacija dostopnih pravic, imana in dedovanih razredov
		cw.visit(52, ACC_PUBLIC + ACC_SUPER, CLASSNAME, null, ClassTypes.WebPageRequestAbsClass, null);
		// Inicializacija datoeke razreda
		cw.visitSource(FILENAME, null);
		// Dodajanje @Entity
		{
			av0 = cw.visitAnnotation(ClassTypes.EntityType, true);
			av0.visitEnd();
		}
		// Inicializacija polja id, ter dodajanje annotacij @Id in @GeneratedValue
		{
			fv = cw.visitField(ACC_PRIVATE, "id", ClassTypes.IntegerType, null, null);
			{
				av0 = fv.visitAnnotation(ClassTypes.IdType, true);
				av0.visitEnd();
			}
			{
				av0 = fv.visitAnnotation(ClassTypes.GeneratedValueType, true);
				av0.visitEnum("strategy", ClassTypes.GenerationTypeType, "IDENTITY");
				av0.visitEnd();
			}
			fv.visitEnd();
		}

		// todo

		cw.visitEnd();
		return cw.toByteArray();
	}

	private static List<FieldType> getFields(List<FieldType> fieldTypes) {
		List<FieldType> retList = new ArrayList<>((int) (fieldTypes.size() / 2));
		for (FieldType type : fieldTypes) {
			if (type.isKey()) {
				retList.add(type);
			}
		}
		return retList;
	}

	public static String getFileName() {
		return FILENAME;
	}

	public static String getClassName() {
		return CLASSNAME.replaceAll("/", ".");
	}
}
