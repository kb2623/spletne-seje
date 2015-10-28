package org.sessionization;

import javassist.convert.TransformAccessArrayField;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.service.ServiceRegistry;
import org.sessionization.fields.FieldType;
import org.sessionization.parser.AbsParser;
import org.sessionization.parser.ArgsParser;
import org.sessionization.parser.datastruct.PageViewDump;
import org.sessionization.parser.datastruct.ResoucesDump;

import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class HibernateUtil implements AutoCloseable {

	public class UrlLoader extends URLClassLoader {
		public UrlLoader(URL[] urls) {
			super(urls);
		}

		public synchronized void defineClass(String name, byte[] bytes) {
			super.defineClass(name, bytes, 0, bytes.length);
		}
	}

	private SessionFactory factory = null;
	private ServiceRegistry registry = null;
	private ClassLoader loader = null;

	public HibernateUtil(ArgsParser argsParser, AbsParser logParser) throws ExceptionInInitializerError {
		this.loader = initClassLoader(argsParser, logParser);
		Properties props = initProperties(argsParser);
		Set<Class> classes = initClasses(logParser.getFieldType());
		registry = new StandardServiceRegistryBuilder()
				.addService(ClassLoaderService.class, new ClassLoaderServiceImpl(loader))
				.applySettings(props)
				.build();
		try {
			MetadataSources sources = new MetadataSources(registry);
			for (Class c : classes) sources.addAnnotatedClass(c);
			factory = sources.buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			StandardServiceRegistryBuilder.destroy(registry);
			throw new ExceptionInInitializerError(e);
		}
	}

	private Properties initProperties(ArgsParser parser) {
		Properties props = new Properties();
		props.setProperty("hibernate.current_session_context_class", "thread");
		props.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.internal.NoCacheProvider");
		props.setProperty("hibernate.connection.driver_class", parser.getDriverClass());
		props.setProperty("hibernate.dialect", parser.getDialectClass());
		props.setProperty("hibernate.connection.url", parser.getDatabaseUrl().toString());
		if (parser.getUserName() != null) {
			props.setProperty("hibernate.connection.username", parser.getUserName());
		}
		if (parser.getPassWord() != null) {
			props.setProperty("hibernate.connection.password", parser.getPassWord());
		}
		props.setProperty("hibernate.connection.pool_size", String.valueOf(parser.getConnectoinPoolSize()));
		props.setProperty("hibernate.show_sql", String.valueOf(parser.isShowSql()));
		props.setProperty("hibernate.format_sql", String.valueOf(parser.isShowSqlFormat()));
		props.setProperty("hibernate.hbm2ddl.auto", parser.getOperation().getValue());
		return props;
	}

	private Set<Class> initClasses(List<FieldType> list) throws ExceptionInInitializerError {
		Set<Class> classes = new HashSet<>();
		for (FieldType f : list) {
			for (Class c : f.getDependencies()) {
				classes.add(c);
			}
			classes.add(f.getClassType());
		}
		try {
			classes.add(loader.loadClass(ResoucesDump.getClassName()));
			classes.add(loader.loadClass(PageViewDump.getClassName()));
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
		return classes;
	}

	private ClassLoader initClassLoader(ArgsParser argsParser, AbsParser logParser) {
		/** Dodaj jar datoeke */
		Set<URL> set = new HashSet<>();
		if (argsParser.getDriverUrl() != null) {
			set.add(argsParser.getDriverUrl());
		}
		if (argsParser.getDialect() != null) {
			set.add(argsParser.getDialect());
		}
		UrlLoader loader = new UrlLoader(set.toArray(new URL[set.size()]));

		/** Ustvari dinamicne razrede */
		loader.defineClass(PageViewDump.getClassName(), PageViewDump.dump(logParser.getFieldType()));
		loader.defineClass(ResoucesDump.getClassName(), ResoucesDump.dump(logParser.getFieldType()));
		return loader;
	}

	public ClassLoader getLoader() {
		return loader;
	}

	private Session getSession() {
		return factory.openSession();
	}

	public <T> Serializable save(T o) {
		Serializable ret = null;
		synchronized (factory) {
			Session s = getSession();
			Transaction t = s.getTransaction();
			try {
				t.begin();
				ret = s.save(o);
				t.commit();
			} catch (HibernateException e) {
				if (t != null) {
					t.rollback();
				}
			}
			s.close();
		}
		return ret;
	}

	public <T> T getObject(T t) {
		T ret = null;
		synchronized (factory) {
			// TODO
		}
		return ret;
	}

	@Override
	public void close() {
		if (registry != null) StandardServiceRegistryBuilder.destroy(registry);
		if (factory != null) factory.close();
	}
}
