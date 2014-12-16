package gmail_plugin_v1;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.imgscalr.Scalr;
import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.inf.iis.bcs.model.DownloadObject;

public class User extends HttpServlet {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String account;
    private String password;
    // GmailSender gs;
    GmailReader gr;
    BCS store;
    mysql db;

    @Override

/*    public void service(HttpServletRequest request, HttpServletResponse response)
	    throws IOException {
	System.out.println(request.getRequestURI());
	if (request.getRequestURI().contains("login")) {
	    account = request.getParameter("account");
	    password = request.getParameter("password");
	    // System.out.println(account + password);
	    // gs = new GmailSender(account, password);
	    gr = new GmailReader(account, password);
	    // System.out.println(account.substring(0, account.indexOf('@')));
	    store = new BCS(account.substring(0, account.indexOf('@')));
	    db = new mysql();
	    Cookie c = new Cookie(account, password);
	    c.setMaxAge(7 * 24 * 3600);
	    c.setDomain("gmailplugin.duapp.com");
	    c.setPath("/");
	    response.addCookie(c);
	    response.sendRedirect("readnew");
	}
	if (request.getRequestURI().contains("readnew")) {
	    try {
		if (db.getConnection() == null)
		    System.out.println("connection is null");
		List<memfile> ml = gr.getmessages(db);
		db.addatt(ml);
		store.download(ml, db);
		db.closeConnection();
		// if(db.getConnection()==null)System.out.println("connection is null");
		// List<memfile> pl=gr.getpart();
		// db.addatt(pl);
		// //for(memfile p:pl) if(p.data==null)
		// System.out.println("p.data is nullb");
		// store.download(pl);
		//
		// db.closeConnection();
		// for(memfile p:pl)
		// {
		// Cookie c=new Cookie(p.name,String.valueOf(p.length));
		// c.setMaxAge(7*24*3600);
		// response.addCookie(c);
		// }
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    // request.getSession().setAttribute("map", gr.map);
	    response.sendRedirect("refresh");
	}
	if (request.getRequestURI().contains("send")) {
	    // String address = null, subject = null, text = null, filesize =
	    // null, filename = null;
	    // DiskFileItemFactory factory = new DiskFileItemFactory(1024 *
	    // 1024,
	    // new File("temp"));
	    // ServletFileUpload upload = new ServletFileUpload(factory);
	    // upload.setSizeMax(1024 * 1024 * 10);
	    // System.out.println("send");
	    // try {
	    // FileItemIterator iter = upload.getItemIterator(request);
	    // while (iter.hasNext()) {
	    // FileItemStream item = iter.next();
	    // String name = item.getFieldName();
	    // InputStream stream = item.openStream();
	    // if (item.isFormField()) {
	    // if (name.equals("address")) {
	    // address = Streams.asString(stream);
	    // } else if (name.equals("subject")) {
	    // subject = Streams.asString(stream);
	    // } else if (name.equals("text")) {
	    // text = Streams.asString(stream);
	    // } else if (name.equals("filesize")) {
	    // filesize = Streams.asString(stream);
	    // }
	    // } else {
	    // List<uploadfile> l = new ArrayList<uploadfile>();
	    // System.out.print(item.getName() + text);
	    // if (item.getName().lastIndexOf('\\') != -1)
	    // filename = item.getName().substring(
	    // item.getName().lastIndexOf('\\') + 1);
	    // l.add(new uploadfile(filename, store.receive(filename,
	    // stream, Integer.valueOf(filesize))));
	    // //gs.send(address, subject, text, l);
	    // }
	    // }
	    // } catch (FileUploadException e) {
	    // // TODO Auto-generated catch block
	    // e.printStackTrace();
	    //
	    // }
	    if (db.getConnection() == null)
		System.out.println("connection is null");
	    String filesize = null, filename = null;
	    DiskFileItemFactory factory = new DiskFileItemFactory(1024 * 1024,
		    new File("temp"));
	    ServletFileUpload upload = new ServletFileUpload(factory);
	    upload.setSizeMax(1024 * 1024 * 10);
	    try {
		FileItemIterator iter = upload.getItemIterator(request);
		while (iter.hasNext()) {
		    FileItemStream item = iter.next();
		    String name = item.getFieldName();
		    InputStream stream = item.openStream();
		    if (item.isFormField()) {
			if (name.equals("filesize")) {
			    filesize = Streams.asString(stream);
			} 
		    } else {
			System.out.print(item.getName());
			if (item.getName().lastIndexOf('\\') != -1)
			    filename = item.getName().substring(
				    item.getName().lastIndexOf('\\') + 1);
			filename = item.getName();
			System.out.print(filename);
			store.receive(filename, stream,
			Integer.valueOf(filesize));
			db.addatt(filename, Integer.valueOf(filesize));
		    }
		}
		db.closeConnection();
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
	    // System.out.print(store.getObjectMetadata("receive" + name).get(
	    // "size"));
	    DownloadObject downobject = store.getObjectWithInputstream(name);
	    int length = (int) downobject.getObjectMetadata()
		    .getContentLength();
	    response.addHeader("Content-Length", String.valueOf(length));
	    InputStream in = downobject.getContent();
	    // byte[] buf = new byte[100 * 1024];
	    // int len;
	    // while ((len = in.read(buf)) != -1) {
	    // out.write(buf, 0, len);
	    // }
	    byte[] data = new byte[length];
	    int readlength = 0;
	    while (readlength < length) {
		readlength += in.read(data, readlength, length - readlength);
	    }
	    out.write(data);
	}
	if (request.getRequestURI().contains("refresh")) {
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
		for (Cookie cookie : cookies) {
		    cookie.setValue("");
		    cookie.setMaxAge(0);
		    cookie.setPath("/");
		    response.addCookie(cookie);
		}
	    }
	    if (db.getConnection() == null)
		System.out.println("connection is null");
	    for (cookiemessage cm : db.refreshatt()) {
		System.out.println(cm.name + cm.mail);
		Cookie c = new Cookie(cm.name, cm.mail);
		c.setMaxAge(7 * 24 * 3600);
		response.addCookie(c);
	    }
	    db.closeConnection();
	    response.sendRedirect("read.html");
	}
	if (request.getRequestURI().contains("search")) {
	    System.out.println("search");
	    String name = request.getParameter("q");
	    System.out.println(name);
	    PrintWriter pw = response.getWriter();
	    db = new mysql();
	    if (db.getConnection() == null)
		System.out.println("connection is null");
	    for (cookiemessage cm : db.refreshatt()) {
		System.out.println(cm.name + cm.mail);
		pw.write(cm.name + cm.mail);
	    }
	    pw.close();
	    db.closeConnection();
	}
    }
*/
    protected void doGet(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {

	System.out.println("doget");
	if (request.getRequestURI().contains("login")) {
	    account = request.getParameter("account");
	    password = request.getParameter("password");
	    System.out.println(account + password);
	    // gs = new GmailSender(account, password);
	    gr = new GmailReader(account, password);
	    // System.out.println(account.substring(0, account.indexOf('@')));
	    store = new BCS(account.substring(0, account.indexOf('@')));
	    db = new mysql();
	    Cookie c = new Cookie(account, password);
	    c.setMaxAge(7 * 24 * 3600);
	    c.setDomain("gmailplugin.duapp.com");
	    c.setPath("/");
	    response.addCookie(c);
	    response.sendRedirect("readnew");
	}
	else if (request.getRequestURI().contains("download")) {
	    OutputStream out = response.getOutputStream();
	    String name = request.getParameter("filename");
	    System.out.print(name);
	    response.addHeader("Content-Disposition", "attachment;filename="
		    + name);
	    response.setContentType("application/octet-stream");
	    // System.out.print(store.getObjectMetadata("receive" + name).get(
	    // "size"));
	    DownloadObject downobject = store.getObjectWithInputstream(name);
	    int length = (int) downobject.getObjectMetadata()
		    .getContentLength();
	    response.addHeader("Content-Length", String.valueOf(length));
	    InputStream in = downobject.getContent();
	    // byte[] buf = new byte[100 * 1024];
	    // int len;
	    // while ((len = in.read(buf)) != -1) {
	    // out.write(buf, 0, len);
	    // }
	    byte[] data = new byte[length];
	    int readlength = 0;
	    while (readlength < length) {
		readlength += in.read(data, readlength, length - readlength);
	    }
	    out.write(data);
	}
	else if (request.getRequestURI().contains("readnew")) {
	    try {
		if (db.getConnection() == null)
		    System.out.println("connection is null");
		List<memfile> ml = gr.getmessages(db);
		db.addatt(ml);
		store.download(ml, db);
		db.closeConnection();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
           response.sendRedirect("refresh");
	}
	else if (request.getRequestURI().contains("refresh")) {	   
	    if (db.getConnection() == null)
		System.out.println("connection is null");
	    List<memfile> ml=db.refresh();
	    request.getSession().setAttribute("memfile",ml);
	    db.closeConnection();
	    response.sendRedirect("kindex.jsp");
	}
	else {
	    if (request.getParameter("getfile") != null
		    && !request.getParameter("getfile").isEmpty()) {
		File file = new File(request.getParameter("getfile"));
		if (file.exists()) {
		    int bytes = 0;
		    ServletOutputStream op = response.getOutputStream();

		    response.setContentType(getMimeType(file));
		    response.setContentLength((int) file.length());
		    response.setHeader("Content-Disposition",
			    "inline; filename=\"" + file.getName() + "\"");

		    byte[] bbuf = new byte[1024];
		    DataInputStream in = new DataInputStream(
			    new FileInputStream(file));

		    while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
			op.write(bbuf, 0, bytes);
		    }

		    in.close();
		    op.flush();
		    op.close();
		}
	    } else if (request.getParameter("delfile") != null
		    && !request.getParameter("delfile").isEmpty()) {
		File file = new File(request.getParameter("delfile"));
		if (file.exists()) {
		    file.delete(); // TODO:check and report success
		}
	    } else if (request.getParameter("getthumb") != null
		    && !request.getParameter("getthumb").isEmpty()) {
		File file = new File(request.getParameter("getthumb"));
		if (file.exists()) {
		    String mimetype = getMimeType(file);
		    if (mimetype.endsWith("png") || mimetype.endsWith("jpeg")
			    || mimetype.endsWith("gif")) {
			BufferedImage im = ImageIO.read(file);
			if (im != null) {
			    BufferedImage thumb = Scalr.resize(im, 75);
			    ByteArrayOutputStream os = new ByteArrayOutputStream();
			    if (mimetype.endsWith("png")) {
				ImageIO.write(thumb, "PNG", os);
				response.setContentType("image/png");
			    } else if (mimetype.endsWith("jpeg")) {
				ImageIO.write(thumb, "jpg", os);
				response.setContentType("image/jpeg");
			    } else {
				ImageIO.write(thumb, "GIF", os);
				response.setContentType("image/gif");
			    }
			    ServletOutputStream srvos = response
				    .getOutputStream();
			    response.setContentLength(os.size());
			    response.setHeader("Content-Disposition",
				    "inline; filename=\"" + file.getName()
					    + "\"");
			    os.writeTo(srvos);
			    srvos.flush();
			    srvos.close();
			}
		    }
		} // TODO: check and report success
	    } else {
		PrintWriter writer = response.getWriter();
		writer.write("call POST with multipart form data");
	    }
	}

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     * 
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	System.out.println("dopost");
	if (!ServletFileUpload.isMultipartContent(request)) {
	    throw new IllegalArgumentException(
		    "Request is not multipart, please 'multipart/form-data' enctype for your form.");
	}

	ServletFileUpload uploadHandler = new ServletFileUpload(
		new DiskFileItemFactory());
	PrintWriter writer = response.getWriter();
	response.setContentType("application/json");
	JSONArray json = new JSONArray();
	try {
	    List<FileItem> items = uploadHandler.parseRequest(request);
	    for (FileItem item : items) {
		if (!item.isFormField()) {
		    File file = new File(item.getName());
		    item.write(file);
		    db.addatt(item.getName(), (int) item.getSize());
		    store.putObjectByFile(item.getName(), file);
		    System.out.println(item.getName());
		    System.out.println(file.getAbsolutePath());
		    JSONObject jsono = new JSONObject();
		    jsono.put("name", item.getName());
		    jsono.put("size", item.getSize());
		    jsono.put("url", "upload?getfile=" + item.getName());
		    jsono.put("thumbnail_url",
			    "upload?getthumb=" + item.getName());
		    jsono.put("delete_url", "upload?delfile=" + item.getName());
		    jsono.put("delete_type", "GET");
		    json.put(jsono);

		}
	    }
	} catch (FileUploadException e) {
	    throw new RuntimeException(e);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	} finally {
	    JSONObject jsonfinal = new JSONObject();
	    jsonfinal.put("files", json);
	    writer.write(jsonfinal.toString());
	    System.out.println(jsonfinal.toString());
	    writer.close();
	}

    }

    private String getMimeType(File file) {
	String mimetype = "";
	if (file.exists()) {
	    // URLConnection uc = new URL("file://" +
	    // file.getAbsolutePath()).openConnection();
	    // String mimetype = uc.getContentType();
	    // MimetypesFIleTypeMap gives PNG as application/octet-stream, but
	    // it seems so does URLConnection
	    // have to make dirty workaround
	    if (getSuffix(file.getName()).equalsIgnoreCase("png")) {
		mimetype = "image/png";
	    } else {
		javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
		mimetype = mtMap.getContentType(file);
	    }
	}
	System.out.println("mimetype: " + mimetype);
	return mimetype;
    }

    private String getSuffix(String filename) {
	String suffix = "";
	int pos = filename.lastIndexOf('.');
	if (pos > 0 && pos < filename.length() - 1) {
	    suffix = filename.substring(pos + 1);
	}
	System.out.println("suffix: " + suffix);
	return suffix;
    }
}
