package com.example.shashank.expensemanager;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Shashank on 27-06-2016.
 */
public class Login extends AppCompatActivity {
    SharedPreferences sp;
    boolean loggdedin = false;
    SQLiteDatabase db;
    boolean success ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        TextView tvregister = (TextView)findViewById(R.id.tvsignup);
        success = false;

        SqliteHelper helper = new SqliteHelper(this);
        db = helper.getReadableDatabase();

        /**To underline the sign up String**/
        String udata="New User? Sign Up!";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);

        tvregister.setText(content);

        /**User clicks on sign up, move to register activity and destroy this activity in onPause, if needed**/

        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this,Register.class);
                startActivity(i);
            }
        });

        /**Using SharedPreferences to start a session **/

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sp.edit();

        loggdedin = sp.getBoolean("loggedin",false);

        /*This is used to keep the user logged in when the next time the user opens the app*/
        if(loggdedin){
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }

        Button blogin = (Button)findViewById(R.id.blogin);
        final EditText etuserid = (EditText) findViewById(R.id.etuserid);
        final EditText etpassword = (EditText) findViewById(R.id.etpassword);

        /**Here, the user logs in. We now save the user_name and start a session
         * in SharedPreferences
         * **/

        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etuserid.getText().toString();
                final String password = etpassword.getText().toString();

                if(username.length() == 0 || password.length() == 0){
                    if(username.length() == 0){
                        Toast.makeText(Login.this,"Please enter your username",Toast.LENGTH_LONG).show();
                    }
                    else if(password.length() == 0){
                        Toast.makeText(Login.this,"Please enter your password",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    /**
                     * Here we should check from the db whether the user is registered or not
                     **/
                    success = false;
                    String q = "select * from users where user_name = '"+username+"' and password = '"+password+"'";
                    Cursor cursor = db.rawQuery(q,null);
                    cursor.moveToFirst();
                    if(cursor.getCount() > 0){
                        success = true;
                    }
                    if(success){
                        /*Logging in successfully to Main Activity
                        * Putting username and loggedin status in SharedPreferences
                        * */

                        editor.putBoolean("loggedin",true);
                        editor.putString("username",username);
                        editor.apply();
                        Intent i = new Intent(Login.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast.makeText(Login.this,"Sorry, invalid username or password.",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
