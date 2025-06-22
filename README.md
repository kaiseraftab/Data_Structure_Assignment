# Data Structure Assignment - Book Sort & Search App

This JavaFX-based application allows users to import a JSON file of books and perform sorting and searching operations using different data structures and algorithms.
---

## Features

- Import book dataset from a JSON file
- Choose from 3 data structures:
  - Linked List
  - Hash Set
  - Dictionary (HashMap-based)
- Perform sorting using:
  - Bubble Sort
  - Insertion Sort
  - Merge Sort
- Perform searching using:
  - Linear Search
  - Binary Search
- View execution time for each operation (in nanoseconds)
- Optionally work on the first half of the dataset
- GUI built using JavaFX


---

## How to Run the App

Prerequisites
-Java 17 or above
-JavaFX SDK (e.g. javafx-sdk-21)
-IntelliJ IDEA or any IDE that supports JavaFX

JavaFX VM Options
To run the JavaFX app correctly:
1. Go to Run → Edit Configurations
2. Under VM options, paste:
  --module-path "path/to/javafx-sdk-21/lib" --add-modules javafx.controls,javafx.fxml
 Replace path/to/... with the actual path where you extracted JavaFX SDK.
3. Run `MainApp.java`








