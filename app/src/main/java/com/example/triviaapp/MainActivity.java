package com.example.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviaapp.Util.Prefs;
import com.example.triviaapp.data.AnswerListAsynResponse;
import com.example.triviaapp.data.QuestionBank;
import com.example.triviaapp.model.Question;
import com.example.triviaapp.model.Score;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //singleton used bcus http request is very expensive foer moemooory space
    private TextView questionTextView;
    private TextView questionCounterTextView;
    private TextView questionScoreTxtView;
    private TextView highestscoreTextView;
    private Button falseButton;
    private Button trueButton;
    private ImageButton prevButton;
    private ImageButton nextButton;
    private Button share;
    private int currentQuestionIndex = 0;
    private int scoreCounter=0;
    private Score score;
    private Prefs prefs;
    private List<Question> questionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        score = new Score(); // score object
        prefs= new Prefs(MainActivity.this);
      //  Log.d("Second","onClick:" + prefs.getHighScore());
        // connecting
        nextButton = findViewById(R.id.next_button);
        prevButton=findViewById(R.id.prev_button);
        trueButton=findViewById(R.id.true_button);
        falseButton=findViewById(R.id.false_button);
        share=findViewById(R.id.share_button);
        questionCounterTextView=findViewById(R.id.counter_text);
        questionTextView=findViewById(R.id.question_textview);
        questionScoreTxtView=findViewById(R.id.score_textView);
        highestscoreTextView=findViewById(R.id.highestscore_textView);
        questionScoreTxtView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(scoreCounter)));
        highestscoreTextView.setText((MessageFormat.format("Highest Score:{0}", String.valueOf(prefs.getHighScore()))));

        // get previous state
        currentQuestionIndex= prefs.getState(); // now the ci is the saved one not o.
        // setting up button
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        share.setOnClickListener(this);
        questionList = new QuestionBank().getQuestion(new AnswerListAsynResponse() { // queslist is obj of question
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                //here we get Question list as this class is interface so override is must
                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer()); // this is a list so for getting index we use get mth
                // getAnswer means we get the obj now we get the ans
                questionCounterTextView.setText(currentQuestionIndex + "/" + questionArrayList.size());//good place to show the counter and we questionarray list is inside the queslist
                Log.d("Inside", "process Finished:" + questionArrayList);}



        });

        /*we doesn't get the question  list so as HTTP req is asynvhronous so when we first fetch all the data from api then a signal is send to
        main activity via class so interface is create/*
         */
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prev_button:
                if(currentQuestionIndex >0){
                    currentQuestionIndex=(currentQuestionIndex-1)%questionList.size();
                    updateQuestion();
                }

                break;
            case R.id.next_button:
                //Log.d("prefs", "onClick:" + prefs.getHighScore());
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
                break;

            case R.id.true_button:
                checkAnswer(true);
                updateQuestion(); // as when update is in next case then it's too late for animation  nd updatequestion is related to cardviiew
                break;
            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                break;

            case R.id.share_button:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("Text/Plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"I am playing Trivia");
                intent.putExtra(Intent.EXTRA_TEXT, "My current score:" + scoreCounter + "My highest score:" + prefs.getHighScore());
                startActivity(intent);
        }

    }

    private void updateQuestion() {

        Log.d("Current", "onClick" + currentQuestionIndex);
        //incrementing the question
        questionTextView.setText(questionList.get(currentQuestionIndex).getAnswer());
        questionCounterTextView.setText(currentQuestionIndex + "/" + questionList.size());// queslist used bcus we are using for whole question

    }

    private void fadeView(){// using alpha anim for fading(with code)
        final CardView cardView=findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation =new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                // we gonna move forward

                    currentQuestionIndex=(currentQuestionIndex+1)%questionList.size();
                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void shakeAnimation(){// loading xml file this class( with xml file)
        Animation shake= AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);// we have shke anim in animation class
        final CardView cardView = findViewById(R.id.cardView);// fetching cardview
        cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void checkAnswer(boolean userChooseCorrect) {
        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
        int toastMesssageId = 0;
       // int sum=0;
        if (userChooseCorrect == answerIsTrue) {
            fadeView();
            toastMesssageId = (R.string.correct_ans);// here we just assigning the variable
            addPoints();
        } else {
            shakeAnimation();
            toastMesssageId = (R.string.incorrect_ans);
            deductPoint();
        }
        // for poping out
        Toast.makeText(MainActivity.this, toastMesssageId,
                Toast.LENGTH_SHORT).show();
    }

    private void addPoints() {
       scoreCounter+=100;
       score.setScore(scoreCounter);
     questionScoreTxtView.setText(MessageFormat.format("Current Score:{0}",String.valueOf(scoreCounter)));
    }

    private void deductPoint()
    { scoreCounter-=100;
        if(scoreCounter>0){
            score.setScore(scoreCounter);
            questionScoreTxtView.setText(MessageFormat.format("Current Score:{0}",String.valueOf(score.getScore())));
        }
        else{
            scoreCounter=0;
            score.setScore(scoreCounter);
            questionScoreTxtView.setText(MessageFormat.format("Current Score:{0}",String.valueOf(scoreCounter)));

        }

}

    @Override
    protected void onPause() {
        prefs.savedHighScore(scoreCounter);
        prefs.setState(currentQuestionIndex);
        super.onPause();
    }
}

