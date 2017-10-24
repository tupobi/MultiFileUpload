package com.Jay.androidClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.Jay.biz.StudentBiz;
import com.Jay.entity.Student;

public class SendMessageToAndroidClient extends HttpServlet {
	private StudentBiz studentBiz;

	public SendMessageToAndroidClient() {
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
		List<Student> students = studentBiz.getJsonData();
		out.write(JsonUtil.beanList2JsonList(students));
		out.flush();
		out.close();
	}
}
