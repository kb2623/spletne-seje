package org.sessionization.parser.datastruct;

import org.objectweb.asm.*;
import org.sessionization.fields.FieldType;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

public class WebPageRequestDump implements Opcodes {

	private static String CLASSNAME = "org/sessionization/fields/WebPageRequest";
	private static String CLASSTYPE = "L" + CLASSNAME + ";";
	private static String FILENAME = "WebPageRequest.java";

	public static byte[] dump(List<FieldType> fieldTypes) {
		List<FieldType> fieldList = getFields(fieldTypes);
		int lineCount = 8;
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
				lineCount++;
			}
			{
				av0 = fv.visitAnnotation(ClassTypes.GeneratedValueType, true);
				av0.visitEnum("strategy", ClassTypes.GenerationTypeType, "IDENTITY");
				av0.visitEnd();
				lineCount++;
			}
			fv.visitEnd();
			lineCount++;
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
				lineCount++;
			} else if (c.isAnnotationPresent(Embeddable.class)) {
				av0 = fv.visitAnnotation(ClassTypes.EmbenddedType, true);
				av0.visitEnd();
				lineCount++;
			}
			fv.visitEnd();
			lineCount++;
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
				lineCount++;
			}
			fv.visitEnd();
			lineCount++;
		}
		// konstruktor ()
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			lineCount++;
			mv.visitCode();
			Label l0 = new Label();
			lineCount++;
			mv.visitLabel(l0);
			mv.visitLineNumber(lineCount, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, ClassTypes.WebPageRequestAbsClass, "<init>", "()V", false);
			Label l1 = new Label();
			lineCount++;
			mv.visitLabel(l1);
			mv.visitLineNumber(lineCount, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ACONST_NULL);
			mv.visitFieldInsn(PUTFIELD, CLASSNAME, "id", ClassTypes.IntegerType);
			for (FieldType f : fieldList) {
				Label l2 = new Label();
				lineCount++;
				mv.visitLabel(l2);
				mv.visitLineNumber(lineCount, l2);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ACONST_NULL);
				mv.visitFieldInsn(PUTFIELD, CLASSNAME, getFieldName(f), f.getType());
			}
			Label l3 = new Label();
			lineCount++;
			mv.visitLabel(l3);
			mv.visitLineNumber(lineCount, l3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ACONST_NULL);
			mv.visitFieldInsn(PUTFIELD, CLASSNAME, "requests", ClassTypes.ListType);
			Label l4 = new Label();
			lineCount++;
			mv.visitLabel(l4);
			mv.visitLineNumber(lineCount, l4);
			mv.visitInsn(RETURN);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLocalVariable("this", CLASSTYPE, null, l0, l5, 0);
			mv.visitMaxs(2, 1);
			mv.visitEnd();
			lineCount++;
		}
		// todo dodaj ostale konstruktorje
		// getId
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getId", "()" + ClassTypes.IntegerType, null, null);
			lineCount++;
			mv.visitCode();
			Label l0 = new Label();
			lineCount++;
			mv.visitLabel(l0);
			mv.visitLineNumber(lineCount, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, CLASSNAME, "id", ClassTypes.IntegerType);
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", CLASSTYPE, null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
			lineCount++;
		}
		// setId
		{
			mv = cw.visitMethod(ACC_PUBLIC, "setId", "(" + ClassTypes.IntegerType + ")V", null, null);
			lineCount++;
			mv.visitCode();
			Label l0 = new Label();
			lineCount++;
			mv.visitLabel(l0);
			mv.visitLineNumber(lineCount, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(PUTFIELD, CLASSNAME, "id", ClassTypes.IntegerType);
			Label l1 = new Label();
			lineCount++;
			mv.visitLabel(l1);
			mv.visitLineNumber(lineCount, l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", CLASSTYPE, null, l0, l2, 0);
			mv.visitLocalVariable("id", ClassTypes.IntegerType, null, l0, l2, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
			lineCount++;
		}
		// Setterji in Getterji
		for (FieldType f : fieldList) {
			// Getter
			{
				mv = cw.visitMethod(ACC_PUBLIC, getGetterName(f), "()" + f.getType(), null, null);
				lineCount++;
				mv.visitCode();
				Label l0 = new Label();
				lineCount++;
				mv.visitLabel(l0);
				mv.visitLineNumber(lineCount, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, CLASSNAME, getFieldName(f), f.getType());
				mv.visitInsn(ARETURN);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLocalVariable("this", CLASSTYPE, null, l0, l1, 0);
				mv.visitMaxs(1, 1);
				mv.visitEnd();
				lineCount++;
			}
			// Setter
			{
				mv = cw.visitMethod(ACC_PUBLIC, getSetterName(f), "(" + f.getType() + ")V", null, null);
				lineCount++;
				mv.visitCode();
				Label l0 = new Label();
				lineCount++;
				mv.visitLabel(l0);
				mv.visitLineNumber(lineCount, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitFieldInsn(PUTFIELD, CLASSNAME, getFieldName(f), f.getType());
				Label l1 = new Label();
				lineCount++;
				mv.visitLabel(l1);
				mv.visitLineNumber(lineCount, l1);
				mv.visitInsn(RETURN);
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLocalVariable("this", CLASSTYPE, null, l0, l2, 0);
				mv.visitLocalVariable(getFieldName(f), f.getType(), null, l0, l2, 1);
				mv.visitMaxs(2, 2);
				mv.visitEnd();
				lineCount++;
			}
		}
		// Getter za tabelo
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getRequests", "()" + ClassTypes.ListType, ClassTypes.ListRequestsGType, null);
			lineCount++;
			mv.visitCode();
			Label l0 = new Label();
			lineCount++;
			mv.visitLineNumber(lineCount, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, CLASSNAME, "requests", ClassTypes.ListType);
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", CLASSTYPE, null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
			lineCount++;
		}
		// Setter za tabelo
		{
			mv = cw.visitMethod(ACC_PUBLIC, "setRequests", "(" + ClassTypes.ListType + ")V", "(" + ClassTypes.ListRequestsGType + ")V", null);
			lineCount++;
			mv.visitCode();
			Label l0 = new Label();
			lineCount++;
			mv.visitLabel(l0);
			mv.visitLineNumber(lineCount, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(PUTFIELD, CLASSNAME, "requests", ClassTypes.ListType);
			Label l1 = new Label();
			lineCount++;
			mv.visitLabel(l1);
			mv.visitLineNumber(lineCount, l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", CLASSTYPE, null, l0, l2, 0);
			mv.visitLocalVariable("requests", ClassTypes.ListType, ClassTypes.ListRequestsGType, l0, l2, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
			lineCount++;
		}
		// todo spremeni delovanje teh dveh metod
		{
			mv = cw.visitMethod(ACC_PUBLIC, "add", "(Lorg/sessionization/parser/datastruct/ParsedLine;)Z", null, null);
			lineCount += 2;
			mv.visitCode();
			Label l0 = new Label();
			lineCount++;
			mv.visitLabel(l0);
			mv.visitLineNumber(lineCount, l0);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", CLASSTYPE, null, l0, l1, 0);
			mv.visitLocalVariable("line", "Lorg/sessionization/parser/datastruct/ParsedLine;", null, l0, l1, 1);
			mv.visitMaxs(1, 2);
			mv.visitEnd();
			lineCount++;
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getKey", "()Ljava/lang/String;", null, null);
			lineCount += 2;
			mv.visitCode();
			Label l0 = new Label();
			lineCount++;
			mv.visitLabel(l0);
			mv.visitLineNumber(lineCount, l0);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", CLASSTYPE, null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
			lineCount++;
		}
		// todo dodaj metode equals in hashCode
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
		String s = fieldType.getClassType().getSimpleName();
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}

	private static String getGetterName(FieldType fieldType) {
		return "get" + fieldType.getClassType().getSimpleName();
	}

	private static String getSetterName(FieldType fieldType) {
		return "set" + fieldType.getClassType().getSimpleName();
	}

	public static String getFileName() {
		return FILENAME;
	}

	public static String getClassTName() {
		return CLASSNAME;
	}

	public static String getClassName() {
		return CLASSNAME.replace("/", ".");
	}
}
