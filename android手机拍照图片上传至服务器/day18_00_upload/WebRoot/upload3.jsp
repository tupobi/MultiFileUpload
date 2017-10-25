<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<script type="text/javascript">
	function addFile(){
		//得到div容器
		var div1 = document.getElementById("div1");
		div1.innerHTML +=  "<div><input type='file' name='photo' /><input type='button' value='删除' onclick='delFile(this)'/><br /></div>";
	}
	
	function delFile(input){
		//使用input对象的爷爷删除他的爸爸
		input.parentNode.parentNode.removeChild(input.parentNode);
	}
</script>
<body>
	<form enctype="multipart/form-data"
		action="${pageContext.request.contextPath }/servlet/uploadServlet2"
		method="post">
		<input type="text" name="name" /><br />
		<div id="div1">
			<div>
				<input type="file" name="photo" /><input type="button" value="添加" onclick="addFile()"/><br />
			</div>
			
		</div>
		<input type="submit" value="上传" /><br />
	</form>
</body>
</html>