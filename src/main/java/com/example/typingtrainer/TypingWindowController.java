package com.example.typingtrainer;

//import com.gluonhq.charm.glisten.mvc.View;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
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
    private Button nextParagraphButton;

    @FXML
    private Button previousParagraphButton;

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
        inputText.setWrapText(true);
        startNewParagraph();

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
        try{
            startNewParagraph();
        } catch (IndexOutOfBoundsException exception){
            System.out.println("Out of borders");
        }

    }

    @FXML
     private void startNewParagraph() throws IndexOutOfBoundsException{
        String paragraph = paragraphs.get(paragraphNumber);
        int paragraphLength = paragraph.length();

        TypeChar[] typeChars = new TypeChar[paragraphLength];
        paragraphNumberLabel.setText(Integer.toString(paragraphNumber + 1));
        TypingManager.setCorrectChars(paragraph, typeChars);
        textField.setText(paragraph);
        if(paragraphNumber < paragraphLength){
            setParagraphNumber(++paragraphNumber);
        } else {
            System.out.println("end of file");
        }



    }


}
