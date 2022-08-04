package com.example.typingtrainer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class MarkedWordsContainer {
    public static void writeWordIntoLibrary(String word) throws IOException {
        File library = new File("Books/Harry Potter/WordLibrary/WordsLibrary.doc");
        //File library = new File("Books/Harry Potter/pr.doc");
        word = ClearTheWord(word);
        word += '\n';
        if(!word.isEmpty()) {
            Files.write(library.toPath(), word.getBytes(), StandardOpenOption.APPEND );

        }

    }

    private static String ClearTheWord(String word) {
        word.replace(',',' ').replace('.',' ').replace('"',' ')
                .replace('?',' ');
        word = word.trim().toLowerCase();
        return word;

    }
}
