package com.example.farmaciaswp;

public class ProductoCarrito {
    private String nombreComercial;
    private String presentacion;
    private String imagen;
    private String molier;
    private double valor;
    private int cantidad;

    public ProductoCarrito(String nombreComercial, String presentacion, String imagen, String molier, double valor, int cantidad) {
        this.nombreComercial = nombreComercial;
        this.presentacion = presentacion;
        this.imagen = imagen;
        this.molier = molier;
        this.valor = valor;
        this.cantidad = cantidad;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public String getImagen() {
        return imagen;
    }

    public String getMolier() {
        return molier;
    }

    public double getValor() {
        return valor;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

}
