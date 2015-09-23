package com.kdyzm.hibernate.secondCache;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.kdyzm.hibernate.domain.Student;

/*
 * 用于测试二级缓存的
 */
public class SecondCacheTest {
	private static SessionFactory sessionFactory;
	static{
		Configuration configuration=new Configuration();
		configuration.configure();
		sessionFactory=configuration.buildSessionFactory();
	}
	/**
	 * 测试二级缓存
	 */
	@Test
	public void testOne(){
		Session session=sessionFactory.openSession();
		Student student1=(Student) session.get(Student.class, 1L);
		Student student2=(Student) session.get(Student.class, 2L);
		Student student3=(Student)session.get(Student.class, 3L);
		Student student4=(Student)session.get(Student.class, 4L);
		
		/**
		 * 这里加上延迟的作用是什么
		 */
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		session.close();
	}
	
	/**
	 * 测试当Session关闭之后，使用Get方法仍然还能在二级缓存中获取数据。
	 * 
	 * Hibernate: select student0_.sid as sid2_1_, student0_.sname as sname2_1_, courses1_.sid as sid2_3_, course2_.cid as cid3_, course2_.cid as cid0_0_, course2_.cname as cname0_0_ from test.stu student0_ left outer join course_stu courses1_ on student0_.sid=courses1_.sid left outer join test.course course2_ on courses1_.cid=course2_.cid where student0_.sid=?
		Student [sid=1, sname=张三]
		Student [sid=1, sname=张三]
		可以看出只有第一次发出了SQL查询，第二次并没有发出SQL语句。
	 */
	@Test
	public void testGet(){
		Session session =sessionFactory.openSession();
		
		Student student=(Student)session.get(Student.class, 1L);
		System.out.println(student);
		session.close();//session关闭之后一级缓存消失，但是二级缓存仍然存在！
		session=sessionFactory.openSession();
		Student student2=(Student)session.get(Student.class, 1L);
		System.out.println(student2);
		
		session.close();
	}
	
	/*
	 * 测试对对象的操作，对于对象的操作也会同步到二级缓存中。
	 * 
	 * Hibernate: select student0_.sid as sid2_1_, student0_.sname as sname2_1_, courses1_.sid as sid2_3_, course2_.cid as cid3_, course2_.cid as cid0_0_, course2_.cname as cname0_0_ from test.stu student0_ left outer join course_stu courses1_ on student0_.sid=courses1_.sid left outer join test.course course2_ on courses1_.cid=course2_.cid where student0_.sid=?
		Student [sid=1, sname=张三]
		Hibernate: update test.stu set sname=? where sid=?
		Student [sid=1, sname=新学生！]
		预计结果是应当只有两次查询，一次是获取，一次是更新。
		更新完成之后的获取不应当再发出SQL语句，结果表明预测是正确的。
	 */
	@Test
	public void test2(){
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		Student student=(Student)session.get(Student.class, 1L);
		System.out.println(student);
		student.setSname("新学生！");
		transaction.commit();//执行commit操作的时候会将数据同时更新到二级缓存。
		session.close();//关闭session,一级缓存消失了，但是二级缓存仍然存在！
		//如果调用clear方法的话，则会将一级缓存和二级缓存都清空掉！
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
