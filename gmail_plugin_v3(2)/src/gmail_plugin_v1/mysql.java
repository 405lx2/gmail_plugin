package gmail_plugin_v1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class mysql {
	private static String dbDriver = "com.mysql.jdbc.Driver";
	private static String dbUrl = "jdbc:mysql://localhost:3306/attchment";
	private static String dbUser = "root";
	private static String dbPass = "171730";
	private static Connection con = null;

	public Connection getConnection() {
		try {
			// Class.forName("com.mysql.jdbc.Driver");
			Class.forName(dbDriver);
			// con=DriverManager.getConnection("jdbc:mysql://localhost:3306/hitnews","root","superbeans");
			con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public void addatt(List<memfile> l) {
		try {
			Statement stmt = con.createStatement();
			for (memfile p : l) {
				String rawname = p.name;
				System.out.println(rawname);
				int nametype = rawname.lastIndexOf('.');
				System.out.println(rawname.substring(nametype));
				ResultSet havenum = stmt
						.executeQuery("select * from attch where name = '"
								+ rawname.substring(0, nametype)
								+ "' and type = '"
								+ rawname.substring(nametype + 1) + "';");
				if (!havenum.next()) {
					System.out.println("not have one"+rawname.substring(0, nametype) + rawname.substring(nametype + 1));
					stmt.executeUpdate("INSERT INTO attch VALUES ('"
							+ rawname.substring(0, nametype) + "','"
							+ Integer.valueOf(p.length) + "','"
							+ rawname.substring(nametype + 1) + "')");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
