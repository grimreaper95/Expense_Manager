package com.example.shashank.expensemanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import java.util.Calendar;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Shashank on 27-06-2016.
 */



public class Cat0 extends AppCompatActivity{
    Toolbar toolbar;
    Button bdone,bdate;
    EditText etamount,etdetails,etdate;
    TextView tvdot;
    SharedPreferences sp;
    float wallet = 0;
    String username ,mode,debit_or_credit,catName;
    SQLiteDatabase db;
    private int day ,month,year;
    int cat_no;
    static final int DIALOG_ID = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cathome);
        toolbar = (Toolbar)findViewById(R.id.mytoolbar1);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        catName = getIntent().getStringExtra("catName");
        wallet = sp.getFloat("wallet",0);
        username = sp.getString("username","guest");
        SqliteHelper helper = new SqliteHelper(this);
        db = helper.getWritableDatabase();

        TextView textView = (TextView)findViewById(R.id.tvcatname);
        textView.setText("Category : "+catName);



        //here we have to get category number through intent
        cat_no = getIntent().getIntExtra("catNo",1);

        bdone = (Button)findViewById(R.id.bdone);
        etamount = (EditText)findViewById(R.id.etamount);
        etdetails = (EditText)findViewById(R.id.etdetails);
        etdate = (EditText)findViewById(R.id.etdate);



        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) ;//starts from 0
        day = cal.get(Calendar.DAY_OF_MONTH);

        showdialogonclick();

        etamount.setSelection(1);
        final RadioButton rbdeduct = (RadioButton)findViewById(R.id.rbdeduct);
        final RadioButton rbcash = (RadioButton)findViewById(R.id.rbcash);

        mode = "cash";
        debit_or_credit = "debit";

        //need to create a date picker





        bdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etamount.getText().toString().length() == 0){
                    Toast.makeText(Cat0.this,"Please enter an amount",Toast.LENGTH_LONG).show();
                    etamount.setText("0.0");
                    return ;
                }
                if(rbcash.isChecked()){
                    mode = "cash";
                }
                if(rbdeduct.isChecked()){
                    debit_or_credit = "debit";
                }
                double amt = Float.parseFloat(etamount.getText().toString());

                ContentValues values1 = new ContentValues();

                values1.put("cat_no",cat_no);
                values1.put("cat_name",catName);
                values1.put("user_name",username);
                db.insert("categories",null,values1);

                ContentValues values = new ContentValues();

                //We do not put trans_no because in SQLite primary value always auto_increments if not inserted

                values.put("cat_no",cat_no);
                values.put("username",username);
                if(rbdeduct.isChecked())
                    values.put("amount",amt*(-1));
                else
                    values.put("amount",amt);
                values.put("date_of_trans",etdate.getText().toString().trim());
                values.put("description",etdetails.getText().toString());
                values.put("trans_mode",mode);
                values.put("debit_or_credit",debit_or_credit);
                db.insert("transactions",null,values);
                //Now we have inserted the transaction into the transaction table

                if(!rbdeduct.isChecked()){
                    wallet += amt;
                    Toast.makeText(Cat0.this,"Rs."+amt+" added to your wallet. Current balance ="+wallet,Toast.LENGTH_LONG).show();
                }
                else{
                    wallet -= amt;
                    Toast.makeText(Cat0.this,"Rs."+amt+" deducted from your wallet. Current balance = Rs."+wallet,Toast.LENGTH_LONG).show();
                }
                etamount.setText("0.0");
                etdetails.setText("");
                etamount.requestFocus();
                etamount.setSelection(1);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("status",true);
                editor.putFloat("wallet",wallet);
                editor.apply();
            }
        });
    }

    public void showdialogonclick(){
        etdate = (EditText)findViewById(R.id.etdate);
        etdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);
            }
        });
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DIALOG_ID)
            return new DatePickerDialog(this,dplistener,year,month,day);
        return null;
    }


    DatePickerDialog.OnDateSetListener dplistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            etdate = (EditText)findViewById(R.id.etdate);
            year = i ;
            month = i1 + 1;
            day = i2;
            etdate.setText("              "+day+"/"+ month +"/"+year);
        }
    };



    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            //Intent i = new Intent(this,MainActivity.class);
           // i.putExtra("balance",wallet);
            //startActivity(i);
            //onBackPressed();
            //Toast.makeText(this,wallet +"",Toast.LENGTH_LONG).show();
            //Home home_frag = new Home();
            NavUtils.navigateUpFromSameTask(this);
            /*
            Intent i = new Intent(this,Home.class);
            startActivity(i);
            finish()
            */
            //home_frag.refresh2();
        }
        return super.onOptionsItemSelected(item);
    }
}
