package org.sessionization.parser.datastruct;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import org.sessionization.ClassPoolLoader;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import java.io.IOException;

public class DumpUserSession {

	private static String CLASSNAME = "org.sessionization.parser.datastuct.UserSession";

	public static Class<?> dump(ClassPoolLoader loader) throws IOException, CannotCompileException, NotFoundException {
		final StringBuilder builder = new StringBuilder();
		ClassPool pool = loader.getPool();
		CtClass aClass = pool.makeClass(CLASSNAME);
		aClass.setModifiers(Modifier.PUBLIC);
		/** Dodajanje super razreda */
		aClass.setSuperclass(pool.get(AbsUserSession.class.getName()));
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
		/** UserSession() */{
			builder.setLength(0);
			builder.append("public UserSession() { super(); }");
			CtConstructor constructor = CtNewConstructor.make(builder.toString(), aClass);
			aClass.addConstructor(constructor);
		}
		/** UserSession(ParsedLine line) */{
			builder.setLength(0);
			builder.append("public UserSession(" + ParsedLine.class.getName() + " line) {");
			builder.append("super(line);");
			builder.append("super.addPageView(new " + pool.get(DumpPageView.getName()).getName() + "(line));");
			builder.append('}');
			CtConstructor constructor = CtNewConstructor.make(builder.toString(), aClass);
			aClass.addConstructor(constructor);
		}
		/** boolean addParsedLine(ParsedLine line) super razreda */{
			builder.setLength(0);
			builder.append("public " + boolean.class.getName() + " addParsedLine(" + ParsedLine.class.getName() + " line) {");
			builder.append("if (super.pages == null) return false;");
			builder.append("if (!((" + DumpPageView.getName() + ") super.pages.get(pages.size() - 1)).addParsedLine(line)) {" +
					" return super.pages.add(new " + DumpPageView.getName() + "(line));" +
					"}");
			builder.append("return true;");
			builder.append('}');
			CtMethod method = CtMethod.make(builder.toString(), aClass);
			aClass.addMethod(method);
		}
		return aClass.toClass(loader, DumpUserSession.class.getProtectionDomain());
	}

	public static String getName() {
		return CLASSNAME;
	}
}
