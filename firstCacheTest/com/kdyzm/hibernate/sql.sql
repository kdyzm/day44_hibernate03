DROP TABLE IF EXISTS course_stu;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS stu;
CREATE TABLE course(
	cid BIGINT,
	cname VARCHAR(32),
	CONSTRAINT course_pk PRIMARY KEY(cid)
);
INSERT INTO course(cid,cname) VALUES (1,'C语言');
INSERT INTO course(cid,cname) VALUES (2,'数据库');
INSERT INTO course(cid,cname) VALUES (3,'数据结构');
INSERT INTO course(cid,cname) VALUES (4,'java程序设计');
CREATE TABLE stu(
	sid BIGINT,
	sname VARCHAR(32),
	CONSTRAINT stu_pk PRIMARY KEY(sid)
);
INSERT INTO stu(sid,sname) VALUES(1,'张三');
INSERT INTO stu(sid,sname) VALUES(2,'李四');
INSERT INTO stu(sid,sname) VALUES(3,'王五');
INSERT INTO stu(sid,sname) VALUES(4,'赵六');
CREATE TABLE course_stu(
	cid BIGINT,
	sid BIGINT,
	PRIMARY KEY(cid,sid),
	CONSTRAINT course_stu_fk_cid FOREIGN KEY (cid) REFERENCES course(cid),
	CONSTRAINT course_stu_fk_sid FOREIGN KEY (sid) REFERENCES stu(sid)
);
INSERT INTO course_stu(cid,sid) VALUES(1,2);
INSERT INTO course_stu(cid,sid) VALUES(1,3);
INSERT INTO course_stu(cid,sid) VALUES(1,4);
INSERT INTO course_stu(cid,sid) VALUES(2,1);
INSERT INTO course_stu(cid,sid) VALUES(2,3);
INSERT INTO course_stu(cid,sid) VALUES(2,4);
INSERT INTO course_stu(cid,sid) VALUES(3,1);
INSERT INTO course_stu(cid,sid) VALUES(3,2);
INSERT INTO course_stu(cid,sid) VALUES(3,4);
INSERT INTO course_stu(cid,sid) VALUES(4,1);
INSERT INTO course_stu(cid,sid) VALUES(4,2);
INSERT INTO course_stu(cid,sid) VALUES(4,3);