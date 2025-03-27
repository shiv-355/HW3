package application;

import java.util.*;

/**
 * AnswerList manages a collection of Answer objects.
 * It uses a HashMap to store answers by their unique IDs.
 */
public class AnswerList {

    // A map to store answers with the key as the answer ID.
    private Map<Integer, Answer> answerMap;

    /**
     * Constructor initializes the answer map.
     */
    public AnswerList() {
        // Create a new HashMap to hold answers.
        answerMap = new HashMap<>();
    }

    /**
     * Adds a new answer to the collection.
     *
     * @param answer the answer to add
     * @throws IllegalArgumentException if the answer is null
     */
    public void addAnswer(Answer answer) {
        // Check if the answer is null and throw an error if it is.
        if (answer == null) {
            throw new IllegalArgumentException("Answer cannot be null.");
        }
        // Put the answer into the map with its unique ID as the key.
        answerMap.put(answer.getId(), answer);
    }

    /**
     * Retrieves all answers in the collection.
     *
     * @return a collection of all answers
     */
    public Collection<Answer> getAllAnswers() {
        // Return all the answers stored in the map.
        return answerMap.values();
    }

    /**
     * Retrieves answers associated with a specific question.
     *
     * @param questionId the ID of the question
     * @return a list of answers related to that question
     */
    public List<Answer> getAnswersByQuestionId(int questionId) {
        // Create a list to store matching answers.
        List<Answer> results = new ArrayList<>();
        // Loop through all answers in the map.
        for (Answer a : answerMap.values()) {
            // If the answer's question ID matches the given ID, add it to the list.
            if (a.getQuestionId() == questionId) {
                results.add(a);
            }
        }
        // Return the list of matching answers.
        return results;
    }

    /**
     * Retrieves an answer by its unique ID.
     *
     * @param id the answer ID
     * @return the Answer object if found, or null if not found
     */
    public Answer getAnswerById(int id) {
        // Look up the answer in the map using its ID.
        return answerMap.get(id);
    }

    /**
     * Updates an answer's text and optionally its resolved status.
     *
     * @param id      the answer ID to update
     * @param newText the new text for the answer
     * @param resolved if not null, update the resolved status of the answer
     * @throws NoSuchElementException   if the answer is not found
     * @throws IllegalArgumentException if the new text is invalid
     */
    public void updateAnswer(int id, String newText, Boolean resolved) {
        // Retrieve the answer with the given ID.
        Answer a = answerMap.get(id);
        if (a == null) {
            // If the answer doesn't exist, throw an exception.
            throw new NoSuchElementException("Answer not found.");
        }
        // Update the text of the answer (this method checks for valid text).
        a.setText(newText);
        // If the resolved parameter is not null, update the answer's resolved status.
        if (resolved != null) {
            a.setResolved(resolved);
        }
    }

    /**
     * Deletes an answer from the collection by its ID.
     *
     * @param id the answer ID to delete
     * @throws NoSuchElementException if the answer is not found
     */
    public void deleteAnswer(int id) {
        // Check if the answer exists in the map.
        if (!answerMap.containsKey(id)) {
            // If not, throw an exception.
            throw new NoSuchElementException("Answer not found.");
        }
        // Remove the answer from the map.
        answerMap.remove(id);
    }
}
