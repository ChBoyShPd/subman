
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import com.mysql.jdbc.MySQLConnection; //TODO which one to use?
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//import com.mysql.jdbc.PreparedStatement;

/**
 * 
 */

/**
 * @author xux8
 *
 */

public class DBA implements Closeable{
	static final String URL="jdbc:mysql://work.skylinecfg.org:3306/Subman";
	static final String USER = "ChBoyShPd"; //FIXME change test username to production 
	static final String DEFAULTCREDENTIALS = "C:\\\\Users\\\\xux8\\\\SubmanC.txt";
	private Connection conn;

	public DBA() throws SQLException {
		String password = "";
		//get password from file
		try {
			BufferedReader credFile = new BufferedReader(new FileReader(DEFAULTCREDENTIALS));
			password = credFile.readLine();
			credFile.close();
		} catch (IOException e) {
			// TODO log it
			System.err.println("Credentials read-from-file failed.");
			e.printStackTrace();
			System.exit(1);
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) { //should not be happening
			System.err.println("PLEASE SETUP THE MYSQL JDBC DRIVER NOW!!!");
			e.printStackTrace();
			System.exit(1);
		}
		conn=(Connection) DriverManager.getConnection(URL, USER, password);//would throw when connection fails
		//TODO log message of successful connection to database
	}

	/**
	 * @deprecated
	 * @param username
	 * @param password
	 */
	public DBA(String username, String password) {
		// TODO
	}

	@Override
	public void close() throws IOException {
		try {
			conn.close();
		} catch (SQLException e) {
			// why the fuck would a close() fail
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 * @param user The uid of the user, presumably from front end
	 * @param key HashedPassword, presumably from front end
	 * @return if authentication is successful
	 * @throws SQLException
	 */
	public boolean login(String user, String key) throws SQLException {
		PreparedStatement statement=conn.prepareStatement("SELECT passwordhash FROM Users WHERE uid = \"?\" ;");
		statement.setString(1, user);
		ResultSet resultSet=statement.executeQuery();
		while(resultSet.next()) {
			String passwordHash=resultSet.getString(1); //FIXME check if this is correct
			resultSet.close();
			statement.close();
			return passwordHash.equals(key); //The resultSet should have only one row despite it being in a loop
		}
		//log WARNING, empty ResultSet
		resultSet.close();
		statement.close();
		return false;
		
	}
}
