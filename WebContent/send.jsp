<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form method="post" action="send">
		<p>请输入</p>
		<p>目标</p>
		<input type="text" name="address"><br>
		<p>主题</p>
		<input type="text" name="subject"><br>
		<textarea name="text" cols=30 rows=10>what do you want to do</textarea>
		<br> <input type="reset" value="重填"> <input type="submit"
			value="查询">
	</form>
</body>
</html>