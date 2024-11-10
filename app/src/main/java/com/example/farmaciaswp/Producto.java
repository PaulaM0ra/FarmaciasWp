package com.example.farmaciaswp;

public class Producto {

    private String imagen;
    private String nombreComercial;
    private String nombreGenerico;
    private String concentracion;
    private String presentacion;
    private String registroInvima;
    private String lote;
    private String valor;
    private String molier;

    // Constructor de la clase
    public Producto(String nombreComercial, String nombreGenerico,
                    String concentracion, String presentacion,
                    String registroInvima, String lote, String valor,
                    String imagen, String molier) {
        this.imagen = imagen;
        this.nombreComercial = nombreComercial;
        this.nombreGenerico = nombreGenerico;
        this.concentracion = concentracion;
        this.presentacion = presentacion;
        this.registroInvima = registroInvima;
        this.lote = lote;
        this.valor = valor;
        this.molier = molier;
    }


    public Producto() {

    }

    public String getImagen() {
        return imagen;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public String getNombreGenerico() {
        return nombreGenerico;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public String getRegistroInvima() {
        return registroInvima;
    }

    public String getLote() {
        return lote;
    }

    public String getValor() {
        return valor;
    }

    public String getMolier() {
        return molier; //
    }


    public void setMolier(String molier) {
        this.molier = molier; // Asignar el valor de molier
    }
}
