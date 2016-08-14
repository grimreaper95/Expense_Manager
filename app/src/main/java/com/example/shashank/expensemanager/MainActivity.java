package com.example.shashank.expensemanager;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    FragmentManager manager;
    String username;
    DrawerLayout dlayout;
    Toolbar toolbar;
    SQLiteDatabase db;
    float wallet = 0 ;
    ActionBarDrawerToggle toggle ;
    TextView tvwelcome;
    int addedMoney = 0;
    Transactions_fragment trans_frag;
    SharedPreferences sp;
    Home home_frag;
    TextView curbal;
    boolean backgrnd_frag_is_home = true;
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Toast.makeText(this,date,Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setting the toolbar
        toolbar = (Toolbar)findViewById(R.id.mytoolbar1);
        setSupportActionBar(toolbar);
        manager = getSupportFragmentManager();
        trans_frag =  new Transactions_fragment();
        home_frag = new Home();
        if(getSupportActionBar()!=null){
            //setting the home button in action bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        SqliteHelper helper = new SqliteHelper(this);
        db = helper.getWritableDatabase();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sp.edit();
        curbal = (TextView)findViewById(R.id.tvcurbal);
        //Navigation drawer is handled here
        dlayout = (DrawerLayout)findViewById(R.id.mydrawerlayout);
        toggle = new ActionBarDrawerToggle(this,dlayout,0,0);
        dlayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        username = sp.getString("username","Guest");
        Cursor cursor = db.rawQuery("select sum(amount) as amt from transactions where username = '"+ username+"'",null);
        if(cursor != null && cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
                wallet = cursor.getFloat(cursor.getColumnIndex("amt"));
            }
        }
        else{
            wallet = 0.0F;
        }
        editor.putFloat("wallet",wallet);
        editor.apply();
        //Cannot find welcome text present in header of drawer
        /*
        tvwelcome = (TextView)(navigationView.findViewById(R.id.tvwelcomehead));
        tvwelcome.setText("Welcome " + username +"!");
        */
        /*
            Using getHeaderView function in navigationView to find items in navigation header
        */
        View header = (navigationView.getHeaderView(0));
        tvwelcome = (TextView)header.findViewById(R.id.tvwelcomehead);
        tvwelcome.setText("Welcome " + username +"!");
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.dummy,home_frag);
        ft.commit();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                final int id = item.getItemId();
                if(id == R.id.nav_home){
                    backgrnd_frag_is_home = true;
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.dummy,home_frag);
                    ft.commit();
                }
                else if(id == R.id.nav_addmoney){
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Add money to wallet");
                    // Create TextView
                    final EditText input = new EditText (MainActivity.this);
                    LinearLayout layout1=new LinearLayout(MainActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    layout1.setLayoutParams(lp);
                    lp.setMargins(200,0,0,0);
                    lp.width=180;
                    input.setLayoutParams(lp);
                    layout1.addView(input);
                    alert.setView(layout1);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            addedMoney = 0;
                            if(input.getText().length() == 0)
                                addedMoney = 0;
                            else
                                addedMoney = Integer.parseInt(input.getText().toString());
                            // Do something with value!

                            ContentValues values2 = new ContentValues();
                            values2.put("cat_no",1000);
                            values2.put("cat_name","+Wallet");
                            values2.put("user_name",username);
                            db.insert("categories",null,values2);

                            int year = Calendar.getInstance().get(Calendar.YEAR);
                            int month = Calendar.getInstance().get(Calendar.MONTH)+1;
                            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                            String date = year + "/" + month + "/" + day ;

                            ContentValues values1 = new ContentValues();
                            values1.put("cat_no",1000);
                            values1.put("username",username);
                            values1.put("amount",addedMoney);
                            values1.put("date_of_trans",date);
                            values1.put("description","self");
                            values1.put("trans_mode","cash");
                            values1.put("debit_or_credit","credit");
                            db.insert("transactions", null, values1);
                            wallet += addedMoney;
                            editor.putFloat("wallet",wallet);
                            editor.apply();
                            home_frag.refresh(backgrnd_frag_is_home);
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });
                    alert.show();
                    editor.putFloat("wallet",wallet);
                    editor.apply();
                }
                else if(id == R.id.nav_graphs){
                    backgrnd_frag_is_home = false;
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.dummy,new Graph());
                    ft.commit();
                }
                else if(id == R.id.nav_transactions){
                    backgrnd_frag_is_home = false;
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.dummy,trans_frag);
                    ft.commit();
                }
                else if(id == R.id.nav_logout){
                    backgrnd_frag_is_home = false;
                    editor.putString("username","guest");
                    editor.putBoolean("loggedin",false);
                    editor.apply();
                    Intent i = new Intent(MainActivity.this,Login.class);
                    startActivity(i);
                    finish();
                    return true;
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mydrawerlayout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mydrawerlayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/
    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);*/
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dlayout.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;
            /*case R.id.settings:
                Intent i = new Intent(this,Pref.class);
                startActivity(i);*/
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }
}