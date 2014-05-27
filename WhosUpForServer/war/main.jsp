<%@page import="edu.dartmouth.cs.whosupfor.server.data.*"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Event Entries</title>
</head>
<body>
	<%
		String retStr = (String) request.getAttribute("_retStr");
		if (retStr != null) {
	%>
	<%=retStr%><br>
	<%
		}
	%>
	<center>
		<b>Query Result</b>
		<form name="input" action="/query.do" method="get">
			Name: <input type="text" name="name"> <input type="submit"
				value="OK">
		</form>
	</center>
	<b>
		---------------------------------------------------------------------<br>
		<%
			ArrayList<EventEntry> resultList = (ArrayList<EventEntry>) request
					.getAttribute("result");
			if (resultList != null) {
				for (EventEntry event : resultList) {
		%> Id:<%=String.valueOf(event.getID())%>&nbsp; Title:<%=event.getEventTitle()%>&nbsp;
		Type:<%=String.valueOf(event.getEventType())%>&nbsp; 
		Timestamp:<%=String.valueOf(event.getTimeStamp())%>&nbsp;  &nbsp;&nbsp; <a
		href="/delete.do?id=<%=event.getID()%>">delete</a> <br> <%
 	}
 	}
 %>
		---------------------------------------------------------------------<br>
	</b> Add new event:
	<br>
	<form name="input" action="/post.do" method="post">
		ID: <input type="number" name="event_id"> Title: <input
			type="text" name="event_title"> Type: <input type="number"
			name="event_type"> Timestamp: <input type="number"
			name="event_time_stamp"> <input type="submit" value="Add">
	</form>
	---------------------------------------------------------------------
	<br>
	<form name="input" action="/update.do" method="post">
		ID: <input type="number" name="event_id">
		Title: <input type="text" name="event_title">
		Type: <input type="number" name="event_type">
		Timestamp: <input type="number" name="event_time_stamp">
		<input type="submit" value="Update">
	</form>

</body>
</html>
