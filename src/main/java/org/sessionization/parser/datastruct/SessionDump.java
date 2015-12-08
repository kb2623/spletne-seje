package org.sessionization.parser.datastruct;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.EnumMemberValue;
import org.objectweb.asm.Opcodes;
import org.sessionization.fields.LogFieldType;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SessionDump implements Opcodes {

	private static String CLASSNAME = "org.sessionization.parser.datastruct.Session";

	public static byte[] dump(Collection<LogFieldType> fieldTypes) throws IOException, CannotCompileException, NotFoundException {
		List<LogFieldType> fields = getFields(fieldTypes);
		ClassPool pool = ClassPool.getDefault();
		CtClass aClass = pool.makeClass(CLASSNAME);
		/** Dodaj super Class */
		aClass.setSuperclass(pool.get(SessionAbs.class.getName()));
		/** Dodaj anoracije */{
			ConstPool constPool = aClass.getClassFile().getConstPool();
			AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
			{
				Annotation anno = new Annotation(Entity.class.getName(), constPool);
				attr.addAnnotation(anno);
			}
			{
				Annotation anno = new Annotation(Cacheable.class.getName(), constPool);
				attr.addAnnotation(anno);
			}
			aClass.getClassFile().addAttribute(attr);
		}
		/** Inicializacija ID polja */{
			CtField field = CtField.make("private " + Integer.class.getName() + " id;", aClass);
			ConstPool constPool = field.getFieldInfo().getConstPool();
			AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
			{
				Annotation anno = new Annotation(Id.class.getName(), constPool);
				attr.addAnnotation(anno);
			}
			{
				Annotation anno = new Annotation(GeneratedValue.class.getName(), constPool);
				EnumMemberValue value = new EnumMemberValue(constPool);
				value.setType(GenerationType.class.getName());
				value.setValue(GenerationType.IDENTITY.name());
				anno.addMemberValue("strategy", value);
				attr.addAnnotation(anno);
			}
			field.getFieldInfo().addAttribute(attr);
			aClass.addField(field);
		}
		/** Inicializacija polji */
		for (LogFieldType f : fields) {
			CtField field = CtField.make("private " + f.getClassType().getName() + " " + f.getFieldName() + ";", aClass);
			ConstPool constPool = field.getFieldInfo().getConstPool();
			AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
			Class c = f.getClassType();
			if (c.isAnnotationPresent(Entity.class)) {
				Annotation anno = new Annotation(OneToOne.class.getName(), constPool);
				EnumMemberValue member = new EnumMemberValue(constPool);
				member.setType(CascadeType.class.getName());
				member.setValue(CascadeType.ALL.name());
				anno.addMemberValue("cascade", member);
				attr.addAnnotation(anno);
			} else if (c.isAnnotationPresent(Embeddable.class)) {
				Annotation anno = new Annotation(Embeddable.class.getName(), constPool);
				attr.addAnnotation(anno);
			} else {
				Annotation anno = new Annotation(Embeddable.class.getName(), constPool);
				attr.addAnnotation(anno);
			}
			field.getFieldInfo().addAttribute(attr);
			aClass.addField(field);
		}
		/** Konstruktorji */{
			// TODO
		}
		/** getId() */{
			CtMethod method = CtMethod.make("public " + Integer.class.getName() + " getId() { return this.id; }", aClass);
			aClass.addMethod(method);
		}
		/** setId(Integer id) */{
			CtMethod method = CtMethod.make("public void setId(" + Integer.class.getName() + " id) { this.id = id; }", aClass);
			aClass.addMethod(method);
		}
		/** setterji in getterji za ostala polja */
		for (LogFieldType f : fields) {
			/** setter */{
				CtMethod method = CtMethod.make(
						"public void " + f.getSetterName() + "(" + f.getClassType().getName() + " " + f.getFieldName() + ") {" +
								"this." + f.getFieldName() + " = " + f.getFieldName() + ";" +
								"}",
						aClass
				);
				aClass.addMethod(method);
			}
			/** getter */{
				CtMethod method = CtMethod.make(
						"public " + f.getClassType().getName() + " " + f.getGetterName() + "() {" +
								"return this." + f.getFieldName() + ";" +
								"}",
						aClass
				);
				aClass.addMethod(method);
			}
		}
		return aClass.toBytecode();
	}

	private static List<LogFieldType> getFields(Collection<LogFieldType> types) {
		List<LogFieldType> list = new ArrayList<>((int) (types.size() / 2));
		for (LogFieldType f : types) {
			if (!f.isKey()) {
				list.add(f);
			}
		}
		return list;
	}

	public static String getName() {
		return CLASSNAME;
	}
}
