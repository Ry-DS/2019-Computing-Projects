package me.ryan_s;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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

        CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
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
        Event event=eventBox.getSelectionModel().getSelectedItem();

        resultArea.setText(format.format(new Date())
                +"\nName: "+nameField.getText()
                +"\nGender: "+((RadioButton)gender.getSelectedToggle()).getText()
                +"\nYear Level: "+ageBox.getSelectionModel().getSelectedItem()
                +"\nEvent: "+event
                +"\nResult: "+event.getUnit().read(resultPane.getChildren().get(0))+" "+event.getUnit().getUnitName()

        );
    }
    @FXML
    public void onGUIAction(){
        if(!nameField.getText().isEmpty()&&gender.getSelectedToggle()!=null&&
        !eventBox.getSelectionModel().isEmpty()&&!ageBox.getSelectionModel().isEmpty()&&!eventBox.getSelectionModel().getSelectedItem().
                getUnit().read(resultPane.getChildren().get(0)).isEmpty()){
            confirmButton.setDisable(false);

        }
        else confirmButton.setDisable(true);
        if(resultArea.getText().isEmpty())
            saveButton.setDisable(true);
        else saveButton.setDisable(false);
    }
    @FXML
    public void onSaveClicked(){
        Toast.makeText(Main.getMainStage(),"Saved under example.txt",3000,500,500);

    }

    private void loadData() {
        ageBox.setItems(FXCollections.observableArrayList(agesFile));
        eventsFile.forEach(s->events.add(Event.parse(s)));
        events= Collections.unmodifiableList(events);
        agesFile=Collections.unmodifiableList(agesFile);
        eventBox.setItems(FXCollections.observableArrayList(events));



    }

    private void readFiles() throws IOException {
        agesFile= Files.readAllLines(Paths.get(getClass().getClassLoader().getResource("ages.txt").getPath()));
        eventsFile=Files.readAllLines(Paths.get(getClass().getClassLoader().getResource("events.txt").getPath()));

    }

}
