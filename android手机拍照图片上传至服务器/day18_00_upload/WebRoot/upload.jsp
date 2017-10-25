<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form enctype="multipart/form-data" action="${pageContext.request.contextPath }/servlet/uploadServlet2" method="post" >
		<input type="text" name="name"/><br/>
		<input type="file" name="photo"/><br/>
		<input type="submit" value="上传"/><br/>
	</form>
</body>
</html>