package org.sessionization.parser.datastruct;

import org.objectweb.asm.*;
import org.sessionization.fields.FieldType;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

public class WebPageRequestDump implements Opcodes {

	private static String CLASSNAME = "org/sessionization/fields/WebPageRequest";
	private static String FILENAME = "WebPageRequest.java";

	public static byte[] dump(List<FieldType> fieldTypes) {
		List<FieldType> fieldList = getFields(fieldTypes);
		int lineCount = 0;
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
		// Inicializcija ostalih polj, ki so del identifikacije uporabnika
		for (FieldType f : fieldList) {
			fv = cw.visitField(ACC_PRIVATE, getFieldName(f), f.getType(), null, null);
			Class c = f.getClass();
			if (c.isAnnotationPresent(Entity.class)) {
				av0 = fv.visitAnnotation(ClassTypes.OneToOneType, true);
				{
					AnnotationVisitor av1 = av0.visitArray("cascade");
					av1.visitEnum(null, ClassTypes.CascadeTypeType, "ALL");
					av1.visitEnd();
				}
				av0.visitEnd();
			} else if (c.isAnnotationPresent(Embeddable.class)) {
				av0 = fv.visitAnnotation(ClassTypes.EmbenddedType, true);
				av0.visitEnd();
			}
			fv.visitEnd();
		}
		// Inicializacija tabele z zahtevami
		{
			fv = cw.visitField(ACC_PRIVATE, "requests", ClassTypes.ListType, ClassTypes.ListRequestsGType, null);
			{
				av0 = fv.visitAnnotation(ClassTypes.OneToManyType, true);
				{
					AnnotationVisitor av1 = av0.visitArray("cascade");
					av1.visitEnum(null, ClassTypes.CascadeTypeType, "ALL");
					av1.visitEnd();
				}
				av0.visitEnd();
			}
			fv.visitEnd();
		}
		// todo dodaj construktorje
		// getId
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getId", "()" + ClassTypes.IntegerType + ";", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(55, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, CLASSNAME, "id", ClassTypes.IntegerType);
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "L" + CLASSNAME + ";", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		// setId
		{
			mv = cw.visitMethod(ACC_PUBLIC, "setId", "(" + ClassTypes.IntegerType + ")V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(59, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(PUTFIELD, CLASSNAME, "id", ClassTypes.IntegerType);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(lineCount, l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", "L" + CLASSNAME + ";", null, l0, l2, 0);
			mv.visitLocalVariable("id", ClassTypes.IntegerType, null, l0, l2, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
		// todo ostali setterji, getterji in metode
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

	private static String getFieldName(FieldType fieldType) {
		String s = fieldType.getClassType().getTypeName();
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}

	public static String getFileName() {
		return FILENAME;
	}

	public static String getClassName() {
		return CLASSNAME.replaceAll("/", ".");
	}
}
