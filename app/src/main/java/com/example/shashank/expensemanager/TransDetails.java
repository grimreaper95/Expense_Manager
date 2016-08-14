package com.example.shashank.expensemanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Shashank on 09-07-2016.
 */
public class TransDetails extends AppCompatActivity {

    TextView tvcat,tvamt,tvdate,tvdesc,tvmode,tvcreditordebit ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_details);
        Toolbar toolbar = (Toolbar)findViewById(R.id.mytoolbar1);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvcat = (TextView)findViewById(R.id.tvcat);
        tvamt = (TextView)findViewById(R.id.tvamt);
        tvdate = (TextView)findViewById(R.id.tvdot);
        tvdesc = (TextView)findViewById(R.id.tvdesc);
        tvmode = (TextView)findViewById(R.id.tvmode);
        tvcreditordebit = (TextView)findViewById(R.id.tvcd);

        Bundle b = getIntent().getExtras();
        tvcat.setText(b.getString("category"));
        tvamt.setText(b.getString("amount"));
        tvdate.setText(b.getString("date"));
        tvdesc.setText(b.getString("desc"));
        tvmode.setText(b.getString("mode"));
        tvcreditordebit.setText(b.getString("d_or_c"));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
