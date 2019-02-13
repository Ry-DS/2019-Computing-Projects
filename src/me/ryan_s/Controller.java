package me.ryan_s;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class Controller {
    @FXML private TextField nameField;
    @FXML private ToggleGroup gender;
    @FXML private RadioButton maleButton;
    @FXML private RadioButton femaleButton;

    @FXML private ComboBox<Event> eventBox;
    @FXML private ComboBox<String> ageBox;
    @FXML private Label unitLabel;
    @FXML private Button confirmButton;
    @FXML private Button saveButton;
    @FXML private TextArea resultArea;
    @FXML private Pane resultPane;

    private List<String> agesFile;
    private List<String> eventsFile;
    private List<Event> events=new ArrayList<>();
    private SimpleDateFormat format=new SimpleDateFormat();



    @FXML
    public void initialize(){

        CompletableFuture.runAsync(() -> {
            try {
                readFiles();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to read files! Application will now exit...");
                System.exit(-1);
                return;
            }
            loadData();
        });
        System.out.println("Finished initialise");



    }
    @FXML
    public void onEventModify(){
        Event selected = eventBox.getSelectionModel().getSelectedItem();
        unitLabel.setText(selected.getUnit().getUnitName());
        resultPane.getChildren().clear();
        resultPane.getChildren().add(selected.getUnit().getInput().get());
    }
    @FXML
    public void onConfirmClicked(){
        Entry entry = createEntry();
        resultArea.setText(format.format(new Date())
                + "\nName: " + entry.getName()
                + "\nGender: " + entry.getGender()
                + "\nYear Level: " + entry.getYearLevel()
                + "\nEvent: " + entry.getEvent().getName()
                + "\nResult: " + entry.getResult() + " " + entry.getEvent().getUnit().getUnitName()

        );
    }
    @FXML
    public void onGUIAction(){
        if(!nameField.getText().isEmpty()&&gender.getSelectedToggle()!=null&&
        !eventBox.getSelectionModel().isEmpty()&&!ageBox.getSelectionModel().isEmpty()&&!eventBox.getSelectionModel().getSelectedItem().
                getUnit().read(resultPane.getChildren().get(0)).isEmpty()){
            confirmButton.setDisable(false);
            saveButton.setDisable(false);

        } else {
            confirmButton.setDisable(true);
            saveButton.setDisable(true);
        }
    }
    @FXML
    public void onSaveClicked(){
        confirmButton.fire();
        Entry entry = createEntry();
        String fileName = entry.getName() + "-" + entry.getEvent().getName() + ".txt";


        CompletableFuture<Boolean> task = CompletableFuture.supplyAsync(() -> {
            String dir = System.getProperty("user.dir") + File.separator + "saves";
            new File(dir).mkdir();
            File save = new File(dir, fileName);
            File csv = new File(dir, "total.csv");
            Set<String> csvContents = new LinkedHashSet<>();
            try {
                if (!csv.createNewFile())
                    csvContents.addAll(Files.readAllLines(Paths.get(csv.getPath())));
                else {
                    csvContents.add("Name,Gender,Event,Year Level,Result,Type,Time");

                }
                csvContents.add(String.join(",", entry.getName(), entry.getGender(), entry.getEvent().getName(), entry.getYearLevel(),
                        entry.getResult(), entry.getEvent().getUnit().getUnitName(), format.format(entry.getTimeStamp())));
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> Platform.runLater(() -> Notifications.create().title("Error").text("Failed to read CSV on saving: " + fileName).position(Pos.BOTTOM_LEFT)
                        .hideAfter(Duration.seconds(2)).owner(Main.getMainStage()).showError()));
                return false;
            }
            System.out.println(save.getPath());
            try {

                if (!save.createNewFile())
                    Platform.runLater(() -> Platform.runLater(() -> Notifications.create().title("Warning").text("Overwriting file: " + fileName).position(Pos.BOTTOM_LEFT)
                            .hideAfter(Duration.seconds(2)).owner(Main.getMainStage()).showWarning()));
                csv.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> Notifications.create().title("Error").text("Failed to create file: " + e.getMessage()).position(Pos.BOTTOM_LEFT)
                        .hideAfter(Duration.seconds(2)).owner(Main.getMainStage()).showError());

                return false;
            }

            try (FileWriter writer = new FileWriter(save); FileWriter csvWriter = new FileWriter(csv)) {
                writer.write(resultArea.getText());
                writer.flush();
                for (String s : csvContents) {
                    csvWriter.write(s + "\n");
                }
                csvWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> Notifications.create().title("Error").text("Failed to write to file: " + e.getMessage()).position(Pos.BOTTOM_LEFT)
                        .hideAfter(Duration.seconds(2)).owner(Main.getMainStage()).showError());
                return false;

            }
            return true;
        });
        task.thenAccept(result -> {
            if (result)
                Platform.runLater(() ->
                        Notifications.create().title("Success!").text("Saved as: " + fileName).position(Pos.BOTTOM_LEFT)
                                .hideAfter(Duration.seconds(2)).owner(Main.getMainStage()).showInformation());
        });



    }

    private void loadData() {
        ageBox.setItems(FXCollections.observableArrayList(agesFile));
        eventsFile.forEach(s->events.add(Event.parse(s)));
        events= Collections.unmodifiableList(events);
        agesFile=Collections.unmodifiableList(agesFile);
        eventBox.setItems(FXCollections.observableArrayList(events));



    }

    private void readFiles() throws IOException {
        String dir = System.getProperty("user.dir");
        agesFile = Files.readAllLines(Paths.get(dir, "ages.txt"));
        eventsFile = Files.readAllLines(Paths.get(dir, "events.txt"));

    }

    private Entry createEntry() {
        return new Entry(nameField.getText(), ((RadioButton) gender.getSelectedToggle()).getText(), eventBox.getSelectionModel().getSelectedItem(),
                ageBox.getSelectionModel().getSelectedItem(), eventBox.getSelectionModel().getSelectedItem().getUnit().read(resultPane.getChildren().get(0)));
    }

}
