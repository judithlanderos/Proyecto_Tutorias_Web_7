package com.example.proyecto_tutorias_web_7;

import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.Toast;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStreamReader;
import android.util.Log;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;

public class ActivityVista extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlumnoAdapter alumnoAdapter;
    private List<Alumno> alumnoList = new ArrayList<>();
    private String url ="http://192.168.0.17:8081/Semestre_Ago_Dic_2024/Proyecto_ProgramacionWeb/api_rest_android_escuela/api_mysql_vista.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        alumnoAdapter = new AlumnoAdapter(alumnoList);
        recyclerView.setAdapter(alumnoAdapter);

        new ObtenerDatosAsyncTask().execute(url);
    }

    private class ObtenerDatosAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(false);

                // Leer la respuesta
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONArray jsonArray = new JSONArray(result); // Parsear la respuesta directamente a un JSONArray

                if (jsonArray.length() > 0) {
                    procesarDatos(jsonArray);
                } else {
                    Toast.makeText(ActivityVista.this, "No se encontraron registros", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ActivityVista.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
            }
        }

        private void procesarDatos(JSONArray jsonArray) {
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject alumno = jsonArray.getJSONObject(i);
                    String numControl = alumno.getString("numControl");
                    String nombre = alumno.getString("nombre");
                    String apellidoP = alumno.getString("apellidoP");
                    String apellidoM = alumno.getString("apellidoM");
                    String fechaNacimiento = alumno.getString("fecha_nacimiento");
                    String telefono = alumno.getString("telefono");
                    String email = alumno.getString("email");
                    String nombreCarrera = alumno.getString("nombre_carrera");

                    alumnoList.add(new Alumno(numControl, nombre, apellidoP, apellidoM, fechaNacimiento, telefono, email, nombreCarrera));
                }

                alumnoAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ActivityVista.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class Alumno {
        private String numControl;
        private String nombre;
        private String apellidoP;
        private String apellidoM;
        private String fechaNacimiento;
        private String telefono;
        private String email;
        private String nombreCarrera;

        public Alumno(String numControl, String nombre, String apellidoP, String apellidoM,
                      String fechaNacimiento, String telefono, String email, String nombreCarrera) {
            this.numControl = numControl;
            this.nombre = nombre;
            this.apellidoP = apellidoP;
            this.apellidoM = apellidoM;
            this.fechaNacimiento = fechaNacimiento;
            this.telefono = telefono;
            this.email = email;
            this.nombreCarrera = nombreCarrera;
        }

        // Getters
        public String getNumControl() {
            return numControl;
        }

        public String getNombre() {
            return nombre;
        }

        public String getApellidoP() {
            return apellidoP;
        }

        public String getApellidoM() {
            return apellidoM;
        }

        public String getFechaNacimiento() {
            return fechaNacimiento;
        }

        public String getTelefono() {
            return telefono;
        }

        public String getEmail() {
            return email;
        }

        public String getNombreCarrera() {
            return nombreCarrera;
        }
    }

    // Adaptador para el RecyclerView
    public class AlumnoAdapter extends RecyclerView.Adapter<AlumnoAdapter.AlumnoViewHolder> {

        private List<Alumno> alumnoList;

        public AlumnoAdapter(List<Alumno> alumnoList) {
            this.alumnoList = alumnoList;
        }

        @Override
        public AlumnoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alumno, parent, false);
            return new AlumnoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AlumnoViewHolder holder, int position) {
            Alumno alumno = alumnoList.get(position);

            holder.numControlTextView.setText("Num Control: " + alumno.getNumControl());
            holder.nombreTextView.setText("Nombre: " + alumno.getNombre());
            holder.apellidoPTextView.setText("Apellido P: " + alumno.getApellidoP());
            holder.apellidoMTextView.setText("Apellido M: " + alumno.getApellidoM());
            holder.fechaNacimientoTextView.setText("Fecha Nacimiento: " + alumno.getFechaNacimiento());
            holder.telefonoTextView.setText("Telefono: " + alumno.getTelefono());
            holder.emailTextView.setText("Email: " + alumno.getEmail());
            holder.carreraTextView.setText("Carrera: " + alumno.getNombreCarrera());
        }

        @Override
        public int getItemCount() {
            return alumnoList.size();
        }

        public class AlumnoViewHolder extends RecyclerView.ViewHolder {
            TextView numControlTextView;
            TextView nombreTextView;
            TextView apellidoPTextView;
            TextView apellidoMTextView;
            TextView fechaNacimientoTextView;
            TextView telefonoTextView;
            TextView emailTextView;
            TextView carreraTextView;

            public AlumnoViewHolder(View itemView) {
                super(itemView);

                numControlTextView = itemView.findViewById(R.id.tv_num_control);
                nombreTextView = itemView.findViewById(R.id.tv_nombre);
                apellidoPTextView = itemView.findViewById(R.id.tv_apellido_paterno);
                apellidoMTextView = itemView.findViewById(R.id.tv_apellido_materno);
                fechaNacimientoTextView = itemView.findViewById(R.id.tv_fecha_nacimiento);
                telefonoTextView = itemView.findViewById(R.id.tv_telefono);
                emailTextView = itemView.findViewById(R.id.tv_email);
                carreraTextView = itemView.findViewById(R.id.tv_carrera);
            }
        }
    }
}
