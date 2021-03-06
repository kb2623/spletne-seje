package org.sessionization.parser.datastruct;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import org.sessionization.ClassPoolLoader;
import org.sessionization.parser.LogFieldTypeImp;

import javax.persistence.Cacheable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PageViewDump {

	private static String CLASSNAME = "org.sessionization.parser.datastruct.PageView";

	public static Class<?> dump(ClassPoolLoader loader) throws IOException, CannotCompileException, NotFoundException {
		try {
			return loader.loadClass(CLASSNAME);
		} catch (ClassNotFoundException e) {
			final StringBuilder builder = new StringBuilder();
			ClassPool pool = loader.getPool();
			CtClass aClass = pool.makeClass(CLASSNAME);
			aClass.setModifiers(Modifier.PUBLIC);
			aClass.getClassFile().setMajorVersion(ClassFile.JAVA_8);
			/** Dodaj super Class */
			aClass.setSuperclass(pool.get(PageViewAbs.class.getName()));
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
				{
					Annotation anno = new Annotation(DiscriminatorValue.class.getName(), constPool);
					StringMemberValue value = new StringMemberValue(constPool);
					value.setValue("1");
					anno.addMemberValue("value", value);
					attr.addAnnotation(anno);
				}
				aClass.getClassFile().addAttribute(attr);
			}
			/** PageView() */{
				builder.setLength(0);
				builder.append("public PageView() { super(); }");
				CtConstructor constructor = CtNewConstructor.make(builder.toString(), aClass);
				aClass.addConstructor(constructor);
			}
			/** PageView(ParsedLine line) */{
				builder.setLength(0);
				builder.append("public PageView(" + ParsedLine.class.getName() + " line) {");
				builder.append("super(line);")
						.append("super.requests.add(new " + RequestDump.getName() + "(line));");
				builder.append('}');
				CtConstructor constructor = CtNewConstructor.make(builder.toString(), aClass);
				aClass.addConstructor(constructor);
			}
			/** boolean addParsedLine(ParsedLine line) */{
				builder.setLength(0);
				builder.append("public " + boolean.class.getName() + " addParsedLine(" + ParsedLine.class.getName() + " line) {");
				builder.append("if (line == null || super.requests == null) { return false; }");
				builder.append("if (line.isWebPageResource()) " +
						"{ return super.requests.add(new " + RequestDump.getName() + "(line)); }");
				builder.append("return false;");
				builder.append('}');
				CtMethod method = CtMethod.make(builder.toString(), aClass);
				aClass.addMethod(method);
			}
			return aClass.toClass(loader, loader.getClass().getProtectionDomain());
		}
	}

	protected static List<LogFieldTypeImp> getFields(Collection<LogFieldTypeImp> types) {
		List<LogFieldTypeImp> list = new ArrayList<>((int) (types.size() / 2));
		for (LogFieldTypeImp f : types) {
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
