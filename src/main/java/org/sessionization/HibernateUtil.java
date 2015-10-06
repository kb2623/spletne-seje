package org.sessionization;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.sessionization.database.ConnectionStatusConverter;
import org.sessionization.database.InetAddressConverter;
import org.sessionization.database.MethodConverter;
import org.sessionization.fields.Address;
import org.sessionization.fields.Method;
import org.sessionization.fields.ncsa.ConnectionStatus;

import java.io.File;
import java.util.Properties;
import java.util.Set;

public class HibernateUtil implements AutoCloseable {

	private SessionFactory factory = null;
	private ServiceRegistry registry = null;
	private ClassLoader loader = null;

	public HibernateUtil(Properties props, ClassLoader loader, Set<Class> classes) {
		this.loader = loader;

		Configuration cfg = new Configuration();
		cfg.addProperties(props);
		for (Class c : classes) cfg.addAnnotatedClass(c);

		registry = new StandardServiceRegistryBuilder()
				.addService(ClassLoaderService.class, new ClassLoaderServiceImpl(loader))
				.applySettings(cfg.getProperties())
				.build();
		factory = cfg.buildSessionFactory(registry);
	}

	public ClassLoader getLoader() {
		return loader;
	}

	public Session getSession() {
		return factory.openSession();
	}

	@Override
	public void close() {
		if (registry != null) StandardServiceRegistryBuilder.destroy(registry);
		if (factory != null) factory.close();
	}
}
