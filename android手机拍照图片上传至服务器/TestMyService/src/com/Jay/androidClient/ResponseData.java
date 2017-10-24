package com.Jay.androidClient;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.Jay.biz.StudentBiz;
import com.Jay.entity.Student;

public class ResponseData extends HttpServlet {
	private StudentBiz studentBiz;

	public ResponseData() {
		super();
		studentBiz = new StudentBiz();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		Student student = studentBiz
				.getResponseData(request.getParameter("id"));
		if (student == null) {
			out.print("false");
		} else {
			String responseJsonData = JsonUtil.bean2Json(student);
			out.print(responseJsonData);
			
			Student temp = JsonUtil.json2Bean(responseJsonData, Student.class);
			System.out.println(temp.getName() + "id:" + temp.getId() + ",name:" + temp.getName() + ",score:" + temp.getScore());
		}
		out.flush();
		out.close();
	}
}
