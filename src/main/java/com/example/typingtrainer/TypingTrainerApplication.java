package com.example.typingtrainer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class TypingTrainerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TypingTrainerApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 1380, 750);
        Scene scene = new Scene(fxmlLoader.load(), 1600, 900);
        stage.setTitle("TypingTrainer");
        stage.setScene(scene);
        stage.initStyle(StageStyle.DECORATED);
        stage.show();


        //animation D:\AARadik\JavaWorkspace\AnimationFX_Lib

    }

    public static void main(String[] args) {
        launch();
    }
}