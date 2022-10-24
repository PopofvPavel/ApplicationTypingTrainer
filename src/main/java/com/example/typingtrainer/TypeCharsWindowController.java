package com.example.typingtrainer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class TypeCharsWindowController {
    private static final double FONT_SIZE = 24;
    private static final int TYPECHARS_ROWS = 19;
    private static final int LABELS_IN_ROW = 110;
    private int pointer = 0;

    public int getPointer() {
        return pointer;
    }
    private String path = ProgramDataContainer.getPath();
    public void setPointer(int i) {
        this.pointer = i;
    }

    TypingText text;

    { // text init from file
        try {
            text = new TypingText(ProgramDataContainer.getFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private VBox vbox;
    @FXML
    private AnchorPane scenePane;
    @FXML
    private AnchorPane typingPane;
    private int paragraphNumber = ProgramDataContainer.getParagraph() - 1;

    TypeChar[] typeChars;
    private ArrayList<String> paragraphs = text.getParagraphs();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private Text WPM;

    @FXML
    private Label WPMLabel;

    @FXML
    private Text accuracyPercent;

    public Button getNextParagraphButton() {
        return nextParagraphButton;
    }

    @FXML
    private Button backButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button nextParagraphButton;

    @FXML
    private Text ofLabel;

    @FXML
    private Text paragraphNumberLabel;

    @FXML
    private Text paragraphsAmount;

    @FXML
    private Label percentLabel;

    @FXML
    private Button previousParagraphButton;

    @FXML
    void initialize() {
        putComponents();
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
        paragraphsAmount.setText(Integer.toString(paragraphs.size()));
        startNewParagraph();
    }

    private void onExitButtonClick() throws IOException {
        exitProgram();
    }

    protected void exitProgram() throws IOException {
        saveParagraphNumber();
        updateEveryBookParagraphNumberFile();

        System.exit(0);
    }

    private void updateEveryBookParagraphNumberFile() throws IOException {
        File everyBookParagraphNumberFile = ProgramDataContainer.getEveryBookParagraphNumberFile();
        Map<String, Integer> booksMap= new HashMap();
        fillBooksMapWithExistingFIles(everyBookParagraphNumberFile, booksMap);
        booksMap.put(path, this.paragraphNumber);
        rewriteEveryBookParagraphNumberFile(everyBookParagraphNumberFile, booksMap);

    }

    private void rewriteEveryBookParagraphNumberFile(File everyBookParagraphNumberFile, Map<String, Integer> booksMap) throws IOException {
        FileWriter fileWriter2 = new FileWriter(everyBookParagraphNumberFile, false);
        for(Map.Entry<String, Integer> entry : booksMap.entrySet()){
            fileWriter2.write(entry.getKey());
            fileWriter2.write("\n");
            fileWriter2.write(entry.getValue().toString());
            fileWriter2.write("\n");

        }
        fileWriter2.close();
    }

    private void fillBooksMapWithExistingFIles(File everyBookParagraphNumberFile, Map<String, Integer> booksMap) throws IOException {
        if (everyBookParagraphNumberFile.exists()) {
            FileReader fileReader = new FileReader(everyBookParagraphNumberFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //String line = null;
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()){
                if (line != null){
                    String bookPath = line;
                    line = bufferedReader.readLine();
                    int paragraphNumber = Integer.parseInt(line);
                    booksMap.put(bookPath, paragraphNumber);

                }

            }
        }
    }

    private void saveParagraphNumber() throws IOException {
        File file = ProgramDataContainer.getParagraphNumberFile();
        FileWriter fileWriter = new FileWriter(file, false);
        fileWriter.write(Integer.toString(this.paragraphNumber));
        fileWriter.close();
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

    private void onPreviousParagraphButtonClick() {
        setParagraphNumber(--paragraphNumber);
        setParagraphNumber(--paragraphNumber);
        try {
            startNewParagraph();
            getFocusOnTypeChars();
        } catch (IndexOutOfBoundsException exception) {
            System.out.println("Out of borders");
            setParagraphNumber(++paragraphNumber);
            setParagraphNumber(++paragraphNumber);
        } catch (IOException e) {
            System.err.println("Problems with focusing on typing labels");
        }
    }

    private void onNextParagraphButtonClick() {
        if (paragraphNumber == paragraphs.size()) {
            System.out.println("YOU HAVE SUCCESSFULLY FINISHED THIS CHAPTER");
        }
        try {
            startNewParagraph();
            getFocusOnTypeChars();
        } catch (IndexOutOfBoundsException exception) {
            System.out.println("Out of borders");
        } catch (IOException e) {
            System.err.println("Problems with focusing on typing labels");
        }
    }

    private void getFocusOnTypeChars() throws IOException {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("TypeCharsWindow.fxml"));
//        loader.load();
//        Parent root = loader.getRoot();
//        root.requestFocus();
        this.vbox.requestFocus();
    }

    private void putComponents() {//need 19 hboxes

//        Label label = new Label("GG");
//        label.setAlignment(Pos.BASELINE_LEFT);
//        label.setVisible(true);
        //vbox.getChildren().addAll(new Button("Cut"), new Button("Copy"), new Button("Paste"));


        ArrayList<HBox> hboxes = getHboxes(TYPECHARS_ROWS);//getting empty hboxes
        fillHboxes(hboxes);
        for (HBox box : hboxes) {
            System.out.println("hboxlen" + box.getChildren().toString());
        }
        //hboxes.add(new HBox(new Label("ggwp")));
        vbox.getChildren().addAll(hboxes);


    }

    private ArrayList<HBox> getHboxes(int amount) {
        ArrayList<HBox> hboxes = new ArrayList<>();
        for (int j = 0; j < amount; j++) {
            HBox hbox = new HBox();
            //hbox.setMaxSize(10,10);
            //hbox.setMaxHeight(1.0);
            hboxes.add(hbox);
        }
        return hboxes;
    }

    private void fillHboxes(ArrayList<HBox> hboxes) {

        for (HBox hbox : hboxes) {
            ArrayList<Label> labels = getTypeCharLabels(LABELS_IN_ROW);
            hbox.getChildren().addAll(labels);

        }
    }

    private ArrayList<Label> getTypeCharLabels(int amount) {
        ArrayList<Label> labels = new ArrayList<>();
        for (int j = 0; j < amount; j++) {
            Label label = new Label("*");
            label.setFont(new Font(this.FONT_SIZE));
            //label.setMaxSize(30,30);
            //label.setMaxHeight(1.0);
            labels.add(label);
        }
        return labels;
    }

    public void processKeyPut(KeyCode code, int pointer) {
        char ch = getCh(code, pointer);
        this.typeChars[pointer].setTyped(ch);//????
        System.out.println("POS: " + pointer + "Is correct: " + this.typeChars[pointer].isTypedCorrect() + " Correct: " + this.typeChars[pointer].getCorrect() + " Typed: " + this.typeChars[pointer].getTyped());
        System.out.println(ch);

        if (!this.typeChars[pointer].isTypedCorrect()) {//all inputted keys are in upper case
            this.typeChars[pointer].setTyped(Character.toLowerCase(ch));

        }
        Label label = getCurrentLabel(pointer);
        if (this.typeChars[pointer].isTypedCorrect()) {
            label.setStyle("-fx-background-color: #6EDD4B;");
            //((Label) ((HBox) (this.vbox.getChildren().get(0))).getChildren().get(pointer)).setStyle("-fx-background-color: #A04B88;");//test
        } else {
            label.setStyle("-fx-background-color: #E32636;");
        }
    }

    private char getCh(KeyCode code, int pointer) {
        if (code.equals(KeyCode.QUOTE)) {
            if ((this.typeChars[pointer].getCorrect() == '«') || (this.typeChars[pointer].getCorrect() == '»')) {
                return '"';
            } else {
                return '\'';
            }
        }
        if (code.equals(KeyCode.SLASH)){
            if(this.typeChars[pointer].getCorrect() == '?'){
                return '?';
            }
        }
        if (code.equals(KeyCode.DIGIT1)){
            if(this.typeChars[pointer].getCorrect() == '!'){
                return '!';
            }
        }if (code.equals(KeyCode.DIGIT9)){
            if(this.typeChars[pointer].getCorrect() == '('){
                return '(';
            }
        }if (code.equals(KeyCode.DIGIT0)){
            if(this.typeChars[pointer].getCorrect() == ')'){
                return ')';
            }
        }
        return code.getChar().charAt(0);
    }

    private Label getCurrentLabel(int pointer) {
        int counter = 0;
        for (int row = 0; row < this.TYPECHARS_ROWS; row++) {
            for (int labelIndex = 0; labelIndex < LABELS_IN_ROW; labelIndex++) {
                if (pointer == counter) {
                    return ((Label) ((HBox) (this.vbox.getChildren().get(row))).getChildren().get(labelIndex));
                }
                counter++;

            }
        }
        return null;
    }

    @FXML
    private void startNewParagraph() throws IndexOutOfBoundsException {
        resetParagraph();
        setVisibleInfoLabels(false);
        putComponents();
        paragraphNumberLabel.setText(Integer.toString(paragraphNumber + 1));
        this.highlightCurrentPosition(this.getPointer());
        this.typeChars = null;
        int paragraphNumber = getParagraphNumber();
        String paragraph = this.paragraphs.get(paragraphNumber);
        int paragraphLength = paragraph.length();

        //setVisibleInfoLabels(false);

        this.typeChars = new TypeChar[paragraphLength];
        for (int i = 0; i < paragraphLength; i++) {
            char symbol = paragraph.charAt(i);
            this.typeChars[i] = new TypeChar();
            this.typeChars[i].setCorrect(symbol);


//            Label label = new Label(Character.toString(symbol));
//            label.setVisible(true);
            System.out.println(this.typeChars[i].toString());
        }
        setLabelsWithParagraphText(paragraph);
        System.out.println(paragraph.toString());
//        TypingManager.setCorrectChars(paragraph, typeChars);


        //paragraphNumberLabel.setText(Integer.toString(paragraphNumber + 1));
        //inputText.setText("");

        //textField.setText(paragraph);
        if (paragraphNumber < paragraphs.size()) {
            setParagraphNumber(++paragraphNumber);
        } else {
            System.out.println("end of file" + paragraphs.size());
        }

    }

    private void setVisibleInfoLabels(boolean isVisible) {//add commented labels
        this.WPMLabel.setVisible(isVisible);
        //this.wordsTypedCorrectLabel.setVisible(isVisible);
        this.ofLabel.setVisible(isVisible);
        this.percentLabel.setVisible(isVisible);

        this.accuracyPercent.setVisible(isVisible);
        this.WPM.setVisible(isVisible);
        //this.wordsTypedCorrect.setVisible(isVisible);
        //this.wordsAmount.setVisible(isVisible);
    }

    private void resetParagraph() {
        this. vbox.getChildren().clear();
        this.pointer = 0;
        this.typeChars = null;
    }


    private void setLabelsWithParagraphText(String paragraph) {
        int paragraphLength = paragraph.length();
        int charIndex = 0;

        for (int row = 0; row < this.TYPECHARS_ROWS; row++) {
            for (int labelIndex = 0; labelIndex < LABELS_IN_ROW; labelIndex++) {
                if (charIndex < paragraphLength) {
                    ((Label) ((HBox) (this.vbox.getChildren().get(row))).getChildren().get(labelIndex)).setText(Character.toString(paragraph.charAt(charIndex)));
                    charIndex++;
                } else {
                    ((Label) ((HBox) (this.vbox.getChildren().get(row))).getChildren().get(labelIndex)).setVisible(false);
                    //break;
                }

            }
        }
        //((Label) ((HBox) (this.vbox.getChildren().get(0))).getChildren().get(charIndex)).setText(Character.toString(symbol));
//        if (charIndex % 2 == 0) {
//            ((Label) ((HBox) (this.vbox.getChildren().get(0))).getChildren().get(charIndex)).setStyle("-fx-background-color: #A04B88;");
//            //((Label)((HBox)(this.vbox.getChildren().get(0))).getChildren().get(i)).setMaxSize(10,10);//not working
//        }
    }

    public int getParagraphNumber() {
        return paragraphNumber;
    }

    private void setParagraphNumber(int i) {
        this.paragraphNumber = i;
    }


    public void clearLabel(int pointer) {
        Label label = getCurrentLabel(pointer);
        Label defaultLabel = new Label("10");
        var background = defaultLabel.getBackground();
        label.setBackground(background);

    }

    public void highlightCurrentPosition(int pointer) {
        Label label = getCurrentLabel(pointer);
        //label.setStyle("-fx-background-color: #006EFF;");
        label.setStyle("-fx-background-color: #7FFFD4;");
        Label defaultLabel = new Label("10");
        defaultLabel.setStyle("-fx-background-color: #006EFF;");
        var background = defaultLabel.getBackground();
        //label.setBackground(new Background(new BackgroundFill(Color.rgb(0, 110, 255), null, null)));//blue cursor
        label.setBackground(new Background(new BackgroundFill(Color.rgb(127,255,212), null, null)));
        int nextpointer = this.getPointer() + 1;
        clearLabel(nextpointer);


    }

    public void processInputtedText() {
        setVisibleInfoLabels(true);
    }
}
