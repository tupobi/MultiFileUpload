package com.itheima.upload;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadServlet1 extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 由于表单中提交数据的方式改为multipart/form-data，所以request.getParameter("name")失效
		 * String name = request.getParameter("name");
		String photo = request.getParameter("photo");
		System.out.println(name);
		System.out.println(photo);*/
		
		InputStream is = request.getInputStream();
		int len = 0;
		byte[] b = new byte[1024];
		while((len=is.read(b))!=-1){
			System.out.println(new String(b,0,len));
		}
		
		is.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
