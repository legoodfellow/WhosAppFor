/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.dartmouth.cs.whosupfor.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.dartmouth.cs.whosupfor.gcm.Message;
import edu.dartmouth.cs.whosupfor.gcm.Sender;
import edu.dartmouth.cs.whosupfor.server.data.RegDatastore;

/**
 * Servlet that adds a new message to all registered devices informing
 * them to update their user database
 */
@SuppressWarnings("serial")
public class SendUserUpdateMessageServlet extends HttpServlet {

	private static final int MAX_RETRY = 5;

	/**
	 * Processes the request to add a new message.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		List<String> devices = RegDatastore.getDevices();
		
		Message message = new Message(devices);
		message.addData("message", "update_user");

		// Have to hard-code the API key when creating the Sender
		Sender sender = new Sender(Globals.GCMAPIKEY);
		// Send the message to device, at most retrying MAX_RETRY times
		sender.send(message, MAX_RETRY);


//		resp.sendRedirect("/get_user_history.do");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doPost(req, resp);
	}
}
