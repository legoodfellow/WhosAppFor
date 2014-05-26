package edu.dartmouth.cs.whosupfor.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.dartmouth.cs.whosupfor.data.EventEntry;
import edu.dartmouth.cs.whosupfor.server.data.EventDatastore;

public class GetHistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		ArrayList<EventEntry> eventList = EventDatastore.query();

		PrintWriter out = resp.getWriter();
		for (EventEntry event : eventList) {
			//TODO: output event
			out.append(event.getEventTitle() + "\n");
		}
		
		//TODO: user history?
		
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doPost(req, resp);
	}

}
