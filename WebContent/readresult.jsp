<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Map.Entry"%>
<%@ page import="javax.mail.Part"%>
<%@ page import="gmail_plugin_v1.messageunit"%>
<%@ page import="org.apache.http.client.ClientProtocolException"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		Map<String, messageunit> map = (Map<String, messageunit>) session.getAttribute("map");
		for (Entry<String,messageunit> entry: map.entrySet()) {
	%>
	<br> <%=entry.getKey()%>
	<%
			for(Part p:entry.getValue().part){
	%>
			<%=p.getFileName()%><a href="download?message=<%=entry.getKey()%>>&filename=<%=p.getFileName()%>">下载</a>
	<% 
			}
		}
	%>
</body>
</html>