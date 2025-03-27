# HW3
Homework 3 

Overview

This project was developed for **Individual Homework 3** in CSE 360. The goal of this assignment is to practice automated testing, proper documentation using Javadoc, and become familiar with JUnit and standalone test mainlines.

The test application is built using a simplified standalone `main()` method (no JUnit required) and focuses on validating core features of the `Answer` and `AnswerList` classes from our TP2 submission.

---

Features Tested

The following five automated tests were implemented in the `AnswerTestingMainline` class:

1. `testAddAnswerWithEmptyTextThrowsException`  
   Ensures that an empty answer string throws an `IllegalArgumentException`.

2. `testAddAnswerWithTooLongTextThrowsException`  
   Validates that answers longer than 500 characters are rejected.

3. `testAddNullAnswerThrowsException`  
   Tests that a `null` answer cannot be added to `AnswerList`.

4. `testUpdateAnswerWithInvalidDataThrowsException`  
   Checks that updating an existing answer with invalid text fails.

5. `testDeleteNonExistentAnswerThrowsException`  
   Confirms deletion of a non-existent answer throws `NoSuchElementException`.

---

Running the Tests

This project does **not** use JUnit. To run the tests:

1. Open the project in Eclipse or any Java IDE.
2. Locate the `AnswerTestingMainline.java` file under `src/application`.
3. Right-click the file → Run As → Java Application.
4. The console will display ✅ (pass) or ❌ (fail) for each test.

---

Javadoc

Professional-quality Javadoc has been written for:
- `AnswerTestingMainline.java` and all test methods
- Output generated using Oracle’s Javadoc tool


Inspiration:  
[Oracle Java Collections Framework Docs](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html)

https://www.youtube.com/watch?v=tYUYz6l4TWw

And my roommates who are Master’s students introduced me to Javadocs and gave me a basic concept of what they are.



---

Screencast

A short screencast has been recorded showing:
- The implementation and logic of each test
- Live test run output in the console
- Generated Javadoc and explanation of how it was written

https://asu.zoom.us/rec/share/NiDLl19aNdDgtDkUf4YFnhqUd8aAEmHi3zwoKH9KP4ISrA8sYIjHIvJrPBV9zRqw.irYzt4fx4mlbAw3J?startTime=1743047258000

Passcode: 3Vz&uhJ3
---


