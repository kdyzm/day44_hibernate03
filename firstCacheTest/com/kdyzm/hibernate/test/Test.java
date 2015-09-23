package com.kdyzm.hibernate.test;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.kdyzm.hibernate.domain.Course;
import com.kdyzm.hibernate.domain.Student;

/**
 * 通过多对多关系测试一级缓存和懒加载
 * @author kdyzm
 *
 */
public class Test {
	private static SessionFactory sessionFactory;
	static{
		Configuration configuration=new Configuration();
		configuration.configure();
		sessionFactory=configuration.buildSessionFactory();
	}
	
	/*
	 * 测试基本配置是不是有问题
	 */
	@org.junit.Test
	public void testBase(){
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		
		Student student=new Student();
		student.setSname("新学生");
		Course course=new Course();
		course.setCname("新课程！");
		Set<Student>students=new HashSet<Student>();
		students.add(student);
		Set<Course>courses=new HashSet<Course>();
		courses.add(course);
		student.setCourses(courses);
		course.setStudents(students);
		session.save(student);
		
		transaction.commit();
		session.close();
	}
	
	/**
	 * 怎么样把数据存放到一级缓存中：、
	 * 使用get方法能够将对象放到一级缓存中
	 * 
	 * 
	 * Hibernate: select student0_.sid as sid2_0_, student0_.sname as sname2_0_ from test.stu student0_ where student0_.sid=?
	 */
	@org.junit.Test
	public void test1(){
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		
		Student student=(Student) session.get(Student.class, 1L);
		
		transaction.commit();
		session.close();
	}
	
	/**
	 * 除了使用get方法，使用save方法也能够将数据放到一级缓存中
	 * 
	 *	Hibernate: select max(sid) from stu
		Hibernate: insert into test.stu (sname, sid) values (?, ?)
		Student [sid=5, sname=新学生！, courses=null]
		
		没有再发出查询语句表示是从缓存中获取的。
	 */
	@org.junit.Test
	public void saveTest(){
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		Student student =new Student();
		student.setSname("新学生！");
		session.save(student);
		student=(Student) session.get(Student.class, student.getSid());
		transaction.commit();
		session.close();
		System.out.println(student);
	}
	
	/*
	 * 使用update方法也能够将数据保存到一级缓存中
	 * 
	 * 问题未解决，无论将打印的方法放在哪里，都会有异常抛出。
	 */
	@org.junit.Test
	public void updateTest(){
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		
		Student student=(Student)session.get(Student.class, 1L);
		session.evict(student);
		
		student.setSname("新学生！");
		session.update(student);
		Student student2=(Student) session.get(Student.class, 1L);//使用该代码测试是否保存到了一级缓存中
		transaction.commit();
//		System.out.println(student2);//加上这句代码就直接报堆栈溢出异常了
		session.close();
//		System.out.println(student2);//这句代码放在这里就会报懒加载异常
	}
	
	
	/*
	 * 测试flush方法在这里的使用。
	 */
	@org.junit.Test
	public void testFlush(){
		//测试方法flush，该方法实际上是处理缓存中数据的方法，检查缓存中的数据
		//如果缓存中的数据有变化则发出sql语句进行更新，否则不发出更新操作的请求sql
	}
	
	/*
	 * 再次测试flush方法
	 */
	@org.junit.Test
	public void testFlush2(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		Student student1=(Student)session.get(Student.class, 1L);
		Student student2=(Student)session.get(Student.class, 2L);
		Student student3=(Student)session.get(Student.class, 3L);
		Student student4=(Student)session.get(Student.class, 4L);
		
		for(Course course:student1.getCourses()){
			course.setCname("新课程！");
			break;
		}
		for(Course course:student4.getCourses()){
			course.setCname("新课程2");
			break;
		}
		
		session.flush();
		transaction.commit();
		session.close();
	}
	
	
	/*
	 * refresh方法的作用就是将数据库中的对应数据重新更新到一级缓存中。
	 * 
	 * 
	 * Hibernate: select student0_.sid as sid2_0_, student0_.sname as sname2_0_ from test.stu student0_ where student0_.sid=?
		Student [sid=1, sname=新同学！]
		Hibernate: select student0_.sid as sid2_0_, student0_.sname as sname2_0_ from test.stu student0_ where student0_.sid=?
		Student [sid=1, sname=张三]
	 */
	@org.junit.Test
	public void testRefresh(){
		Session session = sessionFactory.openSession();
		Student student=(Student)session.get(Student.class, 1L);
		student.setSname("新同学！");
		System.out.println(student);
		session.refresh(student);
		System.out.println(student);
		/*System.out.println(student.getSname());//加上这句之后就会报出堆栈溢出的异常。
		System.out.println(student.getSid());
		Set<Course>courses=student.getCourses();
		for(Course course:courses){
			System.out.println(course);
		}*/
		session.close();
//		System.out.println(student);//加在这里就会出现懒加载异常的错误
	}
	
	//测试批量操作
	//save方法会将数据保存到一级缓存，但是如果数据量太大，则会造成堆栈溢出
	//定时定量的清理缓存是必要的，这里使用十万条数据进行测试
	@org.junit.Test
	public void testEnd(){
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		
		for(int i=0;i<100000;i++){
			Student student=new Student();
			student.setSname("新童鞋！"+i);
			session.save(student);//save方法会将数据保存到缓存但是如果
			if(i%100==0){
				session.flush();
				session.clear();
			}
		}
		transaction.commit();
		session.close();
	}
}
