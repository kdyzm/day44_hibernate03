package com.kdyzm.hibernate.domain;

import java.io.Serializable;
import java.util.Set;

public class Student implements Serializable{
	private static final long serialVersionUID = -6019832693896138158L;
	private Long sid;
	private String sname;
	private Set<Course>courses;
	public Student() {
	}
	
	public Student(Long sid, String sname) {
		this.sid = sid;
		this.sname = sname;
	}
	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	public Long getSid() {
		return sid;
	}
	public void setSid(Long sid) {
		this.sid = sid;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	@Override
	public String toString() {
		return "Student [sid=" + sid + ", sname=" + sname + "]";
	}
}
