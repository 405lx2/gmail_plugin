package gmail_plugin_v1;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class cookiemessage {
    String name;
    String mailid;
    String mail = null;

    public cookiemessage(String name, String mailid) {
	this.name = name;
	this.mailid = mailid;
    }

    public void fetch(Statement stmt) throws SQLException {
	if (!mailid.equals("-1")) {
	    ResultSet havemail = stmt
		    .executeQuery("select * from mail where id = '" + mailid
			    + "' ;");
	    if (havemail.next())
		mail = havemail.getString("subject");
	    // System.out.println(name+mail);
	} else
	    mail = "not from receive mail";

    }
}
