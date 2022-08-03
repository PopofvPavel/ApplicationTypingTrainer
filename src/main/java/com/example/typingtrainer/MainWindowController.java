package com.example.typingtrainer;
///com.example.typingtrainer.MainWindowController

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button checkDataButton;

    @FXML
    private TextField checkDateTextField;

    @FXML
    private TextField paragraphNumberTextField;

    @FXML
    private TextField pathToFileTextField;

    @FXML
    private Button startButton;

    @FXML
    void initialize() {
        checkDataButton.setOnAction(actionEvent -> onCheckDataButtonClick());
        startButton.setOnAction(actionEvent -> openTypingWindow());

    }

    @FXML
    protected void onCheckDataButtonClick() {
        boolean isParagraphCorrect = false;
        boolean isPathCorrect = false;

        try {
            isPathCorrect = checkFilePathData();
            isParagraphCorrect = checkParagraphData();


            if (isParagraphCorrect && isPathCorrect) {

                int paragraph = Integer.parseInt(paragraphNumberTextField.getText());
                ProgramDataContainer.setParagraph(paragraph);
                checkDateTextField.setText("Data is correct" +  ProgramDataContainer.getFile().length());
            } else {
                checkDateTextField.setText("Data is incorrect" );
            }

        } catch (FileNotFoundException exception) {
            checkDateTextField.setText(exception.getMessage());
        } catch (NumberFormatException exception) {
            checkDateTextField.setText(exception.getMessage());
        } catch (NullPointerException exception) {
            checkDateTextField.setText(exception.getMessage());
        } catch (RuntimeException exception) {
            checkDateTextField.setText(exception.getMessage());
        } catch (InvalidObjectException exception) {
            checkDateTextField.setText(exception.getMessage());
        }

    }
    private void openTypingWindow(){
        startButton.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("TypingWindow.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    private boolean checkParagraphData() throws RuntimeException {
        try {
            int paragraph = Integer.parseInt(paragraphNumberTextField.getText());
            return true;
        } catch (NumberFormatException exception) {
            throw new NumberFormatException("You should input only integer numbers");
        }


    }

    private boolean checkFilePathData() throws FileNotFoundException, InvalidObjectException {
        String path = pathToFileTextField.getText();
        if (path.isEmpty()) {
            throw new FileNotFoundException("File path is empty");

        }

        File file = new File(path);
        if (file.exists()) {
            ProgramDataContainer.setFile(file);

        } else {
            throw new FileNotFoundException("File path is invalid");

        }

        return true;

    }
}