package com.example.shashank.expensemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Shashank on 01-07-2016.
 */


class Rowdata{
    String category_name,amount,date_of_transaction;
}


public class Transactions_fragment extends Fragment {
    String username;
    SharedPreferences sp ;
    List<Rowdata>list;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.transactions_fragment,container,false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sp.edit();
        list = new ArrayList<Rowdata>();
        ListView lv = (ListView)getActivity().findViewById(R.id.lvtransactions);

        username = sp.getString("username","guest");

        SQLiteDatabase db ;

        SqliteHelper helper = new SqliteHelper(getActivity());

        db = helper.getReadableDatabase();

        String q = "select t.amount,t.date_of_trans,t.description,t.trans_mode,\n" +
                "t.debit_or_credit,c.cat_name \n" +
                "from transactions t left join categories c on\n" +
                "t.cat_no = c.cat_no\n" +
                "where t.username = '"+username+"'";

        Cursor cursor = db.rawQuery(q,null);

        final List<String> listTrans;
        listTrans = new ArrayList<String>();
        final List<String>category,amount,dot,desc,mode,d_or_c,date;

        category = new ArrayList<String>();
        amount   = new ArrayList<String>();
        desc   = new ArrayList<String>();
        mode   = new ArrayList<String>();
        d_or_c = new ArrayList<String>();
        date = new ArrayList<String>();

        if(cursor != null && cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
                while(cursor.moveToNext()){
                    String cat_name = cursor.getString(cursor.getColumnIndex("cat_name"));
                    Float amt = cursor.getFloat(cursor.getColumnIndex("amount"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    String modeoftrans = cursor.getString(cursor.getColumnIndex("trans_mode"));
                    String dorc = cursor.getString(cursor.getColumnIndex("debit_or_credit"));
                    String date_of_trans = cursor.getString(cursor.getColumnIndex("date_of_trans"));
                    category.add(cat_name);
                    amount.add(amt+"");
                    desc.add(description);
                    mode.add(modeoftrans);
                    d_or_c.add(dorc);
                    date.add(date_of_trans);
                    String temp = cat_name+"            "+amt+" ";
                    Log.i("trans",temp);
                    listTrans.add(temp);
                }
                Collections.reverse(listTrans);
                Collections.reverse(category);
                Collections.reverse(amount);
                Collections.reverse(desc);
                Collections.reverse(mode);
                Collections.reverse(date);
                Collections.reverse(d_or_c);

                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listTrans);
                lv.setAdapter(adapter);
            }
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =  new Intent(getActivity(),TransDetails.class);
                Bundle b = new Bundle();
                b.putString("category",category.get(i));
                b.putString("amount",amount.get(i));
                b.putString("desc",desc.get(i));
                b.putString("mode",mode.get(i));
                b.putString("d_or_c",d_or_c.get(i));
                b.putString("date",date.get(i));
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
}
