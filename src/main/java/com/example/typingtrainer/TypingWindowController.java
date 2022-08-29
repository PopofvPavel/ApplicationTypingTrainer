package com.example.typingtrainer;

//import com.gluonhq.charm.glisten.mvc.View;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
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

    long startTime;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private Text paragraphNumberLabel;
    @FXML
    private Text wordsAmount;

    @FXML
    private Text wordsTypedCorrect;
    @FXML
    private Text WPM;

    @FXML
    private Text accuracyPercent;

    @FXML
    private Text accuracyStringText;
    @FXML
    private Text paragraphsAmount;

    @FXML
    protected Button nextParagraphButton;

    @FXML
    private Button exitButton;

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
    private Label wordsTypedCorrectLabel;
    @FXML
    private Label percentLabel;
    @FXML
    private Text ofLabel;
    @FXML
    private Label WPMLabel;


    @FXML
    void initialize() {
        nextParagraphButton.setOnAction(actionEvent -> onNextParagraphButtonClick());
        previousParagraphButton.setOnAction(actionEvent -> onPreviousParagraphButtonClick());
        backButton.setOnAction(actionEvent -> openMainWindow());
        exitButton.setOnAction(actionEvent -> {
            try {
                onExitButtonClick();
            } catch (IOException e) {
                System.out.println("Problems with rewriting paragraph file");
            }
        });
        inputText.setWrapText(true);
        paragraphsAmount.setText(Integer.toString(paragraphs.size()));
        startNewParagraph();
        inputText.requestFocus();


    }

    private void onExitButtonClick() throws IOException {
        exitProgram();

    }

    protected void exitProgram() throws IOException {
        File file = ProgramDataContainer.getParagraphNumberFile();
        FileWriter fileWriter = new FileWriter(file, false);
        fileWriter.write(Integer.toString(this.paragraphNumber));
        fileWriter.close();
        System.exit(0);
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

        setVisibleInfoLabels(false);

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

    private void setVisibleInfoLabels(boolean isVisible) {
        this.WPMLabel.setVisible(isVisible);
        this.wordsTypedCorrectLabel.setVisible(isVisible);
        this.ofLabel.setVisible(isVisible);
        this.percentLabel.setVisible(isVisible);

        this.accuracyPercent.setVisible(isVisible);
        this.WPM.setVisible(isVisible);
        this.wordsTypedCorrect.setVisible(isVisible);
        this.wordsAmount.setVisible(isVisible);
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

        setVisibleInfoLabels(true);

        String typedText = inputText.getText();
        ArrayList<String> words = getInputTextWords(typedText);
        ArrayList<String> markedWords = getMarkedWords(words);
        int correctTypedWordsAmount = getCorrectTypedWordsAmount(words);
        this.wordsTypedCorrect.setText(Integer.toString(correctTypedWordsAmount));
        this.wordsAmount.setText(Integer.toString(words.size()));

        typedText = getClearedTypedTextFromSlashes(typedText);

        setInputtedTextInTypeChars(typedText);
        String accuracyString = processTypedCharsAccuracy(typeChars);//set field accuracyPercent

        if (!markedWords.isEmpty()) {
            for (String markedWord : markedWords) {
                MarkedWordsContainer.writeWordIntoLibrary(markedWord);
            }
        }

        long endTime = System.currentTimeMillis();
        double WPM = (double)this.typeChars.length / 5.0 / (((double)endTime - (double)this.getStartTime()) / 1000.0 / 60.0 );
        String WPMString =  String.format("%.1f", WPM);
        this.WPM.setText(WPMString);

        System.out.println("Typed text:" + typedText);
        System.out.println("Typed words:" + words);
        System.out.println("Marked words:" + markedWords);
        System.out.println("Typechars len:" + (double)this.typeChars.length + "\nlen/5 = " + (double)this.typeChars.length / 5
        + " sec " + (((double)endTime - (double)this.getStartTime()) / 1000.0) + " WPM = " + WPM);


    }

    private int getCorrectTypedWordsAmount(ArrayList<String> words) {
        String[] wordsInText = textField.getText().split(" ");
        for (int i = 0; i < wordsInText.length; i++) {
            wordsInText[i] = wordsInText[i].replace('«', '"').replace('»', '"').replace('—', '-');
        }

        int correctTypedWordsAmount = 0;

        String[] printedWords = new String[words.size()];
        int j = 0;
        for (String word : words) {
            word = word.replace('/', ' ');
            printedWords[j] = word.trim();
            j++;
        }

        for (int i = 0; i < words.size(); i++) {
            if (printedWords[i].equals(wordsInText[i])) {
                correctTypedWordsAmount++;
            }
        }
        return correctTypedWordsAmount;
    }

    private String getClearedTypedTextFromSlashes(String typedText) {
        String textWithoutSlashes = typedText.replace('/', ' ');
        String clearedText = textWithoutSlashes.replaceAll("  ", " ");//for double spaces

        return clearedText;
    }

    private String processTypedCharsAccuracy(TypeChar[] typeChars) {
        StringBuilder accuracyResult = new StringBuilder();
        int misprinted = 0;
        int correctPrinted = 0;

        for (int i = 0; i < typeChars.length; i++) {
            if (!typeChars[i].isTypedCorrect()) {
                accuracyResult.append("X");
                misprinted++;
            } else {
                accuracyResult.append("V");
                correctPrinted++;

            }
        }
        double percentOfTyping = (double) correctPrinted / (double) typeChars.length * 100;
        String percentOfTypingString = String.format("%.2f", percentOfTyping);
        this.accuracyPercent.setText(percentOfTypingString);

        return accuracyResult.toString();
    }

    private ArrayList<String> getInputTextWords(String typedText) {
        ArrayList<String> words = new ArrayList<String>();
        String[] wordsSplit = typedText.split(" ");
        for (String word : wordsSplit) {

            words.add(word);
        }


        return words;
    }

    private ArrayList<String> getMarkedWords(ArrayList<String> words) {
        ArrayList<String> markedWords = new ArrayList<String>();
        for (String word : words) {
            if (word.charAt(0) == '/') {
                word = word.replace('/', ' ').trim();
                markedWords.add(word);

            }

        }
        return markedWords;
    }

    private void setInputtedTextInTypeChars(String typedText) {
        int length = typedText.length();
        System.out.println("Typechars:");
        for (int i = 0; i < length; i++) {
            try {
                this.typeChars[i].setTyped(typedText.charAt(i));
                System.out.println(typeChars[i]);
            } catch (NullPointerException exception) {
                if (paragraphNumber >= paragraphs.size()) { //if end of the file return to main screen
                    System.out.println(paragraphNumber);
                    openMainWindow();
                    break;
                }
            } catch (IndexOutOfBoundsException exception) {
                break;
            }

        }

    }
}
