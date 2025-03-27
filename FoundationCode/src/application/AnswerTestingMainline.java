package application;

/**
 * Standalone mainline to test Answer and AnswerList functionalities.
 * This does not use JUnit and prints results to the console.
 */
public class AnswerTestingMainline {

    public static void main(String[] args) {
        System.out.println("Running HW3 Tests...\n");

        testAddAnswerWithEmptyTextThrowsException();
        testAddAnswerWithTooLongTextThrowsException();
        testAddNullAnswerThrowsException();
        testUpdateAnswerWithInvalidDataThrowsException();
        testDeleteNonExistentAnswerThrowsException();
    }

    // 1. Test creating an Answer with empty text
    public static void testAddAnswerWithEmptyTextThrowsException() {
        try {
            new Answer(1, "", "user1");
            System.out.println("❌ testAddAnswerWithEmptyTextThrowsException FAILED: No exception thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ testAddAnswerWithEmptyTextThrowsException passed: " + e.getMessage());
        }
    }

    // 2. Test creating an Answer with too long text
    public static void testAddAnswerWithTooLongTextThrowsException() {
        try {
            String longText = "A".repeat(501);
            new Answer(1, longText, "user1");
            System.out.println("❌ testAddAnswerWithTooLongTextThrowsException FAILED: No exception thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ testAddAnswerWithTooLongTextThrowsException passed: " + e.getMessage());
        }
    }

    // 3. Test adding null Answer to AnswerList
    public static void testAddNullAnswerThrowsException() {
        try {
            AnswerList list = new AnswerList();
            list.addAnswer(null);
            System.out.println("❌ testAddNullAnswerThrowsException FAILED: No exception thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ testAddNullAnswerThrowsException passed: " + e.getMessage());
        }
    }

    // 4. Test updating answer with invalid (empty) text
    public static void testUpdateAnswerWithInvalidDataThrowsException() {
        try {
            Answer a = new Answer(1, "Valid", "user1");
            AnswerList list = new AnswerList();
            list.addAnswer(a);
            list.updateAnswer(a.getId(), "", true);
            System.out.println("❌ testUpdateAnswerWithInvalidDataThrowsException FAILED: No exception thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ testUpdateAnswerWithInvalidDataThrowsException passed: " + e.getMessage());
        }
    }

    // 5. Test deleting a non-existent answer
    public static void testDeleteNonExistentAnswerThrowsException() {
        try {
            AnswerList list = new AnswerList();
            list.deleteAnswer(999); // nonexistent ID
            System.out.println("❌ testDeleteNonExistentAnswerThrowsException FAILED: No exception thrown.");
        } catch (java.util.NoSuchElementException e) {
            System.out.println("✅ testDeleteNonExistentAnswerThrowsException passed: " + e.getMessage());
        }
    }
}
