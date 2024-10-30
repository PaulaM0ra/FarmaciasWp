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

        listaProductos = new ArrayList<>();
        cargarProductos();

        productoAdapter = new ProductoAdapter(listaProductos);
        recyclerView.setAdapter(productoAdapter);

        Button buttonCRUD = findViewById(R.id.button5);
        buttonCRUD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Catalogo_Productos.this, ProductActivity.class);
                startActivity(intent);
            }
        });


        Button buttonCarrito = findViewById(R.id.button3);
        buttonCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Catalogo_Productos.this, CarStoreActivity.class);
                startActivity(intent);
            }
        });
        Button buttonmapa = findViewById(R.id.button6);
        buttonmapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Catalogo_Productos.this, LocationActivity.class);
                startActivity(intent);
            }
        });

    }

    private void cargarProductos() {
        listaProductos.add(new Producto("Paracetamol", "Tylenol", "500mg", "Tabletas", "INVIMA 123456", "Lote 789", "$5000", "https://tse4.mm.bing.net/th?id=OIP.PohVuDgIN_Nua_BH_CP2bQHaHa&pid=Api&P=0&h=180","1")); // Asegúrate de usar una URL válida

    }
}


