package com.example.miplata;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupChart();
        setupBottomNav();
    }

    private void setupChart() {
        BarChart chart = findViewById(R.id.weeklyBarChart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 45f));
        entries.add(new BarEntry(1, 110f));
        entries.add(new BarEntry(2, 32f));
        entries.add(new BarEntry(3, 145f));
        entries.add(new BarEntry(4, 88f));
        entries.add(new BarEntry(5, 54f));
        entries.add(new BarEntry(6, 20f));

        BarDataSet dataSet = new BarDataSet(entries, "Gastos diarios");
        dataSet.setColor(Color.parseColor("#00B074"));
        dataSet.setDrawValues(false);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.6f);

        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String[] dias = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
                int index = (int) value;
                return index >= 0 && index < dias.length ? dias[index] : "";
            }
        });

        chart.animateY(600);
        chart.invalidate();
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_inicio);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inicio) {
                return true;
            } else if (id == R.id.nav_add) {
                startActivity(new Intent(this, NuevaTransaccionActivity.class));
                return false;
            } else if (id == R.id.nav_historial) {
                startActivity(new Intent(this, HistorialActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_recordatorios) {
                startActivity(new Intent(this, RecordatoriosActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_analisis) {
                startActivity(new Intent(this, AnalisisActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}