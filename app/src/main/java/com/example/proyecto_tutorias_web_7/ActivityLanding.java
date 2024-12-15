package com.example.proyecto_tutorias_web_7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityLanding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Button btnRegistro = findViewById(R.id.btn_registro);
        Button btnLogin = findViewById(R.id.btn_login);

        public void irAlRegistro(View view) {
            Intent intentRegistro = new Intent(ActivityLanding.this, ActivityRegistro.class);
            startActivity(intentRegistro);
        }

        public void irAlLogin(View view) {
            Intent intentLogin = new Intent(ActivityLanding.this, ActivityLogin.class);
            startActivity(intentLogin);
        }

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegistro = new Intent(ActivityLanding.this, ActivityRegistro.class);
                startActivity(intentRegistro);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(ActivityLanding.this, ActivityLogin.class);
                startActivity(intentLogin);
            }
        });
    }
}
