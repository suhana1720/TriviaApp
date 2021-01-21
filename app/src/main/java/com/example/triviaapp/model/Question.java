package com.example.triviaapp.model;

public class Question {
    private String answer;
    private boolean answerTrue;

    public Question() {
    }

    public Question(String answer, boolean answerTrue) {// constructor works is to assign a value in diff obj
        this.answer = answer;
        this.answerTrue= answerTrue;
    }
// when variable is private then to write our own value in diff class we use get, set
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isAnswerTrue() {
        return answerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        this.answerTrue = answerTrue;
    }
    // we override mth bcus we getting ques address but want in text form

    @Override
    public String toString() {
        return "Question{" +
                "answer='" + answer + '\'' +
                ", answerTrue=" + answerTrue +
                '}';
    }
}
