package com.example.typingtrainer;

public class TypingManager {

    public static void setCorrectChars(String paragraph, TypeChar... typeChars) {
        checkTypeCharsLengthValidation(paragraph, typeChars);
        for (int i = 0; i < paragraph.length(); i++) {
            typeChars[i] = new TypeChar();
            typeChars[i].setCorrect(paragraph.charAt(i));
        }

    }
    private static void checkTypeCharsLengthValidation(String text, TypeChar... typeChars) {
        if (typeChars.length < text.length()) {
            throw new IllegalArgumentException("Not enough typechars was given");
        }
    }


}
