package com.example.shashank.expensemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Shashank on 27-06-2016.
 */
public class Logout extends AppCompatActivity{
    SharedPreferences sp ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("loggedin",false);
        editor.putString("username","Guest");
        editor.putFloat("wallet",0);
        editor.apply();
        Intent i = new Intent(this,Login.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }
}
