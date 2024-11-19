package ej.Tablas;

import java.awt.*;

public class Alumno {
    public int id;
    public String nombre;
    public String apellidos;
    public String dni;
    public String foto;
    public String claveaccesoalumno;
    public Profesor profesor;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setClaveaccesoalumno(String claveaccesoalumno) {
        this.claveaccesoalumno = claveaccesoalumno;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor idprofesor) {
        this.profesor = idprofesor;
    }

    public String getNombre() {
        return nombre;
    }
    
    public String getApellidos() {
        return apellidos;
    }
    
    public String getDni() {
        return dni;
    }
    
    public String getFoto() {
        return foto;
    }
    
    public String getClaveaccesoalumno() {
        return claveaccesoalumno;
    }
    

}
