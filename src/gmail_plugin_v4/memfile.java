package gmail_plugin_v1;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Part;

public class memfile {
	//public Part pp;
	public byte[] data=null;
	public int length;
	public String name;
	public String type;
	public memfile(Part p) throws IOException, MessagingException
	{
		//pp=p;
		length=p.getSize();
		//System.out.println(p.getFileName()+length);
		data = new byte[length];
		int readlength = 0; 
		while (readlength < length) {
		  readlength += p.getInputStream().read(data, readlength, length - readlength);
		}
		name = p.getFileName();
		int nametype = name.lastIndexOf('.');
		type=name.substring(nametype+1);
		//if(data==null) System.out.println("p.data is null");
		//System.out.println(readlength+name+type);
	}
}
