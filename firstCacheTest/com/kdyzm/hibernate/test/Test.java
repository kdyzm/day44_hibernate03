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
 * ͨ����Զ��ϵ����һ�������������
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
	 * ���Ի��������ǲ���������
	 */
	@org.junit.Test
	public void testBase(){
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		
		Student student=new Student();
		student.setSname("��ѧ��");
		Course course=new Course();
		course.setCname("�¿γ̣�");
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
	 * ��ô�������ݴ�ŵ�һ�������У���
	 * ʹ��get�����ܹ�������ŵ�һ��������
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
	 * ����ʹ��get������ʹ��save����Ҳ�ܹ������ݷŵ�һ��������
	 * 
	 *	Hibernate: select max(sid) from stu
		Hibernate: insert into test.stu (sname, sid) values (?, ?)
		Student [sid=5, sname=��ѧ����, courses=null]
		
		û���ٷ�����ѯ����ʾ�Ǵӻ����л�ȡ�ġ�
	 */
	@org.junit.Test
	public void saveTest(){
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		Student student =new Student();
		student.setSname("��ѧ����");
		session.save(student);
		student=(Student) session.get(Student.class, student.getSid());
		transaction.commit();
		session.close();
		System.out.println(student);
	}
	
	/*
	 * ʹ��update����Ҳ�ܹ������ݱ��浽һ��������
	 * 
	 * ����δ��������۽���ӡ�ķ�����������������쳣�׳���
	 */
	@org.junit.Test
	public void updateTest(){
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		
		Student student=(Student)session.get(Student.class, 1L);
		session.evict(student);
		
		student.setSname("��ѧ����");
		session.update(student);
		Student student2=(Student) session.get(Student.class, 1L);//ʹ�øô�������Ƿ񱣴浽��һ��������
		transaction.commit();
//		System.out.println(student2);//�����������ֱ�ӱ���ջ����쳣��
		session.close();
//		System.out.println(student2);//�������������ͻᱨ�������쳣
	}
	
	
	/*
	 * ����flush�����������ʹ�á�
	 */
	@org.junit.Test
	public void testFlush(){
		//���Է���flush���÷���ʵ�����Ǵ����������ݵķ�������黺���е�����
		//��������е������б仯�򷢳�sql�����и��£����򲻷������²���������sql
	}
	
	/*
	 * �ٴβ���flush����
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
			course.setCname("�¿γ̣�");
			break;
		}
		for(Course course:student4.getCourses()){
			course.setCname("�¿γ�2");
			break;
		}
		
		session.flush();
		transaction.commit();
		session.close();
	}
	
	
	/*
	 * refresh���������þ��ǽ����ݿ��еĶ�Ӧ�������¸��µ�һ�������С�
	 * 
	 * 
	 * Hibernate: select student0_.sid as sid2_0_, student0_.sname as sname2_0_ from test.stu student0_ where student0_.sid=?
		Student [sid=1, sname=��ͬѧ��]
		Hibernate: select student0_.sid as sid2_0_, student0_.sname as sname2_0_ from test.stu student0_ where student0_.sid=?
		Student [sid=1, sname=����]
	 */
	@org.junit.Test
	public void testRefresh(){
		Session session = sessionFactory.openSession();
		Student student=(Student)session.get(Student.class, 1L);
		student.setSname("��ͬѧ��");
		System.out.println(student);
		session.refresh(student);
		System.out.println(student);
		/*System.out.println(student.getSname());//�������֮��ͻᱨ����ջ������쳣��
		System.out.println(student.getSid());
		Set<Course>courses=student.getCourses();
		for(Course course:courses){
			System.out.println(course);
		}*/
		session.close();
//		System.out.println(student);//��������ͻ�����������쳣�Ĵ���
	}
	
	//������������
	//save�����Ὣ���ݱ��浽һ�����棬�������������̫�������ɶ�ջ���
	//��ʱ�������������Ǳ�Ҫ�ģ�����ʹ��ʮ�������ݽ��в���
	@org.junit.Test
	public void testEnd(){
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		
		for(int i=0;i<100000;i++){
			Student student=new Student();
			student.setSname("��ͯЬ��"+i);
			session.save(student);//save�����Ὣ���ݱ��浽���浫�����
			if(i%100==0){
				session.flush();
				session.clear();
			}
		}
		transaction.commit();
		session.close();
	}
}
