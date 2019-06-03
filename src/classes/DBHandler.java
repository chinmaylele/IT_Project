package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.byteme.gdgapp.Cleaning;
import com.byteme.gdgapp.Community;
import com.codename1.ui.Dialog;

/**
 * @author leish & kurtg & Chinmay created 27/12/2018
 *
 */
public class DBHandler {

	private Connection conn = null;
	private String url;
	private User user = new User();
	private Deed deed = new Deed();
	private DeedCategory dcat = new DeedCategory();
	private Group group = new Group();
	private Event event = new Event();

	public Connection getConn() {
		return conn;
	}

	// Constructors
	// ------------------------------------------------------------------------
	public DBHandler() {
	}

	// CONNECT TO DB
	// ----------------------------------------------------------------------------
	/**
	 * Code found start author: SQLite Tutorial source:
	 * http://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/ date: 2018
	 */
	public void connect() {
		try {
			// moved conn and url to global declarations
			// modified to db's local location
			url = "jdbc:sqlite:C:\\Users\\Chinmay\\Desktop\\SQLiteStudio\\KudosDB.db";
//	        url = "jdbc:sqlite:C:\\Users\\Chinmay\\Desktop\\SQLiteStudio\\KudosDB.db";
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// moved the finally (don't want to close the connection before we use it)
		// to be in all below functions - will close the db connection.
	}
	/**
	 * Code found stop
	 */

	// SEARCH DB METHODS
	// -----------------------------------------------------------------------------
	/**
	 * Function to log a user into the application if their login details are on the
	 * database it then returns the user details in a populated object
	 * 
	 * @author leish & kurtg
	 * @param username
	 * @param pw
	 * @return
	 */
	public User login(String username, String pw) {

		try {
			connect();

			Statement st = conn.createStatement();
			String sql = "SELECT * FROM User WHERE username = '" + username + "'";
			ResultSet rs = st.executeQuery(sql);

			if (rs.next() == true) {
				if (pw.isEmpty()) { // no password entry
					this.user.setUserID(0);
				} else if (rs.getString("password").equals(pw)) { // password matches the user's
					user.setUserID(rs.getInt("userID"));
					user.setUsername(rs.getString("username"));
					user.setRealName(rs.getString("real_name"));
					user.setDOB(rs.getString("dob"));
					user.setEmail(rs.getString("email"));
					user.setPassword(rs.getString("password"));
					user.setAccType(rs.getBoolean("admin"));
					user.setSecurityQuestion(rs.getString("security_question"));
					user.setSecurityAnswer(rs.getString("security_answer"));
					user.setPrivacy(rs.getBoolean("privacy"));
					user.setKudosTotal(rs.getInt("kudos_total"));
					user.setProfilePicture(rs.getString("profile_pic"));
					user.setOccupation(rs.getString("occupation"));
					// populate groupArray[] with group ID's from Groups
					user = getGroups(user);
				} else {
					this.user.setUserID(0);
				}
			} else {
				this.user.setUserID(0);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return user;
	}

	/**
	 * Function takes passed string a determines if it is a username or password. It
	 * then returns the user details of the found user.
	 * 
	 * @author leish & kurtg
	 * @param identity
	 * @return
	 */
	public User findUsernameForPWReset(String identity) {

		try {
			connect();

			Statement st = conn.createStatement();
			String sql;

			if (identity.contains("@")) {
				sql = "SELECT userID, username,security_question,security_answer FROM User WHERE email = '" + identity
						+ "'";
			} else {
				sql = "SELECT userID, username,security_question,security_answer FROM User WHERE username = '"
						+ identity + "'";
			}
			ResultSet rs = st.executeQuery(sql);

			if (rs.next() == true) {
				user.setSecurityQuestion(rs.getString("security_question"));
				user.setSecurityAnswer(rs.getString("security_answer"));
				user.setUsername(rs.getString("username"));
				user.setUserID(rs.getInt("userID"));
			} else {
				user.setUserID(0);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return user;
	}

	/**
	 * @author kurtg
	 * @param searchedUserID
	 * @param searchedUsername
	 * @return
	 */
	public User searchForUserDB(int searchedUserID, String searchedUsername) {

		User owner = new User();

		try {
			connect();

			Statement st = conn.createStatement();
			String sql = "";
			if (searchedUserID == 0) {
				sql = "SELECT * FROM User WHERE username = '" + searchedUsername + "'";
			} else {
				sql = "SELECT * FROM User WHERE userID = '" + searchedUserID + "'";
			}
			ResultSet rs = st.executeQuery(sql);

			owner.setUserID(rs.getInt("userID"));
			owner.setUsername(rs.getString("username"));
			owner.setPrivacy(rs.getBoolean("privacy"));
			owner.setKudosTotal(rs.getInt("kudos_total"));
			owner.setProfilePicture(rs.getString("profile_pic"));

			if (owner.isPrivacy() == false) {
				owner.setRealName(rs.getString("real_name"));
				owner.setEmail(rs.getString("email"));
				owner.setOccupation(rs.getString("occupation"));
			} else {
				owner.setRealName("Private");
				owner.setEmail("Private");
				owner.setDOB("Private");
				owner.setOccupation("Private");
			}

			owner = getGroups(owner);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return owner;
	}
	
	
	public User searchForUserById(int searchedUserID) {

		User owner = new User();

		try {
			connect();

			Statement st = conn.createStatement();
			String	sql = "SELECT * FROM User WHERE userID = '" + searchedUserID + "'";
			ResultSet rs = st.executeQuery(sql);

			owner.setUserID(rs.getInt("userID"));
			owner.setUsername(rs.getString("username"));
			owner.setPrivacy(rs.getBoolean("privacy"));
			owner.setKudosTotal(rs.getInt("kudos_total"));
			owner.setProfilePicture(rs.getString("profile_pic"));

			if (owner.isPrivacy() == false) {
				owner.setRealName(rs.getString("real_name"));
				owner.setEmail(rs.getString("email"));
				owner.setOccupation(rs.getString("occupation"));
			} else {
				owner.setRealName("Private");
				owner.setEmail("Private");
				owner.setDOB("Private");
				owner.setOccupation("Private");
			}

			owner = getGroups(owner);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return owner;
	}

	// UNIQUE ENTRY METHODS
	// -------------------------------------------------------------------------
	/**
	 * Function takes the username string and compares it the the usernames on the
	 * user table of the database, returning true or false depending on if it
	 * already exists or not
	 * 
	 * @author leish
	 * @param username
	 * @return
	 */
	public boolean uniqueUsername(String username) {

		boolean unique = false;

		try {
			connect();

			String sql = "SELECT username FROM User WHERE username = '" + username + "'";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			if (rs.next() == true) { // if username exists
				unique = false;
			} else {
				unique = true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return unique;
	}

	/**
	 * Function takes the email string and compares it the the email on the user
	 * table of the database, returning true or false depending on if it already
	 * exists or not
	 * 
	 * @author leish
	 * @param email
	 * @return
	 */
	public boolean uniqueEmail(String email) {

		boolean unique = false;

		try {
			connect();

			String sql = "SELECT email FROM User WHERE email = '" + email + "'";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			if (rs.next() == true) { // if email exists
				unique = false;
			} else {
				unique = true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return unique;
	}

	// UPDATE DB METHODS
	// ----------------------------------------------------------------------------
	/**
	 * Function to create a user in the database using the user object
	 * 
	 * @author leish & kurtg
	 * @param user
	 */
	public void createUser(User user) {

		try {
			connect();

			String sql = "INSERT INTO User(username, real_name, email, dob, password, admin, security_question, security_answer, "
					+ "privacy, occupation) Values(?,?,?,?,?,0,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, user.getUsername());
			ps.setString(2, user.getRealName());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getDOB());
			ps.setString(5, user.getPassword());
			ps.setString(6, user.getSecurityQuestion());
			ps.setString(7, user.getSecurityAnswer());
			if (user.isPrivacy() == true) { // is private
				ps.setInt(8, 1);
			} else { // is public
				ps.setInt(8, 0);
			}
			ps.setString(9, user.getOccupation());

			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	
	/**
	 * Methods for uploading deed to database.
	 * When user completes 5 deeds, gets an achievement.
	 * @author Chinmay
	 *
	 */
	
	public void uploadDeedwcat(DeedCategory dcat, User user, Deed deed, Achievement ach, AchievementCategory achcat) {

		try {
			connect();

			String deedCatSql = "INSERT INTO Deed_Category (dcatid,userID,categories,achievement_catID,achievementID,k_points,deed_cat_name) values(?,?,?,?,?,?,?)";
			PreparedStatement deedCatPs = conn.prepareStatement(deedCatSql, Statement.RETURN_GENERATED_KEYS);
			deedCatPs.setInt(1, dcat.getdcatid());
			deedCatPs.setInt(2, user.getUserID());
			deedCatPs.setInt(3, dcat.getcategories());
			deedCatPs.setInt(4, achcat.getAchieveCatID());
			deedCatPs.setInt(5, ach.getAchievementID());
			deedCatPs.setInt(6, deed.getAssignedWeighting());
			deedCatPs.setString(7, dcat.getDeedCatName());

			deedCatPs.execute();

			ResultSet rs = deedCatPs.getGeneratedKeys();
			int generatedId = 0;
			if (rs.next()) {
				generatedId = rs.getInt(1);
			}

			String deednamesql = "INSERT INTO Deeds(userID,deed_catID,deed_name,assigned_weighting,voted_points) values(?,?,?,?,?)";
			PreparedStatement deednameps = conn.prepareStatement(deednamesql);
			deednameps.setInt(1, user.getUserID());
			deednameps.setInt(2, generatedId);
			deednameps.setString(3, dcat.getDeedCatName());
			deednameps.setInt(4, deed.getAssignedWeighting());
			deednameps.setInt(5, deed.getPoints());
			deednameps.execute();

			String kudosSql = "UPDATE User set kudos_total = kudos_total + ? where userID = ?";
			PreparedStatement kudosPs = conn.prepareStatement(kudosSql);
			kudosPs.setInt(1, deed.getAssignedWeighting());
			kudosPs.setInt(2, user.getUserID());
			kudosPs.execute();

			// update achievements

			int[][] acheveMap = { {}, { 1, 2, 3, 4, 5 }, { 6, 7, 8, 9, 10 }, { 11, 12, 13, 14, 15 },
					{ 16, 17, 18, 19, 20 }, { 21, 22, 23, 24, 25 }, { 26, 27, 28, 29, 30 }, { 31, 32, 33, 34, 35 },
					{ 36, 37, 38, 39, 40 }, { 41, 42, 43, 44, 45 }, { 46, 47, 48, 49, 50 },{51,52,53,54,55},{56,57,58,59,60},
					{61,62,63,64,65},{66,67,68,69,70},{71,72,73,74,75},{76,77,78,79,80}};
			
			int[] kudosMap = {0,70,490,90,170,60,210,90,130,90,290,170,90,410,300,200,150};

			String sql = "SELECT achievementID FROM Awarded_Achievement WHERE userID=? AND achievementID= ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, user.getUserID());
			ps.setInt(2, ach.getAchievementID());
			ResultSet achRs = ps.executeQuery();

			if (achRs.next() == false) { // if acheivement not exists
				String deedAchSql = "SELECT categories FROM Deed_Category WHERE userID=? AND dcatid= ?";
				PreparedStatement deedAchSqlps = conn.prepareStatement(deedAchSql);
				deedAchSqlps.setInt(1, user.getUserID());
				deedAchSqlps.setInt(2, dcat.getdcatid());
				ResultSet deedAchSqlpsRs = deedAchSqlps.executeQuery();

				Set<Integer> finishedAchs = new HashSet<Integer>();
				while (deedAchSqlpsRs.next() == true) {
					finishedAchs.add(deedAchSqlpsRs.getInt("categories"));
				}
				Integer[] finishedArr = finishedAchs.toArray(new Integer[0]);
				int[] primArry = new int[finishedArr.length];
				for (int i = 0; i < finishedArr.length; i++) {
					primArry[i] = finishedArr[i];
				}
				Arrays.sort(primArry); 
				if (Arrays.equals(acheveMap[dcat.getdcatid()], primArry)) {
					String accAchvSql = "INSERT into Awarded_Achievement (achievementID,userID) values (?, ?)";
					PreparedStatement accAchvSqlPs = conn.prepareStatement(accAchvSql);
					accAchvSqlPs.setInt(1, ach.getAchievementID());
					accAchvSqlPs.setInt(2, user.getUserID());
					accAchvSqlPs.execute();

					String achkudosSql = "UPDATE User set kudos_total = kudos_total + ? where userID = ?";
					PreparedStatement achkudosPs = conn.prepareStatement(achkudosSql);
					achkudosPs.setInt(1,kudosMap[dcat.getdcatid()]);
					achkudosPs.setInt(2, user.getUserID());
					achkudosPs.execute();
					
					Dialog.show(null,"You Have Unlocked an Achievement.","OK",null);
				}
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());

		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}

	}

	/*
	 * public void selectdeed() { try { connect(); Statement stmt =
	 * conn.createStatement(); String deedCatSql =
	 * "Select deed_name from Deeds where userID=?"; PreparedStatement deedCatPs =
	 * conn.prepareStatement(deedCatSql); ResultSet
	 * rs=stmt.executeQuery(deedCatSql); deedCatPs.setInt(1, user.getUserID());
	 * deedCatPs.execute();
	 * 
	 * while(rs.next()) { String cha=rs.getString("Str"); } }catch (SQLException e)
	 * { System.out.println(e.getMessage());
	 * 
	 * } finally { try { if (conn != null) { conn.close(); } } catch (SQLException
	 * ex) { System.out.println(ex.getMessage()); } } }
	 */

	
	/**
	 * Get Upvotes and downvotes by deed id.
	 * @author Chinmay
	 *
	 */
	public int getVotedPointsByDeedId(int deedId) {

		String sql = "SELECT voted_points FROM Deeds WHERE deedID=?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, deedId);
			ResultSet rs = ps.executeQuery();

			if (rs.next() == true) { // if user vote exists

				return rs.getInt("voted_points");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return 0;
	}

	/**
	 * DB Method for UPvoting and Downvoting.
	 * @author Chinmay
	 *
	 */
	
	public int upvoteDeed(User user, Deed deed) {
		String deedvSql = "UPDATE Deeds set voted_points = voted_points + 1 where deedID = ?";
		try {
			connect();

			String sql = "SELECT type FROM UserVotes WHERE userID=? AND deedID=?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, user.getUserID());
			ps.setInt(2, deed.getDeedID());
			ResultSet rs = ps.executeQuery();

			if (rs.next() == true) { // if user vote exists
				if (rs.getInt("type") == 1) {
					return getVotedPointsByDeedId(deed.getDeedID());
				} else {
					deedvSql = "UPDATE Deeds set voted_points = voted_points + 1 where deedID = ?";
					String updatevotesql = "Update UserVotes set type = 1 where userID = ? AND deedID = ? ";
					PreparedStatement updatevotesqlps = conn.prepareStatement(updatevotesql);
					updatevotesqlps.setInt(1, user.getUserID());
					updatevotesqlps.setInt(2, deed.getDeedID());
					updatevotesqlps.execute();

				}
			} else {

				String updatevotesql = "INSERT into UserVotes (userID,deedID,type) values (?, ?, ?)";
				PreparedStatement updatevotesqlps = conn.prepareStatement(updatevotesql);
				updatevotesqlps.setInt(1, user.getUserID());
				updatevotesqlps.setInt(2, deed.getDeedID());
				updatevotesqlps.setInt(3, 1);
				updatevotesqlps.execute();
			}

			PreparedStatement deedvPs = conn.prepareStatement(deedvSql);
			deedvPs.setInt(1, deed.getDeedID());
			deedvPs.execute();
			return getVotedPointsByDeedId(deed.getDeedID());

			// ResultSet rs = deedvPs.getGeneratedKeys();
			// int generatedId = 0;
			// if (rs.next()) {
			// generatedId = rs.getInt(1);
			// }
			// update votes table

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return getVotedPointsByDeedId(deed.getDeedID());
	}

	public int downvoteDeed(User user, Deed deed) {

		String deedvSql = "UPDATE Deeds set voted_points = voted_points - 1 where deedID = ?";
		try {
			connect();

			String sql = "SELECT type FROM UserVotes WHERE userID=? AND deedID=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, user.getUserID());
			ps.setInt(2, deed.getDeedID());
			ResultSet rs = ps.executeQuery();

			if (rs.next() == true) { // if user vote exists
				if (rs.getInt("type") == 0) {
					return getVotedPointsByDeedId(deed.getDeedID());
				} else {
					deedvSql = "UPDATE Deeds set voted_points = voted_points - 1 where deedID = ?";
					String updatevotesql = "Update UserVotes set type = 0 where userID = ? AND deedID = ? ";
					PreparedStatement updatevotesqlps = conn.prepareStatement(updatevotesql);
					updatevotesqlps.setInt(1, user.getUserID());
					updatevotesqlps.setInt(2, deed.getDeedID());
					updatevotesqlps.execute();
				}
			} else {
				String updatevotesql = "INSERT into UserVotes (userID,deedID,type) values (?, ?, ?)";
				PreparedStatement updatevotesqlps = conn.prepareStatement(updatevotesql);
				updatevotesqlps.setInt(1, user.getUserID());
				updatevotesqlps.setInt(2, deed.getDeedID());
				updatevotesqlps.setInt(3, 0);
				updatevotesqlps.execute();
			}

			PreparedStatement deedvPs = conn.prepareStatement(deedvSql);
			deedvPs.setInt(1, deed.getDeedID());
			deedvPs.execute();

			// ResultSet rs = deedvPs.getGeneratedKeys();
			// int generatedId = 0;
			// if (rs.next()) {
			// generatedId = rs.getInt(1);
			// }
			// update votes table

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return getVotedPointsByDeedId(deed.getDeedID());
	}

	/**
	 * Function finds the passed user and changes the password column on the user
	 * table and the database with the passed password string
	 * 
	 * @author leish
	 * @param user
	 * @param pw
	 */
	public void resetPassword(User u, String pw) {

		try {
			connect();

			String sql = "UPDATE User SET password = ? WHERE username = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, pw);
			ps.setString(2, u.getUsername());

			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	/**
	 * Function takes the user object and updates the users details on the user
	 * table on the database with the information
	 * 
	 * @author Kurt
	 * @param u
	 */
	public void updateUserSettingsDB(User u) {

		try {
			connect();

			String sql = "UPDATE User SET real_name = ?, email = ?, security_question = ?, security_answer = ?, privacy = ? WHERE userID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, u.getRealName());
			ps.setString(2, u.getEmail());
			ps.setString(3, u.getSecurityQuestion());
			ps.setString(4, u.getSecurityAnswer());
			if (u.isPrivacy() == true) {
				ps.setInt(5, 1);
			} else {
				ps.setInt(5, 0);
			}
			ps.setInt(6, u.getUserID());

			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	/**
	 * Function will take the passed user object and use the userID to find the row
	 * in the user table and remove it.
	 * 
	 * @author kurtg
	 * @param u
	 * 
	 * @Note Future Dev: Add more remove details for each new table added to the
	 *       database to ensure no unused data is left behind. Ensure no needed data
	 *       is removed
	 */
	public void deleteAccountDB(User u) {

		try {
			connect();

			String sql = "DELETE FROM User WHERE userID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, u.getUserID());

			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	// REPORTING METHODS
	// -------------------------------------------------------------------------------
	/**
	 * Retrieves the reported count for a deed and determines if the deed should be
	 * deleted or the count added to.
	 * 
	 * @author leish
	 * @param dID
	 */
	public void reportDeed(int dID) {

		try {
			connect();

			String sql = "SELECT * FROM Deeds WHERE deedID = '" + dID + "'";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			if (rs.getInt("reported_count") + 1 >= 3) { // 3 = amount of times deed is reported before it is removed
				deleteDeed(dID);
			} else {
				sql = "UPDATE Deeds SET reported_count = ? WHERE deedID = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, rs.getInt("reported_count") + 1);
				ps.setInt(2, dID);

				ps.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	/**
	 * Deletes a deed from the database
	 * 
	 * @author leish
	 * @param dID
	 */
	private void deleteDeed(int dID) {

		try {
			String sql = "DELETE FROM Deeds WHERE deedID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, dID);

			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Retrieves the reported count for a event and determines if the event should
	 * be deleted or the count added to.
	 * 
	 * @author leish
	 * @param dID
	 */
	public void reportEvent(int eID) {

		try {
			connect();

			String sql = "SELECT * FROM Group_Events WHERE eventID = '" + eID + "'";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			if (rs.getInt("reported_count") + 1 >= 3) { // 3 = amount of times event is reported before it is removed
				deleteEvent(eID);
			} else {
				sql = "UPDATE Group_Events SET reported_count = ? WHERE eventID = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, rs.getInt("reported_count") + 1);
				ps.setInt(2, eID);

				ps.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	/**
	 * Deletes an event from the database. This also cause the trigger in the DB to
	 * run, deleting from the Event_Competitors table any user's associated to the
	 * event being deleted.
	 * 
	 * @author leish
	 * @param dID
	 */
	private void deleteEvent(int eID) {

		try {
			// deletes user's associated to that event
			String sql = "DELETE FROM Group_Events WHERE eventID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, eID);
			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// GROUP METHODS
	// -------------------------------------------------------------------------------
	/**
	 * @author kurtg
	 * @param owner
	 * @return
	 */
	public User getGroups(User owner) {

		try {
			owner.getGroupArray().clear();

			Statement st = conn.createStatement();
			String sqlGroupO = "SELECT * FROM Groups WHERE group_owner = '" + owner.getUserID() + "'";
			ResultSet rsGroupO = st.executeQuery(sqlGroupO);

			// populate groupArray[] with group ID's from Group_Owner
			while (rsGroupO.next() == true) {
				if (rsGroupO.getInt("group_owner") == owner.getUserID()) {
					owner.addGroupArray(rsGroupO.getInt("groupID"));
				}
			}

			// populate groupArray[] with group ID's from Group_Members
			String sqlGroupM = "SELECT * FROM Group_Members WHERE userID = '" + owner.getUserID() + "'";
			ResultSet rsGroupM = st.executeQuery(sqlGroupM);

			while (rsGroupM.next() == true) {
				if (rsGroupM.getInt("userID") == owner.getUserID()) {
					owner.addGroupArray(rsGroupM.getInt("groupID"));
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return owner;
	}

	/**
	 * @author kurtg
	 * @param group
	 * @param eventName
	 * @param selDate
	 * @param curDate
	 * @return
	 */
	public Group createGroupEventDB(Group group, String eventName, String selDate, String curDate) {

		try {
			connect();

			String sql = "INSERT INTO Group_Events(event_name, event_start_date, event_end_date, groupID) Values(?,?,?,?)";

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, eventName);
			ps.setString(2, curDate);
			ps.setString(3, selDate);
			ps.setInt(4, group.getGroupID());
			ps.executeUpdate();

			group = getGroupEvents(group);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return group;
	}

	/**
	 * @author kurtg
	 * @param gname
	 * @param gtype
	 * @param user
	 * @return
	 */
	public User createGroupDB(String gname, String gtype, User user) {
		try {
			connect();

			String sql = "INSERT INTO Groups(group_name, group_type, group_owner) Values(?,?,?)";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, gname);
			ps.setString(2, gtype);
			ps.setInt(3, user.getUserID());
			ps.executeUpdate();

			user = getGroups(user);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return user;
	}

	/**
	 * @author kurtg
	 * @param groupID
	 * @return group
	 */
	public Group getGroupDB(int groupID) {

		try {
			connect();

			Statement st = conn.createStatement();
			String sql = "SELECT * FROM Groups WHERE groupID = '" + groupID + "'";

			ResultSet rs = st.executeQuery(sql);

			group.setGroupID(rs.getInt("groupID"));
			group.setGroupName(rs.getString("group_name"));
			group.setGroupType(rs.getString("group_type"));
			group.setGroupOwner(rs.getInt("group_owner"));

			group.clearMemberArray();
			// populate groupArray[] with group ID from Group owner
			group.addGroupMemberArray(group.getGroupOwner());

			// populate groupArray[] with group ID's from Group_Members
			String sqlGroupM = "SELECT * FROM Group_Members WHERE groupID = '" + group.getGroupID() + "'";
			ResultSet rsGroupM = st.executeQuery(sqlGroupM);

			while (rsGroupM.next() == true) {
				if (rsGroupM.getInt("groupID") == group.getGroupID()) {
					group.addGroupMemberArray(rsGroupM.getInt("userID"));
				}
			}

			group.clearEventArray();

			String sqlGroupE = "Select * FROM Group_Events WHERE groupID = '" + group.getGroupID() + "'";
			ResultSet rsGroupE = st.executeQuery(sqlGroupE);

			while (rsGroupE.next() == true) {
				if (rsGroupE.getInt("groupID") == group.getGroupID()) {
					group.addGroupEventArray(rsGroupE.getInt("eventID"));
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return group;
	}

	/**
	 * @author kurtg
	 * @param addedUser
	 * @param group
	 */
	public void addGroupMemberDB(User addedUser, Group group) {
		try {
			connect();

			String sql = "INSERT INTO Group_Members(userID, groupID) Values(?,?)";

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, addedUser.getUserID());
			ps.setInt(2, group.getGroupID());

			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	// EVENT METHODS
	// ------------------------------------------------------------------------------
	/**
	 * @author kurtg
	 * @param group
	 * @return
	 */
	public Group getGroupEvents(Group group) {

		try {

			group.clearEventArray();

			Statement st = conn.createStatement();
			String sql = "Select * FROM Group_Events WHERE groupID = '" + group.getGroupID() + "'";
			ResultSet rs = st.executeQuery(sql);

			// populate groupEventArray[] with event ID's from Group_Events
			while (rs.next() == true) {
				if (rs.getInt("groupID") == group.getGroupID()) {
					group.addGroupEventArray(rs.getInt("eventID"));
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return group;
	}

	/**
	 * @author kurtg
	 * @param eventID
	 * @return
	 */
	public Event searchEvent(int eventID) {
		try {
			connect();

			Statement st = conn.createStatement();
			String sql = "Select * FROM Group_Events WHERE eventID = '" + eventID + "'";

			ResultSet rs = st.executeQuery(sql);

			event.setEventID(rs.getInt("eventID"));
			event.setEventName(rs.getString("event_name"));
			event.setEventStart(rs.getString("event_start_date"));
			event.setEventEnd(rs.getString("event_end_date"));
			event.setGroupID(rs.getInt("groupID"));

			// populate EventCompetitorsArray[] with user ID's from Event_Competitors
			event.clearEventCompetitorsArray();

			String sqlE = "Select * FROM Event_Competitors WHERE eventID = '" + event.getEventID() + "'";
			ResultSet rsE = st.executeQuery(sqlE);

			while (rsE.next() == true) {
				if (rsE.getInt("eventID") == event.getEventID()) {
					event.addEventCompetitorsArray(rsE.getInt("userID"));
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return event;
	}

	/**
	 * @author kurtg
	 * @param userID
	 * @return
	 */
	public int getCompetitorsKudos(int userID, int eventID) {
		int competitorsKudos = 0;

		try {
			connect();

			Statement st = conn.createStatement();
			String sql = "Select competitor_kudos_total FROM Event_Competitors WHERE userID = '" + userID
					+ "' AND eventID = '" + eventID + "'";
			ResultSet rs = st.executeQuery(sql);

			competitorsKudos = rs.getInt("competitor_kudos_total");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}

		return competitorsKudos;
	}

	/**
	 * @author kurtg
	 * @param user2
	 * @param userID
	 * @param group
	 */
	public void deleteEventCompetitor(User user, int eventID) {
		try {
			connect();

			String sql = "DELETE FROM Event_Competitors WHERE userID = ? AND eventID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, user.getUserID());
			ps.setInt(2, eventID);

			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	/**
	 * @author kurtg
	 * @param user
	 * @param eventID
	 */
	public void addEventCompetitor(User user, int eventID) {
		try {
			connect();

			String sql = "INSERT INTO Event_Competitors(eventID, userID, competitor_kudos_total) " + "Values(?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, eventID);
			ps.setInt(2, user.getUserID());
			ps.setInt(3, user.getKudosTotal());

			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}

	}

	// USER EXTENSION METHODS
	// ------------------------------------------------------------------------------
	/**
	 * Retrieves all achievements awarded to a user.
	 * 
	 * @author leish
	 * @param u user object
	 * @return user object with achievements added
	 */
	public User getAchievements(User u) {

		try {
			connect();

			u.getAchievementArray().clear();

			Statement st = conn.createStatement();
			String sql = "SELECT achievementID FROM Awarded_Achievement WHERE userID = '" + u.getUserID() + "'";
			ResultSet rs = st.executeQuery(sql);

			ArrayList<Integer> idList = new ArrayList<>();
			while (rs.next()) {
				idList.add(rs.getInt("achievementID"));
			}

			for (int i = 0; i < idList.size(); i++) {
				String sqlAch = "SELECT * FROM Achievement WHERE achievementID = '" + idList.get(i) + "'";
				ResultSet rsAch = st.executeQuery(sqlAch);

				Achievement achieve = new Achievement(rsAch.getInt("achievementID"),
						rsAch.getString("achievement_name"));
				u.addAchievementArray(achieve);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return u;
	}

	/**
	 * Retrieves all Deeds from one specific user.
	 * 
	 * @author leish
	 * @param u user object
	 * @return user object
	 */
	public User getSingleUserDeeds(User u) {

		try {
			connect();

			u.getDeedArray().clear();

			Statement st = conn.createStatement();
			String sql = "SELECT * FROM Deeds WHERE userID = '" + u.getUserID() + "'";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				Deed deed = new Deed(rs.getInt("deedID"), rs.getInt("userID"), rs.getInt("deed_catID"),
						rs.getString("deed_name"), rs.getInt("assigned_weighting"), rs.getInt("voted_points"),
						rs.getString("proof_link"));
				u.addDeedArray(deed);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return u;
	}

	
	/**
	 * DB Method to get deeds from all users (to show in deed feed).
	 * @author Chinmay
	 *
	 */
	public User getMultipleUserDeeds(User u) {

		try {
			connect();

			u.getDeedArray().clear();

			Statement st = conn.createStatement();
			String sql = "SELECT * FROM Deeds";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				Deed deed = new Deed(rs.getInt("deedID"), rs.getInt("userID"), rs.getInt("deed_catID"),
						rs.getString("deed_name"), rs.getInt("assigned_weighting"), rs.getInt("voted_points"),
						rs.getString("proof_link"));
				u.addDeedArray(deed);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return u;
	}
	
	/**
	 * DB Methods to return deed by category.
	 * @author Chinmay
	 *
	 */

	public User getDeedsByCategoryAndUser(User u, int catId) {

		try {
			connect();

			u.getDeedArray().clear();

			Statement st = conn.createStatement();
			String sql = "SELECT * FROM Deed_Category WHERE userID = '" + u.getUserID() + "' AND dcatid = '" + catId
					+ "'";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				Deed deed = new Deed(0, 0, 0, rs.getString("deed_cat_name"));
				deed.setAssignedWeighting(rs.getInt("k_points"));
				u.addDeedArray(deed);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return u;
	}

	
	
	public DeedCategory getachieve(User u, DeedCate d) {
		try {
			connect();

			u.getDeedArray().clear();
			Statement st = conn.createStatement();
			String sql = "SELECT categories FROM Deed_Category WHERE userID = '" + u.getUserID();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				dcat.setcategories(rs.getInt("categories"));

			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}

		return dcat;
	}

	/**
	 * Searches the DB for the Achievement Category a Deed Category is related to.
	 * 
	 * @author leish
	 * @param dc object used to search the DB for the achievement cat it's stored in
	 * @return AchievementCategory object
	 */
	public AchievementCategory getAchievementCat(DeedCategory dc) {

		AchievementCategory achCat = new AchievementCategory();

		try {
			connect();

			Statement st = conn.createStatement();
			String sql = "SELECT * FROM Achievement_Category WHERE achievement_catID = '" + dc.getAchieveCatID() + "'";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				achCat.setAchieveCatID(rs.getInt("achievement_catID"));
				achCat.setAchieveCatName(rs.getString("achievement_cat_name"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return achCat;
	}

	/**
	 * Searches the DB for the Deed Category.
	 * 
	 * @author leish
	 * @param deedCatID used to search the DB for the Deed Category
	 * @return Deed Category object
	 */
	public DeedCategory getDeedCat(int deedCatID) {

		DeedCategory deedCat = new DeedCategory();

		try {
			connect();

			Statement st = conn.createStatement();
			String sql = "SELECT * FROM Deed_Category WHERE deed_catID = '" + deedCatID + "'";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				deedCat.setAchieveCatID(rs.getInt("achievement_catID"));
				deedCat.setAchievementID(rs.getInt("achievementID"));
				deedCat.setDeedCatID(rs.getInt("deed_catID"));
				deedCat.setDeedCatName(rs.getString("deed_cat_name"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return deedCat;
	}

	/**
	 * Retrieves all associate relationships related to a specific user.
	 * 
	 * @author leish
	 * @param u user object
	 * @return user object with achievements added
	 */
	public User getAssociates(User u) {

		try {
			connect();

			u.getAssociateArray().clear();

			Statement st = conn.createStatement();
			String sql = "SELECT * FROM Associate WHERE associate_owner = '" + u.getUserID() + "'";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				Associate recipient = new Associate();
				recipient.setUserID(rs.getInt("associate_recipient"));
				recipient.setRelationship(rs.getString("associate_relationship"));

				u.addAssociateArray(recipient);
			}

			for (int i = 0; i < u.getAssociateArray().size(); i++) {
				String sqlRecipient = "SELECT * FROM User WHERE userID = '" + u.getAssociateArray().get(i).getUserID()
						+ "'";
				ResultSet rsRecipient = st.executeQuery(sqlRecipient);

				u.getAssociateArray().get(i).setUsername(rsRecipient.getString("username"));
				u.getAssociateArray().get(i).setKudosTotal(rsRecipient.getInt("kudos_total"));
				u.getAssociateArray().get(i).setProfilePicture(rsRecipient.getString("profile_pic"));
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return u;
	}

	// ADMINISTRATOR CALLS
	// ----------------------------------------------------------------------------
	/**
	 * Retrieves all Achievement Categories stored in the DB
	 * 
	 * @author leish
	 * @return an ArrayList of AchievementCategory objects
	 */
	public ArrayList<AchievementCategory> getAllAchievementCats() {

		AchievementCategory achCat = new AchievementCategory();
		ArrayList<AchievementCategory> achCatArr = new ArrayList<>();

		try {
			connect();

			Statement st = conn.createStatement();
			String sql = "SELECT * FROM Achievement_Category";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				achCat.setAchieveCatID(rs.getInt("achievement_catID"));
				achCat.setAchieveCatName(rs.getString("achievement_cat_name"));

				achCatArr.add(achCat);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return achCatArr;
	}

	/**
	 * Retrieves all Deed Categories stored in the DB
	 * 
	 * @author leish
	 * @return an ArrayList of DeedCategory objects
	 */
	public ArrayList<DeedCategory> getAllDeedCats() {

		DeedCategory deedCat = new DeedCategory();
		ArrayList<DeedCategory> deedCatArr = new ArrayList<>();

		try {
			connect();

			Statement st = conn.createStatement();
			String sql = "SELECT * FROM Deed_Category";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				deedCat.setAchieveCatID(rs.getInt("achievement_catID"));
				deedCat.setAchievementID(rs.getInt("achievementID"));
				deedCat.setDeedCatID(rs.getInt("deed_catID"));
				deedCat.setDeedCatName(rs.getString("deed_cat_name"));

				deedCatArr.add(deedCat);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return deedCatArr;
	}
	
	/**
	 * DB method to return kudos points.
	 * @author Chinmay
	 *
	 */

	public int getKudosByUser(int userID) {
		try {
			connect();

			String sql = "SELECT kudos_total FROM User where userID = ?";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, userID);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				int kudos = rs.getInt("kudos_total");
				return kudos;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return 0;
	}
	
	
	/**
	 * DB method to return username from user id.
	 * @author Chinmay
	 *
	 */
	
	public String getUserNameByUser(int userID) {
		try {
			connect();

			String sql = "SELECT username FROM User where userID = ?";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, userID);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String name = rs.getString("username");
				return name;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return null;
	}
}
