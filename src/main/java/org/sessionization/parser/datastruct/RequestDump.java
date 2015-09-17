package org.sessionization.parser.datastruct;

import org.objectweb.asm.*;
import org.sessionization.fields.FieldType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

public class RequestDump implements Opcodes {

	private static String CLASSNAME = "org/sessionization/fields/Request";
	private static String FILENAME = "Request.java";

	public static byte[] dump(List<FieldType> list) {
		ClassWriter cw = new ClassWriter(0);
		FieldVisitor fv;
		MethodVisitor mv;
		AnnotationVisitor av0;

		cw.visit(52, ACC_PUBLIC + ACC_SUPER, CLASSNAME, null, "java/lang/Object", null);

		cw.visitSource(FILENAME, null);

		{
			av0 = cw.visitAnnotation(ClassTypes.EntityType, true);
			av0.visitEnd();
		}
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

	public static String getTypeName() {
		return CLASSNAME;
	}

	public static String getFileName() {
		return FILENAME;
	}

	public static String getClassName() {
		return CLASSNAME.replaceAll("/", ".");
	}
}
