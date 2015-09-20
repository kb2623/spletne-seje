package org.sessionization.parser.datastruct;

import antlr.collections.AST;
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
		List<FieldType> fields = getFields(fieldTypes);
		int lineCount = 20;
		ClassWriter cw = new ClassWriter(0);
		FieldVisitor fv;
		MethodVisitor mv;
		AnnotationVisitor av0;
		/** Inicializacija dostopnih pravic, imana in dedovanih razredov */
		cw.visit(52, ACC_PUBLIC + ACC_SUPER, CLASSNAME, null, ClassTypes.WebPageRequestAbsClass, null);
		/** Inicializacija datoeke razreda */
		cw.visitSource(FILENAME, null);
		/** Dodajanje @Entity */
		{
			av0 = cw.visitAnnotation(ClassTypes.EntityType, true);
			av0.visitEnd();
		}
		/** Inicializacija polja id, ter dodajanje annotacij @Id in @GeneratedValue */
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
		/** Inicializcija ostalih polj, ki so del identifikacije uporabnika */
		for (FieldType f : fields) {
			fv = cw.visitField(ACC_PRIVATE, f.getFieldName(), f.getType(), null, null);
			Class c = f.getClassType();
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
			} else {
				av0 = fv.visitAnnotation(ClassTypes.EmbenddedType, true);
				av0.visitEnd();
				lineCount++;
			}
			fv.visitEnd();
			lineCount++;
		}
		/** Inicializacija tabele z zahtevami */
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
		/** konstruktor () */
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
			for (FieldType f : fields) {
				Label l2 = new Label();
				lineCount++;
				mv.visitLabel(l2);
				mv.visitLineNumber(lineCount, l2);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ACONST_NULL);
				mv.visitFieldInsn(PUTFIELD, CLASSNAME, f.getFieldName(), f.getType());
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
		/** getId */
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
		/** setId */
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
		/** Setterji in Getterji */
		for (FieldType f : fields) {
			/** Getter */
			{
				mv = cw.visitMethod(ACC_PUBLIC, f.getGetterName(), "()" + f.getType(), null, null);
				lineCount++;
				mv.visitCode();
				Label l0 = new Label();
				lineCount++;
				mv.visitLabel(l0);
				mv.visitLineNumber(lineCount, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, CLASSNAME, f.getFieldName(), f.getType());
				mv.visitInsn(ARETURN);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLocalVariable("this", CLASSTYPE, null, l0, l1, 0);
				mv.visitMaxs(1, 1);
				mv.visitEnd();
				lineCount++;
			}
			/** Setter */
			{
				mv = cw.visitMethod(ACC_PUBLIC, f.getSetterName(), "(" + f.getType() + ")V", null, null);
				lineCount++;
				mv.visitCode();
				Label l0 = new Label();
				lineCount++;
				mv.visitLabel(l0);
				mv.visitLineNumber(lineCount, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitFieldInsn(PUTFIELD, CLASSNAME, f.getFieldName(), f.getType());
				Label l1 = new Label();
				lineCount++;
				mv.visitLabel(l1);
				mv.visitLineNumber(lineCount, l1);
				mv.visitInsn(RETURN);
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLocalVariable("this", CLASSTYPE, null, l0, l2, 0);
				mv.visitLocalVariable(f.getFieldName(), f.getType(), null, l0, l2, 1);
				mv.visitMaxs(2, 2);
				mv.visitEnd();
				lineCount++;
			}
		}
		/** Getter za tabelo */
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getRequests", "()" + ClassTypes.ListType, "()" + ClassTypes.ListRequestsGType, null);
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
		/** Setter za tabelo */
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
		/** Metoda add() */
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
		/** Metoda getKey() */
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
		/** Metoda hashCode() */
		{
			mv = cw.visitMethod(ACC_PUBLIC, "hashCode", "()I", null, null);
			lineCount += 2;
			mv.visitCode();
			Label l0 = new Label();
			lineCount++;
			mv.visitLabel(l0);
			mv.visitLineNumber(lineCount, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, "getId", "()" + ClassTypes.IntegerType, false);
			Label l1 = new Label();
			mv.visitJumpInsn(IFNULL, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, "getId", "()" + ClassTypes.IntegerType, false);
			mv.visitMethodInsn(INVOKEVIRTUAL, ClassTypes.IntegerClass, "hashCode", "()I", false);
			Label l2 = new Label();
			mv.visitJumpInsn(GOTO, l2);
			mv.visitLabel(l1);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitLabel(l2);
			mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{INTEGER});
			mv.visitVarInsn(ISTORE, 1);
			Label l3 = null;
			boolean first = true;
			for (FieldType f : fields) {
				Label ll1 = new Label();
				lineCount++;
				mv.visitLabel(ll1);
				mv.visitLineNumber(lineCount, ll1);
				mv.visitIntInsn(BIPUSH, 31);
				mv.visitVarInsn(ILOAD, 1);
				mv.visitInsn(IMUL);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, f.getGetterName(), "()" + f.getClassType(), false);
				Label ll2 = new Label();
				mv.visitJumpInsn(IFNULL, ll2);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, f.getGetterName(), "()" + f.getClassType(), false);
				mv.visitMethodInsn(INVOKEVIRTUAL, f.getClassClass(), "hashCode", "()I", false);
				Label ll3 = new Label();
				mv.visitJumpInsn(GOTO, ll3);
				mv.visitLabel(ll2);
				if (first) {
					mv.visitFrame(F_FULL, 2, new Object[]{CLASSNAME, INTEGER}, 1, new Object[]{INTEGER});
					l3 = ll1;
					first = false;
				} else {
					mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{INTEGER});
				}
				mv.visitInsn(ICONST_0);
				mv.visitLabel(ll3);
				mv.visitFrame(F_FULL, 2, new Object[]{CLASSNAME, INTEGER}, 2, new Object[]{INTEGER, INTEGER});
				mv.visitInsn(IADD);
				mv.visitVarInsn(ISTORE, 1);
			}
			Label l6 = new Label();
			lineCount++;
			mv.visitLabel(l6);
			mv.visitLineNumber(lineCount, l6);
			mv.visitIntInsn(BIPUSH, 31);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(IMUL);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, "getRequests", "()" + ClassTypes.ListType, false);
			Label l7 = new Label();
			mv.visitJumpInsn(IFNULL, l7);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, "getRequests", "()" + ClassTypes.ListType, false);
			mv.visitMethodInsn(INVOKEVIRTUAL, ClassTypes.ListClass, "hashCode", "()I", true);
			Label l8 = new Label();
			mv.visitJumpInsn(GOTO, l8);
			mv.visitLabel(l7);
			mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{INTEGER});
			mv.visitInsn(ICONST_0);
			mv.visitLabel(l8);
			mv.visitFrame(F_FULL, 2, new Object[]{CLASSNAME, INTEGER}, 2, new Object[]{INTEGER, INTEGER});
			mv.visitInsn(IADD);
			mv.visitVarInsn(ISTORE, 1);
			Label l9 = new Label();
			lineCount++;
			mv.visitLabel(l9);
			mv.visitLineNumber(lineCount, l9);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(IRETURN);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLocalVariable("this", CLASSTYPE, null, l0, l10, 0);
			mv.visitLocalVariable("result", "I", null, l3, l10, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
		/** Metoda equals() */
		{
			mv = cw.visitMethod(ACC_PUBLIC, "equals", "(" + ClassTypes.ObjectType + ")Z", null, null);
			lineCount += 2;
			mv.visitCode();
			Label l0 = new Label();
			lineCount++;
			mv.visitLabel(l0);
			mv.visitLineNumber(lineCount, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			Label l1 = new Label();
			lineCount++;
			mv.visitJumpInsn(IF_ACMPNE, l1);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l1);
			mv.visitLineNumber(lineCount, l1);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 1);
			Label l2 = new Label();
			mv.visitJumpInsn(IFNULL, l2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, ClassTypes.ObjectClass, "getClass", "()" + ClassTypes.ClassType, false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, ClassTypes.ObjectClass, "getClass", "()" + ClassTypes.ClassType, false);
			Label l3 = new Label();
			lineCount++;
			mv.visitJumpInsn(IF_ACMPEQ, l3);
			mv.visitLabel(l2);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l3);
			mv.visitLineNumber(lineCount, l3);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitTypeInsn(CHECKCAST, CLASSNAME);
			mv.visitVarInsn(ASTORE, 2);
			Label l4 = new Label();
			lineCount++;
			mv.visitLabel(l4);
			mv.visitLineNumber(lineCount, l4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, "getId", "()" + ClassTypes.IntegerType, false);
			Label l5 = new Label();
			mv.visitJumpInsn(IFNULL, l5);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, "getId", "()" + ClassTypes.IntegerType, false);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, "getId", "()" + ClassTypes.IntegerType, false);
			mv.visitMethodInsn(INVOKEVIRTUAL, ClassTypes.IntegerClass, "equals", "(" + ClassTypes.ObjectType + ")Z", false);
			Label l6 = new Label();
			mv.visitJumpInsn(IFNE, l6);
			Label l7 = new Label();
			mv.visitJumpInsn(GOTO, l7);
			mv.visitLabel(l5);
			mv.visitFrame(F_APPEND, 1, new Object[]{CLASSNAME}, 0, null);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, "getId", "()" + ClassTypes.IntegerType, false);
			mv.visitJumpInsn(IFNULL, l6);
			mv.visitLabel(l7);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l6);
			lineCount++;
			mv.visitLineNumber(lineCount, l6);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			for (FieldType f : fields) {
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, f.getGetterName(), "()" + f.getClassType(), false);
				Label l8 = new Label();
				mv.visitJumpInsn(IFNULL, l8);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, f.getGetterName(), "()" + f.getClassType(), false);
				mv.visitVarInsn(ALOAD, 2);
				mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, f.getGetterName(), "()" + f.getClassType(), false);
				mv.visitMethodInsn(INVOKEVIRTUAL, f.getClassClass(), "equals", "(" + ClassTypes.ObjectType + ")Z", false);
				Label l9 = new Label();
				mv.visitJumpInsn(IFNE, l9);
				Label l10 = new Label();
				mv.visitJumpInsn(GOTO, l10);
				mv.visitLabel(l8);
				mv.visitFrame(F_SAME, 0, null, 0, null);
				mv.visitVarInsn(ALOAD, 2);
				mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, f.getGetterName(), "()" + f.getClassType(), false);
				mv.visitJumpInsn(IFNULL, l9);
				mv.visitLabel(l10);
				lineCount++;
				mv.visitLineNumber(lineCount, l10);
				mv.visitFrame(F_SAME, 0, null, 0, null);
				mv.visitInsn(ICONST_0);
				mv.visitInsn(IRETURN);
				mv.visitLabel(l9);
				lineCount++;
				mv.visitLineNumber(lineCount, l9);
				mv.visitFrame(F_SAME, 0, null, 0, null);
			}
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, "getRequests", "()" + ClassTypes.ListType, false);
			Label l10 = new Label();
			mv.visitJumpInsn(IFNULL, l10);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, "getRequests", "()" + ClassTypes.ListType, false);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, "getRequests", "()" + ClassTypes.ListType, false);
			mv.visitMethodInsn(INVOKEVIRTUAL, ClassTypes.ListClass, "equals", "(" + ClassTypes.ListType + ")Z", true);
			Label l11 = new Label();
			mv.visitJumpInsn(IFNE, l11);
			Label l12 = new Label();
			mv.visitJumpInsn(GOTO, l12);
			mv.visitLabel(l10);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, "getRequests", "()" + ClassTypes.ListType, false);
			mv.visitJumpInsn(IFNULL, l11);
			mv.visitLabel(l12);
			lineCount++;
			mv.visitLineNumber(lineCount, l12);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l11);
			lineCount++;
			mv.visitLineNumber(lineCount, l11);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitLocalVariable("this", CLASSNAME, null, l0, l13, 0);
			mv.visitLocalVariable("o", ClassTypes.ObjectType, null, l0, l13, 1);
			mv.visitLocalVariable("wpr", CLASSTYPE, null, l4, l13, 2);
			mv.visitMaxs(2, 3);
			mv.visitEnd();
		}
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

	public static String getClassTName() {
		return CLASSNAME;
	}

	public static String getClassName() {
		return CLASSNAME.replace("/", ".");
	}
}
