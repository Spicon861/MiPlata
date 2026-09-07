package com.example.miplata;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class AnalisisActivity extends AppCompatActivity {

    private LinearLayout periodTabsContainer, legendContainer, ledgerContainer;

    private static class Categoria {
        String nombre, monto, porcentaje;
        float valor;
        int color;
        Categoria(String nombre, float valor, String monto, String porcentaje, int color) {
            this.nombre = nombre; this.valor = valor; this.monto = monto;
            this.porcentaje = porcentaje; this.color = color;
        }
    }

    private static class Trans {
        String nombre, sub, monto, hora, tipo, icono;
        Trans(String nombre, String sub, String monto, String hora, String tipo, String icono) {
            this.nombre = nombre; this.sub = sub; this.monto = monto;
            this.hora = hora; this.tipo = tipo; this.icono = icono;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analisis);

        periodTabsContainer = findViewById(R.id.periodTabsContainer);
        legendContainer = findViewById(R.id.legendContainer);
        ledgerContainer = findViewById(R.id.ledgerContainer);

        findViewById(R.id.btnExportar).setOnClickListener(v ->
                Toast.makeText(this, "Exportar no disponible en esta versión", Toast.LENGTH_SHORT).show());
        findViewById(R.id.btnFiltros).setOnClickListener(v ->
                Toast.makeText(this, "Filtros avanzados no disponibles", Toast.LENGTH_SHORT).show());

        renderPeriodTabs();
        setupDonutChart();
        renderLedger();
        setupBottomNav();
    }

    private void renderPeriodTabs() {
        String[] labels = {"Semana", "Mes", "Año", "Personalizado"};
        periodTabsContainer.removeAllViews();

        for (int i = 0; i < labels.length; i++) {
            TextView tab = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            tab.setLayoutParams(params);
            tab.setText(labels[i]);
            tab.setTextSize(12);
            tab.setGravity(Gravity.CENTER);
            tab.setPadding(0, 20, 0, 20);

            boolean selected = (i == 1); // "Mes" activo por defecto
            tab.setBackgroundResource(selected ? R.drawable.bg_chip_primary : android.R.color.transparent);
            tab.setTextColor(getColor(selected ? android.R.color.white : R.color.ink_secondary));
            if (selected) tab.setTypeface(null, android.graphics.Typeface.BOLD);

            tab.setOnClickListener(v -> {
                for (int j = 0; j < periodTabsContainer.getChildCount(); j++) {
                    TextView t = (TextView) periodTabsContainer.getChildAt(j);
                    t.setBackgroundResource(android.R.color.transparent);
                    t.setTextColor(getColor(R.color.ink_secondary));
                    t.setTypeface(null, android.graphics.Typeface.NORMAL);
                }
                tab.setBackgroundResource(R.drawable.bg_chip_primary);
                tab.setTextColor(getColor(android.R.color.white));
                tab.setTypeface(null, android.graphics.Typeface.BOLD);
            });

            periodTabsContainer.addView(tab);
        }
    }

    private void setupDonutChart() {
        PieChart chart = findViewById(R.id.donutChart);

        ArrayList<Categoria> categorias = new ArrayList<>();
        categorias.add(new Categoria("Alimentación & Super", 42f, "$609.00", "42% del total", Color.parseColor("#00B074")));
        categorias.add(new Categoria("Vivienda & Servicios", 25f, "$362.50", "25% del total", Color.parseColor("#565E74")));
        categorias.add(new Categoria("Transporte & Auto", 18f, "$261.00", "18% del total", Color.parseColor("#53DE9E")));
        categorias.add(new Categoria("Entretenimiento & Ocio", 15f, "$217.70", "15% del total", Color.parseColor("#FF6963")));

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        for (Categoria c : categorias) {
            entries.add(new PieEntry(c.valor));
            colors.add(c.color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        dataSet.setSliceSpace(2f);

        PieData data = new PieData(dataSet);
        chart.setData(data);

        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(65f);
        chart.setTransparentCircleRadius(0f);
        chart.setHoleColor(Color.WHITE);
        chart.setDrawEntryLabels(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setRotationEnabled(false);
        chart.setCenterText("Total Gastos\n$1,450.20");
        chart.setCenterTextSize(13f);
        chart.setCenterTextColor(Color.parseColor("#0F172A"));

        chart.animateY(700);
        chart.invalidate();

        renderLegend(categorias);
    }

    private void renderLegend(ArrayList<Categoria> categorias) {
        legendContainer.removeAllViews();

        for (Categoria c : categorias) {
            LinearLayout row = new LinearLayout(this);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rowParams.bottomMargin = 8;
            row.setLayoutParams(rowParams);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setBackgroundResource(R.drawable.bg_pill_gray);
            row.setPadding(20, 16, 20, 16);

            View dot = new View(this);
            LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(24, 24);
            dotParams.setMarginEnd(20);
            dot.setLayoutParams(dotParams);
            dot.setBackgroundResource(R.drawable.bg_circle);
            dot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(c.color));

            LinearLayout infoCol = new LinearLayout(this);
            LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            infoCol.setLayoutParams(infoParams);
            infoCol.setOrientation(LinearLayout.VERTICAL);

            TextView tvNombre = new TextView(this);
            tvNombre.setText(c.nombre);
            tvNombre.setTextSize(13);
            tvNombre.setTextColor(getColor(R.color.ink_primary));

            TextView tvPorcentaje = new TextView(this);
            tvPorcentaje.setText(c.porcentaje);
            tvPorcentaje.setTextSize(11);
            tvPorcentaje.setTextColor(getColor(R.color.ink_secondary));

            infoCol.addView(tvNombre);
            infoCol.addView(tvPorcentaje);

            TextView tvMonto = new TextView(this);
            tvMonto.setText(c.monto);
            tvMonto.setTextSize(13);
            tvMonto.setTypeface(null, android.graphics.Typeface.BOLD);
            tvMonto.setTextColor(getColor(R.color.ink_primary));

            row.addView(dot);
            row.addView(infoCol);
            row.addView(tvMonto);

            legendContainer.addView(row);
        }
    }

    private void renderLedger() {
        ledgerContainer.removeAllViews();

        ArrayList<Trans> items = new ArrayList<>();
        items.add(new Trans("Supermercado Metro", "Alimentación • Tarjeta Débito", "-$45.20", "14:22", "gasto", "ic_shopping_cart"));
        items.add(new Trans("Café Artesanal", "Entretenimiento • Apple Pay", "-$23.30", "09:15", "gasto", "ic_local_cafe"));
        items.add(new Trans("Transferencia Freelance", "Ingreso • Cuenta Ahorros", "+$850.00", "16:40", "ingreso", "ic_payments"));
        items.add(new Trans("Gasolinera Primax", "Transporte • Tarjeta Crédito", "-$42.00", "11:05", "gasto", "ic_gas_station"));
        items.add(new Trans("Pago de Electricidad y Luz", "Vivienda • Transferencia", "-$95.00", "10:18", "gasto", "ic_home"));
        items.add(new Trans("Cine & Concierto", "Entretenimiento • Débito", "-$80.00", "20:45", "gasto", "ic_movie"));

        for (Trans t : items) {
            ledgerContainer.addView(buildTransactionRow(t));
        }
    }

    private LinearLayout buildTransactionRow(Trans t) {
        LinearLayout row = new LinearLayout(this);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rowParams.bottomMargin = 10;
        row.setLayoutParams(rowParams);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setBackgroundResource(R.drawable.bg_card_white);
        row.setPadding(24, 20, 24, 20);
        row.setElevation(1f);

        boolean esGasto = t.tipo.equals("gasto");

        ImageView icon = new ImageView(this);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(76, 76);
        iconParams.setMarginEnd(20);
        icon.setLayoutParams(iconParams);
        icon.setBackgroundResource(R.drawable.bg_circle);
        icon.setBackgroundTintList(getColorStateList(R.color.surface_container_high));
        icon.setPadding(16, 16, 16, 16);
        int resId = getResources().getIdentifier(t.icono, "drawable", getPackageName());
        if (resId != 0) icon.setImageResource(resId);
        icon.setColorFilter(getColor(R.color.ink_primary));

        LinearLayout infoCol = new LinearLayout(this);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        infoCol.setLayoutParams(infoParams);
        infoCol.setOrientation(LinearLayout.VERTICAL);

        TextView tvNombre = new TextView(this);
        tvNombre.setText(t.nombre);
        tvNombre.setTextSize(13);
        tvNombre.setTypeface(null, android.graphics.Typeface.BOLD);
        tvNombre.setTextColor(getColor(R.color.ink_primary));

        TextView tvSub = new TextView(this);
        tvSub.setText(t.sub);
        tvSub.setTextSize(11);
        tvSub.setTextColor(getColor(R.color.ink_secondary));

        infoCol.addView(tvNombre);
        infoCol.addView(tvSub);

        LinearLayout montoCol = new LinearLayout(this);
        montoCol.setOrientation(LinearLayout.VERTICAL);
        montoCol.setGravity(Gravity.END);

        TextView tvMonto = new TextView(this);
        tvMonto.setText(t.monto);
        tvMonto.setTextSize(13);
        tvMonto.setTypeface(null, android.graphics.Typeface.BOLD);
        tvMonto.setTextColor(getColor(esGasto ? R.color.ink_primary : R.color.brand_primary));

        TextView tvHora = new TextView(this);
        tvHora.setText(t.hora);
        tvHora.setTextSize(11);
        tvHora.setTextColor(getColor(R.color.ink_secondary));

        montoCol.addView(tvMonto);
        montoCol.addView(tvHora);

        row.addView(icon);
        row.addView(infoCol);
        row.addView(montoCol);

        return row;
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_analisis);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inicio) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_historial) {
                startActivity(new Intent(this, HistorialActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_add) {
                startActivity(new Intent(this, NuevaTransaccionActivity.class));
                return false;
            } else if (id == R.id.nav_analisis) {
                return true;
            } else if (id == R.id.nav_recordatorios) {
                startActivity(new Intent(this, RecordatoriosActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
