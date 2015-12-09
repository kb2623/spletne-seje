package org.sessionization.parser.datastruct;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.EnumMemberValue;
import org.sessionization.ClassPoolLoader;
import org.sessionization.fields.LogField;
import org.sessionization.fields.LogFieldType;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class DumpUserId {

	private static String CLASSNAME = "org.sessionization.parser.datastuct.UserId";

	public static Class<?> dump(Collection<LogFieldType> fieldsTypes, ClassPoolLoader loader) throws IOException, CannotCompileException, NotFoundException {
		if (fieldsTypes.size() < 1) {
			return null;
		} else {
			List<LogFieldType> fields = getFields(fieldsTypes);
			ClassPool pool = loader.getPool();
			CtClass aClass = pool.makeClass(CLASSNAME);
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
			/** Inicializacije ID poja */{
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
				CtField field = CtField.make("private " + f.getClassE().getName() + " " + f.getFieldName() + ";", aClass);
				ConstPool constPool = field.getFieldInfo().getConstPool();
				AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
				Class c = f.getClassE();
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
					CtMethod method = CtMethod.make("public void " + f.getSetterName() + "(" + f.getClassE().getName() + " " + f.getFieldName() + ") {" + "this." + f.getFieldName() + " = " + f.getFieldName() + ";" + "}", aClass);
					aClass.addMethod(method);
				}
				/** getter */{
					CtMethod method = CtMethod.make("public " + f.getClassE().getName() + " " + f.getGetterName() + "() {" + "return this." + f.getFieldName() + ";" + "}", aClass);
					aClass.addMethod(method);
				}
			}
			if (fields.size() < fieldsTypes.size()) {
				/** Inicializacije UserSessino razreda */{
					CtField field = CtField.make("private " + UserSessionAbs.class.getName() + " session;", aClass);
					ConstPool constPool = field.getFieldInfo().getConstPool();
					AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
					/** Dodajanje anoracije ManyToMany */{
						Annotation anno = new Annotation(ManyToMany.class.getName(), constPool);
						EnumMemberValue value = new EnumMemberValue(constPool);
						value.setType(CascadeType.class.getName());
						value.setValue(CascadeType.ALL.name());
						anno.addMemberValue("cascade", value);
						attr.addAnnotation(anno);
					}
					field.getFieldInfo().addAttribute(attr);
					aClass.addField(field);
				}
				/** getter za Session */{
					CtMethod method = CtMethod.make("public " + UserSessionAbs.class.getName() + " getSession() {" + "return this.session;" + "}", aClass);
					aClass.addMethod(method);
				}
				/** setter za Session */{
					CtMethod method = CtMethod.make("public void setSession(" + UserSessionAbs.class.getName() + " session) {" + "this.session = session;" + "}", aClass);
					aClass.addMethod(method);
				}
			}
			/** getKey() super razreda */{
				StringBuilder builder = new StringBuilder();
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
				StringBuilder builder = new StringBuilder();
				builder.append("public boolean equals(" + Object.class.getName() + " o) {");
				builder.append("if (this == o) { return true; }");
				builder.append("else if (o == null || getClass() != o.getClass()) { return false; }");
				builder.append("else {" + CLASSNAME + " v = (" + CLASSNAME + ") o;");
				builder.append("return ");
				builder.append("(this.id != null ? this.id.equals(v.getId()) : v.getId() == null)");
				for (LogFieldType f : fields) {
					builder.append(" && (this." + f.getFieldName() + " != null ? this." + f.getFieldName() + ".equals(v." + f.getGetterName() + "()) : v." + f.getGetterName() + "() == null)");
				}
				builder.append(";}}");
				CtMethod method = CtMethod.make(builder.toString(), aClass);
				aClass.addMethod(method);
			}
			/** hashCode() */{
				StringBuilder builder = new StringBuilder();
				builder.append("public " + int.class.getName() + " hashCode() {");
				builder.append(int.class.getName() + " res = this.id != null ? id.hashCode() : 0;");
				for (LogFieldType f : fields) {
					builder.append("res = 31 * res + (" + f.getFieldName() + " != null ? this." + f.getFieldName() + ".hashCode() : 0);");
				}
				builder.append("return res;").append('}');
				CtMethod method = CtMethod.make(builder.toString(), aClass);
				aClass.addMethod(method);
			}/** UserId() */{
				StringBuilder builder = new StringBuilder();
				builder.append("public UserId() {");
				builder.append("super();");
				builder.append("this.id = null;");
				for (LogFieldType f : fields) {
					builder.append("this." + f.getFieldName() + " = null;");
				}
				builder.append("session = null;");
				builder.append('}');
				CtConstructor constructor = CtNewConstructor.make(builder.toString(), aClass);
				aClass.addConstructor(constructor);
			}
			/** UserId(ParsedLine line) */{
				StringBuilder builder = new StringBuilder();
				builder.append("public UserId(" + ParsedLine.class.getName() + " line) {");
				builder.append("super();");
				builder.append("this.id = null;");
				builder.append("for (" + Iterator.class.getName() + " it = line.iterator(); it.hasNext(); ) {");
				builder.append(LogField.class.getName() + " f = (" + LogField.class.getName() + ") it.next();");
				for (LogFieldType f : fields) {
					builder.append("if (f.getClass() == " + f.getClassE().getName() + ".class)");
					builder.append("{ this." + f.getFieldName() + " = f; }");
				}
				builder.append('}');
				builder.append("this.session = new " + pool.get(DumpUserSession.getName()).getName() + "(line);");
				builder.append('}');
				CtConstructor constructor = CtNewConstructor.make(builder.toString(), aClass);
				aClass.addConstructor(constructor);
			}
			return aClass.toClass(loader, DumpUserId.class.getProtectionDomain());
		}
	}

	protected static List<LogFieldType> getFields(Collection<LogFieldType> fieldTypes) {
		List<LogFieldType> retList = new ArrayList<>((int) (fieldTypes.size() / 2));
		for (LogFieldType type : fieldTypes) {
			if (type.isKey()) {
				retList.add(type);
			}
		}
		return retList;
	}

	public static String getName() {
		return CLASSNAME;
	}

}
