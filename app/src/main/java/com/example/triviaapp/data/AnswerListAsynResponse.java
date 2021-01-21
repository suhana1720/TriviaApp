package com.example.triviaapp.data;

import com.example.triviaapp.model.Question;

import java.util.ArrayList;

public interface AnswerListAsynResponse {// doen't have any relation to anybody but we implement this
    void processFinished(ArrayList<Question> questionArrayList);
    //this mth w'ill overridden at class level
}


