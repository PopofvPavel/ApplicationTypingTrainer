package com.example.typingtrainer;

import java.io.*;
import java.util.ArrayList;

public class TypingText {
    private String text;
    private ArrayList<String> paragraphs;
    public final int length;

    public ArrayList<String> getParagraphs() {
        return paragraphs;
    }

    private int printedCorrect = 0;
    private int mistyped = 0;

    public int getPrintedCorrect() {
        return printedCorrect;
    }

    public int getMistyped() {
        return mistyped;
    }

    public void incrementPrintedCorrect() {
        this.printedCorrect++;
    }

    public void incrementMistyped() {
        this.mistyped++;
    }

    public TypingText(String text) {
        this.paragraphs = new ArrayList<>();
        this.text = text;
        this.length = text.length();
        splitIntoParagraphs(text);
    }

    private void splitIntoParagraphs(String text) {
        String[] paragraphs = text.split("\n");
        for (String paragraph : paragraphs) {
            if (!paragraph.isEmpty()) {
                this.paragraphs.add(paragraph);

            }
        }


    }

    public TypingText(File file) throws FileNotFoundException {
        this.paragraphs = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsoluteFile()));

        StringBuilder text = new StringBuilder();
        try {
            String line = bufferedReader.readLine();

            while (line != null) {
                text.append(line);
                //text.append(System.lineSeparator());
                text.append("\n");
                line = bufferedReader.readLine();

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.text = text.toString();
        this.length = text.length();
        splitIntoParagraphs(text.toString());

    }

    public char charAt(int index) {
        return text.charAt(index);

    }

    public int getParagraphsTextLength(int firstParagraph) {
        int length = 0;
        for (int i = firstParagraph - 1; i < this.paragraphs.size();i++){
            length += paragraphs.get(i).length();
        }
        return length + paragraphs.size() ;// - 1 ??
    }

    @Override
    public String toString() {
        return "TypingText:\n" +
                text + '\n';
    }
}
