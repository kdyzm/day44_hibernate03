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
	 * n+1条查询是显著的特点。
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
	 * 查询班级cid为1,3的所有学生
	 * 
	 * 如果需要用到子查询一般就是用subselect(fetch属性值)
	 * 使用subselect只需要两条SQL语句。
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
	 * 总结：以上的需求中，使用select和join方法效率相同，使用子查询subselect效率最高。
	 */
	
	
}
