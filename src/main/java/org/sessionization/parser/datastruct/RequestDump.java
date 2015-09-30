package org.sessionization.parser.datastruct;

import org.objectweb.asm.*;
import org.sessionization.fields.FieldType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDump implements Opcodes {

	private static String CLASSNAME = "org/sessionization/fields/Request";
	private static String CLASSTYPE = "L" + CLASSNAME + ";";
	private static String NAME = "Request.java";

	public static byte[] dump(List<FieldType> list) {
		List<FieldType> fields = getFields(list);
		int lineCount = 20;
		ClassWriter cw = new ClassWriter(0);
		FieldVisitor fv;
		MethodVisitor mv;
		AnnotationVisitor av0;
		/** Inicializcija distopnih pravic, imena in dedovanje razreda */
		cw.visit(52, ACC_PUBLIC + ACC_SUPER, CLASSNAME, null, ClassTypes.ObjectClass, null);
		/** Inicializacija datoteke razreda */
		cw.visitSource(NAME + ".java", null);
		/** Dodajanje @Entry */
		{
			av0 = cw.visitAnnotation(ClassTypes.EntityType, true);
			av0.visitEnd();
		}
		/** Ihicializacija id polja, ter dodajanje @Id in @GenereratedValue */
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
			mv.visitMethodInsn(INVOKESPECIAL, ClassTypes.ObjectClass, "<init>", "()V", false);
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
			mv.visitInsn(RETURN);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLocalVariable("this", CLASSTYPE, null, l0, l4, 0);
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
				mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, f.getGetterName(), "()" + f.getType(), false);
				Label ll2 = new Label();
				mv.visitJumpInsn(IFNULL, ll2);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, f.getGetterName(), "()" + f.getType(), false);
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
			Label l4 = new Label();
			lineCount++;
			mv.visitLabel(l4);
			mv.visitLineNumber(lineCount, l4);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(IRETURN);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLocalVariable("this", CLASSTYPE, null, l0, l5, 0);
			mv.visitLocalVariable("result", "I", null, l3, l5, 1);
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
				mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, f.getGetterName(), "()" + f.getType(), false);
				Label l8 = new Label();
				mv.visitJumpInsn(IFNULL, l8);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, f.getGetterName(), "()" + f.getType(), false);
				mv.visitVarInsn(ALOAD, 2);
				mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, f.getGetterName(), "()" + f.getType(), false);
				mv.visitMethodInsn(INVOKEVIRTUAL, f.getClassClass(), "equals", "(" + ClassTypes.ObjectType + ")Z", false);
				Label l9 = new Label();
				mv.visitJumpInsn(IFNE, l9);
				Label l10 = new Label();
				mv.visitJumpInsn(GOTO, l10);
				mv.visitLabel(l8);
				mv.visitFrame(F_SAME, 0, null, 0, null);
				mv.visitVarInsn(ALOAD, 2);
				mv.visitMethodInsn(INVOKEVIRTUAL, CLASSNAME, f.getGetterName(), "()" + f.getType(), false);
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
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLocalVariable("this", CLASSTYPE, null, l0, l11, 0);
			mv.visitLocalVariable("o", ClassTypes.ObjectType, null, l0, l11, 1);
			mv.visitLocalVariable("r", CLASSTYPE, null, l4, l11, 2);
			mv.visitMaxs(2, 3);
			mv.visitEnd();
		}
		cw.visitEnd();
		return cw.toByteArray();
	}

	private static List<FieldType> getFields(List<FieldType> types) {
		List<FieldType> list = new ArrayList<>((int) (types.size() / 2));
		for (FieldType f : types) {
			if (!f.isKey()) {
				list.add(f);
			}
		}
		return list;
	}

	public static String getTypeName() {
		return CLASSNAME;
	}

	public static String getName() {
		return NAME;
	}

	public static String getClassName() {
		return CLASSNAME.replaceAll("/", ".");
	}

	public static String getClassType() {
		return CLASSTYPE;
	}
}
