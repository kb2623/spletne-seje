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
import org.sessionization.parser.ArgumentParser;
import org.sessionization.parser.LogFieldType;
import org.sessionization.parser.WebLogParser;
import org.sessionization.parser.datastruct.*;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class HibernateUtil implements AutoCloseable {

	private SessionFactory factory = null;
	private ServiceRegistry serviceRegistry = null;
	private ClassPoolLoader loader = null;

	public HibernateUtil(ArgumentParser argumentParser, WebLogParser logParser) throws ExceptionInInitializerError, IOException, CannotCompileException, NotFoundException {
		/** Izbrisemo razrede, ki jih je uprabnik podal za ignoriranje */
		List<LogFieldType> list = logParser.getFieldType();
		if (logParser.getIgnoreFieldTypes() != null) {
			list.removeAll(logParser.getIgnoreFieldTypes());
		}
		/** Inicializacija ClassLoaderja */
		this.loader = initClassLoader(argumentParser);
		/** Nastavi dodatne lastnosti za Hibernate */
		Properties props = initProperties(argumentParser);
		/** */
		serviceRegistry = new StandardServiceRegistryBuilder()
				.addService(ClassLoaderService.class, new ClassLoaderServiceImpl(loader))
				.applySettings(props)
				.build();
		/** Dodaj potrebne razrede */
		try {
			MetadataSources sources = new MetadataSources(serviceRegistry);
			for (Class c : initClasses(list, loader)) {
				sources.addAnnotatedClass(c);
			}
			factory = sources.buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			StandardServiceRegistryBuilder.destroy(serviceRegistry);
			throw new ExceptionInInitializerError(e);
		}
	}

	private Properties initProperties(ArgumentParser parser) {
		if (!parser.getDriverClass().equals("org.sqlite.JDBC")) {
			try {
				Class c = loader.loadClass(parser.getDriverClass());
				Driver d = new DriverShim((Driver) c.newInstance());
				DriverManager.registerDriver(d);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		Properties props = new Properties();

		props.setProperty("hibernate.connection.driver_class", parser.getDriverClass());
		props.setProperty("hibernate.dialect", parser.getDialectClass());
		props.setProperty("hibernate.connection.url", parser.getDatabaseUrl().toString());
		if (parser.getUserName() != null) {
			props.setProperty("hibernate.connection.username", parser.getUserName());
		}
		if (parser.getPassWord() != null) {
			props.setProperty("hibernate.connection.password", parser.getPassWord());
		}
		if (parser.getDefaultSchema() != null) {
			props.setProperty("hibernate.default_schema", parser.getDefaultSchema());
		}

		props.setProperty("hibernate.connection.isolation", String.valueOf(Connection.TRANSACTION_READ_UNCOMMITTED));
		props.setProperty("hibernate.connection.provider_class", "org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider");
		props.setProperty("hibernate.c3p0.min_size", "1");
		props.setProperty("hibernate.c3p0.max_size", String.valueOf(parser.getConnectoinPoolSize()));

		props.setProperty("hibernate.show_sql", String.valueOf(parser.isShowSql()));
		props.setProperty("hibernate.format_sql", String.valueOf(parser.isShowSqlFormat()));
		props.setProperty("hibernate.hbm2ddl.auto", parser.getOperation().getValue());

		props.setProperty("hibernate.event.merge.entity_copy_observer", "allow");
		props.setProperty("hibernate.current_session_context_class", "thread");
		props.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.internal.NoCacheProvider");

		return props;
	}

	private Set<Class> initClasses(List<LogFieldType> list, ClassPoolLoader loader) throws ExceptionInInitializerError, NotFoundException, CannotCompileException, IOException {
		Set<Class> classes = new HashSet<>();
		for (LogFieldType f : list) {
			for (Class c : f.getDependencies()) {
				classes.add(c);
			}
			classes.add(f.getClassE());
		}
		classes.add(UserIdDump.dump(list, loader));
		classes.add(UserIdAbs.class);
		classes.add(RequestDump.dump(list, loader));
		classes.add(RequestAbs.class);
		classes.add(PageViewDump.dump(loader));
		classes.add(PageViewAbs.class);
		classes.add(UserSessionDump.dump(loader));
		classes.add(UserSessionAbs.class);
		return classes;
	}

	private ClassPoolLoader initClassLoader(ArgumentParser argumentParser) throws NotFoundException, CannotCompileException, IOException {
		/** Dodaj jar datoeke */
		Set<URL> set = new HashSet<>();
		if (argumentParser.getDriverUrl() != null) {
			set.add(argumentParser.getDriverUrl());
		}
		if (argumentParser.getDialect() != null) {
			set.add(argumentParser.getDialect());
		}
		if (set.size() > 0) {
			URLClassLoader urlLoader = new URLClassLoader(set.toArray(new URL[set.size()]), ClassLoader.getSystemClassLoader());
			/** Ustvari dinamicne razrede */
			return new ClassPoolLoader(urlLoader);
		} else {
			return new ClassPoolLoader();
		}
	}

	public ClassPoolLoader getLoader() {
		return loader;
	}

	public synchronized Object execute(Operation operation, HibernateTable table) {
		Object ret;
		try (Session session = factory.openSession()) {
			ret = operation.run(session, table);
		}
		return ret;
	}

	@Override
	public void close() {
		if (factory != null) {
			factory.close();
		}
		if (serviceRegistry != null) {
			StandardServiceRegistryBuilder.destroy(serviceRegistry);
		}
	}
}
