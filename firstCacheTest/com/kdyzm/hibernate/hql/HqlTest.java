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
	 * 最简单的查询语句
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
	 * 查询所有的学生
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
	 * 排序:这里根据sid进行降序排序。
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
	 * 测试等值连接
	 */
	@Test
	public void testConnectionBySid(){
		Session session=sessionFactory.openSession();
		
		Query query=session.createQuery("from Student,");
		List list=query.list();
		
		session.close();
	}
}

