package com.example.typingtrainer;

import java.io.File;
import java.io.InvalidObjectException;

public class ProgramDataContainer {
    private static File file;

    private static int paragraph = 1;

    public static int getParagraph() {
        return paragraph;
    }

    public static void setParagraph(int paragraph) {
        ProgramDataContainer.paragraph = paragraph;
    }

    public static  File getFile() {
        return file;
    }

    public static void setFile(File file) throws InvalidObjectException {
        if ((file != null) && (file.canRead())) {
            ProgramDataContainer.file = file;
        } else {
            throw new InvalidObjectException("This file is unreadable or does not exixts");
        }
    }
}