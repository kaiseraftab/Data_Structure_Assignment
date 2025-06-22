import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.List;

public class MainApp extends Application
{
    private ComboBox<String> structureSelector;
    private ComboBox<String> algorithmSelector;
    private ComboBox<String> sortKeySelector;
    private CheckBox halfListCheckbox;
    private Button importButton;
    private Button runButton;
    private TextArea rawJsonArea;
    private TextArea halfListArea;
    private TextArea outputArea;
    private TextField searchField;
    private Label timeLabel;
    private List<Book> bookList;

    @Override public void start(Stage stage)
    {
        structureSelector = new ComboBox<>();
        structureSelector.getItems().addAll("LinkedList", "HashSet", "Dictionary");
        structureSelector.setValue("LinkedList");
        Tooltip.install(structureSelector, new Tooltip("Select data structure"));

        algorithmSelector = new ComboBox<>();
        algorithmSelector.getItems().addAll("Bubble Sort", "Insertion Sort", "Merge Sort", "Linear Search", "Binary Search");
        algorithmSelector.setValue("Bubble Sort");
        Tooltip.install(algorithmSelector, new Tooltip("Select algorithm"));

        sortKeySelector = new ComboBox<>();
        sortKeySelector.getItems().addAll("TITLE", "AUTHOR", "YEAR", "GENRE", "PUBLISHER");
        sortKeySelector.setValue("TITLE");
        Tooltip.install(sortKeySelector, new Tooltip("Select sort/search key"));

        halfListCheckbox = new CheckBox("Use first half only");
        Tooltip.install(halfListCheckbox, new Tooltip("Operate on first half of dataset"));

        importButton = new Button("Import JSON");
        runButton = new Button("Run");
        runButton.setDisable(true);

        searchField = new TextField();
        searchField.setPromptText("Search term");
        Tooltip.install(searchField, new Tooltip("Enter term for search algorithms"));

        timeLabel = new Label("Execution Time: -");

        rawJsonArea = createTextArea("Raw JSON content");
        halfListArea = createTextArea("First half of dataset");
        outputArea = createTextArea("Results");

        HBox controls = new HBox(10, structureSelector, algorithmSelector, sortKeySelector, halfListCheckbox, searchField, importButton, runButton);
        HBox previews = new HBox(10, rawJsonArea, halfListArea);
        VBox root = new VBox(10, controls, previews, timeLabel, outputArea);
        root.setPadding(new Insets(10));

        importButton.setOnAction(e -> importJson(stage));
        runButton.setOnAction(e -> runOperation());

        stage.setTitle(" Book Sort & Search");
        stage.setScene(new Scene(root, 1000, 700));
        stage.show();
    }

    private TextArea createTextArea(String prompt)
    {
        TextArea area = new TextArea();
        area.setPromptText(prompt);
        area.setWrapText(true);
        area.setEditable(false);
        area.setPrefRowCount(10);
        return area;
    }

    private void importJson(Stage stage)
    {
        File file = new FileChooser().showOpenDialog(stage);
        if (file != null)
        {
            try
            {
                String rawJson = Files.readString(Path.of(file.getAbsolutePath()));
                rawJsonArea.setText(rawJson);

                bookList = BookLoader.loadBooks(file.getAbsolutePath());
                runButton.setDisable(false);

                List<Book> half = bookList.subList(0, bookList.size() / 2);
                halfListArea.setText(half.stream().map(Book::toString).collect(Collectors.joining("\n")));
            }
            catch (Exception ex)
            {
                showError("Error loading file: " + ex.getMessage());
            }
        }
    }

    private void runOperation()
    {
        if (bookList == null)
        {
            showError("Load dataset first.");
            return;
        }

        List<Book> data = new ArrayList<>(bookList);
        if (halfListCheckbox.isSelected())
        {
            data = new ArrayList<>(data.subList(0, data.size() / 2));
        }

        Function<Book, String> extractor = b ->
                switch (sortKeySelector.getValue())
                {
                    case "TITLE"     -> b.getTitle();
                    case "AUTHOR"    -> b.getAuthor();
                    case "YEAR"      -> String.valueOf(b.getYear());
                    case "GENRE"     -> b.getGenre();
                    default           -> b.getPublisher();
                };

        Comparator<Book> comparator = (a, b) ->
                switch (sortKeySelector.getValue())
                {
                    case "TITLE"     -> a.getTitle().compareToIgnoreCase(b.getTitle());
                    case "AUTHOR"    -> a.getAuthor().compareToIgnoreCase(b.getAuthor());
                    case "GENRE"     -> a.getGenre().compareToIgnoreCase(b.getGenre());
                    case "PUBLISHER" -> a.getPublisher().compareToIgnoreCase(b.getPublisher());
                    default           -> Integer.compare(a.getYear(), b.getYear());
                };

        IDataStructure<Book> struct = switch (structureSelector.getValue())
        {
            case "HashSet"    -> new MyHashSet<>();
            case "Dictionary" -> new MyDictionary<>(Book::getTitle);
            default            -> new MyLinkedList<>();
        };
        data.forEach(struct::insert);

        ISortable<Book> sortable = (ISortable<Book>) struct;
        ILinearSearchable<Book> searchable = (ILinearSearchable<Book>) struct;

        long start = System.nanoTime();
        List<Book> result;

        switch (algorithmSelector.getValue())
        {
            case "Bubble Sort"    ->
            {
                sortable.bubbleSort(comparator);
                result = struct.getAll();
            }
            case "Insertion Sort" ->
            {
                sortable.insertionSort(comparator);
                result = struct.getAll();
            }
            case "Merge Sort"     ->
            {
                sortable.mergeSort(comparator);
                result = struct.getAll();
            }
            case "Linear Search"  -> result = searchable.linearSearch(extractor, searchField.getText());
            default                ->
            {
                result = searchable.binarySearch(extractor, searchField.getText(), comparator);
            }

            case "Binary Search" -> {
                // debug:
                System.out.println("Running binarySearch on structure: " + struct.getName());
                List<Book> bsResults = searchable.binarySearch(extractor, searchField.getText(), comparator);
                System.out.println("binarySearch found " + bsResults.size() + " items.");
                result = bsResults;
            }
        }

        long end = System.nanoTime();
        timeLabel.setText("Execution Time: " + (end - start) + " ns");
        outputArea.setText(result.stream().map(Book::toString).collect(Collectors.joining("\n")));
    }

    private void showError(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public static void main(String[] args)
    {
        launch();
    }
}