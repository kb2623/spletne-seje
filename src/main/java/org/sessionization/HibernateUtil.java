package org.sessionization;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

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

		registry = new StandardServiceRegistryBuilder()
				.addService(ClassLoaderService.class, new ClassLoaderServiceImpl(loader))
				.applySettings(cfg.getProperties())
				.build();
		try {
			MetadataSources sources = new MetadataSources(registry);
			for (Class c : classes) sources.addAnnotatedClass(c);
			factory = sources.buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			StandardServiceRegistryBuilder.destroy(registry);
			throw e;
		}
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
