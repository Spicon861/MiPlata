package com.example.miplata;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NuevaTransaccionActivity extends AppCompatActivity {

    private LinearLayout tabGasto, tabIngreso, btnGuardar;
    private ImageView ivTabGasto, ivTabIngreso;
    private TextView tvTabGasto, tvTabIngreso, tvToolbarTitle, tvCurrencySymbol,
            tvAmountChip, tvCuentaLabel, tvBtnGuardar;
    private EditText etAmount;
    private GridLayout categoryGrid;

    private boolean isGasto = true;
    private String currentCategory;

    // Categorías: {nombre, icono}
    private final String[][] categoriasGasto = {
            {"Alimentación", "ic_restaurant"},
            {"Transporte", "ic_directions_car"},
            {"Hogar & Serv.", "ic_home"},
            {"Salud", "ic_favorite"},
            {"Ocio & Viajes", "ic_movie"},
            {"Educación", "ic_school"}
    };

    private final String[][] categoriasIngreso = {
            {"Salario", "ic_payments"},
            {"Freelance", "ic_laptop_mac"},
            {"Remesa/Transf.", "ic_move_to_inbox"},
            {"Ventas/Negocio", "ic_storefront"},
            {"Inversiones", "ic_trending_up"},
            {"Otros Ingresos", "ic_savings"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_transaccion);

        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tabGasto = findViewById(R.id.tabGasto);
        tabIngreso = findViewById(R.id.tabIngreso);
        ivTabGasto = findViewById(R.id.ivTabGasto);
        ivTabIngreso = findViewById(R.id.ivTabIngreso);
        tvTabGasto = findViewById(R.id.tvTabGasto);
        tvTabIngreso = findViewById(R.id.tvTabIngreso);
        tvCurrencySymbol = findViewById(R.id.tvCurrencySymbol);
        tvAmountChip = findViewById(R.id.tvAmountChip);
        tvCuentaLabel = findViewById(R.id.tvCuentaLabel);
        etAmount = findViewById(R.id.etAmount);
        categoryGrid = findViewById(R.id.categoryGrid);
        btnGuardar = findViewById(R.id.btnGuardar);
        tvBtnGuardar = findViewById(R.id.tvBtnGuardar);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnClose).setOnClickListener(v -> finish());

        tabGasto.setOnClickListener(v -> switchType(true));
        tabIngreso.setOnClickListener(v -> switchType(false));

        findViewById(R.id.btnAdjuntar).setOnClickListener(v ->
                Toast.makeText(this, "Función de adjuntar no disponible en esta versión", Toast.LENGTH_SHORT).show());

        btnGuardar.setOnClickListener(v -> {
            String monto = etAmount.getText().toString().isEmpty() ? "0.00" : etAmount.getText().toString();
            String tipo = isGasto ? "Gasto" : "Ingreso";
            Toast.makeText(this,
                    "¡Registro guardado! " + tipo + " de $" + monto + " en " + currentCategory,
                    Toast.LENGTH_LONG).show();
        });

        renderCategories(categoriasGasto);
    }

    private void switchType(boolean gasto) {
        isGasto = gasto;

        int colorSelected = gasto
                ? getColor(R.color.tertiary_red)
                : getColor(R.color.brand_primary);
        int colorUnselected = getColor(R.color.ink_secondary);

        tabGasto.setBackgroundResource(gasto ? R.drawable.bg_segment_selected : 0);
        tabIngreso.setBackgroundResource(gasto ? 0 : R.drawable.bg_segment_selected);

        tvTabGasto.setTextColor(gasto ? colorSelected : colorUnselected);
        ivTabGasto.setColorFilter(gasto ? colorSelected : colorUnselected);
        tvTabIngreso.setTextColor(gasto ? colorUnselected : colorSelected);
        ivTabIngreso.setColorFilter(gasto ? colorUnselected : colorSelected);

        tvToolbarTitle.setText(gasto ? "Nuevo Gasto" : "Nuevo Ingreso");
        tvCurrencySymbol.setText(gasto ? "$" : "+$");
        tvCurrencySymbol.setTextColor(gasto ? getColor(R.color.ink_primary) : getColor(R.color.brand_primary));
        tvAmountChip.setText(gasto ? "⚡ Sin comisiones añadidas" : "＋ Suma directa a tu balance general");

        tvCuentaLabel.setText(gasto ? "Cuenta origen" : "Cuenta de destino");

        renderCategories(gasto ? categoriasGasto : categoriasIngreso);
    }

    private void renderCategories(String[][] categorias) {
        categoryGrid.removeAllViews();
        currentCategory = categorias[0][0];

        for (int i = 0; i < categorias.length; i++) {
            String nombre = categorias[i][0];
            String iconName = categorias[i][1];
            boolean selected = (i == 0);

            LinearLayout card = new LinearLayout(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(i % 3, 1f);
            params.rowSpec = GridLayout.spec(i / 3);
            params.setMargins(4, 4, 4, 4);
            card.setLayoutParams(params);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setGravity(Gravity.CENTER);
            card.setPadding(24, 24, 24, 24);
            card.setBackgroundResource(selected ? R.drawable.bg_card_selected : R.drawable.bg_card_white);
            card.setTag(nombre);

            ImageView icon = new ImageView(this);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(96, 96);
            iconParams.bottomMargin = 12;
            icon.setLayoutParams(iconParams);
            icon.setBackgroundResource(R.drawable.bg_circle);
            icon.setBackgroundTintList(getColorStateList(
                    selected ? R.color.brand_primary : R.color.surface_container));
            icon.setPadding(20, 20, 20, 20);
            int resId = getResources().getIdentifier(iconName, "drawable", getPackageName());
            if (resId != 0) icon.setImageResource(resId);
            icon.setColorFilter(getColor(selected ? android.R.color.white : R.color.ink_secondary));

            TextView label = new TextView(this);
            label.setText(nombre);
            label.setTextSize(12);
            label.setGravity(Gravity.CENTER);
            label.setTextColor(getColor(R.color.ink_primary));

            card.addView(icon);
            card.addView(label);

            card.setOnClickListener(v -> selectCategory(card, nombre));
            categoryGrid.addView(card);
        }
    }

    private void selectCategory(LinearLayout selectedCard, String nombre) {
        currentCategory = nombre;
        for (int i = 0; i < categoryGrid.getChildCount(); i++) {
            LinearLayout child = (LinearLayout) categoryGrid.getChildAt(i);
            boolean isSelected = child == selectedCard;
            child.setBackgroundResource(isSelected ? R.drawable.bg_card_selected : R.drawable.bg_card_white);
            ImageView icon = (ImageView) child.getChildAt(0);
            icon.setBackgroundTintList(getColorStateList(
                    isSelected ? R.color.brand_primary : R.color.surface_container));
            icon.setColorFilter(getColor(isSelected ? android.R.color.white : R.color.ink_secondary));
        }
    }
}