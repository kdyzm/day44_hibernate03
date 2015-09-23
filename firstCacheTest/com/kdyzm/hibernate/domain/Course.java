package com.kdyzm.hibernate.domain;

import java.io.Serializable;
import java.util.Set;

public class Course implements Serializable{
	private static final long serialVersionUID = -5091490677186446453L;
	private Long cid;
	private String cname;
	private Set<Student>students;
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Set<Student> getStudents() {
		return students;
	}
	public void setStudents(Set<Student> students) {
		this.students = students;
	}
	public Course(Long cid, String cname, Set<Student> students) {
		this.cid = cid;
		this.cname = cname;
		this.students = students;
	}
	public Course() {
		
	}
	@Override
	public String toString() {
		return "Course [cid=" + cid + ", cname=" + cname + "]";
	}
}
