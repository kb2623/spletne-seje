package org.sessionization.parser.datastruct;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import java.io.IOException;

public class DumpUserSession {

	private static String CLASSNAME = "org.sessionization.parser.datastuct.UserSession";

	public static byte[] dump() throws IOException, CannotCompileException, NotFoundException {
		ClassPool pool = ClassPool.getDefault();
		CtClass aClass = pool.makeClass(CLASSNAME);
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
		/** UserSession(ParsedLine line) */{
			StringBuilder builder = new StringBuilder();
			builder.append("public UserSession(" + ParsedLine.class.getName() + " line) {");
			builder.append("super(line);");
			builder.append("super.addPageView(new " + pool.get(DumpPageView.getName()).getName() + "(line));");
			builder.append('}');
			CtConstructor constructor = CtNewConstructor.make(builder.toString(), aClass);
			aClass.addConstructor(constructor);
		}
		aClass.toClass(ClassLoader.getSystemClassLoader(), DumpUserSession.class.getProtectionDomain());
		return aClass.toBytecode();
	}

	public static String getName() {
		return CLASSNAME;
	}
}
