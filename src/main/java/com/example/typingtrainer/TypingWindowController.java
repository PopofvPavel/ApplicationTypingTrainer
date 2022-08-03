package com.example.typingtrainer;

//import com.gluonhq.charm.glisten.mvc.View;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TypingWindowController {
    boolean isFinished = false;
    private int paragraphNumber = ProgramDataContainer.getParagraph() - 1;


    public int getParagraphNumber() {
        return paragraphNumber;
    }

    public void setParagraphNumber(int paragraphNumber) {
            this.paragraphNumber = paragraphNumber;


    }

    TypingText text;

    {
        try {
            text = new TypingText(ProgramDataContainer.getFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<String> paragraphs = text.getParagraphs();

    @FXML
    private ResourceBundle resources;

    @FXML
    private Text paragraphNumberLabel;

    @FXML
    protected Button nextParagraphButton;

    @FXML
    private Button previousParagraphButton;

    @FXML
    private Button backButton;

    @FXML
    private URL location;

    @FXML
    private Text textField;

    @FXML
    private TextArea inputText;


    @FXML
    void initialize() {
        nextParagraphButton.setOnAction(actionEvent -> onNextParagraphButtonClick());
        previousParagraphButton.setOnAction(actionEvent -> onPreviousParagraphButtonClick());
        backButton.setOnAction(actionEvent -> openMainWindow());
        inputText.setWrapText(true);
        startNewParagraph();
        inputText.requestFocus();


    }

    private void onPreviousParagraphButtonClick() {
        setParagraphNumber(--paragraphNumber);
        setParagraphNumber(--paragraphNumber);
        try{
            startNewParagraph();
        } catch (IndexOutOfBoundsException exception){
            System.out.println("Out of borders");
            setParagraphNumber(++paragraphNumber);
            setParagraphNumber(++paragraphNumber);
        }
    }

    private void onNextParagraphButtonClick() {
        if (paragraphNumber == paragraphs.size()){
            textField.setText("YOU HAVE SUCCESSFULLY FINISHED THIS CHAPTER");
        }
        try{

            startNewParagraph();
        } catch (IndexOutOfBoundsException exception){
            System.out.println("Out of borders");
        }

    }

    @FXML
     private void startNewParagraph() throws IndexOutOfBoundsException{
        int paragraphNumber = getParagraphNumber();
        String paragraph = paragraphs.get(paragraphNumber);
        int paragraphLength = paragraph.length();

        TypeChar[] typeChars = new TypeChar[paragraphLength];
        paragraphNumberLabel.setText(Integer.toString(paragraphNumber + 1));
        inputText.setText("");
        TypingManager.setCorrectChars(paragraph, typeChars);
        textField.setText(paragraph);
        if(paragraphNumber < paragraphs.size()){
            setParagraphNumber(++paragraphNumber);
        } else {
            System.out.println("end of file" +  paragraphs.size()) ;
        }



    }

    public TextArea getInputText() {
        return inputText;
    }

    public Button getNextParagraphButton() {
        return nextParagraphButton;
    }

    public void setNextParagraphButton(Button nextParagraphButton) {
        this.nextParagraphButton = nextParagraphButton;
    }

    private void openMainWindow() {

        backButton.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("hello-view.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

}
