package databasePart1;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;



import application.User;


/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation, and handling invitation codes.
 */
public class DatabaseHelper {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmt

	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 


			createTables(); 
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	private void createTables() throws SQLException {
	    String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
	            + "id INT AUTO_INCREMENT PRIMARY KEY, "
	            + "userName VARCHAR(255) UNIQUE, "
	            + "email VARCHAR(255) UNIQUE, " 
	            + "password VARCHAR(255), "
	            + "role VARCHAR(255), "
	            + "one_time_password VARCHAR(10) DEFAULT NULL)";
	    statement.execute(userTable);

	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	            + "code VARCHAR(10) PRIMARY KEY, "
	            + "role VARCHAR(20), "
	            + "isUsed BOOLEAN DEFAULT FALSE, "
	            + "expiration_date TIMESTAMP DEFAULT NULL)";
	    statement.execute(invitationCodesTable);
	}





	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}

	// Registers a new user in the database.
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO cse360users (userName, email, password, role) VALUES (?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getEmail()); 
			pstmt.setString(3, user.getPassword());
			pstmt.setString(4, user.getRole());
			pstmt.executeUpdate();
		}
	}

	// Overloaded login method that accepts username and password
	public boolean login(String userName, String password) throws SQLException {
	    String query = "SELECT role FROM cse360users WHERE userName = ? AND password = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        pstmt.setString(2, password);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            return rs.next(); // Returns true if a user with matching credentials exists
	        }
	    }
	}
	
	// Checks if a user already exists in the database based on their userName.
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}
	
	// Retrieves the role of a user from the database using their UserName.
	public String getUserRoles(String userName) {
	    String query = "SELECT role FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            String roleString = rs.getString("role");
	            if (roleString != null && !roleString.isEmpty()) {
	                return roleString; // Convert to list
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return ""; // Return an empty list if no roles found
	}
	
	
	
	// Generates a new invitation code and inserts it into the database.
	public String generateInvitationCode(String role) {
	    String code = UUID.randomUUID().toString().substring(0, 6); // Generate a random 6-character code
	    String query = "INSERT INTO InvitationCodes (code, role, expiration_date, isUsed) VALUES (?, ?, ?, ?)";

	    Timestamp expirationDate = new Timestamp(System.currentTimeMillis() + (5 * 60 * 1000)); // 5 min expiry

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.setString(2, role);
	        pstmt.setTimestamp(3, expirationDate);
	        pstmt.setBoolean(4, false);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return code;
	}


	// Validates an invitation code to check if it is unused.
	public String validateInvitationCode(String code) {
	    String query = "SELECT role FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            String role = rs.getString("role");
	            markInvitationCodeAsUsed(code);
	            return role;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return null;
	}



	// Marks the invitation code as used in the database.
	private void markInvitationCodeAsUsed(String code) {
	    String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	public void removeExpiredInvitationCodes() {
	    String query = "DELETE FROM InvitationCodes WHERE expiration_date < ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
	        int deletedRows = pstmt.executeUpdate();
	        System.out.println("Removed " + deletedRows + " expired invitation codes.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	// Closes the database connection and statement.
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}
	
	public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        String query = "SELECT userName, email, role FROM cse360users";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("userName");
                String email = rs.getString("email");
                String role = rs.getString("role");

                users.add(username + ", "+email +", "+ role);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
	
	public void updateUserRole(String userName, String newRole) {
	    String query = "UPDATE cse360users SET role = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, newRole);
	        pstmt.setString(2, userName);
	        int affectedRows = pstmt.executeUpdate();
	        
	        if (affectedRows == 0) {
	            System.out.println("No user found with the username: " + userName);
	        } else {
	            System.out.println("Role updated successfully for user: " + userName);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public void deleteUser(String userName) {
	    try {
	        // Check the role of the user being deleted
	    	String debugQuery = "SELECT userName FROM cse360users";
	    	try (Statement stmt = connection.createStatement();
	    	     ResultSet rs = stmt.executeQuery(debugQuery)) {
	    	    System.out.println("Existing Users in Database:");
	    	    while (rs.next()) {
	    	        System.out.println(rs.getString("userName"));
	    	    }
	    	}

	        String getRoleQuery = "SELECT role FROM cse360users WHERE userName = ?";
	        try (PreparedStatement roleStmt = connection.prepareStatement(getRoleQuery)) {
	            roleStmt.setString(1, userName);
	            ResultSet roleRs = roleStmt.executeQuery();

	            if (roleRs.next()) {
	                String userRole = roleRs.getString("role");
	                

	                // If the user is an admin, check if it's the last admin
	                if (userRole.equalsIgnoreCase("admin")) {
	                    String checkAdminQuery = "SELECT COUNT(*) FROM cse360users WHERE role = 'admin'";
	                    try (PreparedStatement checkStmt = connection.prepareStatement(checkAdminQuery);
	                         ResultSet rs = checkStmt.executeQuery()) {
	                        if (rs.next() && rs.getInt(1) == 1) {
	                            System.out.println("Cannot delete the last admin.");
	                            return;
	                        }
	                    }
	                }

	                // Proceed with deletion if not the last admin
	                String deleteQuery = "DELETE FROM cse360users WHERE userName = ?";
	                try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
	                    deleteStmt.setString(1, userName);
	                    int affectedRows = deleteStmt.executeUpdate();

	                    if (affectedRows > 0) {
	                        System.out.println("User deleted successfully: " + userName);
	                    } else {
	                        System.out.println("No user found with the username: " + userName);
	                    }
	                }
	            } else {
	                System.out.println("User not found.");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}



	// Retrieve all usernames for the dropdown
	public List<String> getAllUsernames() {
	    List<String> users = new ArrayList<>();
	    String query = "SELECT userName FROM cse360users WHERE role != 'admin'"; // Prevent deleting admins

	    try (PreparedStatement pstmt = connection.prepareStatement(query);
	         ResultSet rs = pstmt.executeQuery()) {

	        while (rs.next()) {
	            users.add(rs.getString("userName"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return users;
	}


	public void addUserRole(String userName, String newRole) {
	    String currentRoles = getUserRoles(userName);

	    if (!currentRoles.contains(newRole)) {
	        String updatedRoles = currentRoles.isEmpty() ? newRole : currentRoles + "_" + newRole;

	        String query = "UPDATE cse360users SET role = ? WHERE userName = ?";
	        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	            pstmt.setString(1, updatedRoles);
	            pstmt.setString(2, userName);
	            pstmt.executeUpdate();
	            System.out.println("Added role: " + newRole + " to user: " + userName);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	public void removeUserRole(String userName, String roleToRemove) {
	    String currentRoles = getUserRoles(userName);
	    String[] rolesArray = currentRoles.split("_");

	    if (rolesArray.length == 1) {
	        System.out.println("User only has one role and cannot be removed.");
	        return;
	    }

	    // Remove the selected role from the list
	    String updatedRoles = Arrays.stream(rolesArray)
	            .filter(role -> !role.equals(roleToRemove))
	            .reduce((a, b) -> a + "_" + b)
	            .orElse("");

	    String query = "UPDATE cse360users SET role = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, updatedRoles);
	        pstmt.setString(2, userName);
	        pstmt.executeUpdate();
	        System.out.println("Removed role: " + roleToRemove + " from user: " + userName);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// Set One-Time Password
	public void setOTP(String userName, String otp) {
	    String query = "UPDATE cse360users SET one_time_password = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, otp);
	        pstmt.setString(2, userName);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	// Verify One-Time Password
	public boolean verifyOTP(String userName, String otp) {
	    String query = "SELECT one_time_password FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return otp.equals(rs.getString("one_time_password")); // Match stored OTP
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	// Clear One-Time Password after reset
	public void clearOTP(String userName) {
	    String query = "UPDATE cse360users SET one_time_password = NULL WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	// Update user's password
	public void updatePassword(String userName, String newPassword) {
	    String query = "UPDATE cse360users SET password = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, newPassword);
	        pstmt.setString(2, userName);
	        int updatedRows = pstmt.executeUpdate();
	        if (updatedRows > 0) {
	            System.out.println("Password updated successfully for user: " + userName);
	        } else {
	            System.out.println("Failed to update password. User not found: " + userName);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}



}
