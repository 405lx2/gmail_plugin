package gmail_plugin_v1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Part;

public class memfile {
	public byte[] data;
	public int length;
	public String name;
	public String type;
	public memfile(Part p) throws IOException, MessagingException
	{
		length=p.getSize();
		//System.out.println(p.getFileName()+length);
		byte[] data = new byte[length];
		int readlength = 0; 
		while (readlength < length) {
		  readlength += p.getInputStream().read(data, readlength, length - readlength);
		}
		String rawname = p.getFileName();
		int nametype = rawname.lastIndexOf('.');
		type=rawname.substring(nametype+1);
		FileOutputStream f=new FileOutputStream(new File(rawname));
		f.write(data);
		//System.out.println(readlength);
	}
}
