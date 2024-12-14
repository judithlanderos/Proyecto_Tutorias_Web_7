package controlador;

import conexion.conexion_bd_tutorias;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlumnoDAO {
    private conexion_bd_tutorias conexion;

    public AlumnoDAO() {
        conexion = new conexion_bd_tutorias();
    }

    // ------------------ MÉTODO DE ALTAS ------------------
    public boolean agregarAlumno(String numControl, String nombre, String apellidoP, String apellidoM,
                                 String fechaNacimiento, String telefono, String email, int carreraId) {
        String sql = "INSERT INTO alumnos (numControl, nombre, apellidoP, apellidoM, fecha_nacimiento, telefono, email, Carrera_carrera_id) " +
                "VALUES ('" + numControl + "', '" + nombre + "', '" + apellidoP + "', '" + apellidoM + "', '" +
                fechaNacimiento + "', '" + telefono + "', '" + email + "', " + carreraId + ")";
        return conexion.ejecutarInstruccionDML(sql);
    }

    // ------------------ MÉTODO DE CONSULTAS ------------------
    public ResultSet obtenerAlumnoPorNumControl(String numControl) {
        String sql = "SELECT * FROM alumnos WHERE numControl = '" + numControl + "'";
        return conexion.ejecutarConsultaSQL(sql);
    }

    public ResultSet obtenerTodosLosAlumnos() {
        String sql = "SELECT * FROM alumnos";
        return conexion.ejecutarConsultaSQL(sql);
    }

    // ------------------ MÉTODO DE CAMBIOS ------------------
    public boolean modificarAlumno(String numControl, String nombre, String apellidoP, String apellidoM,
                                   String fechaNacimiento, String telefono, String email, int carreraId) {
        String sql = "UPDATE alumnos SET nombre = '" + nombre + "', apellidoP = '" + apellidoP +
                "', apellidoM = '" + apellidoM + "', fecha_nacimiento = '" + fechaNacimiento +
                "', telefono = '" + telefono + "', email = '" + email +
                "', Carrera_carrera_id = " + carreraId +
                " WHERE numControl = '" + numControl + "'";
        return conexion.ejecutarInstruccionDML(sql);
    }

    // ------------------ MÉTODO DE BAJAS ------------------
    public boolean eliminarAlumno(String numControl) {
        String sql = "DELETE FROM alumnos WHERE numControl = '" + numControl + "'";
        return conexion.ejecutarInstruccionDML(sql);
    }

    public boolean registrarBaja(String numControl) {
        String insertSql = "INSERT INTO historial_bajas (numControl, nombre, apellidoP, apellidoM, motivo) " +
                "SELECT numControl, nombre, apellidoP, apellidoM, 'Baja solicitada' FROM listado_alumnos_carreras " +
                "WHERE numControl = '" + numControl + "'";
        boolean inserted = conexion.ejecutarInstruccionDML(insertSql);

        if (inserted) {
            String deleteSql = "DELETE FROM alumnos WHERE numControl = '" + numControl + "'";
            return conexion.ejecutarInstruccionDML(deleteSql);
        }
        return false;
    }

    // ------------------ MÉTODOS DE TRANSACCIÓN ------------------
    public void iniciarTransaccion() {
        conexion.ejecutarInstruccionDML("START TRANSACTION");
    }

    public void confirmarTransaccion() {
        conexion.ejecutarInstruccionDML("COMMIT");
    }

    public void revertirTransaccion() {
        conexion.ejecutarInstruccionDML("ROLLBACK");
    }
}
