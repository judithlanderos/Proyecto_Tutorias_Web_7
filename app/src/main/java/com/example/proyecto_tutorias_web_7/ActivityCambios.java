package com.example.proyecto_tutorias_web_7;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.net.ConnectivityManager;
import android.net.Network;
import android.app.Activity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_tutorias_web_7.AlumnoAdapter;
import com.example.proyecto_tutorias_web_7.modelo.Alumno;
import controlador.AnalizadorJSON;

public class ActivityCambios extends Activity implements AlumnoAdapter.OnAlumnoClickListener {

    private EditText inputNumControl, inputNombre, inputApellidoP, inputApellidoM, inputFechaNacimiento, inputTelefono, inputEmail;
    private Spinner inputCarrera;
    private TextView alertaExito, alertaError;
    private Button btnActualizar;
    private AlumnoAdapter alumnoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambios);

        inputNumControl = findViewById(R.id.inputNumControl);
        inputNombre = findViewById(R.id.inputNombre);
        inputApellidoP = findViewById(R.id.inputApellidoP);
        inputApellidoM = findViewById(R.id.inputApellidoM);
        inputFechaNacimiento = findViewById(R.id.inputFechaNacimiento);
        inputTelefono = findViewById(R.id.inputTelefono);
        inputEmail = findViewById(R.id.inputEmail);
        inputCarrera = findViewById(R.id.input_carrera);
        alertaExito = findViewById(R.id.alerta_exito);
        alertaError = findViewById(R.id.alerta_error);
        btnActualizar = findViewById(R.id.btn_actualizar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.carreras_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputCarrera.setAdapter(adapter);

        String numControlEdit = getIntent().getStringExtra("NUM_CONTROL_EDIT");
        inputNumControl.setText(numControlEdit);

        RecyclerView recyclerView = findViewById(R.id.rv_listado_alumnos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        alumnoAdapter = new AlumnoAdapter(new ArrayList<>());
        alumnoAdapter.setOnAlumnoClickListener(this);
        recyclerView.setAdapter(alumnoAdapter);

        EditText etBuscar = findViewById(R.id.inputNumControl);
        etBuscar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alumnoAdapter.filtrar(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        cargarListadoDeAlumnos();
    }
    @Override
    public void onAlumnoClick(Alumno alumno) {
        cargarDatosEnFormulario(alumno);
    }

    public void actualizar(View view) {
        String numControl = inputNumControl.getText().toString().trim();
        String nombre = inputNombre.getText().toString().trim();
        String apellidoP = inputApellidoP.getText().toString().trim();
        String apellidoM = inputApellidoM.getText().toString().trim();
        String fechaNacimiento = inputFechaNacimiento.getText().toString().trim();
        String telefono = inputTelefono.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String carrera = inputCarrera.getSelectedItem().toString();

        Log.d("DatosFormulario", "NumControl: " + numControl);
        Log.d("DatosFormulario", "Nombre: " + nombre);
        Log.d("DatosFormulario", "Apellido P: " + apellidoP);
        Log.d("DatosFormulario", "Apellido M: " + apellidoM);
        Log.d("DatosFormulario", "Fecha Nacimiento: " + fechaNacimiento);
        Log.d("DatosFormulario", "Telefono: " + telefono);
        Log.d("DatosFormulario", "Email: " + email);
        Log.d("DatosFormulario", "Carrera: " + carrera);

        boolean datosCorrectos = true;
        StringBuilder errores = new StringBuilder();

        if (!numControl.matches("^[a-zA-Z]\\d{8}$")) {
            datosCorrectos = false;
            errores.append("Número de control debe ser 1 letra seguida de 8 números.\n");
        }

        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            datosCorrectos = false;
            errores.append("El nombre solo puede contener letras.\n");
        }

        if (!apellidoP.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            datosCorrectos = false;
            errores.append("El primer apellido solo puede contener letras.\n");
        }

        if (!TextUtils.isEmpty(apellidoM) && !apellidoM.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            datosCorrectos = false;
            errores.append("El segundo apellido solo puede contener letras.\n");
        }

        if (!telefono.matches("^\\d{10}$")) {
            datosCorrectos = false;
            errores.append("El teléfono debe contener 10 dígitos.\n");
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            datosCorrectos = false;
            errores.append("El correo electrónico no tiene un formato válido.\n");
        }

        if ("Seleccionar carrera".equals(carrera)) {
            datosCorrectos = false;
            errores.append("Por favor selecciona una carrera.\n");
        }

        if (datosCorrectos) {
            alertaExito.setVisibility(View.VISIBLE);
            alertaExito.setText("Registro actualizado correctamente");
            alertaError.setVisibility(View.GONE);

            inputNumControl.setText("");
            inputNombre.setText("");
            inputApellidoP.setText("");
            inputApellidoM.setText("");
            inputFechaNacimiento.setText("");
            inputTelefono.setText("");
            inputEmail.setText("");
            inputCarrera.setSelection(0);

            actualizarDatosAPI(numControl, nombre, apellidoP, apellidoM, fechaNacimiento, telefono, email, carrera);
        } else {
            alertaError.setVisibility(View.VISIBLE);
            alertaError.setText(errores.toString());
            alertaExito.setVisibility(View.GONE);
        }
    }

    private void actualizarDatosAPI(String numControl, String nombre, String apellidoP, String apellidoM, String fechaNacimiento, String telefono, String email, String carrera) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = cm.getActiveNetwork();

        if (network != null && cm.getNetworkCapabilities(cm.getActiveNetwork()) != null) {
            Log.d("Datos Enviados", "Nombre: " + nombre + ", Apellido P: " + apellidoP + ", Apellido M: " + apellidoM);

            String URL = "http://192.168.0.17:8081/Semestre_Ago_Dic_2024/Proyecto_ProgramacionWeb/api_rest_android_escuela/api_mysql_cambios.php";
            String metodo = "POST";

            JSONObject datos = new JSONObject();
            try {
                datos.put("nc", numControl);
                datos.put("nombre", nombre);
                datos.put("apellidoP", apellidoP);
                datos.put("apellidoM", apellidoM);
                datos.put("fecha_nacimiento", fechaNacimiento);
                datos.put("telefono", telefono);
                datos.put("email", email);
                datos.put("carrera_id", carrera);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(URL);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod(metodo);
                            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                            connection.setDoOutput(true);

                            OutputStream os = connection.getOutputStream();
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                            writer.write(datos.toString());
                            writer.flush();
                            writer.close();
                            os.close();

                            int responseCode = connection.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                                BufferedReader reader = new BufferedReader(inputStreamReader);
                                StringBuilder response = new StringBuilder();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    response.append(line);
                                }
                                reader.close();

                                JSONObject responseJSON = new JSONObject(response.toString());
                                String resultado = responseJSON.getString("cambio");
                                if (resultado.equals("exito")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getBaseContext(), "Actualización correcta", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getBaseContext(), "Error al actualizar", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "Error en la conexión", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getBaseContext(), "Error en la conexión de red", Toast.LENGTH_LONG).show();
        }
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

                        runOnUiThread(() -> alumnoAdapter.updateData(alumnos));

                    } else {
                        runOnUiThread(() -> Toast.makeText(getBaseContext(), "No se encontraron datos", Toast.LENGTH_LONG).show());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(getBaseContext(), "Error al consultar los datos", Toast.LENGTH_LONG).show());
                }
            }).start();
        } else {
            Toast.makeText(getBaseContext(), "Error en la conexión de red", Toast.LENGTH_LONG).show();
        }
    }
    private void cargarDatosEnFormulario(Alumno alumno) {

        EditText inputNumControl = findViewById(R.id.inputNumControl);
        EditText inputNombre = findViewById(R.id.inputNombre);
        EditText inputApellidoP = findViewById(R.id.inputApellidoP);
        EditText inputApellidoM = findViewById(R.id.inputApellidoM);
        EditText inputFechaNacimiento = findViewById(R.id.inputFechaNacimiento);
        EditText inputTelefono = findViewById(R.id.inputTelefono);
        EditText inputEmail = findViewById(R.id.inputEmail);
        Spinner inputCarrera = findViewById(R.id.input_carrera);


        inputNumControl.setText(alumno.getNumControl());
        inputNombre.setText(alumno.getNombre());
        inputApellidoP.setText(alumno.getApellidoP());
        inputApellidoM.setText(alumno.getApellidoM());
        inputFechaNacimiento.setText(alumno.getFechaNacimiento());
        inputTelefono.setText(alumno.getTelefono());
        inputEmail.setText(alumno.getEmail());


        ArrayAdapter<String> carreraAdapter = (ArrayAdapter<String>) inputCarrera.getAdapter();
        int position = carreraAdapter.getPosition(alumno.getCarreraId());
        inputCarrera.setSelection(position);
    }

}
