package com.example.typingtrainer;

import java.io.File;
import java.io.InvalidObjectException;

public class ProgramDataContainer {
    private static File file;
    private static File everyBookParagraphNumberFile = new File("Books/LOG/BooksParagraphs");
    private static File paragraphNumberFile = new File("Books/LOG/ParagraphLog");
    private static String path;
    private static int paragraph = 1;



    public static File getEveryBookParagraphNumberFile() {
        return everyBookParagraphNumberFile;
    }

    public static void setEveryBookParagraphNumberFile(File everyBookParagraphNumberFile) {
        ProgramDataContainer.everyBookParagraphNumberFile = everyBookParagraphNumberFile;
    }
    public static File getParagraphNumberFile() {
        return paragraphNumberFile;
    }
    public static void setParagraphNumberFile(File paragraphNumberFile) {
        ProgramDataContainer.paragraphNumberFile = paragraphNumberFile;
    }
    public static int getParagraph() {
        return paragraph;
    }
    public static void setParagraph(int paragraph) {
        ProgramDataContainer.paragraph = paragraph;
    }
    public static File getFile() {
        return file;
    }
    public static void setFile(File file) throws InvalidObjectException {
        if ((file != null) && (file.canRead())) {
            ProgramDataContainer.file = file;
        } else {
            throw new InvalidObjectException("This file is unreadable or does not exixts");
        }
    }
    public static String getPath() {
        return path;
    }
    public static void setPath(String path) {
        ProgramDataContainer.path = path;
    }
}