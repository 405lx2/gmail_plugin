package gmail_plugin_v1;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
public class GmailSender {
 
	private String account;
	private String password;
	private void addAttachment(Multipart multipart, uploadfile file) throws MessagingException, IOException
	{
	    MimeBodyPart messageBodyPart = new MimeBodyPart();
	    ByteArrayDataSource ds = new ByteArrayDataSource(file.file, "application/octet-stream"); 
	    messageBodyPart.setDataHandler(new DataHandler(ds));
	    messageBodyPart.setFileName(file.name);
	    multipart.addBodyPart(messageBodyPart);
	}
	public GmailSender(String account,String password)
	{
		this.account = account;
		this.password = password;
	}
	public void send(String address,String subject,String text,List<uploadfile> files) throws IOException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		System.out.println(account+password+address);
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(account, password);
			}
		  });
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(account));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(address));
			message.setSubject(subject);
			message.setText(text);
			Multipart multipart = new MimeMultipart();
			for(uploadfile s:files) addAttachment(multipart,s);
			message.setContent(multipart);
			Transport.send(message); 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
class uploadfile
{
	String name;
	InputStream file;
	public uploadfile(String name,InputStream file)
	{
		this.name=name;
		this.file=file;
	}
}