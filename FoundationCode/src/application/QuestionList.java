package application;

import java.util.*;

/**
 * The QuestionList class manages a collection of Question objects.
 * It uses a HashMap to store questions by their unique IDs, and provides
 * methods to add, retrieve, search, update, and delete questions.
 */
public class QuestionList {

    // A map to store questions, using the question ID as the key.
    private Map<Integer, Question> questionMap;

    /**
     * Constructor: Initializes the questionMap as an empty HashMap.
     */
    public QuestionList() {
        // Create a new HashMap to hold questions.
        questionMap = new HashMap<>();
    }

    /**
     * Adds a new question to the collection.
     *
     * @param question the Question object to add.
     * @throws IllegalArgumentException if the question is null.
     */
    public void addQuestion(Question question) {
        // Check if the provided question is null.
        if (question == null) {
            throw new IllegalArgumentException("Question cannot be null.");
        }
        // Add the question to the map using its unique ID.
        questionMap.put(question.getId(), question);
    }

    /**
     * Retrieves all questions in the collection.
     *
     * @return a Collection of all Question objects.
     */
    public Collection<Question> getAllQuestions() {
        // Return all the questions stored in the map.
        return questionMap.values();
    }

    /**
     * Retrieves a question by its unique ID.
     *
     * @param id the question ID.
     * @return the corresponding Question object, or null if not found.
     */
    public Question getQuestionById(int id) {
        // Look up the question in the map by its ID.
        return questionMap.get(id);
    }

    /**
     * Searches for questions that contain the keyword in the title or description.
     *
     * @param keyword the search keyword.
     * @return a List of matching Question objects.
     */
    public List<Question> searchQuestions(String keyword) {
        // Create a list to hold the search results.
        List<Question> results = new ArrayList<>();
        // Convert the keyword to lower case for case-insensitive matching.
        String lowerKeyword = keyword.toLowerCase();
        // Loop through each question in the map.
        for (Question q : questionMap.values()) {
            // If the title or description contains the keyword, add it to results.
            if (q.getTitle().toLowerCase().contains(lowerKeyword)
                    || q.getDescription().toLowerCase().contains(lowerKeyword)) {
                results.add(q);
            }
        }
        // Return the list of matching questions.
        return results;
    }

    /**
     * Updates a question's title and description.
     *
     * @param id             the question ID.
     * @param newTitle       the new title.
     * @param newDescription the new description.
     * @throws NoSuchElementException   if the question is not found.
     * @throws IllegalArgumentException if the new title or description is invalid.
     */
    public void updateQuestion(int id, String newTitle, String newDescription) {
        // Retrieve the question from the map using its ID.
        Question q = questionMap.get(id);
        if (q == null) {
            // If the question doesn't exist, throw an exception.
            throw new NoSuchElementException("Question not found.");
        }
        // Update the question's title and description.
        q.setTitle(newTitle);
        q.setDescription(newDescription);
    }

    /**
     * Deletes a question by its ID.
     *
     * @param id the question ID.
     * @throws NoSuchElementException if the question is not found.
     */
    public void deleteQuestion(int id) {
        // Check if the question exists in the map.
        if (!questionMap.containsKey(id)) {
            throw new NoSuchElementException("Question not found.");
        }
        // Remove the question from the map.
        questionMap.remove(id);
    }
}
