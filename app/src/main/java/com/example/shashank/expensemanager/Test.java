package com.example.shashank.expensemanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Shashank on 30-06-2016.
 */
public class Test extends AppCompatActivity {
    Button click ;
    TextView tv;

    SQLiteDatabase db ;
    int c=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        tv  = (TextView)findViewById(R.id.textView6);
        click = (Button)findViewById(R.id.button);

        SqliteHelper helper = new SqliteHelper(this);

        db = helper.getWritableDatabase();

        getCounter();
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCounter();
            }
        });
    }
    void getCounter(){
        Cursor cursor=db.rawQuery("select val from test",null);

        while(cursor.moveToNext()){
            c=cursor.getInt(cursor.getColumnIndex("val"));
        }
        tv.setText(c+"");

    }
    void updateCounter(){
        ContentValues values=new ContentValues();
        values.put("val",(c+1));
        db.update("test",values,null,null);
        getCounter();
    }
}
