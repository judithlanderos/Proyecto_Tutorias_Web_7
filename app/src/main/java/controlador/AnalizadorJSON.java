package controlador;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AnalizadorJSON {
    private InputStream is = null;
    private OutputStream os = null;
    private JSONObject jsonObject = null;
    private HttpURLConnection conexion = null;
    private URL url;

    // Código para la petición HTTP (Altas)
    public JSONObject peticionHTTP(String direccionURL, String metodo, ArrayList<String> datos) {
        // Cadena JSON --> {"nc":"01", "n":"lope", "e":"20"}
        String cadenaJSON = "{" +
                "\"nc\":\"" + datos.get(0) + "\", " +
                "\"n\":\"" + datos.get(1) + "\", " +
                "\"primer_ap\":\"" + datos.get(2) + "\", " +
                "\"segundo_ap\":\"" + datos.get(3) + "\", " +
                "\"fecha_nacimiento\":\"" + datos.get(4) + "\", " +
                "\"telefono\":\"" + datos.get(5) + "\", " +
                "\"email\":\"" + datos.get(6) + "\", " +
                "\"carrera_id\":\"" + datos.get(7) + "\"" + "}";

        try {
            url = new URL(direccionURL);
            conexion = (HttpURLConnection) url.openConnection();

            conexion.setDoOutput(true);

            conexion.setRequestMethod(metodo);

            conexion.setFixedLengthStreamingMode(cadenaJSON.length());

            conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            os = new BufferedOutputStream(conexion.getOutputStream());
            os.write(cadenaJSON.getBytes());
            os.flush();
            os.close();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Recibir la respuesta
        try {
            is = new BufferedInputStream(conexion.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder cadena = new StringBuilder();
            String fila = null;
            while ((fila = br.readLine()) != null) {
                cadena.append(fila + "\n");
            }

            is.close();
            jsonObject = new JSONObject(String.valueOf(cadena));

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

        return jsonObject;
    }

    // Código para la petición de Baja
    public JSONObject peticionHTTPBaja(String direccionURL, String metodo, String nc) {
        // JSON para la petición de baja --> {"nc":"01"}
        String cadenaJSON = "{\"nc\":\"" + nc + "\"}";

        try {
            url = new URL(direccionURL);
            conexion = (HttpURLConnection) url.openConnection();

            conexion.setDoOutput(true);

            conexion.setRequestMethod(metodo);

            conexion.setFixedLengthStreamingMode(cadenaJSON.length());

            // Establecer formato de petición
            conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            os = new BufferedOutputStream(conexion.getOutputStream());
            os.write(cadenaJSON.getBytes());
            os.flush();
            os.close();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Recibir la respuesta
        try {
            is = new BufferedInputStream(conexion.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder cadena = new StringBuilder();
            String fila = null;
            while ((fila = br.readLine()) != null) {
                cadena.append(fila + "\n");
            }

            is.close();
            jsonObject = new JSONObject(cadena.toString());

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

        return jsonObject;
    }

    public JSONObject peticionHTTPCambio(String direccionURL, String metodo, ArrayList<String> datos) {
        // Crear el JSON con los datos de entrada
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("numControl", datos.get(0));
            jsonRequest.put("nombre", datos.get(1));
            jsonRequest.put("apellidoP", datos.get(2));
            jsonRequest.put("apellidoM", datos.get(3));
            jsonRequest.put("fecha_nacimiento", datos.get(4));
            jsonRequest.put("telefono", datos.get(5));
            jsonRequest.put("email", datos.get(6));
            jsonRequest.put("Carrera_carrera_id", datos.get(7));
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el objeto JSON: " + e.getMessage());
        }

        // Enviar la solicitud HTTP
        HttpURLConnection conexion = null;
        BufferedOutputStream os = null;
        BufferedInputStream is = null;
        JSONObject jsonResponse = null;

        try {
            URL url = new URL(direccionURL);
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod(metodo);
            conexion.setDoOutput(true);

            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setFixedLengthStreamingMode(jsonRequest.toString().getBytes().length);

            os = new BufferedOutputStream(conexion.getOutputStream());
            os.write(jsonRequest.toString().getBytes());
            os.flush();

            is = new BufferedInputStream(conexion.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder respuesta = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                respuesta.append(linea);
            }

            jsonResponse = new JSONObject(respuesta.toString());

        } catch (MalformedURLException e) {
            throw new RuntimeException("URL mal formada: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Error de entrada/salida: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error desconocido: " + e.getMessage());
        } finally {
            try {
                if (os != null) os.close();
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonResponse;
    }

    public JSONObject peticionHTTPConsultas(String cadenaURL, String metodo, String filtro) {
        try {
            url = new URL(cadenaURL);
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoOutput(true);
            conexion.setRequestMethod(metodo);
            conexion.setRequestProperty("Content-Type", "application/json");

            String jsonFiltro = "{\"filtro_nc\":\"filtro_nc\"}";
            os = new BufferedOutputStream(conexion.getOutputStream());
            os.write(jsonFiltro.getBytes());
            os.flush();
            os.close();

            is = new BufferedInputStream(conexion.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder cad = new StringBuilder();
            String fila;
            while ((fila = br.readLine()) != null) {
                cad.append(fila);
            }
            is.close();

            return new JSONObject(cad.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject peticionHTTPTriggers(String direccionURL, String metodo) {
        try {
            url = new URL(direccionURL);
            conexion = (HttpURLConnection) url.openConnection();

            conexion.setDoOutput(true);
            conexion.setRequestMethod(metodo);
            conexion.setRequestProperty("Content-Type", "application/json");

            os = new BufferedOutputStream(conexion.getOutputStream());
            os.flush();
            os.close();

        } catch (MalformedURLException e) {
            throw new RuntimeException("URL mal formada: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Error de entrada/salida: " + e.getMessage());
        }

        try {
            is = new BufferedInputStream(conexion.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder cadena = new StringBuilder();
            String fila;
            while ((fila = br.readLine()) != null) {
                cadena.append(fila + "\n");
            }
            is.close();

            jsonObject = new JSONObject(cadena.toString());

        } catch (IOException | JSONException e) {
            throw new RuntimeException("Error al leer la respuesta JSON: " + e.getMessage());
        }

        return jsonObject;
    }

    public JSONObject peticionHTTPVista(String direccionURL, String metodo) {
        try {
            url = new URL(direccionURL);
            conexion = (HttpURLConnection) url.openConnection();

            conexion.setRequestMethod(metodo);
            conexion.setDoInput(true);
            conexion.setRequestProperty("Content-Type", "application/json");

            is = new BufferedInputStream(conexion.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder respuesta = new StringBuilder();
            String linea;

            while ((linea = br.readLine()) != null) {
                respuesta.append(linea);
            }

            is.close();

            return new JSONObject(respuesta.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject peticionHTTPProcedimientoFuncion(String direccionURL, String metodo, String action, String carreraNombre) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("action", action);
            if (action.equals("totalAlumnosPorCarrera") && carreraNombre != null) {
                jsonRequest.put("carrera_nombre", carreraNombre);
            }
        } catch (JSONException e) {
            throw new RuntimeException("Error al crear el objeto JSON: " + e.getMessage());
        }

        HttpURLConnection conexion = null;
        BufferedOutputStream os = null;
        BufferedInputStream is = null;
        JSONObject jsonResponse = null;

        try {
            URL url = new URL(direccionURL);
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod(metodo);
            conexion.setDoOutput(true);

            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setFixedLengthStreamingMode(jsonRequest.toString().getBytes().length);

            os = new BufferedOutputStream(conexion.getOutputStream());
            os.write(jsonRequest.toString().getBytes());
            os.flush();

            is = new BufferedInputStream(conexion.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder respuesta = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                respuesta.append(linea);
            }

            jsonResponse = new JSONObject(respuesta.toString());

        } catch (MalformedURLException e) {
            throw new RuntimeException("URL mal formada: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Error de entrada/salida: " + e.getMessage());
        } catch (JSONException e) {
            throw new RuntimeException("Error al procesar JSON", e);
        } catch (Exception e) {
            throw new RuntimeException("Error desconocido", e);
        } finally {
            try {
                if (os != null) os.close();
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonResponse;
    }


    public JSONObject peticionHTTPRegistro(String direccionURL, String metodo, ArrayList<String> datos) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("Nombre_Usuario_sha1", datos.get(0));
            jsonRequest.put("password_sha1", datos.get(1));
            jsonRequest.put("email", datos.get(2));
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el objeto JSON: " + e.getMessage());
        }

        HttpURLConnection conexion = null;
        BufferedOutputStream os = null;
        BufferedInputStream is = null;
        JSONObject jsonResponse = null;

        try {
            URL url = new URL(direccionURL);
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod(metodo);
            conexion.setDoOutput(true);

            // Establecer los headers
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setFixedLengthStreamingMode(jsonRequest.toString().getBytes().length);

            os = new BufferedOutputStream(conexion.getOutputStream());
            os.write(jsonRequest.toString().getBytes());
            os.flush();

            is = new BufferedInputStream(conexion.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder respuesta = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                respuesta.append(linea);
            }

            jsonResponse = new JSONObject(respuesta.toString());

        } catch (MalformedURLException e) {
            throw new RuntimeException("URL mal formada: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Error de entrada/salida: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error desconocido: " + e.getMessage());
        } finally {
            try {
                if (os != null) os.close();
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonResponse;
    }
    public JSONObject peticionHTTPLogin(String direccionURL, String metodo, ArrayList<String> datos) {
        HttpURLConnection conexion = null;
        JSONObject jsonRequest = new JSONObject();
        JSONObject jsonResponse = null;

        try {
            jsonRequest.put("Nombre_Usuario", datos.get(0));
            jsonRequest.put("Password", datos.get(1));
            jsonRequest.put("Correo", datos.get(2));

            URL url = new URL(direccionURL);
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod(metodo);
            conexion.setDoOutput(true);
            conexion.setRequestProperty("Content-Type", "application/json");

            // Enviar datos al servidor
            try (OutputStream os = conexion.getOutputStream()) {
                os.write(jsonRequest.toString().getBytes("UTF-8"));
                os.flush();
            }

            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200
                // Leer la respuesta
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()))) {
                    StringBuilder respuesta = new StringBuilder();
                    String linea;
                    while ((linea = reader.readLine()) != null) {
                        respuesta.append(linea);
                    }

                    // Procesar la respuesta JSON
                    jsonResponse = new JSONObject(respuesta.toString());
                    String status = jsonResponse.optString("status");
                    String mensaje = jsonResponse.optString("mensaje");

                    if ("success".equals(status)) {
                        System.out.println("Inicio de sesión exitoso: " + mensaje);
                    } else {
                        System.out.println("Error: " + mensaje);
                    }
                }
            } else {
                throw new IOException("Código de respuesta HTTP: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse = new JSONObject();
        } finally {
            if (conexion != null) {
                conexion.disconnect();
            }
        }

        return jsonResponse;
    }
}