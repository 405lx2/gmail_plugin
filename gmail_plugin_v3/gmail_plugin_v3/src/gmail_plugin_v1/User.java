package gmail_plugin_v1;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class User extends HttpServlet {
	private String account;
	private String password;
	GmailSender gs;
	GmailReader gr;

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (request.getRequestURI().contains("login")) {
			account = request.getParameter("account");
			password = request.getParameter("password");
			gs = new GmailSender(account, password);
			gr = new GmailReader(account, password);
			response.sendRedirect("read.jsp");
		}
		if (request.getRequestURI().contains("read")) {
			try {
				gr.getmessages();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.getSession().setAttribute("mapkey", gr.map.keySet());
			response.sendRedirect("readresult.jsp");
		}
		if (request.getRequestURI().contains("send")) {
			String address = request.getParameter("address");
			String subject = request.getParameter("subject");
			String text=request.getParameter("text");
			request.getSession().setAttribute("mapkey", gr.map.keySet());
			response.sendRedirect("readresult.jsp");
		}
	}
}
