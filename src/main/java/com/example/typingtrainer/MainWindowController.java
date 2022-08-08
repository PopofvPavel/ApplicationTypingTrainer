package com.example.typingtrainer;
///com.example.typingtrainer.MainWindowController

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
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
        startButton.requestFocus();
        checkDataButton.setOnAction(actionEvent -> onCheckDataButtonClick());
        startButton.setOnAction(actionEvent -> {
            try {
                onStartButtonClick();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        try {
            String paragraphNumberString = getParagraphNumberFromFile();
            if(!(paragraphNumberString == null)){
                this.paragraphNumberTextField.setText(paragraphNumberString);
            }
        } catch (IOException e) {
            System.out.println("Problems with paragraph file");
        }


    }

    @FXML
    private void onStartButtonClick() throws IOException {
        if (checkData()) {
            openTypingWindow();
        }
        //checkData();

    }

    @FXML
    protected void onCheckDataButtonClick() {
        checkData();

    }

    private boolean checkData() {
        boolean isParagraphCorrect = false;
        boolean isPathCorrect = false;

        try {
            isPathCorrect = checkFilePathData();
            isParagraphCorrect = checkParagraphData();


            if (isParagraphCorrect && isPathCorrect) {

                int paragraph = Integer.parseInt(paragraphNumberTextField.getText());
                ProgramDataContainer.setParagraph(paragraph);
                checkDateTextField.setText("Data is correct" );
                return true;
            } else {
                checkDateTextField.setText("Data is incorrect");
                return false;
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
        return false;
    }

    private void openTypingWindow() throws IOException {

        startButton.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("TypingWindow.fxml"));

        loader.load();
        TypingWindowController controller = loader.getController();

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);


        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode code = keyEvent.getCode();
                System.out.println("You pressed key:" + code);
                switch (code) {
                    case ALT:
                    case ESCAPE:
                        controller.getNextParagraphButton().requestFocus();
                        try {
                            controller.processInputtedText();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case CONTROL:
                        break;
                    default:
                        controller.getInputText().requestFocus();
                        break;


                }
            }
        });
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

    private String getParagraphNumberFromFile() throws IOException {
        File file = ProgramDataContainer.getParagraphNumberFile();
        if (file.exists()){
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String paragraphNumber = bufferedReader.readLine();
            return paragraphNumber;
        }
        return null;
    }
}