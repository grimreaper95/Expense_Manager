package com.example.shashank.expensemanager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Shashank on 27-06-2016.
 */
public class Register extends AppCompatActivity {

    Button bregister;
    Toolbar toolbar;
    EditText etusername,etpassword,etconpassword;
    SQLiteDatabase db ;
    String username,password,conpassword;
    boolean success = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        bregister = (Button) findViewById(R.id.bregister);
        toolbar = (Toolbar) findViewById(R.id.mytoolbar1);
        setSupportActionBar(toolbar);
        etusername =(EditText)findViewById(R.id.etusername_2);
        etpassword =(EditText)findViewById(R.id.etpassword_2);
        etconpassword = (EditText)findViewById(R.id.etconpassword);
        success = true;


        final SqliteHelper helper = new SqliteHelper(this);
        db = helper.getReadableDatabase();
        /**
         * Here, we have to check if the user_name already exists in our database.
         * **/
        bregister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                username = etusername.getText().toString();
                password = etpassword.getText().toString();
                conpassword = etconpassword.getText().toString();
                if(username.length() == 0 || password.length() == 0){
                    if(username.length() == 0){
                        Toast.makeText(Register.this,"Please enter a username",Toast.LENGTH_LONG).show();
                    }
                    else if(password.length() == 0){
                        Toast.makeText(Register.this,"Please enter a password",Toast.LENGTH_LONG).show();
                    }
                return ;
                }

                if(!password.equals(conpassword)){
//                    final View coordinatorLayoutView = findViewById(R.id.snackbarPosition);
/*
                    Snackbar
                            .make(coordinatorLayoutView, "Try again", Snackbar.LENGTH_LONG)
                            .setAction("Password and Confirm Password fields differ. Try again.",).show();
                            */
                    Toast.makeText(Register.this,"Password and Confirm Password fields differ. Try again.",Toast.LENGTH_LONG).show();
                    return ;
                }


                String q = "select * from users where user_name='"+username+"'";
                Cursor cursor = db.rawQuery(q,null);
                ///cursor.moveToFirst(); No need here, only need getCount()
                if(cursor.getCount()>0){
                    success = false;
                }
                if(!success){
                    //The user_name already exists in the database. Therefore, not registration is unsuccessful
                    Toast.makeText(Register.this,"Sorry, this username already exists",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(Register.this,"Congratulations, you have successfully signed up!",Toast.LENGTH_LONG).show();
                    /*
                    * Now making new entry into the database
                    * */
                    db = helper.getWritableDatabase();


                    ContentValues values = new ContentValues();
                    values.put("user_name", username);
                    values.put("password", password);
                    db.insert("users", null, values);


                    /*
                    * Inserting a fake transaction whenever a user signs up.
                     * This will help us in getting initial wallet budget for each user
                    * */


                    ContentValues values2 = new ContentValues();
                    values2.put("cat_no",-1);
                    values2.put("cat_name","newuser");
                    values2.put("user_name",username);
                    db.insert("categories",null,values2);



                    ContentValues values1 = new ContentValues();
                    values1.put("cat_no",-1);
                    values1.put("username",username);
                    values1.put("amount",0);
                    values1.put("date_of_trans","2016-01-01");
                    values1.put("description","newuser");
                    values1.put("trans_mode","cash");
                    values1.put("debit_or_credit","credit");
                    db.insert("transactions", null, values1);




                    Intent i = new Intent(Register.this,Login.class);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

}
