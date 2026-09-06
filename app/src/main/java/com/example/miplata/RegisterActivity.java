package com.example.miplata;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etRegisterEmail, etRegisterPassword, etConfirmPassword;
    private LinearLayout btnCreateAccount;
    private TextView tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        btnCreateAccount.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etRegisterEmail.getText().toString().trim();
            String password = etRegisterPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (TextUtils.isEmpty(name)) {
                etName.setError("Ingresa tu nombre");
                etName.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etRegisterEmail.setError("Ingresa un correo válido");
                etRegisterEmail.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password) || password.length() < 6) {
                etRegisterPassword.setError("Mínimo 6 caracteres");
                etRegisterPassword.requestFocus();
                return;
            }

            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError("Las contraseñas no coinciden");
                etConfirmPassword.requestFocus();
                return;
            }

            // TODO: aquí luego conectamos el registro real (backend / Firebase / etc.)
            Toast.makeText(this, "Cuenta creada. Ahora inicia sesión.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        tvGoToLogin.setOnClickListener(v -> finish());
    }
}