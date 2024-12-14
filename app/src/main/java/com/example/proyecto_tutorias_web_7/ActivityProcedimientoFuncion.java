package com.example.proyecto_tutorias_web_7;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
import controlador.AnalizadorJSON;


public class ActivityProcedimientoFuncion extends AppCompatActivity {

    private TextView textViewResultado;
    private Button buttonConsultar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedimientofuncion);

        textViewResultado = findViewById(R.id.textViewResultado);
        buttonConsultar = findViewById(R.id.buttonConsultar);

        buttonConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarDatos();
            }
        });

    }

    public void consultarDatos() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AnalizadorJSON analizador = new AnalizadorJSON();
                    String url = "http://192.168.0.17:8081/Semestre_Ago_Dic_2024/Proyecto_ProgramacionWeb/api_rest_android_escuela/api_mysql_procedimientofuncion.php";
                    String action = "totalAlumnosSistemas";
                    String carrera = "Ingeniería en Sistemas";

                    // Realizar la solicitud HTTP
                    JSONObject respuesta = analizador.peticionHTTPProcedimientoFuncion(url, "POST", action, carrera);

                    if (respuesta != null) {
                        Log.d("ResultadoHTTP", "Respuesta obtenida: " + respuesta.toString());
                    } else {
                        Log.d("ResultadoHTTP", "La respuesta es nula.");
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarResultado(respuesta);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Error", "Excepción en la consulta HTTP: " + e.getMessage());
                }
            }
        }).start();
    }

    private void mostrarResultado(JSONObject respuesta) {
        if (respuesta != null) {
            try {
                if (respuesta.has("total_alumnos")) {
                    String resultado = respuesta.getString("total_alumnos");
                    textViewResultado.setText("Total de alumnos en Ingeniería en Sistemas Computacionales: " + resultado);
                } else {
                    textViewResultado.setText("No se encontraron datos.");
                }
            } catch (JSONException e) {
                textViewResultado.setText("Error al procesar la respuesta: " + e.getMessage());
            }
        } else {
            textViewResultado.setText("No se recibió respuesta.");
        }
    }
}

