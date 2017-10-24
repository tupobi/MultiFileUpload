package com.Jay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Jay.entity.Student;

public class StudentDao {
	private JdbcConnectionUtil jdbcConnectionUtil;
	
	public StudentDao(){
		jdbcConnectionUtil = new JdbcConnectionUtil();
	}

	public List<Student> getJsonData() {
		Connection  connection = jdbcConnectionUtil.getConnection_jdbc("jdbc:mysql://localhost:3306/mydatabase", "root", "123");
		String sql = "SELECT * FROM student";
		ArrayList<Student> students = new ArrayList<Student>();//变量放在方法里，否则不会被销毁。
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
//			ps.setString(1, "01");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				students.add(new Student(rs.getString(1), rs.getString(2), rs.getFloat(3)));
			}
			connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return students;
	}

	public Student getResponseData(String id) {
		Connection  connection = jdbcConnectionUtil.getConnection_jdbc("jdbc:mysql://localhost:3306/mydatabase", "root", "123");
		String sql = "SELECT * FROM student WHERE id = ?";
		Student student = null;
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				student = new Student();
				student.setId(rs.getString(1));
				student.setName(rs.getString(2));
				student.setScore(rs.getFloat(3));
			}
			connection.close();
			return student;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
