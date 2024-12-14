package com.example.proyecto_tutorias_web_7.modelo;

public class Alumno {
    private String numControl;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private String fechaNacimiento;
    private String telefono;
    private String email;
    private String carreraId;

    public Alumno(String numControl, String nombre, String apellidoP, String apellidoM, String fechaNacimiento, String telefono, String email, String carreraId) {
        this.numControl = numControl;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
        this.carreraId = carreraId;
    }

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

    public String getCarreraId() {
        return carreraId;
    }
}
