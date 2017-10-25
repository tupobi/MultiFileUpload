package com.itheima.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

public class UploadServlet2 extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// request.setCharacterEncoding("UTF-8");
		// 要执行文件上传的操作
		// 判断表单是否支持文件上传。即：enctype="multipart/form-data"
		boolean isMultipartContent = ServletFileUpload
				.isMultipartContent(request);
		if (!isMultipartContent) {
			throw new RuntimeException("your form is not multipart/form-data");
		}

		// 创建一个DiskFileItemfactory工厂类
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File("f:\\"));// 指定临时文件的存储目录
		// 创建一个ServletFileUpload核心对象
		ServletFileUpload sfu = new ServletFileUpload(factory);
		// 解决上传表单项乱码问题
		sfu.setHeaderEncoding("UTF-8");
		// 限制上传文件的大小

		// 解析request对象，并得到一个表单项的集合
		try {
			// sfu.setFileSizeMax(1024*1024*3);//表示3M大小
			// sfu.setSizeMax(1024*1024*6);
			List<FileItem> fileItems = sfu.parseRequest(request);

			// 遍历表单项数据
			for (FileItem fileitem : fileItems) {
				if (fileitem.isFormField()) {
					// 普通表单项
					processFormField(fileitem);
				} else {
					// 上传表单项
					processUploadField(fileitem);
				}
			}

		} catch (FileUploadBase.FileSizeLimitExceededException e) {
			// throw new RuntimeException("文件过在，不能超过3M");

			System.out.println("文件过在，不能超过3M");
		} catch (FileUploadBase.SizeLimitExceededException e) {
			System.out.println("总文件大小不能超过6M");
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}

	private void processUploadField(FileItem fileitem) {
		try {
			// 得到文件输入流
			InputStream is = fileitem.getInputStream();

			// 创建一个文件存盘的目录
			String directoryRealPath = this.getServletContext().getRealPath(
					"/WEB-INF/upload");
			File storeDirectory = new File(directoryRealPath);// 即代表文件又代表目录
			if (!storeDirectory.exists()) {
				storeDirectory.mkdirs();// 创建一个指定的目录
			}
			// 得到上传的名子
			String filename = fileitem.getName();// 文件项中的值 F:\图片素材\小清新\43.jpg 或者
													// 43.jpg
			if (filename != null) {
				// filename =
				// filename.substring(filename.lastIndexOf(File.separator)+1);
				filename = FilenameUtils.getName(filename);// 效果同上
			}

			// 解决文件同名的问题
			filename = UUID.randomUUID() + "_" + filename;
			// 目录打散
			// String childDirectory = makeChildDirectory(storeDirectory); //
			// 2015-10-19
			String childDirectory = makeChildDirectory(storeDirectory, filename); // a/b

			// 上传文件，自动删除临时文件
			File file = new File(storeDirectory, childDirectory
					+ File.separator + filename);
			System.out.println("uri == " + file.getAbsolutePath() + ",,fileName == " + file.getName());
			fileitem.write(file);
			fileitem.delete();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 上传表单项
	private void processUploadField1(FileItem fileitem) {

		try {
			// 得到文件输入流
			InputStream is = fileitem.getInputStream();

			// 创建一个文件存盘的目录
			String directoryRealPath = this.getServletContext().getRealPath(
					"/WEB-INF/upload");
			File storeDirectory = new File(directoryRealPath);// 即代表文件又代表目录
			if (!storeDirectory.exists()) {
				storeDirectory.mkdirs();// 创建一个指定的目录
			}
			// 得到上传的名子
			String filename = fileitem.getName();// 文件项中的值 F:\图片素材\小清新\43.jpg 或者
													// 43.jpg
			// 处理文件名
			/*
			 * 解决此问题： F:\\apache-tomcat-7.0.52\\webapps\\day18_00_upload\\upload\\F:\\图片素材\\小清新\\41.jpg
			 */
			if (filename != null) {
				// filename =
				// filename.substring(filename.lastIndexOf(File.separator)+1);
				filename = FilenameUtils.getName(filename);// 效果同上
			}

			// 解决文件同名的问题
			filename = UUID.randomUUID() + "_" + filename;

			// 目录打散
			// String childDirectory = makeChildDirectory(storeDirectory); //
			// 2015-10-19
			String childDirectory = makeChildDirectory(storeDirectory, filename); // 2015-10-19

			// 在storeDirectory下，创建完整目录下的文件
			File file = new File(storeDirectory, childDirectory
					+ File.separator + filename); // 绝对目录/日期目录/文件名
			// 通过文件输出流将上传的文件保存到磁盘
			FileOutputStream fos = new FileOutputStream(file);

			int len = 0;
			byte[] b = new byte[1024];
			while ((len = is.read(b)) != -1) {
				fos.write(b, 0, len);
			}
			fos.close();
			is.close();

			fileitem.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 目录打散
	private String makeChildDirectory(File storeDirectory, String filename) {
		int hashcode = filename.hashCode();// 返回字符转换的32位hashcode码
		System.out.println(hashcode);
		String code = Integer.toHexString(hashcode); // 把hashcode转换为16进制的字符
														// abdsaf2131safsd
		System.out.println(code);
		String childDirectory = code.charAt(0) + File.separator
				+ code.charAt(1); // a/b
		// 创建指定目录
		File file = new File(storeDirectory, childDirectory);
		if (!file.exists()) {
			file.mkdirs();
		}
		return childDirectory;
	}

	// 按日期打散
	/*
	 * private String makeChildDirectory(File storeDirectory) {
	 * 
	 * SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd"); String
	 * dateDirectory = sdf.format(new Date()); //只管创建目录 File file = new
	 * File(storeDirectory,dateDirectory); if(!file.exists()){ file.mkdirs(); }
	 * 
	 * return dateDirectory; }
	 */
	// 普通表单项
	private void processFormField(FileItem fileitem) {
		try {
			String fieldname = fileitem.getFieldName();// 字段名
			String fieldvalue = fileitem.getString("UTF-8");// 字段值
			//fieldvalue = new String(fieldvalue.getBytes("iso-8859-1"),"utf-8");
			System.out.println(fieldname + "=" + fieldvalue);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
