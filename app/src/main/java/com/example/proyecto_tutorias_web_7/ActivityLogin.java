package com.example.proyecto_tutorias_web_7;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import java.security.MessageDigest;
import java.util.ArrayList;

import controlador.AnalizadorJSON;

public class ActivityLogin extends AppCompatActivity {

    private EditText editTextUsuario, editTextPassword, editTextCorreo;
    private Button btnLogin;
    private TextView registerTextView;
    private AnalizadorJSON analizadorJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextCorreo = findViewById(R.id.editTextEmail);
        btnLogin = findViewById(R.id.btnLogin);
        registerTextView = findViewById(R.id.register);

        analizadorJSON = new AnalizadorJSON();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUsuario(view);
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivityRegistro.class);
                startActivity(intent);
            }
        });
    }

    public void loginUsuario(View view) {
        String usuario = editTextUsuario.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();

        if (usuario.isEmpty() || password.isEmpty() || correo.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        String usuarioCifrado = cifrarSHA1(usuario);
        String passwordCifrada = cifrarSHA1(password);

        new Thread(() -> {
            ArrayList<String> datos = new ArrayList<>();
            datos.add(usuarioCifrado); // Usuario cifrado una sola vez
            datos.add(passwordCifrada); // Contraseña cifrada una sola vez
            datos.add(correo);

            JSONObject respuesta = analizadorJSON.peticionHTTPLogin(
                    "http://192.168.0.17:8081/Semestre_Ago_Dic_2024/Proyecto_ProgramacionWeb/api_rest_android_usuarios/api_mysql_login.php",
                    "POST", datos);

            Log.d("Respuesta del servidor", respuesta != null ? respuesta.toString() : "Respuesta nula");

            runOnUiThread(() -> {
                if (respuesta != null) {
                    try {
                        String mensaje = respuesta.optString("mensaje");
                        String status = respuesta.optString("status");

                        if ("success".equals(status)) {
                            Toast.makeText(ActivityLogin.this, "Inicio de sesión correcto: " + mensaje, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ActivityLogin.this, mensaje, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(ActivityLogin.this, "Error procesando la respuesta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActivityLogin.this, "Error al obtener la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    public static String cifrarSHA1(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(texto.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al cifrar con SHA1", e);
        }
    }
}