package com.example.proyecto_tutorias_web_7;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.view.LayoutInflater;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.text.Editable;
import android.text.TextWatcher;
import controlador.AnalizadorJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityConsultas extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter1;
    private RecyclerView.LayoutManager layoutManager;
    private static ArrayList<String> registros = new ArrayList<>();
    private LinearLayout datosAlumnoContainer;
    private EditText editTextBuscar;
    private ArrayList<String> registrosFiltrados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        recyclerView = findViewById(R.id.recyclerview_alumnos);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        editTextBuscar = findViewById(R.id.editTextBuscar);

        registros.clear();

        new Thread(() -> {
            String url = "http://192.168.0.17:8081/Semestre_Ago_Dic_2024/Proyecto_ProgramacionWeb/api_rest_android_escuela/api_mysql_consultas.php";
            String metodo = "POST";

            AnalizadorJSON analizadorJSON = new AnalizadorJSON();
            String filtro = "";
            JSONObject jsonObject = analizadorJSON.peticionHTTPConsultas(url, metodo, filtro);
            Log.d("RespuestaServidor", jsonObject != null ? jsonObject.toString() : "Respuesta nula");

            if (jsonObject == null) {
                runOnUiThread(() -> Toast.makeText(ActivityConsultas.this, "Error al obtener datos del servidor", Toast.LENGTH_LONG).show());
                return;
            }

            try {
                if (jsonObject.has("consulta") && jsonObject.getString("consulta").equals("exito")) {
                    JSONArray alumnosArray = jsonObject.getJSONArray("alumnos");

                    for (int i = 0; i < alumnosArray.length(); i++) {
                        JSONObject alumno = alumnosArray.getJSONObject(i);
                        String cadena = alumno.getString("nc") + " " +
                                alumno.getString("n") + " " +
                                alumno.getString("primer_ap") + " " +
                                alumno.getString("segundo_ap") + " " +
                                alumno.getString("fecha_nacimiento") + " " +
                                alumno.getString("telefono") + " " +
                                alumno.getString("email") + " " +
                                alumno.getString("carrera_id");

                        registros.add(cadena);
                    }

                    runOnUiThread(() -> {
                        registrosFiltrados.addAll(registros);
                        adapter1 = new AdaptadorRegistros(registrosFiltrados);
                        recyclerView.setAdapter(adapter1);
                    });

                } else {
                    runOnUiThread(() -> Toast.makeText(ActivityConsultas.this, "No se encontraron registros", Toast.LENGTH_LONG).show());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(ActivityConsultas.this, "Error al procesar JSON", Toast.LENGTH_LONG).show());
            }
        }).start();

        editTextBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String textoBusqueda = charSequence.toString().toLowerCase();
                if (!textoBusqueda.isEmpty()) {
                    ArrayList<String> registrosFiltrados = new ArrayList<>();
                    for (String registro : registros) {
                        if (registro.toLowerCase().contains(textoBusqueda)) {
                            registrosFiltrados.add(registro);
                        }
                    }
                    actualizarRecyclerView(registrosFiltrados);
                } else {
                    actualizarRecyclerView(registros);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void actualizarRecyclerView(ArrayList<String> datosFiltrados) {
        runOnUiThread(() -> {
            adapter1 = new AdaptadorRegistros(datosFiltrados);
            recyclerView.setAdapter(adapter1);
        });
    }

    class AdaptadorRegistros extends RecyclerView.Adapter<AdaptadorRegistros.MyViewHolder> {
        private List<String> datos;

        public AdaptadorRegistros(List<String> datos) {
            this.datos = datos;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_alumno, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String[] datosAlumno = datos.get(position).split(" ");

            holder.tvNumControl.setText("No. Control: " + datosAlumno[0]);
            holder.tvNombre.setText("Nombre: " + datosAlumno[1] + " " + datosAlumno[2] + " " + datosAlumno[3]);
            holder.tvFechaNacimiento.setText("Fecha Nacimiento: " + datosAlumno[4]);
            holder.tvTelefono.setText("Teléfono: " + datosAlumno[5]);
            holder.tvEmail.setText("Email: " + datosAlumno[6]);
            holder.tvCarrera.setText("Carrera: " + datosAlumno[7]);


            holder.itemView.setOnLongClickListener(v -> {
                String numControl = datosAlumno[0];

                new AlertDialog.Builder(v.getContext())
                        .setTitle("Eliminar Registro")
                        .setMessage("¿Estás seguro de que deseas eliminar este registro?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            new Thread(() -> {
                                try {
                                    String url = "http://192.168.0.17:8081/Semestre_Ago_Dic_2024/Proyecto_ProgramacionWeb/api_rest_android_escuela/api_mysql_bajas.php";
                                    JSONObject jsonBaja = new JSONObject();
                                    jsonBaja.put("nc", numControl);

                                    HttpURLConnection conexion = (HttpURLConnection) new URL(url).openConnection();
                                    conexion.setRequestMethod("POST");
                                    conexion.setRequestProperty("Content-Type", "application/json");
                                    conexion.setDoOutput(true);

                                    try (OutputStreamWriter out = new OutputStreamWriter(conexion.getOutputStream())) {
                                        out.write(jsonBaja.toString());
                                    }

                                    int codigoRespuesta = conexion.getResponseCode();
                                    StringBuilder respuesta = new StringBuilder();

                                    try (BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()))) {
                                        String linea;
                                        while ((linea = in.readLine()) != null) {
                                            respuesta.append(linea);
                                        }
                                    }

                                    runOnUiThread(() -> {
                                        try {
                                            JSONObject jsonRespuesta = new JSONObject(respuesta.toString());
                                            if ("exito".equals(jsonRespuesta.getString("baja"))) {
                                                datos.remove(position);
                                                registros.remove(position);

                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position, datos.size());

                                                Toast.makeText(ActivityConsultas.this,
                                                        jsonRespuesta.getString("mensaje"),
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(ActivityConsultas.this,
                                                        jsonRespuesta.getString("mensaje"),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(ActivityConsultas.this,
                                                    "Error al procesar la respuesta",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    runOnUiThread(() ->
                                            Toast.makeText(ActivityConsultas.this,
                                                    "Error al eliminar: " + e.getMessage(),
                                                    Toast.LENGTH_SHORT).show()
                                    );
                                }
                            }).start();
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            });
        }

        @Override
        public int getItemCount() {
            return datos.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvNumControl, tvNombre, tvFechaNacimiento,
                    tvTelefono, tvEmail, tvCarrera;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvNumControl = itemView.findViewById(R.id.tv_num_control);
                tvNombre = itemView.findViewById(R.id.tv_nombre);
                tvFechaNacimiento = itemView.findViewById(R.id.tv_fecha_nacimiento);
                tvTelefono = itemView.findViewById(R.id.tv_telefono);
                tvEmail = itemView.findViewById(R.id.tv_email);
                tvCarrera = itemView.findViewById(R.id.tv_carrera);
            }
        }
    }
}
