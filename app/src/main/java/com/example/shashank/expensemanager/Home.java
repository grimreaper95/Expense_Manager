package com.example.shashank.expensemanager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by Shashank on 08-07-2016.
 */
class Categories{
    String name;
    ImageView iv;
    Categories(String name){
        this.name = name;
    }
}
public class Home extends Fragment {
    SQLiteDatabase db;
    List<Categories>list;
    GridView categories;
    TextView tvdate,curbal;
    SqliteHelper helper;
    SharedPreferences sp;
    String username;
    float wallet = 0 ;
    String names[]={"Food","Travel","Stationary","Bills","Entertainment","Groceries","Electronics","Clothes","Others"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home,container,false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = sp.edit();
        //getting username from shared_preferences
        username = sp.getString("username","Guest");
        //Setting today's date
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        tvdate = (TextView)getActivity().findViewById(R.id.tvdate);
        tvdate.setText(date);
        curbal = (TextView)getActivity().findViewById(R.id.tvcurbal);
        /*
            Getting the current amount in user's wallet
        */
        helper = new SqliteHelper(getActivity());
        db = helper.getReadableDatabase();
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
        curbal.setText("Current Balance : Rs. " + wallet);
        list = new ArrayList<Categories>();
        //finding and setting the grid_view
        categories = (GridView)getActivity().findViewById(R.id.gridView);
        for(int i = 0 ; i < 9 ;i++){
            list.add(new Categories(names[i]));
        }
        Myadapter myadapter = new Myadapter(getActivity(),list);
        categories.setAdapter(myadapter);
        categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(),Cat0.class);
                intent.putExtra("catName",names[i]);
                intent.putExtra("catNo",i);
                startActivity(intent);
            }
        });
    }
    public void refresh(boolean backgrnd_frag_is_home){
        Cursor cursor = helper.getReadableDatabase().rawQuery("select sum(amount) as amt from transactions where username = '"+ username+"'",null);
        if(cursor != null && cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
                wallet = cursor.getFloat(cursor.getColumnIndex("amt"));
            }
        }
        else{
            wallet = 0.0F;
        }
        final SharedPreferences.Editor editor2 = sp.edit();
        editor2.putFloat("wallet",wallet);
        editor2.apply();
        //Add these lines when current navigation drawer's selected item is home, otherwise not
        if(backgrnd_frag_is_home){
            curbal = (TextView)getActivity().findViewById(R.id.tvcurbal);
            curbal.setText("Current Balance : Rs. " + wallet);
        }
    }
    /*public void refresh2(){
        curbal = (TextView)getActivity().findViewById(R.id.tvcurbal);
        curbal.setText("Current Balance : Rs. " + wallet);
    }*/
    class Myadapter extends BaseAdapter {
        Context c;
        List<Categories> list;
        Myadapter(Context c,List<Categories>list){
            this.c =c ;
            this.list = list;
        }
        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public Categories getItem(int i) {
            return list.get(i);
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View cell = null;
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            cell = inflater.inflate(R.layout.gridcell,viewGroup,false);
            TextView tvcatname = (TextView)cell.findViewById(R.id.tvcategoryname);
            Categories cat = getItem(i);
            tvcatname.setText(cat.name);
            return cell;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        refresh(true);
    }
}
