package gmail_plugin_v1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class mysql {
    private static String dbDriver = "com.mysql.jdbc.Driver";
    private static Connection con = null;

    public Connection getConnection() {
	try {
	    // Class.forName("com.mysql.jdbc.Driver");
	    System.out.print("db get connection");
	    Class.forName(dbDriver);
	    con = DriverManager.getConnection(
		    "jdbc:mysql://localhost:3306/attchment", "root", "171730");
//	    con=DriverManager.getConnection("jdbc:mysql://sqld.duapp.com:4050/WcxQkZMBDRcMdtQKsfwG","SiYHO70TSG1UEV9pzMI29wnQ","55YVSdGNEBFzlc2T6kNifScVZKFyCFGf");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return con;
    }

    public void addatt(List<memfile> l) {
	try {
	    Statement stmt = con.createStatement();
	    for (memfile p : l) {

		// System.out.println("not have one"+rawname.substring(0,
		// nametype) + rawname.substring(nametype + 1));
		stmt.executeUpdate("INSERT INTO attch (name,size,type,mailid) VALUES ('"
			+ p.name
			+ "' , '"
			+ Integer.valueOf(p.length)
			+ "' , '" + p.type + "' , '" + p.mailid + "')");
	    }
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public void addatt(String name,int  length) {
	try {
	    Statement stmt = con.createStatement();
		// System.out.println("not have one"+rawname.substring(0,
		// nametype) + rawname.substring(nametype + 1));
		stmt.executeUpdate("INSERT INTO attch (name,size,type,mailid) VALUES ('"
			+ name
			+ "' , '"
			+ length
			+ "' , '" + name.substring(name.lastIndexOf('.')+1) + "' , '-1')");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public List<cookiemessage> refreshatt() {
	List<cookiemessage> list=new ArrayList<cookiemessage>();
	try {
	    Statement stmt = con.createStatement();
	    ResultSet haveatt = stmt.executeQuery("select * from attch;");
	    while(haveatt.next())
	    {
		list.add(new cookiemessage(haveatt.getString("name"),haveatt.getString("mailid")));
	    }
	    for(cookiemessage cm:list)
		cm.fetch(stmt);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	for(cookiemessage cm:list)
	    System.out.println(cm.name+cm.mail);
	return list;
    }

    public int addmail(String name) throws SQLException {
	Statement stmt = con.createStatement();
	// System.out.println(name);
	stmt.executeUpdate("INSERT INTO mail (subject) VALUES ('" + name + "')");
	ResultSet haveid = stmt
		.executeQuery("select id from mail where subject = '" + name
			+ "' ;");
	if (haveid.next())
	    return haveid.getInt("id");
	else
	    return -1;
    }

    public boolean checkmail(String name) throws SQLException {
	Statement stmt = con.createStatement();
	ResultSet havenum = stmt
		.executeQuery("select * from mail where subject = '" + name
			+ "' ;");
	return havenum.next();
    }

    public void searchat(String pattern, String lowsize, String upsize,
	    String type) {
	try {
	    Statement stmt = con.createStatement();
	    stmt.executeQuery("SELECT * FROM attch WHERE CONTAINS(name, '"
		    + pattern + "') AND size BETWEEN '" + lowsize + "' AND '"
		    + upsize + "' AND type = '" + type + "'");
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void closeConnection() {
	if (con != null)
	    try {
		con.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
    }
}
