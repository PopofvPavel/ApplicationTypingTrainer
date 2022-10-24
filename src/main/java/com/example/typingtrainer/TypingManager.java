package com.example.typingtrainer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TypingManager {

    public static void setCorrectChars(String paragraph, TypeChar... typeChars) {
        checkTypeCharsLengthValidation(paragraph, typeChars);
        for (int i = 0; i < paragraph.length(); i++) {
            typeChars[i] = new TypeChar();
            typeChars[i].setCorrect(paragraph.charAt(i));
        }

    }

    private static void ProcessCommand() {
        int code = 0;
        try {
            code = System.in.read();
            char ch = (char) code;
            if (ch == 'q') {
                System.exit(0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private static int ProcessDoubleEnter(int doubleEnterPressCounter) {
        doubleEnterPressCounter++;
        if (doubleEnterPressCounter == 1) {
            System.out.println("Double click Enter!!!");
        } else if (doubleEnterPressCounter == 2) {
            System.out.println("TRIPLE click Enter!!!");
        } else {
            System.out.println("MULTIPLE CLICK ENTER!!!");
        }
        return doubleEnterPressCounter;
    }

//    public static void writeTypedTextInLog(String paragraph, TypeChar[] typeChars) throws IOException {
//        InputLog log = new InputLog();
//        StringBuilder typedText = new StringBuilder();
//        for (int i = 0; i < typeChars.length; i++) {
//            typedText.append(typeChars[i].getTyped());
//        }
//        typedText.append('\n');
//        log.writeInFile(typedText.toString());
//
//    }

    public static void printAccuracyInfoString(String paragraph, long startTime, TypeChar[] typeChars) {
        StringBuilder accuracyResult = new StringBuilder();
        int misprinted = 0;
        int correctPrinted = 0;


        for (int i = 0; i < paragraph.length(); i++) {
            System.out.println(typeChars[i]);

            if (!typeChars[i].isTypedCorrect()) {
                accuracyResult.append(ANSI_COLORS.ANSI_RED + "X" + ANSI_COLORS.ANSI_RESET);
                misprinted++;

            } else {
                accuracyResult.append(ANSI_COLORS.ANSI_GREEN + "V" + ANSI_COLORS.ANSI_RESET);
                correctPrinted++;

            }

        }
        double percentOfTyping = (double) correctPrinted / (double) typeChars.length * 100;

        accuracyResult.append("\n").append(String.format("PERCENT OF TYPING: %s, WPM = %s, printed correct: %d, misprinted: %d",
                getPercentOfTypingColeredString(percentOfTyping), getWPMColoredString(typeChars.length, startTime), correctPrinted, misprinted));
        System.out.println(accuracyResult.toString());
    }

    private static String getWPMColoredString(int length, long startTime) {
        long endTime = System.nanoTime();

        long totalTime = (endTime - startTime);
        TimeUnit.SECONDS.convert(totalTime, TimeUnit.NANOSECONDS);
        totalTime /= 60;

        double WPM = (double) length / 5 / (double) totalTime * 1_000_000_000;//???

        String WPMColored = "";
        String formattedDouble = String.format("%.2f", WPM);
        if (WPM >= 40.0) {
            WPMColored += ANSI_COLORS.ANSI_GREEN + formattedDouble + ANSI_COLORS.ANSI_RESET;
        } else if (WPM < 25.0) {
            WPMColored += ANSI_COLORS.ANSI_RED + formattedDouble + ANSI_COLORS.ANSI_RESET;
        } else {
            WPMColored += ANSI_COLORS.ANSI_BLUE + formattedDouble + ANSI_COLORS.ANSI_RESET;
        }

        return WPMColored;

    }

    private static String getPercentOfTypingColeredString(double percentOfTyping) {
        String percentOfTypingColored = "";
        String formattedDouble = String.format("%.12f", percentOfTyping);
        if (percentOfTyping >= 90.0) {
            percentOfTypingColored += ANSI_COLORS.ANSI_GREEN + formattedDouble + ANSI_COLORS.ANSI_RESET;
        } else if (percentOfTyping < 60.0) {
            percentOfTypingColored += ANSI_COLORS.ANSI_RED + formattedDouble + ANSI_COLORS.ANSI_RESET;
        } else {
            percentOfTypingColored += ANSI_COLORS.ANSI_BLUE + formattedDouble + ANSI_COLORS.ANSI_RESET;
        }
        return percentOfTypingColored;
    }

    private static void checkTypeCharsLengthValidation(String text, TypeChar... typeChars) {
        if (typeChars.length < text.length()) {
            throw new IllegalArgumentException("Not enough typechars was given");
        }
    }


}
