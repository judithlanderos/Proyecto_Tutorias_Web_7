package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class conexion_bd_tutorias {
    private Connection conexion;
    private Statement stm;
    private ResultSet rs;

    public conexion_bd_tutorias() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Configura la URL de conexión
            String URL = "jdbc:mysql://localhost:3306/Proyecto_Tutorias";
            conexion = DriverManager.getConnection(URL, "judith", "judilth@3");

            if (conexion != null) {
                System.out.println("¡Conexión exitosa a Proyecto_Tutorias!");
            } else {
                System.err.println("Conexión fallida: La conexión es nula.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el controlador de MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public ResultSet ejecutarConsultaSQL(String consultaSQL) {
        rs = null;
        try {
            if (conexion == null) {
                System.err.println("Error: La conexión es nula.");
                return null;
            }
            stm = conexion.createStatement();
            rs = stm.executeQuery(consultaSQL);
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta SQL: " + e.getMessage());
            System.err.println("Consulta: " + consultaSQL);
        }
        return rs;
    }

    public boolean ejecutarInstruccionDML(String instruccionDML) {
        boolean resultado = false;
        try {
            if (conexion == null) {
                System.err.println("Error: La conexión es nula.");
                return false;
            }
            stm = conexion.createStatement();
            resultado = stm.executeUpdate(instruccionDML) > 0;
        } catch (SQLException e) {
            System.err.println("Error en la instrucción DML: " + e.getMessage());
            System.err.println("Instrucción: " + instruccionDML);
        }
        return resultado;
    }

    public void cerrarConexion() {
        try {
            if (conexion != null) {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}