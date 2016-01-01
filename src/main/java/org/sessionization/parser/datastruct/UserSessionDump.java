package org.sessionization.parser.datastruct;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.*;
import org.sessionization.ClassPoolLoader;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
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
		if (userId != null || pageView != null) {
			final StringBuilder builder = new StringBuilder();
			ClassPool pool = loader.getPool();
			CtClass aClass = pool.makeClass(CLASSNAME);
			aClass.setModifiers(Modifier.PUBLIC);
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
				builder.append("public UserSession() { super(); }");
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
				builder.append("public " + String.class.getName() + " getKey() {")
						.append("return userId.getKey();");
				builder.append('}');
				CtMethod method = CtMethod.make(builder.toString(), aClass);
				aClass.addMethod(method);
			}
			/** Getter in Setter za UserId */
			if (userId != null) {
				/** Getter */{
					builder.setLength(0);
					builder.append("public " + userId.getName() + " getUserId() {")
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
			return aClass.toClass(loader, UserSessionDump.class.getProtectionDomain());
		} else {
			return null;
		}
	}

	public static String getName() {
		return CLASSNAME;
	}
}
