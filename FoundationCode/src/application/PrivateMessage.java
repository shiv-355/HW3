package application;

/**
 * The PrivateMessage class represents a feedback message between users
 * related to a specific question. It stores details like sender, recipient,
 * message content, and whether the message has been read.
 */
public class PrivateMessage {

    // The ID of the question associated with this feedback message.
    private int questionId;
    // Username of the person sending the message.
    private String sender;
    // Username of the person receiving the message.
    private String recipient;
    // The actual message content.
    private String message;
    // Flag to indicate whether the message has been read.
    private boolean read;

    /**
     * Constructor for creating a new PrivateMessage.
     *
     * @param questionId the ID of the related question.
     * @param sender     the username of the sender.
     * @param recipient  the username of the recipient.
     * @param message    the feedback message text.
     * @throws IllegalArgumentException if the message is empty or only whitespace.
     */
    public PrivateMessage(int questionId, String sender, String recipient, String message) {
        // Check that the message is not null or empty.
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Feedback message cannot be empty.");
        }
        // Set the values for each field.
        this.questionId = questionId;
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        // New messages are initially marked as unread.
        this.read = false;
    }

    // ---------------- GETTERS ----------------

    /**
     * Gets the question ID this message is related to.
     *
     * @return the question ID.
     */
    public int getQuestionId() {
        return questionId;
    }

    /**
     * Gets the username of the sender.
     *
     * @return the sender's username.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Gets the username of the recipient.
     *
     * @return the recipient's username.
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Gets the message text.
     *
     * @return the message content.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Checks whether the message has been read.
     *
     * @return true if the message is read, false otherwise.
     */
    public boolean isRead() {
        return read;
    }

    // ---------------- SETTERS / UPDATES ----------------

    /**
     * Marks the message as read.
     */
    public void markAsRead() {
        this.read = true;
    }

    // ---------------- OVERRIDE ----------------

    /**
     * Returns a string representation of the private message.
     * This includes the sender and the message content.
     *
     * @return a formatted string with sender and feedback.
     */
    @Override
    public String toString() {
        return "From: " + sender + "\nFeedback: " + message;
    }
}
