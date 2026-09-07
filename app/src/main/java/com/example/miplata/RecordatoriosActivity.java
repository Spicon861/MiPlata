package com.example.miplata;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RecordatoriosActivity extends AppCompatActivity {

    private LinearLayout filterChipsContainer, vencenProntoContainer, proximasSemanasContainer, historialContent;
    private boolean historialVisible = false;

    private static class Recordatorio {
        String nombre, fecha, monto, icono, estado, subAccion;
        Recordatorio(String nombre, String fecha, String monto, String icono, String estado, String subAccion) {
            this.nombre = nombre; this.fecha = fecha; this.monto = monto;
            this.icono = icono; this.estado = estado; this.subAccion = subAccion;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorios);

        filterChipsContainer = findViewById(R.id.filterChipsContainer);
        vencenProntoContainer = findViewById(R.id.vencenProntoContainer);
        proximasSemanasContainer = findViewById(R.id.proximasSemanasContainer);
        historialContent = findViewById(R.id.historialContent);

        findViewById(R.id.btnPagarUrgente).setOnClickListener(v ->
                Toast.makeText(this, "Pago no disponible en esta versión", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnNuevoRecordatorio).setOnClickListener(v ->
                Toast.makeText(this, "Crear recordatorio no disponible aún", Toast.LENGTH_SHORT).show());

        findViewById(R.id.toggleHistorial).setOnClickListener(v -> toggleHistorial());

        renderFilterChips();
        renderVencenPronto();
        renderProximasSemanas();
        renderHistorial();
        setupBottomNav();
    }

    private void toggleHistorial() {
        historialVisible = !historialVisible;
        ImageView icon = findViewById(R.id.accordionIcon);
        if (historialVisible) {
            historialContent.setVisibility(View.VISIBLE);
            icon.setRotation(180f);
        } else {
            historialContent.setVisibility(View.GONE);
            icon.setRotation(0f);
        }
    }

    private void renderFilterChips() {
        String[] labels = {"Todos (5)", "Por vencer (2)", "Pagados (3)", "Recurrentes (4)"};
        filterChipsContainer.removeAllViews();

        for (int i = 0; i < labels.length; i++) {
            TextView chip = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd(16);
            chip.setLayoutParams(params);
            chip.setText(labels[i]);
            chip.setTextSize(13);
            chip.setPadding(36, 20, 36, 20);

            boolean selected = (i == 0);
            chip.setBackgroundResource(selected ? R.drawable.bg_chip_primary : R.drawable.bg_pill_gray);
            chip.setTextColor(getColor(selected ? android.R.color.white : R.color.ink_secondary));

            chip.setOnClickListener(v -> {
                for (int j = 0; j < filterChipsContainer.getChildCount(); j++) {
                    TextView c = (TextView) filterChipsContainer.getChildAt(j);
                    c.setBackgroundResource(R.drawable.bg_pill_gray);
                    c.setTextColor(getColor(R.color.ink_secondary));
                }
                chip.setBackgroundResource(R.drawable.bg_chip_primary);
                chip.setTextColor(getColor(android.R.color.white));
            });

            filterChipsContainer.addView(chip);
        }
    }

    private void renderVencenPronto() {
        vencenProntoContainer.removeAllViews();
        vencenProntoContainer.addView(buildReminderCard(
                "Luz y Energía · Enel", "18 de Octubre (en 2 días)", "$185.000",
                "ic_electric_bolt", "Por vencer", "Pagar ahora"));
        vencenProntoContainer.addView(buildReminderCard(
                "Internet Fibra · Claro", "22 de Octubre (en 6 días)", "$110.000",
                "ic_wifi", "Próximo", "Pagar"));
    }

    private void renderProximasSemanas() {
        proximasSemanasContainer.removeAllViews();
        proximasSemanasContainer.addView(buildReminderCard(
                "Arriendo Apartamento", "30 de Octubre (en 14 días)", "$950.000",
                "ic_apartment", "Fijo mensual", null));
        proximasSemanasContainer.addView(buildReminderCard(
                "Suscripciones (Netflix + Spotify)", "Vence 2 de Noviembre", "$45.000",
                "ic_subscriptions", "Débito automático", null));
    }

    private void renderHistorial() {
        historialContent.removeAllViews();
        historialContent.addView(buildReminderCard(
                "Acueducto y Alcantarillado", "Pagado el 12 de Octubre", "$85.000",
                "ic_water_drop", "Pagado", null));
    }

    private LinearLayout buildReminderCard(String nombre, String fecha, String monto,
                                           String iconName, String estado, String accion) {
        LinearLayout card = new LinearLayout(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.bottomMargin = 12;
        card.setLayoutParams(cardParams);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.bg_card_white);
        card.setPadding(28, 24, 28, 24);
        card.setElevation(2f);

        LinearLayout topRow = new LinearLayout(this);
        topRow.setOrientation(LinearLayout.HORIZONTAL);
        topRow.setGravity(Gravity.CENTER_VERTICAL);

        ImageView icon = new ImageView(this);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(80, 80);
        iconParams.setMarginEnd(20);
        icon.setLayoutParams(iconParams);
        icon.setBackgroundResource(R.drawable.bg_circle);
        icon.setBackgroundTintList(getColorStateList(R.color.brand_light));
        icon.setPadding(18, 18, 18, 18);
        int resId = getResources().getIdentifier(iconName, "drawable", getPackageName());
        if (resId != 0) icon.setImageResource(resId);
        icon.setColorFilter(getColor(R.color.brand_primary));

        LinearLayout infoCol = new LinearLayout(this);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        infoCol.setLayoutParams(infoParams);
        infoCol.setOrientation(LinearLayout.VERTICAL);

        TextView tvNombre = new TextView(this);
        tvNombre.setText(nombre);
        tvNombre.setTextSize(14);
        tvNombre.setTypeface(null, android.graphics.Typeface.BOLD);
        tvNombre.setTextColor(getColor(R.color.ink_primary));

        TextView tvFecha = new TextView(this);
        tvFecha.setText(fecha);
        tvFecha.setTextSize(12);
        tvFecha.setTextColor(getColor(R.color.ink_secondary));

        infoCol.addView(tvNombre);
        infoCol.addView(tvFecha);

        LinearLayout montoCol = new LinearLayout(this);
        montoCol.setOrientation(LinearLayout.VERTICAL);
        montoCol.setGravity(Gravity.END);

        TextView tvMonto = new TextView(this);
        tvMonto.setText(monto);
        tvMonto.setTextSize(14);
        tvMonto.setTypeface(null, android.graphics.Typeface.BOLD);
        tvMonto.setTextColor(getColor(R.color.ink_primary));

        TextView tvEstado = new TextView(this);
        tvEstado.setText(estado);
        tvEstado.setTextSize(10);
        tvEstado.setPadding(16, 4, 16, 4);
        tvEstado.setBackgroundResource(R.drawable.bg_badge_green);
        tvEstado.setTextColor(getColor(R.color.brand_primary));

        montoCol.addView(tvMonto);
        montoCol.addView(tvEstado);

        topRow.addView(icon);
        topRow.addView(infoCol);
        topRow.addView(montoCol);
        card.addView(topRow);

        if (accion != null) {
            TextView btnAccion = new TextView(this);
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            btnParams.topMargin = 20;
            btnParams.gravity = Gravity.END;
            btnAccion.setLayoutParams(btnParams);
            btnAccion.setText(accion);
            btnAccion.setTextSize(12);
            btnAccion.setTextColor(android.graphics.Color.WHITE);
            btnAccion.setPadding(32, 16, 32, 16);
            btnAccion.setBackgroundResource(R.drawable.bg_chip_primary);
            btnAccion.setOnClickListener(v ->
                    Toast.makeText(this, "Pago no disponible en esta versión", Toast.LENGTH_SHORT).show());
            card.addView(btnAccion);
        }

        return card;
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_recordatorios);
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
            } else if (id == R.id.nav_recordatorios) {
                return true;
            }  else if (id == R.id.nav_analisis) {
            startActivity(new Intent(this, AnalisisActivity.class));
            finish();
            return true;
        }
            return false;
        });
    }
}
