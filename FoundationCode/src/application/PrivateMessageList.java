package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PrivateMessageList manages a collection of PrivateMessage objects.
 * It provides methods to add messages and to retrieve conversations and unread messages
 * for a particular question and user.
 */
public class PrivateMessageList {

    // A list to store all private messages.
    private List<PrivateMessage> messages;

    /**
     * Constructor: Initializes the messages list.
     */
    public PrivateMessageList() {
        // Create an empty list to hold our messages.
        messages = new ArrayList<>();
    }

    /**
     * Adds a new private message to the list.
     *
     * @param pm the PrivateMessage object to add.
     * @throws IllegalArgumentException if the message is null.
     */
    public void addMessage(PrivateMessage pm) {
        // Check to make sure the message isn't null.
        if (pm == null) {
            throw new IllegalArgumentException("Feedback message cannot be null.");
        }
        // Add the message to our list.
        messages.add(pm);
    }

    /**
     * Retrieves all unread messages for a specific question and recipient.
     *
     * @param questionId the ID of the question.
     * @param recipient  the username of the recipient.
     * @return a list of unread PrivateMessage objects for the given question and recipient.
     */
    public List<PrivateMessage> getUnreadMessagesForQuestion(int questionId, String recipient) {
        // Create a list to store messages that meet the criteria.
        List<PrivateMessage> result = new ArrayList<>();
        // Loop through each message.
        for (PrivateMessage pm : messages) {
            // Check if the message is for the given question, the recipient matches (ignoring case),
            // and if the message hasn't been marked as read.
            if (pm.getQuestionId() == questionId
                    && pm.getRecipient().equalsIgnoreCase(recipient)
                    && !pm.isRead()) {
                result.add(pm);
            }
        }
        // Return all matching unread messages.
        return result;
    }

    /**
     * Returns a map of conversation threads for a specific question.
     * Each key in the map represents the "other party" in the conversation.
     * (If the current user is the question owner, the key is the sender; if the current user sent the message,
     * the key is the recipient.)
     *
     * @param questionId the ID of the question.
     * @param owner      the username of the question owner.
     * @return a map where each key is the other party's username and the value is a list of messages exchanged with them.
     */
    public Map<String, List<PrivateMessage>> getConversationsForQuestion(int questionId, String owner) {
        // Create a new map to store conversation threads.
        Map<String, List<PrivateMessage>> conversations = new HashMap<>();
        // Loop through all messages.
        for (PrivateMessage pm : messages) {
            // Only consider messages related to the given question.
            if (pm.getQuestionId() == questionId) {
                // Determine who the "other party" is:
                // If the sender is the owner, then the other party is the recipient.
                // Otherwise, the other party is the sender.
                String otherParty = pm.getSender().equalsIgnoreCase(owner) ? pm.getRecipient() : pm.getSender();
                // If there's not already a conversation for this other party, create a new list.
                conversations.computeIfAbsent(otherParty, k -> new ArrayList<>()).add(pm);
            }
        }
        // Return the map of conversation threads.
        return conversations;
    }
}
