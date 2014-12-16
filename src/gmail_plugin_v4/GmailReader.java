package gmail_plugin_v1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.lang3.StringUtils;

public class GmailReader {

    public String account;
    public String password;
    //public List<messageunit> list = new ArrayList<messageunit>();

    public List<memfile> getmessages(mysql sql) throws Exception {
	Properties props = System.getProperties();
	props.setProperty("mail.store.protocol", "imaps");
	Session session = Session.getDefaultInstance(props, null);
	Store store = session.getStore("imaps");
	store.connect("imap.gmail.com", account, password);
	// System.out.println(store.toString());
	Folder inbox = store.getFolder("Inbox");
	inbox.open(Folder.READ_ONLY);
	FetchProfile fetchProfile = new FetchProfile();
	fetchProfile.add(FetchProfile.Item.CONTENT_INFO);
	fetchProfile.add(FetchProfile.Item.ENVELOPE);
	fetchProfile.add(FetchProfile.Item.FLAGS);
	Message[] tempmessages = inbox.getMessages();
	inbox.fetch(tempmessages, fetchProfile);
	List<memfile> list = new ArrayList<memfile>();
	for (Message message : tempmessages) {
	    // if (message.getContentType().contains("multipart")) {
	    // Multipart multiPart = (Multipart) message.getContent();
	    // for (int i = 0; i < multiPart.getCount(); i++) {
	    // MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
	    // messageunit temp = null;
	    // if (part.getFileName() != null && part.getFileName() != "") {
	    // if ((part.getDisposition() != null)
	    // && (part.getDisposition()
	    // .equalsIgnoreCase(Part.ATTACHMENT))) {
	    // if (temp == null)
	    // temp = new messageunit(message);
	    // temp.add(part);
	    // }
	    // }
	    // if (temp != null)
	    // list.add(temp);
	    //
	    // }
	    // }
	    list.addAll(getAttachments(message, sql));
	}
	return list;
    }

    public GmailReader(String account, String password) {
	this.account = account;
	this.password = password;
    }

    public List<memfile> getAttachments(Message message, mysql sql)
	    throws Exception {
	List<memfile> list = new ArrayList<memfile>();
	//System.out.println(message.getSubject());
	SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String date=dateformat.format(message.getSentDate());
	String subject=message.getSubject();
	String from=message.getFrom()[0].toString();
	from=from.substring(from.indexOf('<')+1, from.length()-1);
	if (!sql.check(message.getSubject())) {
	    // Object content = message.getContent();
	    //System.out.println(message.getSubject());
	    if (message.getContentType().contains("multipart")) {
		//System.out.println(message.getSubject());
		Multipart multipart = (Multipart) message.getContent();
		for (int i = 0; i < multipart.getCount(); i++) {
		    list.addAll(getAttachments(multipart.getBodyPart(i), sql,date,subject,from));
		}
	    }
	    /*if (list.size() != 0) {
		// System.out.println(list.size());
		int mailid = sql.addmail(message.getSubject());
		for (memfile m : list) {
		    m.mailid = mailid;
		    m.mailname = message.getSubject();
		}
	    }*/
	    
	}
	return list;
    }

    private List<memfile> getAttachments(BodyPart part, mysql sql,String date,String subject,String from)
	    throws Exception {
	List<memfile> list = new ArrayList<memfile>();
	//System.out.println(part.getFileName());
	if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())
		|| StringUtils.isNotBlank(part.getFileName())) {
	    System.out.println(part.getFileName());
	    if(!sql.check(subject)){
	    list.add(new memfile(part, date, subject, from));
	    }
	}

	if (part.getContentType().contains("multipart")) {
	    Multipart multipart = (Multipart) part.getContent();
	    for (int i = 0; i < multipart.getCount(); i++) {
		BodyPart bodyPart = multipart.getBodyPart(i);
		list.addAll(getAttachments(bodyPart, sql,date,subject,from));
	    }
	}
	return list;
    }
}