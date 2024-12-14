package com.example.proyecto_tutorias_web_7;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ActivityTrigger extends AppCompatActivity {

    private static final String URL_API = "http://192.168.0.17:8081/Semestre_Ago_Dic_2024/Proyecto_ProgramacionWeb/api_rest_android_escuela/api_mysql_trigger.php";
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<Historial> historialList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);

        recyclerView = findViewById(R.id.reciclador_historial);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar lista
        historialList = new ArrayList<>();

        consultarTriggers(URL_API, "POST");
    }

    public void consultarTriggers(String direccionURL, String metodo) {
        new AsyncTask<String, Void, JSONArray>() {

            @Override
            protected JSONArray doInBackground(String... params) {
                String url = params[0];
                String metodo = params[1];
                return peticionHTTPTriggers(url, metodo);
            }

            @Override
            protected void onPostExecute(JSONArray result) {
                super.onPostExecute(result);

                if (result != null) {
                    Log.d("Triggers", result.toString());

                    for (int i = 0; i < result.length(); i++) {
                        try {
                            JSONObject jsonObject = result.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String numControl = jsonObject.getString("numControl");
                            String columnaModificada = jsonObject.getString("columna_modificada");
                            String valorAnterior = jsonObject.getString("valor_anterior");
                            String valorNuevo = jsonObject.getString("valor_nuevo");
                            String fecha = jsonObject.getString("fecha");
                            String usuario = jsonObject.getString("usuario");

                            historialList.add(new Historial(id, numControl, columnaModificada, valorAnterior, valorNuevo, fecha, usuario));
                            Log.d("JSON Response", result.toString());

                        } catch (JSONException e) {
                            Log.e("JSONException", "Error al procesar el objeto JSON: " + e.getMessage());
                        }
                    }

                    myAdapter = new MyAdapter(historialList);
                    recyclerView.setAdapter(myAdapter);

                } else {
                    Log.e("Error", "No se pudo obtener la información de los triggers.");
                }
            }
        }.execute(direccionURL, metodo);
    }

    public JSONArray peticionHTTPTriggers(String direccionURL, String metodo) {
        HttpURLConnection conexion;
        BufferedInputStream is;
        JSONArray jsonArray = null;

        try {
            URL url = new URL(direccionURL);
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoOutput(true);
            conexion.setRequestMethod(metodo);
            conexion.setRequestProperty("Content-Type", "application/json");

            is = new BufferedInputStream(conexion.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder cadena = new StringBuilder();
            String fila;
            while ((fila = br.readLine()) != null) {
                cadena.append(fila + "\n");
            }
            is.close();

            try {
                jsonArray = new JSONArray(cadena.toString());
            } catch (JSONException e) {
                Log.e("JSONException", "Error al convertir la respuesta en JSONArray: " + e.getMessage());
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("URL mal formada: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Error de entrada/salida: " + e.getMessage());
        }

        return jsonArray;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<Historial> historialList;

        public MyAdapter(List<Historial> historialList) {
            this.historialList = historialList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Historial historial = historialList.get(position);
            holder.idRegistro.setText(String.valueOf(historial.getId()));
            holder.numControl.setText(historial.getNumControl());
            holder.columnaModificada.setText(historial.getColumnaModificada());
            holder.valorAnterior.setText(historial.getValorAnterior());
            holder.valorNuevo.setText(historial.getValorNuevo());
            holder.fecha.setText(historial.getFecha());
            holder.usuario.setText(historial.getUsuario());
        }

        @Override
        public int getItemCount() {
            return historialList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView idRegistro, numControl, columnaModificada, valorAnterior, valorNuevo, fecha, usuario;

            public ViewHolder(View itemView) {
                super(itemView);
                idRegistro = itemView.findViewById(R.id.idRegistro);
                numControl = itemView.findViewById(R.id.numControl);
                columnaModificada = itemView.findViewById(R.id.columnaModificada);
                valorAnterior = itemView.findViewById(R.id.valorAnterior);
                valorNuevo = itemView.findViewById(R.id.valorNuevo);
                fecha = itemView.findViewById(R.id.fecha);
                usuario = itemView.findViewById(R.id.usuario);
            }
        }
    }

    public class Historial {
        private int id;
        private String numControl, columnaModificada, valorAnterior, valorNuevo, fecha, usuario;

        public Historial(int id, String numControl, String columnaModificada, String valorAnterior, String valorNuevo, String fecha, String usuario) {
            this.id = id;
            this.numControl = numControl;
            this.columnaModificada = columnaModificada;
            this.valorAnterior = valorAnterior;
            this.valorNuevo = valorNuevo;
            this.fecha = fecha;
            this.usuario = usuario;
        }

        public int getId() {
            return id;
        }

        public String getNumControl() {
            return numControl;
        }

        public String getColumnaModificada() {
            return columnaModificada;
        }

        public String getValorAnterior() {
            return valorAnterior;
        }

        public String getValorNuevo() {
            return valorNuevo;
        }

        public String getFecha() {
            return fecha;
        }

        public String getUsuario() {
            return usuario;
        }
    }
}
