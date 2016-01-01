package org.sessionization.parser.datastruct;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.MemberValue;
import org.sessionization.ClassPoolLoader;
import org.sessionization.parser.LogField;
import org.sessionization.parser.LogFieldType;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class UserIdDump {

	private static String CLASSNAME = "org.sessionization.parser.datastuct.UserId";

	/**
	 * Metoda za izdelavo dinamicnega razreda za hranjenje identifikacije o uporabniku
	 *
	 * @param fieldsTypes
	 * @param loader
	 * @return
	 * @throws IOException
	 * @throws CannotCompileException
	 * @throws NotFoundException
	 */
	public static Class<?> dump(final Collection<LogFieldType> fieldsTypes, ClassPoolLoader loader) throws IOException, CannotCompileException, NotFoundException {
		if (fieldsTypes.size() < 1) {
			return null;
		} else {
			List<LogFieldType> fields = getFields(fieldsTypes);
			final StringBuilder builder = new StringBuilder();
			ClassPool pool = loader.getPool();
			CtClass aClass = pool.makeClass(CLASSNAME);
			aClass.setModifiers(Modifier.PUBLIC);
			/** Dodaj super class */
			aClass.setSuperclass(pool.get(UserIdAbs.class.getName()));
			/** Dodaj anotacije */{
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
			/** Inicializacija polji */
			for (LogFieldType f : fields) {
				builder.setLength(0);
				builder.append("private " + f.getClassE().getName() + " " + f.getFieldName() + ";");
				CtField field = CtField.make(builder.toString(), aClass);
				ConstPool constPool = field.getFieldInfo().getConstPool();
				AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
				Class c = f.getClassE();
				if (c.isAnnotationPresent(Entity.class)) {
					Annotation anno = new Annotation(OneToOne.class.getName(), constPool);
					{
						EnumMemberValue member = new EnumMemberValue(constPool);
						member.setType(CascadeType.class.getName());
						member.setValue(CascadeType.ALL.name());
						ArrayMemberValue array = new ArrayMemberValue(constPool);
						array.setValue(new MemberValue[]{member});
						anno.addMemberValue("cascade", array);
					}
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
			/** UserId() */{
				builder.setLength(0);
				builder.append("public UserId() {");
				builder.append("super();");
				for (LogFieldType f : fields) {
					builder.append("this." + f.getFieldName() + " = null;");
				}
				builder.append('}');
				CtConstructor constructor = CtNewConstructor.make(builder.toString(), aClass);
				aClass.addConstructor(constructor);
			}
			/** UserId(ParsedLine line) */{
				builder.setLength(0);
				builder.append("public UserId(" + ParsedLine.class.getName() + " line) {");
				builder.append("super(line);");
				builder.append("for (" + Iterator.class.getName() + " it = line.iterator(); it.hasNext(); ) {");
				builder.append(LogField.class.getName() + " f = (" + LogField.class.getName() + ") it.next();");
				for (LogFieldType f : fields) {
					builder.append("if (f instanceof " + f.getClassE().getName() + ")");
					builder.append("{ this." + f.getFieldName() + " = (" + f.getClassE().getName() + ") f; }");
				}
				builder.append('}').append('}');
				CtConstructor constructor = CtNewConstructor.make(builder.toString(), aClass);
				aClass.addConstructor(constructor);
			}
			/** setterji in getterji za ostala polja */
			for (LogFieldType f : fields) {
				/** setter */{
					builder.setLength(0);
					builder.append("public void " + f.getSetterName() + "(" + f.getClassE().getName() + " " + f.getFieldName() + ") {" + "this." + f.getFieldName() + " = " + f.getFieldName() + ";" + "}");
					CtMethod method = CtMethod.make(builder.toString(), aClass);
					aClass.addMethod(method);
				}
				/** getter */{
					builder.setLength(0);
					builder.append("public " + f.getClassE().getName() + " " + f.getGetterName() + "() {" + "return this." + f.getFieldName() + ";" + "}");
					CtMethod method = CtMethod.make(builder.toString(), aClass);
					aClass.addMethod(method);
				}
			}
			/** String getKey() super razreda */{
				builder.setLength(0);
				builder.append("public " + String.class.getName() + " getKey() {");
				builder.append("return ");
				for (LogFieldType f : fields) {
					builder.append("(this." + f.getFieldName() + " != null ? " + f.getFieldName()).append(".getKey() : \"\") + ");
				}
				if (builder.length() > 2) {
					builder.delete(builder.length() - 2, builder.length());
				}
				builder.append(";}");
				CtMethod method = CtMethod.make(builder.toString(), aClass);
				aClass.addMethod(method);
			}
			/** equals(Object o) */{
				builder.setLength(0);
				builder.append("public boolean equals(" + Object.class.getName() + " o) {");
				builder.append("if (this == o) { return true; }");
				builder.append("else if (o == null || getClass() != o.getClass()) { return false; }");
				builder.append("else {" + CLASSNAME + " v = (" + CLASSNAME + ") o;");
				builder.append("return super.equals(o)");
				for (LogFieldType f : fields) {
					builder.append(" && (this." + f.getFieldName() + " != null ? this." + f.getFieldName() + ".equals(v." + f.getGetterName() + "()) : v." + f.getGetterName() + "() == null)");
				}
				builder.append(';').append("}}");
				CtMethod method = CtMethod.make(builder.toString(), aClass);
				aClass.addMethod(method);
			}
			/** hashCode() */{
				builder.setLength(0);
				builder.append("public " + int.class.getName() + " hashCode() {");
				builder.append(int.class.getName() + " res = super.hashCode();");
				for (LogFieldType f : fields) {
					builder.append("res = 31 * res + (" + f.getFieldName() + " != null ? this." + f.getFieldName() + ".hashCode() : 0);");
				}
				builder.append("return res;").append('}');
				CtMethod method = CtMethod.make(builder.toString(), aClass);
				aClass.addMethod(method);
			}
			return aClass.toClass(loader, UserIdDump.class.getProtectionDomain());
		}
	}

	/**
	 * Metoda izdela novo tabelo, ki vsebuje polja za identifikacijo uporabnika
	 *
	 * @param fieldTypes
	 * @return Tabela ki vsebuje identifikacijska polja
	 */
	protected static List<LogFieldType> getFields(Collection<LogFieldType> fieldTypes) {
		List<LogFieldType> retList = new ArrayList<>((int) (fieldTypes.size() / 2));
		for (LogFieldType type : fieldTypes) {
			if (type.isKey()) {
				retList.add(type);
			}
		}
		return retList;
	}

	/**
	 * Metoda ki vrne ime razreda
	 *
	 * @return Niz ki predstavlja ime razreda
	 */
	public static String getName() {
		return CLASSNAME;
	}

}
