package org.sessionization.parser.datastruct;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.*;
import org.hibernate.Session;
import org.sessionization.ClassPoolLoader;
import org.sessionization.database.HibernateUtil;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserSessionDump {

	private static String CLASSNAME = "org.sessionization.parser.datastuct.UserSession";

	public static Class<?> dump(ClassPoolLoader loader) throws IOException, CannotCompileException, NotFoundException {
		Class userId, pageView;
		try {
			userId = loader.loadClass(UserIdDump.getName());
		} catch (ClassNotFoundException e) {
			userId = null;
		}
		try {
			pageView = loader.loadClass(PageViewDump.getName());
		} catch (ClassNotFoundException e) {
			pageView = null;
		}
		try {
			return loader.loadClass(CLASSNAME);
		} catch (ClassNotFoundException e) {
			if (userId != null || pageView != null) {
				final StringBuilder builder = new StringBuilder();
				ClassPool pool = loader.getPool();
				CtClass aClass = pool.makeClass(CLASSNAME);
				aClass.setModifiers(Modifier.PUBLIC);
				aClass.getClassFile().setMajorVersion(ClassFile.JAVA_8);
				/** Dodajanje super razreda */
				aClass.setSuperclass(pool.get(UserSessionAbs.class.getName()));
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
					{
						Annotation anno = new Annotation(DiscriminatorValue.class.getName(), constPool);
						StringMemberValue value = new StringMemberValue(constPool);
						value.setValue("1");
						anno.addMemberValue("value", value);
						attr.addAnnotation(anno);
					}
					aClass.getClassFile().addAttribute(attr);
				}
				/** Polje UserId */
				if (userId != null) {
					builder.setLength(0);
					builder.append("private " + userId.getName() + " userId;");
					CtField field = CtField.make(builder.toString(), aClass);
					{
						ConstPool constPool = field.getFieldInfo().getConstPool();
						AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
						{
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
						}
						field.getFieldInfo().addAttribute(attr);
					}
					aClass.addField(field);
				}
				/** Polje List ki vebuje PageViewAbs */
				if (pageView != null) {
					builder.setLength(0);
					builder.append("private " + List.class.getName() + " pages;");
					CtField field = CtField.make(builder.toString(), aClass);
					{
						ConstPool constPool = field.getFieldInfo().getConstPool();
						AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
						{
							Annotation anno = new Annotation(OneToMany.class.getName(), constPool);
							{
								EnumMemberValue member = new EnumMemberValue(constPool);
								member.setType(CascadeType.class.getName());
								member.setValue(CascadeType.ALL.name());
								ArrayMemberValue array = new ArrayMemberValue(constPool);
								array.setValue(new MemberValue[]{member});
								anno.addMemberValue("cascade", array);
							}
							{
								ClassMemberValue member = new ClassMemberValue(constPool);
								member.setValue(PageViewAbs.class.getName());
								anno.addMemberValue("targetEntity", member);
							}
							attr.addAnnotation(anno);
						}
						field.getFieldInfo().addAttribute(attr);
					}
					aClass.addField(field);
				}
				/** UserSession() */{
					builder.setLength(0);
					builder.append("public UserSession() {");
					builder.append("super();");
					if (userId != null) {
						builder.append("this.userId = null;");
					}
					if (pageView != null) {
						builder.append("this.pages = null;");
					}
					builder.append('}');
					CtConstructor constructor = CtNewConstructor.make(builder.toString(), aClass);
					aClass.addConstructor(constructor);
				}
				/** UserSession(ParsedLine line) */{
					builder.setLength(0);
					builder.append("public UserSession(" + ParsedLine.class.getName() + " line) {")
							.append("super(line);");
					if (userId != null) {
						builder.append("this.userId = new " + userId.getName() + "(line);");
					}
					if (pageView != null) {
						builder.append("this.pages = new " + ArrayList.class.getName() + "();")
								.append("this.pages.add(new " + pageView.getName() + "(line));");
					}
					builder.append('}');
					CtConstructor constructor = CtNewConstructor.make(builder.toString(), aClass);
					aClass.addConstructor(constructor);
				}
				/** String getKey() */
				if (userId != null) {
					builder.setLength(0);
					builder.append("public " + String.class.getName() + " getKey() {");
					if (userId != null) {
						builder.append("return userId.getKey();");
					} else {
						builder.append("return \"\"");
					}
					builder.append('}');
					CtMethod method = CtMethod.make(builder.toString(), aClass);
					aClass.addMethod(method);
				}
				/** Getter in Setter za UserId */
				if (userId != null) {
					/** Getter */{
						builder.setLength(0);
						builder.append("public " + UserIdAbs.class.getName() + " getUserId() {")
								.append("return this.userId;");
						builder.append('}');
						CtMethod method = CtMethod.make(builder.toString(), aClass);
						aClass.addMethod(method);
					}
					/** Setter */{
						builder.setLength(0);
						builder.append("public void setUserId(" + userId.getName() + " userId) {")
								.append("this.userId = userId;");
						builder.append('}');
						CtMethod method = CtMethod.make(builder.toString(), aClass);
						aClass.addMethod(method);
					}
				}
				/** Getter in Setter za PageView */
				if (pageView != null) {
					/** Getter */{
						builder.setLength(0);
						builder.append("public " + List.class.getName() + " getPages() {")
								.append("return this.pages;");
						builder.append('}');
						CtMethod method = CtMethod.make(builder.toString(), aClass);
						aClass.addMethod(method);
					}
					/** Setter */{
						builder.setLength(0);
						builder.append("public void setPages(" + List.class.getName() + " pages) {")
								.append("this.pages = pages;");
						builder.append('}');
						CtMethod method = CtMethod.make(builder.toString(), aClass);
						aClass.addMethod(method);
					}
				}
				/** boolean addParsedLine(ParsedLine line) super razreda */{
					builder.setLength(0);
					builder.append("public " + boolean.class.getName() + " addParsedLine(" + ParsedLine.class.getName() + " line) {");
					builder.append("if (line == null) { return false; }");
					if (userId != null) {
						builder.append("if (!line.getKey().equals(getKey())) {");
						builder.append("return false;");
						builder.append('}');
					}
					if (pageView != null) {
						builder.append("if (!((" + pageView.getName() + ") pages.get(pages.size() - 1)).addParsedLine(line)) {");
						builder.append("pages.add(new " + pageView.getName() + "(line));");
						builder.append('}');
					}
					builder.append("return true;");
					builder.append('}');
					CtMethod method = CtMethod.make(builder.toString(), aClass);
					aClass.addMethod(method);
				}
				/** getLocalDate() */{
					if (pageView != null) {
						builder.setLength(0);
						builder.append("public " + LocalDate.class.getName() + " getLocalDate() {");
						builder.append("return pages != null ? ((" + PageViewDump.getName() + ") pages.get(pages.size() - 1)).getLocalDate() : null;");
						builder.append('}');
						CtMethod method = CtMethod.make(builder.toString(), aClass);
						aClass.addMethod(method);
					}
				}
				/** getLocalTime() */{
					if (pageView != null) {
						builder.setLength(0);
						builder.append("public " + LocalTime.class.getName() + " getLocalTime() {");
						builder.append("return pages != null ? ((" + PageViewDump.getName() + ") pages.get(pages.size() - 1)).getLocalTime() : null;");
						builder.append('}');
						CtMethod method = CtMethod.make(builder.toString(), aClass);
						aClass.addMethod(method);
					}
				}
				/** equals(Object o) */{
					builder.setLength(0);
					builder.append("public " + boolean.class.getName() + " equals(" + Object.class.getName() + " o) {");
					builder.append("if (o == null || !(o instanceof " + CLASSNAME + ")) { return false; }");
					builder.append("if (o == this) { return true; }");
					builder.append(CLASSNAME + " oo = (" + CLASSNAME + ") o;");
					builder.append("return super.equals(oo)");
					if (userId != null) {
						builder.append(" && (getUserId() != null ? getUserId().equals(oo.getUserId()) : oo.getUserId() == null)");
					}
					if (pageView != null) {
						builder.append(" && (getPages() != null ? getPages().equals(oo.getPages()) : oo.getPages() == null)");
					}
					builder.append(';');
					builder.append('}');
					CtMethod method = CtMethod.make(builder.toString(), aClass);
					aClass.addMethod(method);
				}
				/** hashCode() */{
					builder.setLength(0);
					builder.append("public " + int.class.getName() + " hashCode() {");
					builder.append("int res = super.hashCode();");
					if (userId != null) {
						builder.append("res = 31 * res + (getUserId() != null ? getUserId().hashCode() : 0);");
					}
					if (pageView != null) {
						builder.append("res = 31 * res + (getPages() != null ? getPages().hashCode() : 0);");
					}
					builder.append("return res;");
					builder.append('}');
					CtMethod method = CtMethod.make(builder.toString(), aClass);
					aClass.addMethod(method);
				}
				/** toString() */{
					builder.setLength(0);
					builder.append("public " + String.class.getName() + " toString() {");
					builder.append("return ");
					if (userId != null) {
						builder.append("(getUserId() != null ? getUserId().toString() : \"\") + \" \" + ");
					}
					if (pageView != null) {
						builder.append("(getPages() != null ? getPages().toString() : \"\") + \" \" + ");
					}
					builder.delete(builder.length() - 9, builder.length());
					builder.append(';');
					builder.append('}');
					CtMethod method = CtMethod.make(builder.toString(), aClass);
					aClass.addMethod(method);
				}
				/** Object setDbId(Session session) */{
					builder.setLength(0);
					builder.append("public " + Object.class.getName() + " setDbId(" + Session.class.getName() + " session) {");
					if (userId != null) {
						builder.append("this.getUserId().setDbId(session);");
					}
					if (pageView != null) {
						builder.append("for (" + Iterator.class.getName() + " it = pages.iterator(); it.hasNext(); ) {")
								.append(HibernateUtil.HibernateTable.class.getName() + " p = (" + HibernateUtil.HibernateTable.class.getName() + ") it.next();")
								.append("p.setDbId(session);")
								.append('}');
					}
					builder.append("return null;");
					builder.append('}');
					CtMethod method = CtMethod.make(builder.toString(), aClass);
					aClass.addMethod(method);
				}
				return aClass.toClass(loader, loader.getClass().getProtectionDomain());
			} else {
				return null;
			}
		}
	}

	public static String getName() {
		return CLASSNAME;
	}
}
