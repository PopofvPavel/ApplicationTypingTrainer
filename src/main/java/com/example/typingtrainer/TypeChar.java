package com.example.typingtrainer;

public class TypeChar {
    private char correct;
    private char typed;
    private boolean isTypedCorrect = false;


    public void setCorrect(char correct) {
        this.correct = correct;
        //this.setTypedCorrect();
    }

    public void setTyped(char typed) {
        this.typed = typed;
        this.setTypedCorrect();
    }

    public void setTypedCorrect() {
        if (((this.correct == '«') || (this.correct == '»')) && (this.typed == '"')) {
            this.isTypedCorrect = true;
        } else if ((this.correct == '—') && (this.typed == '-')) {
            this.isTypedCorrect = true;
        } else {
            this.isTypedCorrect = this.correct == this.typed;
        }
    }

    public char getCorrect() {
        return correct;
    }

    public char getTyped() {
        return typed;
    }

    @Override
    public String toString() {
        return "[TypeChar:" +
                "correct=" + correct +
                ", typed=" + typed +
                ", isTypedCorrect=" + isTypedCorrect + "]"
                ;
    }

    public boolean isTypedCorrect() {

        return isTypedCorrect;

    }
}
