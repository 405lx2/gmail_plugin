package gmail_plugin_v1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;

public class messageunit {
    public Message message;
    public List<memfile> part = new ArrayList<memfile>();

    public messageunit(Message message) {
	this.message = message;
    }

    public void add(Part part) throws MessagingException, IOException {
//	this.part.add(new memfile(part));
    }

}
