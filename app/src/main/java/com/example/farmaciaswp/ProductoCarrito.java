package com.example.farmaciaswp;

public class ProductoCarrito {
    private String nombreComercial;
    private String presentacion;
    private String imagen;
    private double valor;
    private int cantidad;  // Añadido para manejar la cantidad

    // Constructor actualizado
    public ProductoCarrito(String nombreComercial, String presentacion, String imagen, double valor, int cantidad) {
        this.nombreComercial = nombreComercial;
        this.presentacion = presentacion;
        this.imagen = imagen;
        this.valor = valor;
        this.cantidad = cantidad;  // Asignación de la cantidad
    }

    // Métodos getter y setter
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

    public int getCantidad() {
        return cantidad;  // Getter para cantidad
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;  // Setter para cantidad
    }
}
