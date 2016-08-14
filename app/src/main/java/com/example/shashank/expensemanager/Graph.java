package com.example.shashank.expensemanager;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
//import com.github.mikephil.charting.utils.PercentFormatter;

//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shashank on 11-07-2016.
 */


public class Graph extends Fragment {


    ArrayList<String> categories;
    ArrayList<Float> amt;
    SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.graph, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
             WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        SQLiteDatabase db;
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sp.edit();
        String username = sp.getString("username", "guest");
        //categories = new ArrayList<String>();
        //amt = new ArrayList<Float>();
        SqliteHelper helper = new SqliteHelper(getActivity());

        //Toast.makeText(getActivity(), "No transactions to show ", Toast.LENGTH_LONG).show();


        String q2 = "select sum(t.amount) as amount,c.cat_name \n" +
                "from transactions t left join categories c on\n" +
                "t.cat_no = c.cat_no\n" +
                "where t.username = '" + username + "'\n" +
                "group by c.cat_name;";

        //String q = "Select t.amount,c.cat_name from transactions t left join categories c on t.cat_no = c.cat_no where username  = '"+username+"'";
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(q2, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No transactions to show ", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(getActivity(),cursor.getCount()+"",Toast.LENGTH_SHORT).show();
            amt = new ArrayList<>();
            categories = new ArrayList<>();
            while (cursor.moveToNext()) {
                String category = cursor.getString(cursor.getColumnIndex("cat_name"));
                //Toast.makeText(getActivity(),category,Toast.LENGTH_SHORT).show();
                if (category.equalsIgnoreCase("+Wallet") || category.equalsIgnoreCase("newuser"))
                    continue;
                Float amount = cursor.getFloat(cursor.getColumnIndex("amount"));
                categories.add(category);
                if (amount < 0)
                    amount *= -1;
                amt.add(amount);
            }
            PieChart pieChart = (PieChart) getActivity().findViewById(R.id.chart);
            ArrayList<Entry> entries = new ArrayList<>();
            for (int i = 0; i < amt.size(); i++) {
                entries.add(new Entry(amt.get(i), i));
            }
            ArrayList<String> labels = new ArrayList<String>();
            for (int i = 0; i < categories.size(); i++) {
                labels.add(categories.get(i));
            }
            PieDataSet dataset = new PieDataSet(entries, "Categories");
            //xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            //XAxis xAxis = new XAxis();
            //xAxis.setLabelsToSkip(9);
            //String l = xAxis.getLongestLabel();
            //Toast.makeText(getActivity(),l,Toast.LENGTH_SHORT).show();
            PieData data = new PieData(labels, dataset);
            //PieData data = new PieData();
            //data.addEntry(entries,0);
            data.setDataSet(dataset);
            pieChart.setData(data); //set data into chart
            pieChart.setDescription("Transaction chart");
            //dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                String mycol[] = {"#039be5", "#1de9b6", "#ffeb3b", "#f44336", "#ffccbc", "#6a1b9a", "#e0f7fa", "#76ff03", "#9e9e9e"};
                int[] colors = new int[9];
                for (int i = 0; i < colors.length; i++) {
                    colors[i] = Color.parseColor(mycol[i]);
                }
                colors[colors.length - 1] = Color.parseColor("#d7ccc8");
                dataset.setColors(colors);
                pieChart.animateY(1200);

        }
    }
}