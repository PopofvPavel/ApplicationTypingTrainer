package com.example.typingtrainer;

public class RussianKeyCodeTranslator {
    public static char getRussianChar(char character, boolean isShiftPressed) {
        switch ((character)) {
            case 'Q' : return 'Й';
            case 'W' : return 'Ц';
            case 'E' : return 'У';
            case 'R' : return 'К';
            case 'T' : return 'Е';
            case 'Y' : return 'Н';
            case 'U' : return 'Г';
            case 'I' : return 'Ш';
            case 'O' : return 'Щ';
            case 'P' : return 'З';
            case '[' : return 'Х';
            case ']' : return 'Ъ';
            case 'A' : return 'Ф';
            case 'S' : return 'Ы';
            case 'D' : return 'В';
            case 'F' : return 'А';
            case 'G' : return 'П';
            case 'H' : return 'Р';
            case 'J' : return 'О';
            case 'K' : return 'Л';
            case 'L' : return 'Д';
            case ';' : return 'Ж';
            case '\'' : return 'Э';
            case 'Z' : return 'Я';
            case 'X' : return 'Ч';
            case 'C' : return 'С';
            case 'V' : return 'М';
            case 'B' : return 'И';
            case 'N' : return 'Т';
            case 'M' : return 'Ь';
            case ',' : return 'Б';
            case '.' : return 'Ю';
            case '2' :
                if (isShiftPressed) {
                    return '"';
                } else {
                    return '2';
                }
            case '4' :
                if (isShiftPressed) {
                    return ';';
                } else {
                    return '4';
                }
            case '6' :
                if (isShiftPressed) {
                    return ':';
                } else {
                    return '6';
                }
            case '7' :
                if (isShiftPressed) {
                    return '?';
                } else {
                    return '7';
                }
            case '/' :
                if (isShiftPressed) {
                    return ',';
                } else {
                    return '.';
                }
        }
        return 'ё';
    }

}
