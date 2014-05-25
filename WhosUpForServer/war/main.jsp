<%@page import="edu.dartmouth.cs.whosupfor.server.data.PostEntity"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Post</title>
</head>
<body>
	<b>Post</b>
	<table>
		<tr>
			<td>
				<%
					ArrayList<PostEntity> postList = (ArrayList<PostEntity>) request
							.getAttribute("postList");
					for (PostEntity post : postList) {
				%> <%=post.mPostDate.toString()%>&nbsp;&nbsp;&nbsp;&nbsp; <%=post.mPostString%><br>
				<%
					}
				%>
			</td>
		</tr>

		<tr>
			<td>
				<form name="input" action="/post.do" method="post">
					<input type="text" name="post_text"> <input type="submit"
						value="Post">
				</form>
			</td>
		</tr>
	</table>

</body>
</html>
