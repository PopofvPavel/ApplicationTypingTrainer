package com.example.typingtrainer;
///com.example.typingtrainer.MainWindowController


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainWindowController {
    @FXML
    private Label welcomeText;
    @FXML
    private Button typeCharButton;

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
    private Button fileChooserButton;

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
        //fileChooserButton.setOnAction(actionEvent -> chooseNewBookInFileManager());
        startButton.setOnAction(actionEvent -> {
            try {
                onStartButtonClick();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        typeCharButton.setOnAction(actionEvent -> {
            try {
                onOpenTypeCharsWindowCilck();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        try {
            //String paragraphNumberString = getParagraphNumberFromFile();
            String path = pathToFileTextField.getText();
            String paragraphNumberString = getParagraphNumberForCurrentBook(path);
            if (!(paragraphNumberString == null)) {
                this.paragraphNumberTextField.setText(paragraphNumberString);
                ProgramDataContainer.setPath(this.paragraphNumberTextField.getText());

            }
        } catch (IOException e) {
            System.out.println("Problems with paragraph file");
        } catch (NullPointerException e) {
            System.err.println("nullpoint");
        }


    }


    @FXML
    private void chooseNewBookInFileManager(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        File book = fileChooser.showOpenDialog(stage);


        if (book != null && book.isFile()) {
            try {
                ProgramDataContainer.setFile(book);
                System.out.println(ProgramDataContainer.getFile().getAbsolutePath());
                this.pathToFileTextField.setText(book.getAbsolutePath());
                paragraphNumberTextField.setText("1");
                ProgramDataContainer.setPath(this.paragraphNumberTextField.getText());

                String path = pathToFileTextField.getText();
                String paragraphNumberString = null;
                try {
                    paragraphNumberString = getParagraphNumberForCurrentBook(path);
                } catch (NullPointerException exception) {
                    paragraphNumberString = "1";
                }

                if (!(paragraphNumberString == null)) {
                    this.paragraphNumberTextField.setText(paragraphNumberString);
                    ProgramDataContainer.setPath(this.paragraphNumberTextField.getText());//?? why setpath

                }

                startButton.requestFocus();
            } catch (InvalidObjectException e) {
                System.err.println("Exception in file chooser");
            } catch (IOException e) {
                System.err.println("Exception in getting from current book");
            }
        }

    }

    @FXML
    private void onStartButtonClick() throws IOException {
        if (checkData()) {
            ProgramDataContainer.setPath(this.pathToFileTextField.getText());//maybe not working
            openTypingWindow();
        }
        //checkData();

    }

    @FXML
    protected void onCheckDataButtonClick() {
        checkData();
        String path = pathToFileTextField.getText();
        try {
            System.out.println("PARAGRAPH FOR CURRENT BOOK" + getParagraphNumberForCurrentBook(path));
        } catch (IOException e) {
            System.err.println("Problems in check data getting paragraphs");
        }
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
                checkDateTextField.setText("Data is correct");
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

    private void onOpenTypeCharsWindowCilck() throws IOException {
        if (checkData()) {
            ProgramDataContainer.setPath(this.pathToFileTextField.getText());//maybe not working
            openTypeCharsWindow();
        }

    }

    private void openTypeCharsWindow() throws IOException {

        typeCharButton.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("TypeCharsWindow.fxml"));

        loader.load();
        TypeCharsWindowController controller = loader.getController();

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.requestFocus();
        root.requestFocus();//need to not focus SPACE on button

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode code = keyEvent.getCode();

                System.out.println("You pressed key:" + code);
                switch (code) {
                    case ALT:
                    case ESCAPE:
                        controller.getNextParagraphButton().requestFocus();
                        controller.processInputtedText();
                        break;
                    case CONTROL:
                    case SHIFT:
                    case WINDOWS:
                        break;
                    case F10:
                        try {
                            controller.exitProgram();
                        } catch (IOException e) {
                            System.out.println("problems with exit");
                        }
                        break;
                    case BACK_SPACE:
                        int ptr = controller.getPointer();
                        controller.setPointer(--ptr);
                        controller.clearLabel(controller.getPointer());
                        controller.highlightCurrentPosition(controller.getPointer());

                        break;
                    case SLASH: {
                        int pointer = controller.getPointer();
                        if (keyEvent.isControlDown()) {
                            controller.putNextWordIntoLibraryFile(pointer);
                        } else {
                            controller.processKeyPut(code, pointer, keyEvent.isShiftDown());
                            controller.setPointer(++pointer);
                            controller.highlightCurrentPosition(controller.getPointer());
                        }
                        break;
                    }
                    default:

                        int pointer = controller.getPointer();
                        controller.processKeyPut(code, pointer, keyEvent.isShiftDown());
                        controller.setPointer(++pointer);
                        controller.highlightCurrentPosition(controller.getPointer());//fix minus border ex
//                        long startTIme = System.currentTimeMillis();
//                        controller.setStartTime(startTIme);
                        break;


                }
            }
        });

        stage.showAndWait();
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
                    case F10:
                        try {
                            controller.exitProgram();
                        } catch (IOException e) {
                            System.out.println("problems with exit");
                        }
                        break;
                    default:
                        controller.getInputText().requestFocus();
                        long startTIme = System.currentTimeMillis();
                        controller.setStartTime(startTIme);
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
        if (file.exists()) {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String paragraphNumber = bufferedReader.readLine();
            return paragraphNumber;
        }
        return null;
    }

    private String getParagraphNumberForCurrentBook(String path) throws IOException {
        File file = ProgramDataContainer.getEveryBookParagraphNumberFile();
        Map<String, Integer> booksMap = new HashMap();

        if (file.exists()) {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String bookPath = line;
                line = bufferedReader.readLine();
                int paragraphNumber = Integer.parseInt(line);
                booksMap.put(bookPath, paragraphNumber);

            }

        }
        Integer paragraph = 1;
        paragraph = booksMap.get(path);

        return Integer.toString(paragraph);
    }


}