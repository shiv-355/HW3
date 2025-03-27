package application;

// Import Java's HashSet and Set for storing likes.
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an Answer in our Q&A system.
 * Each answer is tied to a question, has text, an owner,
 * and keeps track of whether it’s resolved, liked, or read.
 */
public class Answer {

    // Static counter to generate unique IDs for answers.
    private static int nextId = 1;

    // Unique identifier for this answer.
    private int id;
    // The ID of the question to which this answer belongs.
    private int questionId;
    // The text content of the answer.
    private String text;
    // Whether this answer is marked as resolving the question.
    private boolean resolved;
    // The username of the user who created this answer.
    private String owner;
    // A set of usernames that have liked this answer.
    private Set<String> likedBy;
    // Indicates whether the answer has been read by the question's owner.
    private boolean read;

    /**
     * Constructs a new Answer for a given question.
     *
     * @param questionId the ID of the question this answer relates to.
     * @param text       the answer text.
     * @param owner      the username of the user posting this answer.
     * @throws IllegalArgumentException if the answer text is empty, too long, or if the owner is missing.
     */
    public Answer(int questionId, String text, String owner) {
        // Validate the answer text is not empty or just whitespace.
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Answer cannot be empty.");
        }
        // Limit answer length to 500 characters.
        if (text.length() > 500) {
            throw new IllegalArgumentException("Answer is too long.");
        }
        // Validate the owner is provided.
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner must be provided.");
        }
        // Set a unique id and initialize fields.
        this.id = nextId++;
        this.questionId = questionId;
        this.text = text;
        this.resolved = false;  // By default, the answer is not resolved.
        this.owner = owner;
        // Use a HashSet to store unique usernames who like this answer.
        this.likedBy = new HashSet<>();
        this.read = false;      // New answers are marked as not read initially.
    }

    // ------------------- GETTERS -------------------

    // Returns the unique answer ID.
    public int getId() {
        return id;
    }

    // Returns the ID of the question associated with this answer.
    public int getQuestionId() {
        return questionId;
    }

    // Returns the answer text.
    public String getText() {
        return text;
    }

    // Checks whether the answer is marked as resolved.
    public boolean isResolved() {
        return resolved;
    }

    // Returns the username of the answer's creator.
    public String getOwner() {
        return owner;
    }

    // Returns the current number of likes.
    public int getLikeCount() {
        return likedBy.size();
    }

    // Checks if the answer has been read by the question's owner.
    public boolean isRead() {
        return read;
    }

    // ------------------- SETTERS AND UPDATERS -------------------

    /**
     * Updates the answer text.
     *
     * @param text new answer text.
     * @throws IllegalArgumentException if the text is empty or too long.
     */
    public void setText(String text) {
        // Validate text before updating.
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Answer cannot be empty.");
        }
        if (text.length() > 500) {
            throw new IllegalArgumentException("Answer is too long.");
        }
        this.text = text;
    }

    /**
     * Sets the resolved status of the answer.
     *
     * @param resolved true if the answer resolves the question; false otherwise.
     */
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    /**
     * Sets the read status of the answer.
     *
     * @param read true if the answer has been read; false otherwise.
     */
    public void setRead(boolean read) {
        this.read = read;
    }

    // ------------------- LIKE FEATURE -------------------

    /**
     * Marks this answer as liked by the specified user.
     *
     * @param username the username of the user liking the answer.
     * @throws IllegalArgumentException if username is null or empty.
     */
    public void like(String username) {
        // Validate that the username is valid.
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username must be provided to like an answer.");
        }
        // Add the username to the set of likes.
        likedBy.add(username);
    }

    // ------------------- OVERRIDE TO STRING -------------------

    /**
     * Returns a string representation of the answer.
     * Useful for displaying answer details in the UI.
     */
    @Override
    public String toString() {
        return "Answer ID: " + id +
                "\nQuestion ID: " + questionId +
                "\nText: " + text +
                "\nResolved: " + resolved +
                "\nLikes: " + getLikeCount() +
                "\nRead: " + read;
    }
}
