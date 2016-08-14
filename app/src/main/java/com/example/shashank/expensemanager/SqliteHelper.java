package com.example.shashank.expensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


/**
 * Created by Shashank on 27-06-2016.
 */



public class SqliteHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME="wallet.db";
    public static int DATABASE_VERSION=1;

    String q1="CREATE TABLE users(\n" +
            "user_name varchar(100) constraint user_name_pk primary key,\n" +
            "password varchar(100)\n" +
            ")";


    /**
     * Remove these two sample tables
     * **/
    String q4 =  "create table test(\n" +
                "val integer primary key\n" +
                ")";
    String q5 = "Insert into test values (0)";




    String q2 = "CREATE TABLE categories(\n" +
            "cat_no integer constraint cat_no_pk primary key,\n" +
            "cat_name varchar(100),\n" +
            "user_name varchar(100) constraint user_name_categories_user_fk \n" +
            "references users(user_name))";

    String q3 = "CREATE TABLE transactions(\n" +
            "trans_no integer constraint trans_no_pk primary key,\n" +
            "cat_no constraint cat_no_transactions_categories_fk references categories(cat_no),\n" +
            "username constraint username_transactions_users_fk references users(user_name),\n" +
            "amount decimal(10,3) ,\n" +
            "date_of_trans date,\n" +
            "description varchar(200),\n" +
            "trans_mode varchar(20),\n" +
            "debit_or_credit varchar(20))";


    String []q={q1,q2,q3};
    Context c;


    public SqliteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        c=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for(String s : q){
            sqLiteDatabase.execSQL(s);
        }
        /**
         * Remove default created user
         */
    //    sqLiteDatabase.execSQL("insert into users values('admin','shashank95');");

        /**
         * Remove the toast
         * **/
       // Toast.makeText(c,"On Create Called",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        DATABASE_VERSION++;
        Toast.makeText(c,"on Upgrade called",Toast.LENGTH_LONG).show();

    }

}
