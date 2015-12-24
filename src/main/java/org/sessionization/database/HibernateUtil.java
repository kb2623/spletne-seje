package org.sessionization.database;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.service.ServiceRegistry;
import org.sessionization.ClassPoolLoader;
import org.sessionization.parser.AbsWebLogParser;
import org.sessionization.parser.ArgsParser;
import org.sessionization.parser.LogFieldType;
import org.sessionization.parser.datastruct.DumpPageView;
import org.sessionization.parser.datastruct.DumpUserId;
import org.sessionization.parser.datastruct.DumpUserSession;

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
	private ClassPoolLoader loader = null;

	public HibernateUtil(ArgsParser argsParser, AbsWebLogParser logParser) throws ExceptionInInitializerError, IOException, CannotCompileException, NotFoundException {
		/** Izbrisemo razrede, ki jih je uprabnik podal za ignoriranje */
		List<LogFieldType> list = logParser.getFieldType();
		list.removeAll(logParser.getIgnoreFieldTypes());
		/** Inicializacija ClassLoaderja */
		this.loader = initClassLoader(argsParser);
		Properties props = initProperties(argsParser);
		/** Dodaj potrebne razrede */
		Set<Class> classes = initClasses(list, loader);
		serviceRegistry = new StandardServiceRegistryBuilder()
				.addService(ClassLoaderService.class, new ClassLoaderServiceImpl(loader))
				.applySettings(props)
				.build();
		try {
			MetadataSources sources = new MetadataSources(serviceRegistry);
			for (Class c : classes) {
				sources.addAnnotatedClass(c);
			}
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

	private Set<Class> initClasses(List<LogFieldType> list, ClassPoolLoader loader) throws ExceptionInInitializerError {
		Set<Class> classes = new HashSet<>();
		for (LogFieldType f : list) {
			for (Class c : f.getDependencies()) {
				classes.add(c);
			}
			classes.add(f.getClassE());
		}
		Class c;
		try {
			c = DumpPageView.dump(loader);
		} catch (IOException | CannotCompileException | NotFoundException e) {
			c = null;
		}
		if (c != null) {
			classes.add(c);
			try {
				classes.add(DumpUserSession.dump(loader));
			} catch (IOException | CannotCompileException | NotFoundException e) {
				throw new ExceptionInInitializerError(e);
			}
		}
		try {
			c = DumpUserId.dump(list, loader);
		} catch (IOException | CannotCompileException | NotFoundException e) {
		}
		if (c != null) {
			classes.add(c);
		}
		return classes;
	}

	private ClassPoolLoader initClassLoader(ArgsParser argsParser) throws NotFoundException, CannotCompileException, IOException {
		/** Dodaj jar datoeke */
		Set<URL> set = new HashSet<>();
		if (argsParser.getDriverUrl() != null) {
			set.add(argsParser.getDriverUrl());
		}
		if (argsParser.getDialect() != null) {
			set.add(argsParser.getDialect());
		}
		URLClassLoader urlLoader = new URLClassLoader(set.toArray(new URL[set.size()]), ClassLoader.getSystemClassLoader());
		/** Ustvari dinamicne razrede */
		return new ClassPoolLoader(urlLoader);
	}

	public ClassPoolLoader getLoader() {
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
}
