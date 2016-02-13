package ca.albertlockett.atlanticchip.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * adapted from mkyong
 * @author Albert.Lockett
 *
 */
public class HibernateUtils {

	private static final Logger logger = LoggerFactory
			.getLogger(HibernateUtils.class);
	
	private static final SessionFactory sessionFactory = buildSessionFactory();
	
	private static SessionFactory buildSessionFactory() {
		try {
			// Create sessionFactory from hibernate.cfg.xml
			return new Configuration().configure().buildSessionFactory();
		} catch(Throwable ex) {
			logger.error("Initial sessionFactory creation failed: {}", ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static void shutdown() {
		// close caches and connection pools
		getSessionFactory().close();
	}
	
}
