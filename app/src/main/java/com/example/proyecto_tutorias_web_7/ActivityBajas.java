package com.example.proyecto_tutorias_web_7;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_tutorias_web_7.modelo.Alumno;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import controlador.AnalizadorJSON;
import com.example.proyecto_tutorias_web_7.AlumnoAdapter;
import java.util.List;
import java.util.ArrayList;

public class ActivityBajas extends Activity {

    private EditText inputNumControl;
    private static ArrayList<Alumno> listaAlumnos = new ArrayList<>();
    private AlumnoAdapter alumnoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajas);

        inputNumControl = findViewById(R.id.input_num_control);

        RecyclerView recyclerView = findViewById(R.id.rv_listado_alumnos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        alumnoAdapter = new AlumnoAdapter(new ArrayList<>());
        recyclerView.setAdapter(alumnoAdapter);

        cargarListadoDeAlumnos();

        inputNumControl.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String textoBusqueda = charSequence.toString().toLowerCase();
                List<Alumno> alumnosFiltrados = new ArrayList<>();

                for (Alumno alumno : listaAlumnos) {
                    if (alumno.getNumControl().toLowerCase().contains(textoBusqueda)) {
                        alumnosFiltrados.add(alumno);
                    }
                }

                alumnoAdapter.updateData(alumnosFiltrados);
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {
            }
        });
    }

    private void cargarListadoDeAlumnos() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = cm.getActiveNetwork();

        if (network != null && cm.getNetworkCapabilities(cm.getActiveNetwork()) != null) {
            String URL = "http://192.168.0.17:8081/Semestre_Ago_Dic_2024/Proyecto_ProgramacionWeb/api_rest_android_escuela/api_mysql_consultas.php";
            String metodo = "POST";

            final String numeroControl = inputNumControl.getText().toString().trim().isEmpty() ? "%" : inputNumControl.getText().toString().trim();
            AnalizadorJSON analizadorJSON = new AnalizadorJSON();

            new Thread(() -> {
                try {
                    JSONObject jsonObject = analizadorJSON.peticionHTTPConsultas(URL, metodo, numeroControl);

                    Log.d("JSON Respuesta", jsonObject.toString());

                    if (jsonObject != null && jsonObject.getString("consulta").equals("exito")) {
                        JSONArray datos = jsonObject.getJSONArray("alumnos");

                        List<Alumno> alumnos = new ArrayList<>();
                        for (int i = 0; i < datos.length(); i++) {
                            JSONObject alumno = datos.getJSONObject(i);
                            alumnos.add(new Alumno(
                                    alumno.getString("nc"),
                                    alumno.getString("n"),
                                    alumno.getString("primer_ap"),
                                    alumno.getString("segundo_ap"),
                                    alumno.getString("fecha_nacimiento"),
                                    alumno.getString("telefono"),
                                    alumno.getString("email"),
                                    alumno.getString("carrera_id")
                            ));
                        }

                        listaAlumnos.clear();
                        listaAlumnos.addAll(alumnos);
                        runOnUiThread(() -> alumnoAdapter.updateData(alumnos));
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "No se encontraron registros", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        } else {
            Log.e("MSJ ->", "Error en la red (wifi)");
            Toast.makeText(this, "Error en la conexión a la red", Toast.LENGTH_SHORT).show();
        }
    }

    public void eliminarAlumno(View view) {
        final String numeroControl = inputNumControl.getText().toString().trim();

        if (numeroControl.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un número de control válido", Toast.LENGTH_SHORT).show();
            return;
        }

        String URL = "http://192.168.0.17:8081/Semestre_Ago_Dic_2024/Proyecto_ProgramacionWeb/api_rest_android_escuela/api_mysql_bajas.php";
        String metodo = "POST";
        AnalizadorJSON analizadorJSON = new AnalizadorJSON();

        new Thread(() -> {
            try {
                JSONObject jsonObject = analizadorJSON.peticionHTTPBaja(URL, metodo, numeroControl);

                Log.d("Respuesta JSON", jsonObject.toString());

                // Accede a la clave "consulta" y valida su valor
                String resultado = jsonObject.getString("consulta");
                if ("exito".equalsIgnoreCase(resultado)) {
                    runOnUiThread(() -> {
                        cargarListadoDeAlumnos();
                        Toast.makeText(this, "Alumno eliminado correctamente", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "No se encontró el alumno para eliminar", Toast.LENGTH_SHORT).show());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Log.d("Error JSON", "Error en la respuesta JSON: " + e.getMessage());
                    Toast.makeText(this, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Log.d("Error General", "Error en la conexión con el servidor: " + e.getMessage());
                    Toast.makeText(this, "Error en la conexión con el servidor", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}
