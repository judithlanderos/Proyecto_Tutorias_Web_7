package com.example.proyecto_tutorias_web_7;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityRegistro extends Activity {
    private EditText edtNombre, edtEmail, edtPassword;
    private Button btnRegistrar;
    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        edtNombre = findViewById(R.id.nombreUsuarioEditText);
        edtEmail = findViewById(R.id.emailEditText);
        edtPassword = findViewById(R.id.passwordEditText);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        loginTextView = findViewById(R.id.loginTextView);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = edtNombre.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if (!nombre.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registrarUsuario(nombre, email, password);
                } else {
                    Toast.makeText(ActivityRegistro.this, "Por favor, rellene todos los campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la actividad de login
                Intent intent = new Intent(ActivityRegistro.this, ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registrarUsuario(String nombre, String email, String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String direccionURL = "http://192.168.0.17:8081/Semestre_Ago_Dic_2024/Proyecto_ProgramacionWeb/api_rest_android_usuarios/api_mysql_registro.php";
                    URL url = new URL(direccionURL);
                    HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

                    conexion.setRequestMethod("POST");
                    conexion.setDoOutput(true);
                    conexion.setDoInput(true);
                    conexion.setRequestProperty("Content-Type", "application/json");

                    JSONObject jsonRequest = new JSONObject();
                    jsonRequest.put("Nombre_Usuario_sha1", nombre);
                    jsonRequest.put("password_sha1", password);
                    jsonRequest.put("email", email);

                    OutputStream os = conexion.getOutputStream();
                    os.write(jsonRequest.toString().getBytes());
                    os.flush();
                    os.close();

                    BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    Log.d("API_Response", response.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ActivityRegistro.this, "Registro exitoso.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ActivityRegistro.this, "Error al registrar.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}