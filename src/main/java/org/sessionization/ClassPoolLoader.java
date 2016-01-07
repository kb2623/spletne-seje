package org.sessionization;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class ClassPoolLoader extends ClassLoader {

	private ClassPool pool;

	public ClassPoolLoader() {
		super();
		pool = ClassPool.getDefault();
	}

	public ClassPoolLoader(ClassLoader loader, ClassPool pool) {
		super(loader);
		if (pool != null) {
			this.pool = pool;
		} else {
			this.pool = ClassPool.getDefault();
		}
	}

	public ClassPoolLoader(ClassLoader loader) {
		this(loader, ClassPool.getDefault());
	}

	public Class<?> defineClass(String name, byte[] bytes) {
		Class<?> c = super.defineClass(name, bytes, 0, bytes.length);
		super.resolveClass(c);
		return c;
	}

	public ClassPool getPool() {
		return pool;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			return super.findClass(name);
		} catch (ClassNotFoundException e) {
			try {
				return pool.getCtClass(name).toClass(this, getClass().getProtectionDomain());
			} catch (CannotCompileException | NotFoundException e1) {
				throw new ClassNotFoundException(e1.getMessage(), e1);
			}
		}
	}
}
