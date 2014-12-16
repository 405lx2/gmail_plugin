package gmail_plugin_v1;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.Part;

public class memfile {
    // public Part pp;
    public byte[] data = null;
    public int length;
    public String name;
    public String type;
    public String date;
    public String subject;
    public String from;
    public memfile(Part p,String date,String subject,String from) throws IOException, MessagingException {
	// pp=p;
	this.date=date;
	this.subject=subject;
	this.from=from;
	length = p.getSize();
	// System.out.println(p.getFileName()+length);
	data = new byte[length];
	int readlength = 0;
	while (readlength < length) {
	    readlength += p.getInputStream().read(data, readlength,
		    length - readlength);
	}
	name = p.getFileName();
	int nametype = name.lastIndexOf('.');
	type = name.substring(nametype + 1);
	// if(data==null) System.out.println("p.data is null");
	 System.out.println(name);
    }
    public memfile(ResultSet haveatt) throws SQLException
    {
	this.name=haveatt.getString("name");
	this.length=haveatt.getInt("length");
	this.type=haveatt.getString("type");
	this.subject=haveatt.getString("subject");
	this.date=haveatt.getString("date");
	this.from=haveatt.getString("from");
    }
}
