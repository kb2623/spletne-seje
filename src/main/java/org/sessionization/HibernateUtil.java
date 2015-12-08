package org.sessionization;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.service.ServiceRegistry;
import org.sessionization.fields.LogFieldType;
import org.sessionization.parser.AbsParser;
import org.sessionization.parser.ArgsParser;
import org.sessionization.parser.datastruct.PageViewDump;
import org.sessionization.parser.datastruct.SessionDump;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class HibernateUtil implements AutoCloseable {

	private SessionFactory factory = null;
	private ServiceRegistry serviceRegistry = null;
	private ClassLoader loader = null;

	public HibernateUtil(ArgsParser argsParser, AbsParser logParser) throws ExceptionInInitializerError, IOException, CannotCompileException, NotFoundException {
		this.loader = initClassLoader(argsParser, logParser);
		Properties props = initProperties(argsParser);
		/** Dodaj potrebne razrede */
		Set<Class> classes = initClasses(logParser.getFieldType());
		serviceRegistry = new StandardServiceRegistryBuilder()
				.addService(ClassLoaderService.class, new ClassLoaderServiceImpl(loader))
				.applySettings(props)
				.build();
		try {
			MetadataSources sources = new MetadataSources(serviceRegistry);
			for (Class c : classes) sources.addAnnotatedClass(c);
			factory = sources.buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			StandardServiceRegistryBuilder.destroy(serviceRegistry);
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

	private Set<Class> initClasses(List<LogFieldType> list) throws ExceptionInInitializerError {
		Set<Class> classes = new HashSet<>();
		for (LogFieldType f : list) {
			for (Class c : f.getDependencies()) {
				classes.add(c);
			}
			classes.add(f.getClassType());
		}
		try {
			classes.add(loader.loadClass(SessionDump.getName()));
			classes.add(loader.loadClass(PageViewDump.getName()));
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
		return classes;
	}

	private ClassLoader initClassLoader(ArgsParser argsParser, AbsParser logParser) throws NotFoundException, CannotCompileException, IOException {
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
		loader.defineClass(PageViewDump.getName(), PageViewDump.dump(logParser.getFieldType()));
		loader.defineClass(SessionDump.getName(), SessionDump.dump(logParser.getFieldType()));
		return loader;
	}

	public ClassLoader getLoader() {
		return loader;
	}

	public Session getSession() {
		return factory.openSession();
	}

	@Override
	public void close() {
		if (serviceRegistry != null) StandardServiceRegistryBuilder.destroy(serviceRegistry);
		if (factory != null) factory.close();
	}

	public class UrlLoader extends URLClassLoader {

		public UrlLoader(URL[] urls) {
			super(urls);
		}

		public Class<?> defineClass(String name, byte[] bytes) {
			Class<?> c = super.defineClass(name, bytes, 0, bytes.length);
			super.resolveClass(c);
			return c;
		}
	}
}
