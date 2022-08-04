package com.example.typingtrainer;

//import com.gluonhq.charm.glisten.mvc.View;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
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
    TypeChar[] typeChars;
    private ArrayList<String> paragraphs = text.getParagraphs();

    @FXML
    private ResourceBundle resources;

    @FXML
    private Text paragraphNumberLabel;

    @FXML
    private Text paragraphsAmount;

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
        paragraphsAmount.setText(Integer.toString(paragraphs.size()));
        startNewParagraph();
        inputText.requestFocus();


    }

    private void onPreviousParagraphButtonClick() {
        setParagraphNumber(--paragraphNumber);
        setParagraphNumber(--paragraphNumber);
        try {
            startNewParagraph();
        } catch (IndexOutOfBoundsException exception) {
            System.out.println("Out of borders");
            setParagraphNumber(++paragraphNumber);
            setParagraphNumber(++paragraphNumber);
        }
    }

    private void onNextParagraphButtonClick() {
        if (paragraphNumber == paragraphs.size()) {
            textField.setText("YOU HAVE SUCCESSFULLY FINISHED THIS CHAPTER");
        }
        try {

            startNewParagraph();
        } catch (IndexOutOfBoundsException exception) {
            System.out.println("Out of borders");
        }

    }

    @FXML
    private void startNewParagraph() throws IndexOutOfBoundsException {
        this.typeChars = null;
        int paragraphNumber = getParagraphNumber();
        String paragraph = this.paragraphs.get(paragraphNumber);
        int paragraphLength = paragraph.length();

        this.typeChars = new TypeChar[paragraphLength];
        paragraphNumberLabel.setText(Integer.toString(paragraphNumber + 1));
        inputText.setText("");
        TypingManager.setCorrectChars(paragraph, typeChars);
        textField.setText(paragraph);
        if (paragraphNumber < paragraphs.size()) {
            setParagraphNumber(++paragraphNumber);
        } else {
            System.out.println("end of file" + paragraphs.size());
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

    public void processInputtedText() throws IOException {
        System.out.println(this.paragraphs.get(this.paragraphNumber - 1));

        String typedText = inputText.getText();
        setInputtedTextInTypeChars(typedText);

        ArrayList<String> words = getInputTextWords(typedText);
        ArrayList<String> markedWords = getMarkedWords(words);
//        for (String markedWord : markedWords) {
//            MarkedWordsContainer.writeWordIntoLibrary(markedWord);
//        }
        System.out.println("Typed text:" + typedText);
        System.out.println("Typed words:" + words);
        System.out.println("Marked words:" + markedWords);




    }

    private ArrayList<String> getMarkedWords(ArrayList<String> words) {
        return null;
    }

    private ArrayList<String> getInputTextWords(String typedText) {
        return null;
    }

    private void setInputtedTextInTypeChars(String typedText) {
        int length = typedText.length();
        System.out.println("Typechars:");
        for(int i = 0; i < length; i++){
            try{
                this.typeChars[i].setTyped(typedText.charAt(i));
                System.out.println(typeChars[i]);
            } catch (NullPointerException exception){
                if(paragraphNumber >= paragraphs.size()){
                    System.out.println(paragraphNumber);
                    openMainWindow();
                    break;
                }
            }catch (IndexOutOfBoundsException exception){
                break;
            }

        }

    }
}
