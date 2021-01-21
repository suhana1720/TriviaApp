package com.example.triviaapp.Util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences preferences;
// we use sp class so we need to connect with Activity along with context(activity)
    public Prefs(Activity activity) {
        // we instantiating to make sure that these sp is set up before we use any where
        this.preferences = activity.getPreferences(activity.MODE_PRIVATE);
    }
    public void savedHighScore(int score){
        int currentScore=score;
        // as we want to stored the highest one we compared
        int lastScore= preferences.getInt("high_score", 0);// when user using app 1st time then will receive 0 from sp class
        if(score > lastScore){
            // we have a new highest nd we save it
            preferences.edit().putInt("high_score", score).apply();
        }
    }
    public int getHighScore(){
        return preferences.getInt("high_score",0);
    }

    public void setState(int Index){
        preferences.edit().putInt("index",Index).apply();// if 0 then we always get 0
    }

    public int getState(){
        return preferences.getInt("index",0);
    }
}
