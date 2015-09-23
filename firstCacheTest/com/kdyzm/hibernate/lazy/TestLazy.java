package com.kdyzm.hibernate.lazy;

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.kdyzm.hibernate.domain.Course;
import com.kdyzm.hibernate.domain.Student;

public class TestLazy {
	private static SessionFactory sessionFactory;
	static{
		Configuration configuration=new Configuration();
		configuration.configure();
		sessionFactory=configuration.buildSessionFactory();
	}
	 /*
	  * *    ���������
	  *    ���ϵ�������    
	  *    ���˹�����������
	  *    */
	/*
	 * ���������
	 * ���������Ĭ�Ͽ���
	 * ����ʹ��load��������������
	 */
	@Test
	public void testOne(){
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		
		Student student=(Student)session.load(Student.class, 1L);
		System.out.println(student.getSname());
		
		transaction.commit();
		session.close();
	}
	/*
	 * ���ϵ�������
	 * 
	 * ������Ե��ǹ�����������������⣬��Set����ʲôʱ����м��ء�
	 * ������set��ǩ������lazy���Ե�ֵ�����Ϊtrue��Ϊ�����أ������false��ر������ء�
	 * 
	 * ���ϵ�������Ҳ��Ĭ�Ͽ�����
	 * ����ʹ��get������ʹ��load����Ч����ȫ��ͬ��
	 * ��ӳ���ļ�������set��ǩ��lazy��ǩΪfalse֮�������ؾ�ʧȥЧ���ˡ�
	 * 
	 * ��set��ǩ������lazy���Ե�ֵΪtrue����������Ϊextra�������ܹ����׵�ʵ�������ص����ԡ�
	 */
	@Test
	public void testTwo(){
		Session session=sessionFactory.openSession();
		
		Student student=(Student)session.get(Student.class,1L);
		Set<Course>courses=student.getCourses();
		System.out.println(courses.size());
		
		session.close();
	}
	/*
	 * ���˹�����������
	 * �ԡ�
	 */
	@Test
	public void testThree(){

		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		
		transaction.commit();
		session.close();
	}
}
