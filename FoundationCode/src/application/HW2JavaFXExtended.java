// Define the package for this application
package application;

// Import JavaFX and utility classes
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos; // For aligning elements in layouts
import javafx.scene.Scene;
import javafx.scene.control.*; // Contains UI controls like Button, Label, TextField, etc.
import javafx.scene.layout.*; // Contains layout panes like HBox, VBox, etc.
import javafx.stage.Stage;
import java.util.*; // For using collections like List, Set, etc.
import javafx.scene.control.TextInputDialog; // For pop-up text input dialogs

/**
 * This is our main JavaFX application class that extends Application.
 * It handles the Q&A system functionality including questions, answers, search,
 * and managing trusted users.
 */
public class HW2JavaFXExtended extends Application {

    // ============================ DATA CONTAINERS ============================
    // These objects store our questions and answers
    private QuestionList questionsList = new QuestionList();
    private AnswerList answersList = new AnswerList();

    // The currently logged in user's username
    private String currentUser;

    // A User object to hold the current user's information (trusted users, etc.)
    private User user;

    // Setter method to set the current user object
    public void setUser(User currentUser) {
        this.user = currentUser;
    }

    // ============================ QUESTIONS TAB UI COMPONENTS ============================
    // List view to display questions
    private ListView<Question> questionsListView = new ListView<>();
    // Text field for the question title
    private TextField questionTitleField = new TextField();
    // Text area for the question description/details
    private TextArea questionDescriptionArea = new TextArea();
    // Label to show status messages (e.g., success, error messages) related to questions
    private Label questionStatusLabel = new Label();

    // Check boxes to filter questions: one for showing only the current user's questions
    private CheckBox showOnlyMyQuestionsCheckBox = new CheckBox("Show Only My Questions");
    // Another filter to show only unanswered questions
    private CheckBox showOnlyAllUnansweredQuestionsCheckBox = new CheckBox("Show Only Unanswered Questions");

    // ============================ ANSWERS TAB UI COMPONENTS ============================
    // ComboBox to choose a question for which answers are being managed
    private ComboBox<Question> answerQuestionCombo = new ComboBox<>();
    // ListView to display answers related to a question
    private ListView<Answer> answersListView = new ListView<>();
    // Text area for the answer text itself
    private TextArea answerTextArea = new TextArea();
    // CheckBox for marking if an answer is resolved (not really used in filtering later)
    private CheckBox resolvedCheckBox = new CheckBox("Resolved");
    // CheckBox filter to show only unresolved answers
    private CheckBox showOnlyUnresolvedCheckBox = new CheckBox("Show Only Unresolved Answers");
    // New filter to show only liked answers
    private CheckBox showOnlyLikedAnswersCheckBox = new CheckBox("Show Only Liked Answers");
    // New filter to show only unread answers
    private CheckBox showOnlyUnreadAnswersCheckBox = new CheckBox("Show Only Unread Answers");
    // CheckBox filter to show only answers from trusted users
    private CheckBox showOnlyTrustedAnswersCheckBox = new CheckBox("Show Only Trusted Answers");
    // Label for status messages related to answers
    private Label answerStatusLabel = new Label();

    // ============================ SEARCH TAB UI COMPONENTS ============================
    // Text field for entering search keywords
    private TextField searchField = new TextField();
    // Check boxes to filter search results by answered or unanswered questions
    private CheckBox filterAnswered = new CheckBox("Only Answered");
    private CheckBox filterUnanswered = new CheckBox("Only Unanswered");
    // List view to display search results (questions matching the search)
    private ListView<Question> searchResultsListView = new ListView<>();
    // Label for search-related status messages
    private Label searchStatusLabel = new Label();

    // ============================ PRIVATE MESSAGES (for feedback) ============================
    // Object to manage private messages between users
    private PrivateMessageList privateMessages = new PrivateMessageList();

    // ============================ MAIN METHOD ============================
    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }

    /**
     * Setter to update the currently logged in user's username.
     */
    public void setCurrentUser(String currentUser) {

        this.currentUser = currentUser;

    }

    /**
     * The start method sets up the primary stage and builds the UI.
     */
    @Override
    public void start(Stage primaryStage) {
        // Pre-populate some test questions with various owners
        questionsList.addQuestion(new Question("How do I make the perfect sandwich?", "I'm trying to perfect my recipe.", "Emma"));
        questionsList.addQuestion(new Question("What's your favorite video game?", "Looking for some new game recommendations.", "Emma"));
        questionsList.addQuestion(new Question("Why does my car make that weird noise?", "My car has been acting up lately.", "Alice"));
        questionsList.addQuestion(new Question("How do I make the perfect sandwich?", "I'm trying to perfect my recipe.", "Jack"));
        questionsList.addQuestion(new Question("What's your favorite video game?", "Looking for some new game recommendations.", "Emily"));
        questionsList.addQuestion(new Question("Why does my car make that weird noise?", "My car has been acting up lately.", "Alex"));

        // Pre-populate some test answers for specific questions
        Answer a1 = new Answer(questionsList.getQuestionById(1).getId(), "Try using fresh ingredients!", "john");
        Answer a2 = new Answer(questionsList.getQuestionById(1).getId(), "Maybe add a secret sauce.", "Emma");
        Answer a3 = new Answer(questionsList.getQuestionById(1).getId(), "I really enjoy Minecraft.", "Bob");
        Answer a4 = new Answer(questionsList.getQuestionById(3).getId(), "It could be a timing belt issue.", "Alice");
        answersList.addAnswer(a1);
        answersList.addAnswer(a2);
        answersList.addAnswer(a3);
        answersList.addAnswer(a4);

        // Add some trusted users for the current user (for filtering purposes)
        user.addTrustedUser("John");
        user.addTrustedUser("Emma");

        // Pre-populate private messages (simulating a conversation for feedback)
        PrivateMessage pm1 = new PrivateMessage(questionsList.getQuestionById(1).getId(),
                "john",
                "Emma",
                "Try using whole grain bread for a healthier option.");
        PrivateMessage pm2 = new PrivateMessage(questionsList.getQuestionById(1).getId(),
                "Emma",
                "john",
                "Thanks, I'll consider that.");
        PrivateMessage pm3 = new PrivateMessage(questionsList.getQuestionById(1).getId(),
                "Bob",
                "Emma",
                "Try using whole grain bread for a healthier option.");
        privateMessages.addMessage(pm1);
        privateMessages.addMessage(pm2);
        privateMessages.addMessage(pm3);

        // ----------------------- BUILDING THE TABS -----------------------
        // Create a TabPane to hold different sections of the app
        TabPane tabPane = new TabPane();
        // Add all tabs: Questions, Answers, Search, and Trusted Users
        tabPane.getTabs().addAll(
                createQuestionsTab(),
                createAnswersTab(),
                createSearchTab(),
                createTrustedUsersTab() // new tab for managing trusted users
        );

        // Set up the scene and stage
        Scene scene = new Scene(tabPane, 900, 600);
        primaryStage.setTitle("HW2 Q&A System Extended");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Refresh the question selection combo box in the answers tab
        refreshAnswerQuestionCombo();

        // When any answer filter checkbox is toggled, refresh the answer list view
        showOnlyUnresolvedCheckBox.setOnAction(e -> refreshAnswersListView());
        showOnlyLikedAnswersCheckBox.setOnAction(e -> refreshAnswersListView());
        showOnlyUnreadAnswersCheckBox.setOnAction(e -> refreshAnswersListView());
        showOnlyTrustedAnswersCheckBox.setOnAction(e -> refreshAnswersListView());
    }

    // ============================ QUESTIONS TAB METHODS ============================

    /**
     * Creates and returns the "Questions" tab.
     * This tab lets users add, update, delete, and re-ask questions.
     */
    private Tab createQuestionsTab() {
        Tab tab = new Tab("Questions");
        tab.setClosable(false);

        // Fill the ListView with all questions in our data container
        ObservableList<Question> questionsObservable = FXCollections.observableArrayList(questionsList.getAllQuestions());
        questionsListView.setItems(questionsObservable);
        questionsListView.setPrefWidth(300);

        // When a question is selected from the list, populate the title and description fields
        questionsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldQ, newQ) -> {
            if (newQ != null) {
                questionTitleField.setText(newQ.getTitle());
                questionDescriptionArea.setText(newQ.getDescription());
            }
        });

        // Set up filters to refresh the question list when toggled
        showOnlyMyQuestionsCheckBox.setOnAction(e -> refreshQuestionsListView());
        showOnlyAllUnansweredQuestionsCheckBox.setOnAction(e -> refreshQuestionsListView());

        // Build a vertical form for entering question details
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));
        form.getChildren().addAll(
                new Label("Title:"), questionTitleField,
                new Label("Description:"), questionDescriptionArea,
                showOnlyMyQuestionsCheckBox, showOnlyAllUnansweredQuestionsCheckBox
        );

        // Create secondary action buttons
        Button feedbackConversationBtn = new Button("Feedback Conversation");
        feedbackConversationBtn.setOnAction(e -> openFeedbackConversationDialog());

        Button reAskBtn = new Button("Re-Ask Question");
        reAskBtn.setOnAction(e -> reAskQuestion());

        // Create primary action buttons for question operations
        Button addBtn = new Button("Add Question");
        addBtn.setOnAction(e -> addQuestion());
        Button updateBtn = new Button("Update Question");
        updateBtn.setOnAction(e -> updateQuestion());
        Button deleteBtn = new Button("Delete Question");
        deleteBtn.setOnAction(e -> deleteQuestion());
        Button clearBtn = new Button("Clear");
        clearBtn.setOnAction(e -> clearQuestionForm());

        // Group primary buttons horizontally and center them
        HBox primaryButtons = new HBox(10, addBtn, updateBtn, deleteBtn, clearBtn);
        primaryButtons.setAlignment(Pos.CENTER);

        // Group secondary buttons horizontally and center them
        HBox secondaryButtons = new HBox(10, feedbackConversationBtn, reAskBtn);
        secondaryButtons.setAlignment(Pos.CENTER);

        // Combine both button groups vertically
        VBox btnBox = new VBox(10, primaryButtons, secondaryButtons);
        btnBox.setAlignment(Pos.CENTER);

        // Add buttons and the status label to the form
        form.getChildren().addAll(btnBox, questionStatusLabel);

        // Create the overall layout by placing the list view and form side-by-side
        HBox layout = new HBox(10, questionsListView, form);
        layout.setPadding(new Insets(10));
        tab.setContent(layout);
        return tab;
    }

    /**
     * Allows a user to re-ask (or update) a question based on feedback.
     * Only the original question owner can re-ask their question.
     */
    private void reAskQuestion() {
        // Get the selected question from the list view
        Question selected = questionsListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            questionStatusLabel.setText("No question selected to re-ask.");
            return;
        }
        // Check if the current user is the owner of the selected question
        if (!selected.getOwner().equalsIgnoreCase(currentUser)) {
            questionStatusLabel.setText("You can only re-ask your own questions.");
            return;
        }

        // Create a dialog where the user can edit the question details before re-posting it
        Dialog<Question> dialog = new Dialog<>();
        dialog.setTitle("Re-Ask Question");
        dialog.setHeaderText("Edit your question to improve it based on feedback:");

        // Define dialog buttons for re-asking and cancelling
        ButtonType askButtonType = new ButtonType("Re-Ask", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(askButtonType, ButtonType.CANCEL);

        // Pre-populate the dialog with the current question title and description
        TextField titleField = new TextField(selected.getTitle());
        TextArea descArea = new TextArea(selected.getDescription());
        descArea.setPrefRowCount(4);

        // Arrange the dialog content in a vertical box
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(new Label("Title:"), titleField,
                new Label("Description:"), descArea);
        dialog.getDialogPane().setContent(content);

        // Convert the dialog result into a new Question object when "Re-Ask" is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == askButtonType) {
                return new Question(titleField.getText(), descArea.getText(), currentUser);
            }
            return null;
        });

        // If the user confirms, add the new question and refresh the view
        dialog.showAndWait().ifPresent(newQuestion -> {
            questionsList.addQuestion(newQuestion);
            refreshQuestionsListView();
            questionStatusLabel.setText("New question posted based on your feedback.");
        });
    }

    /**
     * Opens a dialog showing a unified feedback conversation for a selected question.
     * Only the question owner is allowed to view the conversation.
     */
    private void openFeedbackConversationDialog() {
        // Get the selected question.
        Question selected = questionsListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            questionStatusLabel.setText("No question selected for conversation.");
            return;
        }

        // Retrieve conversation threads for this question using currentUser as context.
        Map<String, List<PrivateMessage>> conversations = privateMessages.getConversationsForQuestion(selected.getId(), currentUser);

        // If the current user is not the owner, ensure they are involved in at least one conversation.
        boolean isOwner = selected.getOwner().equalsIgnoreCase(currentUser);
        if (!isOwner) {
            boolean isParticipant = false;
            String autoSelectedKey = null;
            for (Map.Entry<String, List<PrivateMessage>> entry : conversations.entrySet()) {
                for (PrivateMessage pm : entry.getValue()) {
                    if (pm.getSender().equalsIgnoreCase(currentUser)) {
                        isParticipant = true;
                        autoSelectedKey = entry.getKey();
                        break;
                    }
                }
                if (isParticipant) break;
            }
            if (!isParticipant) {
                questionStatusLabel.setText("Only participants can view feedback conversations.");
                return;
            }
            // Auto-select the conversation thread for the non-owner.
            conversations = new HashMap<>();
            conversations.put(autoSelectedKey, privateMessages.getConversationsForQuestion(selected.getId(), currentUser).get(autoSelectedKey));
        }

        if (conversations.isEmpty()) {
            questionStatusLabel.setText("No conversation exists for you on this question.");
            return;
        }

        // Create a dialog to display the conversation.
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Feedback Conversation for: " + selected.getTitle());
        VBox dialogContent = new VBox(10);
        dialogContent.setPadding(new Insets(10));

        ComboBox<String> conversationCombo = new ComboBox<>();
        TextArea conversationArea = new TextArea();
        conversationArea.setEditable(true);
        conversationArea.setWrapText(true);
        conversationArea.setPrefHeight(300);

        // If the current user is the owner, let them choose a conversation thread.
        if (isOwner) {
            conversationCombo.setPromptText("Select a Conversation");
            conversationCombo.getItems().addAll(conversations.keySet());
            Map<String, List<PrivateMessage>> finalConversations = conversations;
            conversationCombo.setOnAction(e -> {
                String key = conversationCombo.getSelectionModel().getSelectedItem();
                if (key != null) {
                    List<PrivateMessage> conv = finalConversations.get(key);
                    StringBuilder sb = new StringBuilder();
                    for (PrivateMessage pm : conv) {
                        sb.append(pm.toString()).append("\n---------------------\n");
                        pm.markAsRead();
                    }
                    conversationArea.setText(sb.toString());
                }
            });
            dialogContent.getChildren().add(conversationCombo);
        } else {
            // Non-owners don't see the ComboBox; auto-select the only thread.
            String onlyKey = conversations.keySet().iterator().next();
            List<PrivateMessage> conv = conversations.get(onlyKey);
            StringBuilder sb = new StringBuilder();
            for (PrivateMessage pm : conv) {
                sb.append(pm.toString()).append("\n---------------------\n");
                pm.markAsRead();
            }
            conversationArea.setText(sb.toString());
        }

        // Input field for new message.
        TextField newMessageField = new TextField();
        newMessageField.setPromptText("Enter your message here...");
        Button sendButton = new Button("Send");
        Map<String, List<PrivateMessage>> finalConversations1 = conversations;
        sendButton.setOnAction(e -> {
            String messageText = newMessageField.getText();
            if (messageText == null || messageText.trim().isEmpty()) {
                return;
            }
            String conversationKey;
            if (isOwner) {
                conversationKey = conversationCombo.getSelectionModel().getSelectedItem();
                if (conversationKey == null) {
                    questionStatusLabel.setText("Please select a conversation thread.");
                    return;
                }
            } else {
                conversationKey = finalConversations1.keySet().iterator().next();
            }
            try {
                // Determine recipient: if current user is owner, reply goes to conversationKey;
                // otherwise, reply goes to the question owner.
                String recipient = isOwner ? conversationKey : selected.getOwner();
                PrivateMessage newPm = new PrivateMessage(selected.getId(), currentUser, recipient, messageText);
                privateMessages.addMessage(newPm);
                List<PrivateMessage> conv = finalConversations1.get(conversationKey);
                conv.add(newPm);
                newMessageField.clear();
                StringBuilder sb = new StringBuilder();
                for (PrivateMessage pm : conv) {
                    sb.append(pm.toString()).append("\n---------------------\n");
                    pm.markAsRead();
                }
                conversationArea.setText(sb.toString());
            } catch (Exception ex) {
                questionStatusLabel.setText("Error sending message: " + ex.getMessage());
            }
        });

        HBox inputBox = new HBox(10, newMessageField, sendButton);
        VBox conversationBox = new VBox(10, new Label("Conversation:"), conversationArea,
                new Label("Your Message:"), inputBox);
        dialogContent.getChildren().add(conversationBox);

        dialog.getDialogPane().setContent(dialogContent);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
        questionStatusLabel.setText("Conversation viewed.");
    }





    /**
     * Adds a new question using the text from the title and description fields.
     * The current user becomes the owner of the question.
     */
    private void addQuestion() {
        try {
            String title = questionTitleField.getText();
            String desc = questionDescriptionArea.getText();
            // Create a new question with the current user's username as owner
            Question q = new Question(title, desc, currentUser);
            questionsList.addQuestion(q);
            refreshQuestionsListView();
            questionStatusLabel.setText("Question added successfully.");
            refreshAnswerQuestionCombo();
            clearQuestionForm();
        } catch (Exception ex) {
            questionStatusLabel.setText("Error: " + ex.getMessage());
        }
    }

    /**
     * Updates the selected question with new details.
     * Only the owner of the question is allowed to update it.
     */
    private void updateQuestion() {
        try {
            Question q = questionsListView.getSelectionModel().getSelectedItem();
            if (q == null) {
                questionStatusLabel.setText("No question selected.");
                return;
            }
            // Check ownership: only allow update if the current user is the owner
            if (!q.getOwner().equals(currentUser)) {
                questionStatusLabel.setText("You are not authorized to update this question.");
                return;
            }
            String newTitle = questionTitleField.getText();
            String newDesc = questionDescriptionArea.getText();
            questionsList.updateQuestion(q.getId(), newTitle, newDesc);
            refreshQuestionsListView();
            questionStatusLabel.setText("Question updated.");
            refreshAnswerQuestionCombo();
            clearQuestionForm();
        } catch (Exception ex) {
            questionStatusLabel.setText("Error: " + ex.getMessage());
        }
    }

    /**
     * Deletes the selected question.
     * Only the owner of the question can delete it.
     */
    private void deleteQuestion() {
        try {
            Question q = questionsListView.getSelectionModel().getSelectedItem();
            if (q == null) {
                questionStatusLabel.setText("No question selected.");
                return;
            }
            // Check that the current user is the owner before deleting
            if (!q.getOwner().equals(currentUser)) {
                questionStatusLabel.setText("You are not authorized to delete this question.");
                return;
            }
            questionsList.deleteQuestion(q.getId());
            refreshQuestionsListView();
            questionStatusLabel.setText("Question deleted.");
            refreshAnswerQuestionCombo();
            clearQuestionForm();
        } catch (Exception ex) {
            questionStatusLabel.setText("Error: " + ex.getMessage());
        }
    }

    /**
     * Refreshes the questions list view.
     * Applies filters such as "Only My Questions" and "Only Unanswered Questions" if selected.
     */
    private void refreshQuestionsListView() {
        ObservableList<Question> obs = FXCollections.observableArrayList();
        for (Question q : questionsList.getAllQuestions()) {
            // If "Show Only My Questions" is checked, skip questions not owned by the current user
            if (showOnlyMyQuestionsCheckBox.isSelected() && !q.getOwner().equals(currentUser)) {
                continue;
            }
            // If "Show Only Unanswered Questions" is checked, only add questions that have no answers
            if (showOnlyAllUnansweredQuestionsCheckBox.isSelected()) {
                if (answersList.getAnswersByQuestionId(q.getId()).isEmpty()) {
                    obs.add(q);
                }
            } else {
                obs.add(q);
            }
        }
        questionsListView.setItems(obs);
    }

    /**
     * Clears the question input fields.
     */
    private void clearQuestionForm() {
        questionTitleField.clear();
        questionDescriptionArea.clear();
    }

    // ============================ ANSWERS TAB METHODS ============================

    /**
     * Creates and returns the "Answers" tab.
     * This tab lets users add, update, delete, like, and mark answers as read.
     */
    private Tab createAnswersTab() {
        Tab tab = new Tab("Answers");
        tab.setClosable(false);

        // Set prompt text and listener for when a question is selected from the combo box
        answerQuestionCombo.setPromptText("Select a Question");
        answerQuestionCombo.setOnAction(e -> refreshAnswersListView());

        // Set up the answers list view
        answersListView.setPrefWidth(300);
        answersListView.getSelectionModel().selectedItemProperty().addListener((obs, oldA, newA) -> {
            if (newA != null) {
                answerTextArea.setText(newA.getText());
            }
        });

        // Build the form for entering or editing an answer
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));
        form.getChildren().addAll(
                new Label("Answer Text:"),
                answerTextArea,
                // Add checkboxes for various answer filters
                showOnlyLikedAnswersCheckBox,
                showOnlyUnreadAnswersCheckBox,
                showOnlyTrustedAnswersCheckBox
        );

        // Primary action buttons for answer operations
        Button addBtn = new Button("Add Answer");
        addBtn.setOnAction(e -> addAnswer());
        Button updateBtn = new Button("Update Answer");
        updateBtn.setOnAction(e -> updateAnswer());
        Button deleteBtn = new Button("Delete Answer");
        deleteBtn.setOnAction(e -> deleteAnswer());
        Button clearBtn = new Button("Clear");
        clearBtn.setOnAction(e -> clearAnswerForm());
        HBox primaryButtons = new HBox(10, addBtn, updateBtn, deleteBtn, clearBtn);
        primaryButtons.setAlignment(Pos.CENTER);

        // Secondary action buttons for liking an answer or marking it as read
        Button likeBtn = new Button("Like Answer");
        likeBtn.setOnAction(e -> likeSelectedAnswer());
        Button markReadBtn = new Button("Mark as Read");
        markReadBtn.setOnAction(e -> markSelectedAnswerAsRead());
        HBox secondaryButtons = new HBox(10, likeBtn, markReadBtn);
        secondaryButtons.setAlignment(Pos.CENTER);

        // Combine both groups of buttons vertically
        VBox btnBox = new VBox(10, primaryButtons, secondaryButtons);
        btnBox.setAlignment(Pos.CENTER);
        form.getChildren().addAll(btnBox, answerStatusLabel);

        // Left pane contains the question selection and list of answers
        VBox leftPane = new VBox(10, new Label("Question:"), answerQuestionCombo,
                new Label("Answers:"), answersListView);
        leftPane.setPadding(new Insets(10));

        // Overall layout for the Answers tab: left pane and form side-by-side
        HBox layout = new HBox(10, leftPane, form);
        layout.setPadding(new Insets(10));
        tab.setContent(layout);
        return tab;
    }

    /**
     * Refreshes the ComboBox that lists all questions for answering.
     */
    private void refreshAnswerQuestionCombo() {
        ObservableList<Question> items = FXCollections.observableArrayList(questionsList.getAllQuestions());
        answerQuestionCombo.setItems(items);
    }

    /**
     * Refreshes the answers list view based on the selected question and any active filters.
     */
    private void refreshAnswersListView() {
        Question selectedQ = answerQuestionCombo.getSelectionModel().getSelectedItem();
        if (selectedQ != null) {
            // Get all answers for the selected question
            var allAnswers = answersList.getAnswersByQuestionId(selectedQ.getId());
            // Filter out resolved answers if that filter is on
            if (showOnlyUnresolvedCheckBox.isSelected()) {
                allAnswers.removeIf(answer -> answer.isResolved());
            }
            // Filter to show only liked answers if that filter is on
            if (showOnlyLikedAnswersCheckBox.isSelected()) {
                allAnswers.removeIf(answer -> answer.getLikeCount() == 0);
            }
            // Filter to show only unread answers if that filter is on
            if (showOnlyUnreadAnswersCheckBox.isSelected()) {
                allAnswers.removeIf(answer -> answer.isRead());
            }
            // Filter to show only answers from trusted users if that filter is on
            if (showOnlyTrustedAnswersCheckBox.isSelected()) {
                allAnswers.removeIf(answer -> !user.getTrustedUsers().contains(answer.getOwner()));
            }
            ObservableList<Answer> ansObs = FXCollections.observableArrayList(allAnswers);
            answersListView.setItems(ansObs);
        } else {
            answersListView.setItems(FXCollections.observableArrayList());
        }
    }

    /**
     * Adds a new answer for the selected question.
     * The answer text is taken from the answer text area.
     */
    private void addAnswer() {
        try {
            Question q = answerQuestionCombo.getSelectionModel().getSelectedItem();
            if (q == null) {
                answerStatusLabel.setText("Select a question first.");
                return;
            }
            String text = answerTextArea.getText();
            // Create a new answer with the current user as the owner
            Answer a = new Answer(q.getId(), text, currentUser);
            answersList.addAnswer(a);
            refreshAnswersListView();
            answerStatusLabel.setText("Answer added.");
            clearAnswerForm();
        } catch (Exception ex) {
            answerStatusLabel.setText("Error: " + ex.getMessage());
        }
    }

    /**
     * Updates the selected answer.
     * Only the owner of the answer can update it.
     */
    private void updateAnswer() {
        try {
            Answer a = answersListView.getSelectionModel().getSelectedItem();
            if (a == null) {
                answerStatusLabel.setText("No answer selected.");
                return;
            }
            // Ownership check: only allow the current user to update their own answer
            if (!a.getOwner().equals(currentUser)) {
                answerStatusLabel.setText("You are not authorized to update this answer.");
                return;
            }
            String newText = answerTextArea.getText();
            // resolvedCheckBox is still used here to indicate if an answer is resolved
            boolean resolved = resolvedCheckBox.isSelected();
            answersList.updateAnswer(a.getId(), newText, resolved);
            refreshAnswersListView();
            answerStatusLabel.setText("Answer updated.");
            clearAnswerForm();
        } catch (Exception ex) {
            answerStatusLabel.setText("Error: " + ex.getMessage());
        }
    }

    /**
     * Deletes the selected answer.
     * Only the owner of the answer is allowed to delete it.
     */
    private void deleteAnswer() {
        try {
            Answer a = answersListView.getSelectionModel().getSelectedItem();
            if (a == null) {
                answerStatusLabel.setText("No answer selected.");
                return;
            }
            // Check ownership before deleting
            if (!a.getOwner().equals(currentUser)) {
                answerStatusLabel.setText("You are not authorized to delete this answer.");
                return;
            }
            answersList.deleteAnswer(a.getId());
            refreshAnswersListView();
            answerStatusLabel.setText("Answer deleted.");
            clearAnswerForm();
        } catch (Exception ex) {
            answerStatusLabel.setText("Error: " + ex.getMessage());
        }
    }

    /**
     * Likes the selected answer.
     * The current user's like is recorded.
     */
    private void likeSelectedAnswer() {
        Answer a = answersListView.getSelectionModel().getSelectedItem();
        if (a == null) {
            answerStatusLabel.setText("No answer selected.");
            return;
        }
        try {
            a.like(currentUser);
            refreshAnswersListView();
            answerStatusLabel.setText("Answer liked.");
        } catch (Exception ex) {
            answerStatusLabel.setText("Error: " + ex.getMessage());
        }
    }

    /**
     * Marks the selected answer as read.
     * Only the question owner is allowed to mark an answer as read.
     */
    private void markSelectedAnswerAsRead() {
        Answer a = answersListView.getSelectionModel().getSelectedItem();
        if (a == null) {
            answerStatusLabel.setText("No answer selected.");
            return;
        }
        // Ensure that the current user is the owner of the question for which this answer was given
        Question q = answerQuestionCombo.getSelectionModel().getSelectedItem();
        if (q == null || !q.getOwner().equals(currentUser)) {
            answerStatusLabel.setText("Only the question owner can mark answers as read.");
            return;
        }
        a.setRead(true);
        refreshAnswersListView();
        answerStatusLabel.setText("Answer marked as read.");
    }

    /**
     * Clears the answer text field and resets the resolved checkbox.
     */
    private void clearAnswerForm() {
        answerTextArea.clear();
        resolvedCheckBox.setSelected(false);
    }

    // ============================ SEARCH TAB METHODS ============================

    /**
     * Creates and returns the "Search" tab.
     * This tab lets users search for questions by keyword and filter results.
     */
    private Tab createSearchTab() {
        Tab tab = new Tab("Search");
        tab.setClosable(false);

        // Set up the vertical layout for the search tab
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Create a search button that performs the search when clicked
        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(e -> performSearch());

        // Ensure that only one of the two filter checkboxes is selected at a time
        filterAnswered.setOnAction(e -> {
            if (filterAnswered.isSelected()) filterUnanswered.setSelected(false);
        });
        filterUnanswered.setOnAction(e -> {
            if (filterUnanswered.isSelected()) filterAnswered.setSelected(false);
        });

        // Arrange all search UI elements in the layout
        layout.getChildren().addAll(new Label("Enter keyword:"), searchField,
                new HBox(10, filterAnswered, filterUnanswered),
                searchBtn, new Label("Results:"), searchResultsListView, searchStatusLabel);
        tab.setContent(layout);
        return tab;
    }

    /**
     * Performs a search over the questions based on the keyword entered.
     * Applies filters based on whether the user wants only answered or unanswered questions.
     */
    private void performSearch() {
        String keyword = searchField.getText();
        if (keyword == null || keyword.trim().isEmpty()) {
            searchStatusLabel.setText("Enter a keyword to search.");
            return;
        }
        var results = questionsList.searchQuestions(keyword);
        // Apply filter: answered means at least one answer exists; unanswered means none.
        if (filterAnswered.isSelected()) {
            results.removeIf(q -> answersList.getAnswersByQuestionId(q.getId()).isEmpty());
        } else if (filterUnanswered.isSelected()) {
            results.removeIf(q -> !answersList.getAnswersByQuestionId(q.getId()).isEmpty());
        }
        searchResultsListView.setItems(FXCollections.observableArrayList(results));
        searchStatusLabel.setText("Found " + results.size() + " matching questions.");
    }

    // ============================ TRUSTED USERS TAB METHODS ============================

    /**
     * Creates and returns the "Trusted Users" tab.
     * This tab lets users manage (add/remove) their trusted users.
     */
    private Tab createTrustedUsersTab() {
        Tab tab = new Tab("Trusted Users");
        tab.setClosable(false);

        // ListView to display the current trusted users
        ListView<String> trustedUsersListView = new ListView<>();
        refreshTrustedUsersList(trustedUsersListView);

        // TextField to enter a new trusted user's name
        TextField newTrustedUserField = new TextField();
        newTrustedUserField.setPromptText("Enter username to trust");

        // Button to add a trusted user
        Button addTrustedUserBtn = new Button("Add Trusted User");
        addTrustedUserBtn.setOnAction(e -> {
            String newTrustedUser = newTrustedUserField.getText();
            if (newTrustedUser != null && !newTrustedUser.trim().isEmpty()) {
                try {
                    user.addTrustedUser(newTrustedUser);
                    newTrustedUserField.clear();
                    refreshTrustedUsersList(trustedUsersListView);
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        });

        // Button to remove the selected trusted user
        Button removeTrustedUserBtn = new Button("Remove Trusted User");
        removeTrustedUserBtn.setOnAction(e -> {
            String selectedUser = trustedUsersListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                user.removeTrustedUser(selectedUser);
                refreshTrustedUsersList(trustedUsersListView);
            }
        });

        // Arrange the add and remove buttons horizontally
        HBox buttonBox = new HBox(10, addTrustedUserBtn, removeTrustedUserBtn);
        buttonBox.setAlignment(Pos.CENTER);

        // Arrange all components vertically in the tab
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                new Label("Your Trusted Users:"),
                trustedUsersListView,
                newTrustedUserField,
                buttonBox
        );

        tab.setContent(layout);
        return tab;
    }

    /**
     * Helper method to refresh the trusted users list view.
     */
    private void refreshTrustedUsersList(ListView<String> listView) {
        // Get the current set of trusted users from the user object and display it
        Set<String> trustedSet = user.getTrustedUsers();
        ObservableList<String> items = FXCollections.observableArrayList(trustedSet);
        listView.setItems(items);
    }
}
