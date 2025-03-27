package application;

import java.util.HashSet;
import java.util.Set;

/**
 * The User class represents a user in the system.
 * It stores user details like username, password, email, and role.
 * Each user also has their own list of trusted users.
 */
public class User {

    // User's basic information
    private String userName;  // Username of the user
    private String password;  // User's password
    private String email;     // User's email address
    private String role;      // Role of the user (e.g., Student, Teacher, Admin)

    // A set containing usernames that this user trusts.
    private Set<String> trustedUsers;

    /**
     * Constructs a new User with the given details.
     * Also initializes the trustedUsers set as empty.
     *
     * @param userName the username of the user.
     * @param password the user's password.
     * @param email    the user's email address.
     * @param role     the role of the user.
     * @throws IllegalArgumentException if userName or password is null or empty.
     */
    public User(String userName, String password, String email, String role) {
        // Validate that the username is provided
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        // Validate that the password is provided
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        // Set the provided values to the corresponding fields.
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.role = role;
        // Initialize trustedUsers as an empty set.
        this.trustedUsers = new HashSet<>();
    }

    // ----------------- GETTERS AND SETTERS -----------------

    /**
     * Sets the user's role.
     * @param role the new role to set.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns the user's username.
     * @return the username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Returns the user's password.
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the user's email address.
     * @return the email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the user's role.
     * @return the role.
     */
    public String getRole() {
        return role;
    }

    // ----------------- TRUSTED USERS METHODS -----------------

    /**
     * Returns a copy of the trusted users list.
     * This ensures the original set is not modified externally.
     *
     * @return a Set of trusted usernames.
     */
    public Set<String> getTrustedUsers() {
        return new HashSet<>(trustedUsers);
    }

    /**
     * Adds a username to the trusted users list.
     *
     * @param trustedUser the username to add.
     * @throws IllegalArgumentException if the username is null or empty.
     */
    public void addTrustedUser(String trustedUser) {
        // Check that the trusted user's name isn't null or empty.
        if (trustedUser == null || trustedUser.trim().isEmpty()) {
            throw new IllegalArgumentException("Trusted username cannot be empty.");
        }
        trustedUsers.add(trustedUser);
    }

    /**
     * Removes a username from the trusted users list.
     *
     * @param trustedUser the username to remove.
     */
    public void removeTrustedUser(String trustedUser) {
        trustedUsers.remove(trustedUser);
    }

    // ----------------- OVERRIDE METHODS -----------------

    /**
     * Returns a simple string representation of the user.
     *
     * @return a string with the username and role.
     */
    @Override
    public String toString() {
        return "User: " + userName + ", Role: " + role;
    }
}
