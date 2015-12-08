package org.sessionization;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;

import java.io.IOException;

public class ClassPoolLoader extends ClassLoader {

	private ClassPool pool;

	public ClassPoolLoader() {
		super();
		pool = ClassPool.getDefault();
	}

	public ClassPoolLoader(ClassLoader loader, ClassPool pool) {
		super(loader);
		this.pool = pool;
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
				byte[] bytes = pool.get(name).toBytecode();
				return defineClass(name, bytes);
			} catch (NotFoundException | CannotCompileException | IOException e1) {
				throw e;
			}
		}
	}
}
