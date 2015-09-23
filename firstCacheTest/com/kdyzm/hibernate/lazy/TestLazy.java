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
	  * *    类的懒加载
	  *    集合的懒加载    
	  *    单端关联的懒加载
	  *    */
	/*
	 * 类的懒加载
	 * 类的懒加载默认开启
	 * 必须使用load方法开启懒加载
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
	 * 集合的懒加载
	 * 
	 * 这里针对的是关联对象的懒加载问题，即Set集合什么时候进行加载。
	 * 可以在set标签上设置lazy属性的值，如果为true则为懒加载；如果是false则关闭懒加载。
	 * 
	 * 集合的懒加载也是默认开启的
	 * 这里使用get方法和使用load方法效果完全相同？
	 * 在映射文件中配置set标签的lazy标签为false之后懒加载就失去效果了。
	 * 
	 * 在set标签上设置lazy属性的值为true还不如设置为extra，这样能够彻底的实现懒加载的特性。
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
	 * 单端关联的懒加载
	 * 略。
	 */
	@Test
	public void testThree(){

		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		
		transaction.commit();
		session.close();
	}
}
