package com.sample;

import java.io.PrintWriter;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ServletEx extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		try {
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			String name;

			name = request.getParameter("last_name");

			DownloadFile downloadFile = new DownloadFile();

			downloadFile.download();
			out.println(downloadFile.getStatus());
			out.println("<a href=\"C:\\file.mp3\">Click Here!</a>");
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
