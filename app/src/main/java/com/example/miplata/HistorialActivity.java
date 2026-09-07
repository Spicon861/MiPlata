package com.example.miplata;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HistorialActivity extends AppCompatActivity {

    private LinearLayout ledgerContainer, filterChipsContainer;

    // Modelo simple de transacción
    private static class Trans {
        String nombre, sub1, sub2, monto, hora, tipo, icono, badge;
        Trans(String nombre, String sub1, String sub2, String monto, String hora, String tipo, String icono, String badge) {
            this.nombre = nombre; this.sub1 = sub1; this.sub2 = sub2;
            this.monto = monto; this.hora = hora; this.tipo = tipo; this.icono = icono; this.badge = badge;
        }
    }

    private static class Grupo {
        String label, count;
        List<Trans> items;
        Grupo(String label, String count, List<Trans> items) {
            this.label = label; this.count = count; this.items = items;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        ledgerContainer = findViewById(R.id.ledgerContainer);
        filterChipsContainer = findViewById(R.id.filterChipsContainer);

        findViewById(R.id.btnMesAnterior).setOnClickListener(v ->
                Toast.makeText(this, "Navegación de meses no disponible en esta versión", Toast.LENGTH_SHORT).show());
        findViewById(R.id.btnMesSiguiente).setOnClickListener(v ->
                Toast.makeText(this, "Navegación de meses no disponible en esta versión", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnFiltros).setOnClickListener(v ->
                Toast.makeText(this, "Filtros avanzados no disponibles en esta versión", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnExportar).setOnClickListener(v ->
                Toast.makeText(this, "Preparando descarga de reporte...", Toast.LENGTH_SHORT).show());

        renderFilterChips();
        renderLedger();
        setupBottomNav();
    }

    private void renderFilterChips() {
        String[] labels = {"Todos (24)", "Gastos (18)", "Ingresos (6)", "Categorías", "Métodos de pago"};
        filterChipsContainer.removeAllViews();

        for (int i = 0; i < labels.length; i++) {
            TextView chip = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 16, 0);
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

    private void renderLedger() {
        ledgerContainer.removeAllViews();

        List<Grupo> grupos = new ArrayList<>();

        List<Trans> hoy = new ArrayList<>();
        hoy.add(new Trans("Carulla Express", "Alimentación", "Tarjeta Débito", "-$185.000", "11:42 AM", "gasto", "ic_shopping_cart", null));
        hoy.add(new Trans("Transferencia Freelance", "Diseño UI/UX", "Nequi", "+$1.200.000", "09:15 AM", "ingreso", "ic_payments", null));
        hoy.add(new Trans("Café Juan Valdez", "Restaurantes", "Efectivo", "-$14.500", "08:04 AM", "gasto", "ic_local_cafe", null));
        grupos.add(new Grupo("HOY, 16 DE OCTUBRE", "3 movimientos", hoy));

        List<Trans> ayer = new ArrayList<>();
        ayer.add(new Trans("Nómina Quincenal", "Empresa SAS", "Bancolombia", "+$3.650.000", "06:00 PM", "ingreso", "ic_account_balance", null));
        ayer.add(new Trans("Estación Terpel", "Transporte", "Tarjeta Crédito", "-$80.000", "02:30 PM", "gasto", "ic_local_gas_station", null));
        ayer.add(new Trans("Drogas La Rebaja", "Salud y Bienestar", "Efectivo", "-$32.400", "10:18 AM", "gasto", "ic_medication", null));
        grupos.add(new Grupo("AYER, 15 DE OCTUBRE", "3 movimientos", ayer));

        List<Trans> dia12 = new ArrayList<>();
        dia12.add(new Trans("Factura Acueducto", "Servicios Públicos", "PSE Débito", "-$85.000", "04:12 PM", "gasto", "ic_water_drop", "Pagado"));
        dia12.add(new Trans("Spotify Dúo", "Entretenimiento", "Automático", "-$24.900", "01:00 AM", "gasto", "ic_music_note", null));
        grupos.add(new Grupo("12 DE OCTUBRE", "2 movimientos", dia12));

        for (Grupo grupo : grupos) {
            ledgerContainer.addView(buildGroupHeader(grupo.label, grupo.count));
            ledgerContainer.addView(buildGroupCard(grupo.items));
        }
    }

    private LinearLayout buildGroupHeader(String label, String count) {
        LinearLayout row = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 24;
        params.bottomMargin = 8;
        row.setLayoutParams(params);
        row.setOrientation(LinearLayout.HORIZONTAL);

        TextView tvLabel = new TextView(this);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        tvLabel.setLayoutParams(labelParams);
        tvLabel.setText(label);
        tvLabel.setTextSize(12);
        tvLabel.setTypeface(null, android.graphics.Typeface.BOLD);
        tvLabel.setTextColor(getColor(R.color.ink_secondary));

        TextView tvCount = new TextView(this);
        tvCount.setText(count);
        tvCount.setTextSize(12);
        tvCount.setTextColor(getColor(R.color.ink_secondary));

        row.addView(tvLabel);
        row.addView(tvCount);
        return row;
    }

    private LinearLayout buildGroupCard(List<Trans> items) {
        LinearLayout card = new LinearLayout(this);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.bg_card_white);
        card.setPadding(12, 8, 12, 8);

        for (Trans t : items) {
            card.addView(buildTransactionRow(t));
        }
        return card;
    }

    private LinearLayout buildTransactionRow(Trans t) {
        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(8, 16, 8, 16);

        boolean esGasto = t.tipo.equals("gasto");

        ImageView icon = new ImageView(this);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(80, 80);
        iconParams.setMarginEnd(24);
        icon.setLayoutParams(iconParams);
        icon.setBackgroundResource(R.drawable.bg_circle);
        icon.setBackgroundTintList(getColorStateList(esGasto ? R.color.red_bg : R.color.brand_light));
        icon.setPadding(18, 18, 18, 18);
        int resId = getResources().getIdentifier(t.icono, "drawable", getPackageName());
        if (resId != 0) icon.setImageResource(resId);
        icon.setColorFilter(getColor(esGasto ? R.color.tertiary_red : R.color.brand_primary));

        LinearLayout infoCol = new LinearLayout(this);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        infoCol.setLayoutParams(infoParams);
        infoCol.setOrientation(LinearLayout.VERTICAL);

        LinearLayout nombreRow = new LinearLayout(this);
        nombreRow.setOrientation(LinearLayout.HORIZONTAL);
        nombreRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView tvNombre = new TextView(this);
        tvNombre.setText(t.nombre);
        tvNombre.setTextSize(15);
        tvNombre.setTypeface(null, android.graphics.Typeface.BOLD);
        tvNombre.setTextColor(getColor(R.color.ink_primary));
        nombreRow.addView(tvNombre);

        if (t.badge != null) {
            TextView badge = new TextView(this);
            LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            badgeParams.setMargins(12, 0, 0, 0);
            badge.setLayoutParams(badgeParams);
            badge.setText(t.badge);
            badge.setTextSize(10);
            badge.setPadding(10, 4, 10, 4);
            badge.setBackgroundResource(R.drawable.bg_badge_green);
            badge.setTextColor(getColor(R.color.brand_primary));
            nombreRow.addView(badge);
        }

        TextView tvSub = new TextView(this);
        tvSub.setText(t.sub1 + "  •  " + t.sub2);
        tvSub.setTextSize(12);
        tvSub.setTextColor(getColor(R.color.ink_secondary));

        infoCol.addView(nombreRow);
        infoCol.addView(tvSub);

        LinearLayout montoCol = new LinearLayout(this);
        montoCol.setOrientation(LinearLayout.VERTICAL);
        montoCol.setGravity(Gravity.END);

        TextView tvMonto = new TextView(this);
        tvMonto.setText(t.monto);
        tvMonto.setTextSize(15);
        tvMonto.setTypeface(null, android.graphics.Typeface.BOLD);
        tvMonto.setTextColor(getColor(esGasto ? R.color.tertiary_red : R.color.brand_primary));

        TextView tvHora = new TextView(this);
        tvHora.setText(t.hora);
        tvHora.setTextSize(12);
        tvHora.setTextColor(getColor(R.color.ink_secondary));

        montoCol.addView(tvMonto);
        montoCol.addView(tvHora);

        row.addView(icon);
        row.addView(infoCol);
        row.addView(montoCol);

        row.setOnClickListener(v ->
                Toast.makeText(this, "Detalle de transacción no disponible aún", Toast.LENGTH_SHORT).show());

        return row;
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_historial);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inicio) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_add) {
                startActivity(new Intent(this, NuevaTransaccionActivity.class));
                return false;
            } else if (id == R.id.nav_historial) {
                return true;
            } else if (id == R.id.nav_recordatorios) {
            startActivity(new Intent(this, RecordatoriosActivity.class));
               return true;
            } else if (id == R.id.nav_analisis) {
            startActivity(new Intent(this, AnalisisActivity.class));
            finish();
            return true;
        }
            // nav_analisis y nav_recordatorios: pendientes de construir
            return false;
        });
    }
}