package gmail_plugin_v1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import gmail_plugin_v1.uploadfile;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

public class User extends HttpServlet {
	private String account;
	private String password;
	GmailSender gs;
	GmailReader gr;
	BCS store;
	mysql db;

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (request.getRequestURI().contains("login")) {
			account = request.getParameter("account");
			password = request.getParameter("password");
			System.out.println(account+password);
			gs = new GmailSender(account, password);
			gr = new GmailReader(account, password);
			System.out.println(account.substring(0, account.indexOf('@')));
			store = new BCS(account.substring(0, account.indexOf('@')));
			db=new mysql();
			response.sendRedirect("index.html");
		}
		if (request.getRequestURI().contains("read")) {
			try {
				gr.getmessages();
				List<memfile> pl=gr.getpart();
				store.download(pl);
				if(db.getConnection()==null)System.out.println("connection is null");
				db.addatt(pl);
				db.closeConnection();
				for(memfile p:pl)
				{
					Cookie c=new Cookie(p.name,String.valueOf(p.length));
					c.setMaxAge(7*24*3600);
					response.addCookie(c);
				}
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			//request.getSession().setAttribute("map", gr.map);
			response.sendRedirect("readresult.html");
		}
		if (request.getRequestURI().contains("send")) {
			String address = null, subject = null, text = null, filesize = null, filename = null;
			DiskFileItemFactory factory = new DiskFileItemFactory(1024 * 1024,
					new File("temp"));
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(1024 * 1024 * 10);
			System.out.println("send");
			try {
				FileItemIterator iter = upload.getItemIterator(request);
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					String name = item.getFieldName();
					InputStream stream = item.openStream();
					if (item.isFormField()) {
						if (name.equals("address")) {
							address = Streams.asString(stream);
						} else if (name.equals("subject")) {
							subject = Streams.asString(stream);
						} else if (name.equals("text")) {
							text = Streams.asString(stream);
						} else if (name.equals("filesize")) {
							filesize = Streams.asString(stream);
						}
					} else {
						List<uploadfile> l = new ArrayList<uploadfile>();
						System.out.print(item.getName() + text);
						if (item.getName().lastIndexOf('\\') != -1)
							filename = item.getName().substring(
									item.getName().lastIndexOf('\\')+1);
						l.add(new uploadfile(filename, store.receive(
								filename, stream, Integer.valueOf(filesize))));
						gs.send(address, subject, text, l);
					}
				}
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.sendRedirect("index.html");
		}
		if (request.getRequestURI().contains("download")) {
			OutputStream out = response.getOutputStream();
			String name = request.getParameter("filename");
			System.out.print(name);
			response.addHeader("Content-Disposition", "attachment;filename="
					+ name);
			response.setContentType("application/octet-stream");
			System.out.print(store.getObjectMetadata("receive"+name).get("size"));
			response.addHeader("Content-Length",
					String.valueOf(store.getObjectMetadata("receive"+name).get("size")));
			byte[] buf = new byte[100 * 1024];
			int len;
			InputStream in = store.getObjectWithInputstream("receive"+name);
			while ((len=in.read(buf))!=-1) {
				out.write(buf,0,len);
			}
		}
	}
}
