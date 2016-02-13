package ca.albertlockett.atlanticchip.dao;

import java.io.Serializable;

import org.hibernate.Session;

import ca.albertlockett.atlanticchip.util.HibernateUtils;

public class AbstractModelDao {

	public void save(Serializable s) {
		Session session = HibernateUtils.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(s);
		session.getTransaction().commit();
	}
	
}
