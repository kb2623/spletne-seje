package org.sessionization.parser.datastruct;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import org.sessionization.ClassPoolLoader;
import org.sessionization.fields.LogFieldType;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DumpPageView {

	private static String CLASSNAME = "org.sessionization.parser.datastruct.PageView";

	public static Class<?> dump(ClassPoolLoader loader) throws IOException, CannotCompileException, NotFoundException {
		ClassPool pool = loader.getPool();
		CtClass aClass = pool.makeClass(CLASSNAME);
		aClass.setModifiers(Modifier.PUBLIC);
		/** Dodaj super Class */
		aClass.setSuperclass(pool.get(AbsPageView.class.getName()));
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
		/** PageView() */{
			StringBuilder builder = new StringBuilder();
			builder.append("public PageView() { super(); }");
			CtConstructor constructor = CtNewConstructor.make(builder.toString(), aClass);
			aClass.addConstructor(constructor);
		}
		/** PageView(ParsedLine line) */{
			StringBuilder builder = new StringBuilder();
			builder.append("public PageView(" + ParsedLine.class.getName() + " line) {");
			builder.append("super(line);");
			builder.append("addParsedLine(line);");
			builder.append('}');
			CtConstructor constructor = CtNewConstructor.make(builder.toString(), aClass);
			aClass.addConstructor(constructor);
		}
		/** boolean addParsedLine(ParsedLine line) */{
			StringBuilder builder = new StringBuilder();
			builder.append("public " + boolean.class.getName() + " addParsedLine(" + ParsedLine.class.getName() + " line) {");
			builder.append("if (line == null || super.requests == null) { return false; }");
			builder.append("if (line.isResource()) " +
					"{ return super.requests.add(new " + DumpRequest.getName() + "(line)); }");
			builder.append("return false;");
			builder.append('}');
			CtMethod method = CtMethod.make(builder.toString(), aClass);
			aClass.addMethod(method);
		}
		return aClass.toClass(loader, DumpPageView.class.getProtectionDomain());
	}

	protected static List<LogFieldType> getFields(Collection<LogFieldType> types) {
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
