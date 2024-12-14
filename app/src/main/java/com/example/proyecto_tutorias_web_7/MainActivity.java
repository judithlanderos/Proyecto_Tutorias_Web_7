package com.example.proyecto_tutorias_web_7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Métodos para los botones
    public void irAAltas(View view) {
        Intent intent = new Intent(this, ActivityAltas.class);
        startActivity(intent);
    }
    public void irABajas(View view) {
        Intent intent = new Intent(this, ActivityBajas.class);
        startActivity(intent);
    }

    public void irACambios(View view) {
        Intent intent = new Intent(this, ActivityCambios.class);
        startActivity(intent);
    }

    public void irAConsultas(View view) {
        Intent intent = new Intent(this, ActivityConsultas.class);
        startActivity(intent);
    }

    public void irATrigger(View view) {
        Intent intent = new Intent(this, ActivityTrigger.class);
        startActivity(intent);
    }
    public void irAVista(View view) {
        Intent intent = new Intent(this, ActivityVista.class);
        startActivity(intent);
    }
    public void irAProFun(View view) {
        Intent intent = new Intent(this, ActivityProcedimientoFuncion.class);
        startActivity(intent);

    }public void irALogin(View view) {
        Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
    }
    public void irARegistro(View view) {
        Intent intent = new Intent(this, ActivityRegistro.class);
        startActivity(intent);
    }
    public void cerrarSesion(View view) {
        Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
        finish();
    }


}
