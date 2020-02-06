package com.example.retrofitupt;

public class Cliente {

    private String Cod_persona;
    private String Nombre;
    private String Apellidos;

    public Cliente(String Cod_persona, String Nombre, String Apellidos) {
        this.Cod_persona = Cod_persona;
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
    }

    public String getcodigo() {
        return Cod_persona;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getApellido() {
        return Apellidos;
    }
}

