package com.example.farmaciaswp;

public class ProductoCarrito {
    private String nombreComercial;
    private String presentacion;
    private String imagen;
    private double valor;


    public ProductoCarrito(String nombreComercial, String presentacion, String imagen, String molier, double valor, int cantidad) {
        this.nombreComercial = nombreComercial;
        this.presentacion = presentacion;
        this.imagen = imagen;

        this.valor = valor;

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



    public double getValor() {
        return valor;
    }


}
