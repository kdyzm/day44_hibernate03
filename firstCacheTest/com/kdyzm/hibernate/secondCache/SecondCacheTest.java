package com.kdyzm.hibernate.secondCache;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.kdyzm.hibernate.domain.Student;

/*
 * ���ڲ��Զ��������
 */
public class SecondCacheTest {
	private static SessionFactory sessionFactory;
	static{
		Configuration configuration=new Configuration();
		configuration.configure();
		sessionFactory=configuration.buildSessionFactory();
	}
	/**
	 * ���Զ�������
	 */
	@Test
	public void testOne(){
		Session session=sessionFactory.openSession();
		Student student1=(Student) session.get(Student.class, 1L);
		Student student2=(Student) session.get(Student.class, 2L);
		Student student3=(Student)session.get(Student.class, 3L);
		Student student4=(Student)session.get(Student.class, 4L);
		
		/**
		 * ��������ӳٵ�������ʲô
		 */
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		session.close();
	}
	
	/**
	 * ���Ե�Session�ر�֮��ʹ��Get������Ȼ�����ڶ��������л�ȡ���ݡ�
	 * 
	 * Hibernate: select student0_.sid as sid2_1_, student0_.sname as sname2_1_, courses1_.sid as sid2_3_, course2_.cid as cid3_, course2_.cid as cid0_0_, course2_.cname as cname0_0_ from test.stu student0_ left outer join course_stu courses1_ on student0_.sid=courses1_.sid left outer join test.course course2_ on courses1_.cid=course2_.cid where student0_.sid=?
		Student [sid=1, sname=����]
		Student [sid=1, sname=����]
		���Կ���ֻ�е�һ�η�����SQL��ѯ���ڶ��β�û�з���SQL��䡣
	 */
	@Test
	public void testGet(){
		Session session =sessionFactory.openSession();
		
		Student student=(Student)session.get(Student.class, 1L);
		System.out.println(student);
		session.close();//session�ر�֮��һ��������ʧ�����Ƕ���������Ȼ���ڣ�
		session=sessionFactory.openSession();
		Student student2=(Student)session.get(Student.class, 1L);
		System.out.println(student2);
		
		session.close();
	}
	
	/*
	 * ���ԶԶ���Ĳ��������ڶ���Ĳ���Ҳ��ͬ�������������С�
	 * 
	 * Hibernate: select student0_.sid as sid2_1_, student0_.sname as sname2_1_, courses1_.sid as sid2_3_, course2_.cid as cid3_, course2_.cid as cid0_0_, course2_.cname as cname0_0_ from test.stu student0_ left outer join course_stu courses1_ on student0_.sid=courses1_.sid left outer join test.course course2_ on courses1_.cid=course2_.cid where student0_.sid=?
		Student [sid=1, sname=����]
		Hibernate: update test.stu set sname=? where sid=?
		Student [sid=1, sname=��ѧ����]
		Ԥ�ƽ����Ӧ��ֻ�����β�ѯ��һ���ǻ�ȡ��һ���Ǹ��¡�
		�������֮��Ļ�ȡ��Ӧ���ٷ���SQL��䣬�������Ԥ������ȷ�ġ�
	 */
	@Test
	public void test2(){
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		Student student=(Student)session.get(Student.class, 1L);
		System.out.println(student);
		student.setSname("��ѧ����");
		transaction.commit();//ִ��commit������ʱ��Ὣ����ͬʱ���µ��������档
		session.close();//�ر�session,һ��������ʧ�ˣ����Ƕ���������Ȼ���ڣ�
		//�������clear�����Ļ�����Ὣһ������Ͷ������涼��յ���
		session=sessionFactory.openSession();
		Student student2=(Student)session.get(Student.class, 1L);
		System.out.println(student2);
		session.close();
	}
	
	
	/**
	 * 
	 */
	@Test
	public void test3(){
		
	}
	
}
