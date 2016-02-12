package ca.albertlockett.dao;

import java.io.Serializable;

import org.hibernate.Session;

import ca.albertlockett.atlanticchip.util.HibernateUtil;

public class AbstractModelDao {

	public void save(Serializable s) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(s);
		session.getTransaction().commit();
	}
	
}
