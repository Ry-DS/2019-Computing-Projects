package me.ryan_s;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage MAIN_STAGE;

    @Override
    public void start(Stage primaryStage) throws Exception{
        MAIN_STAGE = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Athletics Carnival Event Tracker");
        primaryStage.setScene(new Scene(root, 640, 400));
        primaryStage.show();
    }

    public static Stage getMainStage() {
        return MAIN_STAGE;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
