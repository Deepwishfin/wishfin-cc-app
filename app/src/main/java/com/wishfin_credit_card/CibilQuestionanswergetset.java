package com.wishfin_credit_card;

class CibilQuestionanswergetset {

    private String question;
    private String answerid;
    private String questionkey;
    private String answerkey;
    private String asnwer;
    private String radiostatus = "false";

    CibilQuestionanswergetset(String question, String answerid) {
        this.question = question;
        this.answerid = answerid;
    }

    CibilQuestionanswergetset() {

    }

    String getRadiostatus() {
        return radiostatus;
    }

    void setRadiostatus(String radiostatus) {
        this.radiostatus = radiostatus;
    }

    String getAsnwer() {
        return asnwer;
    }

    void setAsnwer(String asnwer) {
        this.asnwer = asnwer;
    }

    String getQuestionkey() {
        return questionkey;
    }

    void setQuestionkey(String questionkey) {
        this.questionkey = questionkey;
    }

    String getAnswerkey() {
        return answerkey;
    }

    void setAnswerkey(String answerkey) {
        this.answerkey = answerkey;
    }

    private String optionone;

    String getOptionone() {
        return optionone;
    }

    void setOptionone(String optionone) {
        this.optionone = optionone;
    }

    String getQuestion() {
        return question;
    }

    String getAnswerid() {
        return answerid;
    }

    void setAnswerid(String answerid) {
        this.answerid = answerid;
    }

    void setQuestion(String question) {
        this.question = question;
    }
}
