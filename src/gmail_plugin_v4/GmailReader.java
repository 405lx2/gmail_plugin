package gmail_plugin_v1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

public class GmailReader {

	public String account;
	public String password;
	public Map<String, messageunit> map = new HashMap<String, messageunit>();

	public void getmessages() throws MessagingException, IOException {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore("imaps");
		store.connect("imap.gmail.com", account, password);
		System.out.println(store.toString());
		Folder inbox = store.getFolder("Inbox");
		inbox.open(Folder.READ_ONLY);
		Message[] tempmessages = inbox.getMessages();
		for (Message message : tempmessages) {
			if (message.getContentType().contains("multipart")) {
				Multipart multiPart = (Multipart) message.getContent();
				for (int i = 0; i < multiPart.getCount(); i++) {
					MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
					messageunit temp = null;
					if (part.getFileName() != null && part.getFileName() != "") {
						if ((part.getDisposition() != null)
								&& (part.getDisposition()
										.equalsIgnoreCase(Part.ATTACHMENT))) {
							if (temp == null)
								temp = new messageunit(message);
							temp.add(part);
						}
					}
					if (temp != null)
						map.put(message.getSubject(), temp);
				}
			}
		}
	}

	public GmailReader(String account, String password) {
		this.account = account;
		this.password = password;
	}

	public List<memfile> getpart() throws IOException, MessagingException {
		List<memfile> l = new ArrayList<memfile>();
		for (messageunit m : map.values()) {
			for (Part p : m.part) {
				memfile mf=new memfile(p);
				l.add(mf);
				//if(mf.data==null) System.out.println("p.data is nulla");
			}
		}
		return l;
	}
}