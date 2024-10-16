package com.example.farmaciaswp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Catalogo_Productos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductoAdapter productoAdapter;
    private List<Producto> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catalogo_productos);

        // Configuración de las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Crear una lista de productos (puedes obtenerlos de una base de datos)
        listaProductos = new ArrayList<>();
        cargarProductos(); // Método que llenará la lista con productos

        // Configurar el adaptador
        productoAdapter = new ProductoAdapter(listaProductos);
        recyclerView.setAdapter(productoAdapter);

        // Botón "CRUD FarmaciaWP" redirige a ProductActivity
        Button buttonCRUD = findViewById(R.id.button5);
        buttonCRUD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Catalogo_Productos.this, ProductActivity.class);
                startActivity(intent);
            }
        });

        // Botón "Agregar al Carrito" redirige a CarStoreActivity
        Button buttonCarrito = findViewById(R.id.button3);
        buttonCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Catalogo_Productos.this, CarStoreActivity.class);
                startActivity(intent);
            }
        });
    }

    private void cargarProductos() {
        // Aquí puedes agregar productos manualmente o cargar desde una base de datos
        listaProductos.add(new Producto("Paracetamol", "Tylenol", "500mg", "Tabletas", "INVIMA 123456", "Lote 789", "$5000", R.drawable.imagen_paracetamol));

    }
}
