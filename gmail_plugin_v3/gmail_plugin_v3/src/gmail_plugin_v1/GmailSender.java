package gmail_plugin_v1;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

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

public class GmailSender {
 
	private String account;
	private String password;
	private void addAttachment(Multipart multipart, String filename) throws MessagingException, IOException
	{
	    File source = new File(filename);
	    MimeBodyPart messageBodyPart = new MimeBodyPart();        
	    messageBodyPart.attachFile(source);
	    multipart.addBodyPart(messageBodyPart);
	}
	public GmailSender(String account,String password)
	{
		this.account = account;
		this.password = password;
	}
	public void send(String address,String subject,String text,String[] file) throws IOException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
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
			for(String s:file) addAttachment(multipart,s);
			message.setContent(multipart);
			Transport.send(message); 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
