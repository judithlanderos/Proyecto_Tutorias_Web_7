package com.example.proyecto_tutorias_web_7;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import controlador.AnalizadorJSON;
import android.widget.Toast;
import android.view.View;
import android.view.LayoutInflater;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;
import android.view.ViewGroup;

public class ActivityConsultas extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter1;
    private RecyclerView.LayoutManager layoutManager;
    private static ArrayList<String> registros = new ArrayList<>();
    private LinearLayout datosAlumnoContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        recyclerView = findViewById(R.id.recyclerview_alumnos);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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
                        adapter1 = new AdaptadorRegistros(registros);
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