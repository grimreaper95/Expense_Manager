package com.example.shashank.expensemanager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by Shashank on 18-07-2016.
 */
public class Piedemo extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piegraph);
        PieChart pieChart = (PieChart) findViewById(R.id.chart);
        // creating data values
        ArrayList<Entry> entries = new ArrayList<>();
        /*entries.add(new PieEntry(4f, 0));
        entries.add(new PieEntry(8f, 1));
        entries.add(new PieEntry(6f, 2));
        entries.add(new PieEntry(12f, 3));
        entries.add(new PieEntry(18f, 4));
        entries.add(new PieEntry(9f, 5));*/
        PieDataSet dataset = new PieDataSet(entries,"Categories");
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        PieData data = new PieData(); // initialize Piedata
        pieChart.setData(data); //set data into chart
        pieChart.setDescription("Description");
        dataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieChart.animateY(1200);
    }
}
