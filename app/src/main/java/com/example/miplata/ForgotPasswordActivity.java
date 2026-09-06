package com.example.miplata;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etRecoveryEmail;
    private LinearLayout btnSendRecovery;
    private TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etRecoveryEmail = findViewById(R.id.etRecoveryEmail);
        btnSendRecovery = findViewById(R.id.btnSendRecovery);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        btnSendRecovery.setOnClickListener(v -> {
            String email = etRecoveryEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etRecoveryEmail.setError("Ingresa un correo válido");
                etRecoveryEmail.requestFocus();
                return;
            }

            // TODO: aquí luego conectamos el envío real del correo (backend / Firebase / etc.)
            Toast.makeText(this, "Si el correo existe, te enviamos un enlace.", Toast.LENGTH_SHORT).show();
            finish();
        });

        tvBackToLogin.setOnClickListener(v -> finish());
    }
}