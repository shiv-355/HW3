package application;

/**
 * The Question class represents a question posted by a user.
 * It stores details such as a unique ID, title, description, and the owner (creator).
 */
public class Question {

    // A static counter used to generate unique IDs for each question.
    private static int nextId = 1;

    // ------------------ INSTANCE FIELDS ------------------
    // Unique identifier for this question.
    private int id;
    // The title or short summary of the question.
    private String title;
    // Detailed description of the question.
    private String description;
    // Username of the person who created the question.
    private String owner;

    // ------------------ CONSTRUCTOR ------------------
    /**
     * Constructs a new Question with the given title, description, and owner.
     *
     * @param title       the title of the question
     * @param description the detailed description of the question
     * @param owner       the username of the creator (current logged-in user)
     * @throws IllegalArgumentException if title, description, or owner is null or empty.
     */
    public Question(String title, String description, String owner) {
        // Validate that the title is not null or just empty spaces.
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Question title cannot be empty.");
        }
        // Validate that the description is provided.
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Question description cannot be empty.");
        }
        // Validate that the owner (user posting the question) is provided.
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner must be provided.");
        }
        // Set the ID using the static counter and increment it for the next question.
        this.id = nextId++;
        // Save the title, description, and owner.
        this.title = title;
        this.description = description;
        this.owner = owner;
    }

    // ------------------ GETTERS ------------------

    /**
     * Returns the unique ID of this question.
     *
     * @return the question's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the title of this question.
     *
     * @return the question's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the description of this question.
     *
     * @return the question's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the owner (creator) of this question.
     *
     * @return the owner's username.
     */
    public String getOwner() {
        return owner;
    }

    // ------------------ SETTERS ------------------

    /**
     * Updates the title of the question.
     *
     * @param title the new title.
     * @throws IllegalArgumentException if the title is null or empty.
     */
    public void setTitle(String title) {
        // Validate that the new title is not empty.
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Question title cannot be empty.");
        }
        this.title = title;
    }

    /**
     * Updates the description of the question.
     *
     * @param description the new description.
     * @throws IllegalArgumentException if the description is null or empty.
     */
    public void setDescription(String description) {
        // Validate that the new description is not empty.
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Question description cannot be empty.");
        }
        this.description = description;
    }

    // ------------------ OVERRIDE ------------------

    /**
     * Returns a string representation of the question.
     * Useful for debugging or displaying the question details.
     *
     * @return a formatted string with the question details.
     */
    @Override
    public String toString() {
        return "Question ID: " + id + "\n" +
                "Title: " + title + "\n" +
                "Description: " + description + "\n" +
                "Owner: " + owner;
    }
}
