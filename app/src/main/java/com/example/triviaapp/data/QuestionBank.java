package com.example.triviaapp.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.triviaapp.controller.AppController;
import com.example.triviaapp.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.triviaapp.controller.AppController.TAG;

public class QuestionBank {
    // here we get ques from api
    ArrayList<Question> questionArrayList = new ArrayList<>();// we need to create the arraylist of question object so as to return the list when wwe get into question bank
    private String url ="https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
    public List<Question> getQuestion(final AnswerListAsynResponse callBack){//everytime we use this mth we use this class
        //this func return a ques arraylist
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(
                Request.Method.GET,
                url,
                (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Log.d("Json Stuff" ,"onResponse:" + response);
                        for (int i = 0; i < response.length(); i++) { //getting all obj from api
                            try {
                                Question question = new Question();// we made 1 obj
                                question.setAnswer(response.getJSONArray(i).get(0).toString());// constructor
                                question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));
                                //  Log.d("JSON", "onResponse" + response.getJSONArray(i).get(0));// for ques
                                //  Log.d("JSON2","onResponse"+ response.getJSONArray(i).getBoolean(1));// for true or false
                                //adding question obj in arraylist
                                questionArrayList.add(question);
                                // Log.d("Hello", "onResponse:" +question);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (null != callBack)
                            callBack.processFinished(questionArrayList);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }


        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;


    }
}
