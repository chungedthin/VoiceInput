package com.example.chung.voiceinput;

public class QuizQuestion {
    private int id;
    private String question;
    private int correctAnswer;
    private String answerOptions1;
    private String answerOptions2;
    private String answerOptions3;
    private int score;

    public QuizQuestion(int id, String question,int correctAnswer, String answerOptions1,String answerOptions2,String answerOptions3,int score){
        this.id = id;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answerOptions1 = answerOptions1;
        this.answerOptions2 = answerOptions2;
        this.answerOptions3 = answerOptions3;
        this.score = score;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public int getcorrectAnswer() {
        return correctAnswer;
    }
    public void setcorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getAnswerOptions1() {
        return answerOptions1;
    }
    public void setAnswerOptions1(String answerOptions1) {
        this.answerOptions1 = answerOptions1;
    }

    public String getAnswerOptions2() {
        return answerOptions2;
    }
    public void setAnswerOptions2(String answerOptions2) {
        this.answerOptions2 = answerOptions2;
    }

    public String getAnswerOptions3() {
        return answerOptions3;
    }
    public void setAnswerOptions3(String answerOptions3) {
        this.answerOptions3 = answerOptions3;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

}
