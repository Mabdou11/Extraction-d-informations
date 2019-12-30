package ext;

import java.sql.DriverManager;

public class MySQL {

	// BufferedReader lire = new BufferedReader(new InputStreamReader(new
	// FileInputStream("Subst.dic"), "UTF-16LE"));
	//
	//
	// String string = null; int nbrligne = 0;
	// while ((string = lire.readLine()) != null) {
	// String[] b = string.split(",");
	// String a = b[0];
	// string = a.replaceAll("'", " ");
	// string.replaceAll("'", " ");
	// }

	public static void BDD(String str, int nl) throws Exception {
		java.sql.Connection c = null;

			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/Extraction", "root", "");

		String req = "INSERT INTO Substances VALUES(NULL,'" + str + "'," + nl + ");";
		java.sql.Statement s = c.createStatement();
		
		s.executeUpdate(req);

	}

	public static void ViderAllBDD() throws Exception {

		java.sql.Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/Extraction", "root", "");
		String req = "TRUNCATE TABLE Substances ;";
		java.sql.Statement s = c.createStatement();
		s.executeUpdate(req);

	}
	

}
