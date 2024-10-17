package com.example.farmaciaswp;

public class Producto {
    // Atributos de la clase
    private int imagen; // Recurso de imagen
    private String nombreComercial; // Nombre comercial del producto
    private String nombreGenerico; // Nombre genérico del producto
    private String concentracion; // Concentración del producto
    private String presentacion; // Presentación del producto (tabletas, líquido, etc.)
    private String registroInvima; // Registro INVIMA del producto
    private String lote; // Lote del producto
    private String valor; // Valor o precio del producto
    private int molier; // Número único de identificación (molier)

    // Constructor de la clase
    public Producto(String nombreComercial, String nombreGenerico,
                    String concentracion, String presentacion,
                    String registroInvima, String lote, String valor,
                    int imagen, int molier) { // Cambia a int
        this.imagen = imagen;
        this.nombreComercial = nombreComercial;
        this.nombreGenerico = nombreGenerico;
        this.concentracion = concentracion;
        this.presentacion = presentacion;
        this.registroInvima = registroInvima;
        this.lote = lote;
        this.valor = valor;
        this.molier = molier; // Asignar el valor de molier
    }

    // Métodos getter para acceder a los atributos
    public int getImagen() {
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

    public int getMolier() {
        return molier; // Getter para molier
    }
}
