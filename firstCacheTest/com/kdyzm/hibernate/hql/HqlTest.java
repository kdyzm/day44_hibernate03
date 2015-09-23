package com.kdyzm.hibernate.hql;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.kdyzm.hibernate.domain.Student;

public class HqlTest {
	private static SessionFactory sessionFactory;
	static{
		Configuration configuration = new Configuration();
		configuration.configure();
		sessionFactory = configuration.buildSessionFactory();
	}
	
	/**
	 * ��򵥵Ĳ�ѯ���
	 */
	@Test
	public void testOne(){
		Session session=sessionFactory.openSession();
		Query query=session.createQuery("from Student where sid='1'");
		List<Student>students=query.list();
		for(Student student:students){
			System.out.println(student);
		}
		session.close();
	}
	
	/**
	 * ��ѯ���е�ѧ��
	 */
	@Test
	public void test2(){
		Session session=sessionFactory.openSession();
		Query query=session.createQuery("" +
				"select new com.kdyzm.hibernate.domain.Student(sid,sname) from Student");
		List<Student>students=query.list();
		session.close();
	}
	
	/**
	 * ����:�������sid���н�������
	 */
	@Test
	public void testOrder(){
		Session session=sessionFactory.openSession();
		Query query=session.createQuery("select new com.kdyzm.hibernate.domain.Student(sid,sname) from Student order by sid desc");
		List<Student>students=query.list();
		System.out.println(students);
		session.close();
	}
	
	/**
	 * ���Ե�ֵ����
	 */
	@Test
	public void testConnectionBySid(){
		Session session=sessionFactory.openSession();
		
		Query query=session.createQuery("from Student,");
		List list=query.list();
		
		session.close();
	}
}

