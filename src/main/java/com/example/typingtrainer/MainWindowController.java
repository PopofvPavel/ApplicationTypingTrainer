package com.example.typingtrainer;
///com.example.typingtrainer.MainWindowController

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileNotFoundException;
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
        checkDataButton.setOnAction(actionEvent -> {
            onCheckDataButtonClick();
        });

    }

    @FXML
    protected void onCheckDataButtonClick() {
        boolean isParagraphCorrect = false;
        boolean isPathCorrect = false;

        try {
            isPathCorrect = checkFilePathData();
            isParagraphCorrect = checkParagraphData();


            if (isParagraphCorrect && isPathCorrect) {
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