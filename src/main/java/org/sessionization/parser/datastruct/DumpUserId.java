package org.sessionization.parser.datastruct;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.EnumMemberValue;
import org.sessionization.ClassPoolLoader;
import org.sessionization.parser.LogField;
import org.sessionization.parser.LogFieldType;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class DumpUserId {

	private static String CLASSNAME = "org.sessionization.parser.datastuct.UserId";

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
			aClass.setSuperclass(pool.get(AbsUserId.class.getName()));
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
				builder.setLength(0);
				builder.append("private " + Integer.class.getName() + " id;");
				CtField field = CtField.make(builder.toString(), aClass);
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
				builder.setLength(0);
				builder.append("private " + f.getClassE().getName() + " " + f.getFieldName() + ";");
				CtField field = CtField.make(builder.toString(), aClass);
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
			/** Integer getId() */{
				builder.setLength(0);
				builder.append("public synchronized " + Integer.class.getName() + " getId() { return this.id; }");
				CtMethod method = CtMethod.make(builder.toString(), aClass);
				aClass.addMethod(method);
			}
			/** void setId(Integer id) */{
				builder.setLength(0);
				builder.append("public void setId(" + Integer.class.getName() + " id) { this.id = id; }");
				CtMethod method = CtMethod.make(builder.toString(), aClass);
				aClass.addMethod(method);
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
			/** Dodajanje polja UserSession, ter getterj in setterja za to polje */
			if (fields.size() < fieldsTypes.size()) {
				/** Inicializacije UserSessino razreda */{
					builder.setLength(0);
					builder.append("private " + AbsUserSession.class.getName() + " session;");
					CtField field = CtField.make(builder.toString(), aClass);
					ConstPool constPool = field.getFieldInfo().getConstPool();
					AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
					/** Dodajanje anoracije ManyToMany */{
						Annotation anno = new Annotation(OneToMany.class.getName(), constPool);
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
					builder.setLength(0);
					builder.append("public " + AbsUserSession.class.getName() + " getSession() {" + "return this.session;" + "}");
					CtMethod method = CtMethod.make(builder.toString(), aClass);
					aClass.addMethod(method);
				}
				/** setter za Session */{
					builder.setLength(0);
					builder.append("public void setSession(" + AbsUserSession.class.getName() + " session) {" + "this.session = session;" + "}");
					CtMethod method = CtMethod.make(builder.toString(), aClass);
					aClass.addMethod(method);
				}
				/** getLocalTime() */{
					builder.setLength(0);
					builder.append("public " + LocalTime.class.getName() + " getLocalTime() {");
					builder.append("if (session != null ) { return this.session.getLocalTime(); }");
					builder.append("else {return " + LocalTime.class.getName() + ".MIDNIGHT; }");
					builder.append('}');
					CtMethod method = CtMethod.make(builder.toString(), aClass);
					aClass.addMethod(method);
				}
				/** getLocalDate() */{
					builder.setLength(0);
					builder.append("public " + LocalDate.class.getName() + " getLocalDate() {");
					builder.append("if (session != null) { return this.session.getLocalDate(); }");
					builder.append("else { return " + LocalDate.class.getName() + ".MIN; }");
					builder.append('}');
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
			/** boolean addParsedLine(ParsedLine line) super razreda */{
				builder.setLength(0);
				builder.append("public " + boolean.class.getName() + " addParsedLine(" + ParsedLine.class.getName() + " line) {");
				if (fields.size() < fieldsTypes.size()) {
					builder.append("if (getKey().equals(line.getKey())) {");
					builder.append("return this.session.addParsedLine(line);");
					builder.append(" } else { return false; }");
				} else {
					builder.append("return false;");
				}
				builder.append('}');
				CtMethod method = CtMethod.make(builder.toString(), aClass);
				aClass.addMethod(method);
			}
			/** equals(Object o) */{
				builder.setLength(0);
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
				builder.setLength(0);
				builder.append("public " + int.class.getName() + " hashCode() {");
				builder.append(int.class.getName() + " res = this.id != null ? id.hashCode() : 0;");
				for (LogFieldType f : fields) {
					builder.append("res = 31 * res + (" + f.getFieldName() + " != null ? this." + f.getFieldName() + ".hashCode() : 0);");
				}
				builder.append("return res;").append('}');
				CtMethod method = CtMethod.make(builder.toString(), aClass);
				aClass.addMethod(method);
			}/** UserId() */{
				builder.setLength(0);
				builder.append("public UserId() {");
				builder.append("super();");
				builder.append("this.id = null;");
				for (LogFieldType f : fields) {
					builder.append("this." + f.getFieldName() + " = null;");
				}
				if (fields.size() < fieldsTypes.size()) {
					builder.append("session = null;");
				}
				builder.append('}');
				CtConstructor constructor = CtNewConstructor.make(builder.toString(), aClass);
				aClass.addConstructor(constructor);
			}
			/** UserId(ParsedLine line) */{
				builder.setLength(0);
				builder.append("public UserId(" + ParsedLine.class.getName() + " line) {");
				builder.append("super();");
				builder.append("this.id = null;");
				builder.append("for (" + Iterator.class.getName() + " it = line.iterator(); it.hasNext(); ) {");
				builder.append(LogField.class.getName() + " f = (" + LogField.class.getName() + ") it.next();");
				for (LogFieldType f : fields) {
					builder.append("if (f instanceof " + f.getClassE().getName() + ")");
					builder.append("{ this." + f.getFieldName() + " = (" + f.getClassE().getName() + ") f; }");
				}
				builder.append('}');
				if (fields.size() < fieldsTypes.size()) {
					builder.append("this.session = new " + pool.get(DumpUserSession.getName()).getName() + "(line);");
				}
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
