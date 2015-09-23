package com.kdyzm.fetchtest;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.kdyzm.hibernate.domain.Course;
import com.kdyzm.hibernate.domain.Student;

public class FetchTest {
	private static SessionFactory sessionFactory;
	static{
		Configuration configuration=new Configuration();
		configuration.configure();
		sessionFactory=configuration.buildSessionFactory();
	}
	/*
	 * n+1����ѯ���������ص㡣
	 */
	@Test
	public void baseTest(){
		Session session=sessionFactory.openSession();
		List<Student>students=session.createQuery("from Student").list();
		for(Student student:students){
			Set<Course>courses=student.getCourses();
			for(Course course:courses){
				System.out.println(course);
			}
		}
		session.close();
	}
	
	/*
	 * ��ѯ�༶cidΪ1,3������ѧ��
	 * 
	 * �����Ҫ�õ��Ӳ�ѯһ�������subselect(fetch����ֵ)
	 * ʹ��subselectֻ��Ҫ����SQL��䡣
	 * 
	 */
	@Test
	public void test2(){
		Session session=sessionFactory.openSession();
		List<Course>courses=session.createQuery("from Course where cid in(1,3)").list();
		for(Course course:courses){
			Set<Student>students=course.getStudents();
			for(Student student:students){
				System.out.println(student);
			}
		}
		session.close();
	}
	
	/*
	 * �ܽ᣺���ϵ������У�ʹ��select��join����Ч����ͬ��ʹ���Ӳ�ѯsubselectЧ����ߡ�
	 */
	
	
}
