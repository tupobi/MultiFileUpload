package com.Jay.biz;

import java.util.List;

import com.Jay.dao.StudentDao;
import com.Jay.entity.Student;

public class StudentBiz {
	private StudentDao studentDao;
	
	public StudentBiz(){
		studentDao = new StudentDao();
	}

	public List<Student> getJsonData() {
		return studentDao.getJsonData();
	}

	public Student getResponseData(String id) {
		// TODO Auto-generated method stub
		return studentDao.getResponseData(id);
	}

}
