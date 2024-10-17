package com.example.farmaciaswp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);

        // Manejo de las inserciones de la ventana
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botón para agregar un producto
        ImageButton buttonAdd = findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar Agregar_ Activity
                Intent intent = new Intent(ProductActivity.this, Agregar_.class);
                startActivity(intent);
            }
        });

        // Botón para buscar un producto
        ImageButton buttonSearch = findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar Buscar Activity
                Intent intent = new Intent(ProductActivity.this, Buscar.class);
                startActivity(intent);
            }
        });

        // Botón para actualizar un producto
        ImageButton buttonUpdate = findViewById(R.id.button_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar Actualizar Activity
                Intent intent = new Intent(ProductActivity.this, Actualizar.class);
                startActivity(intent);
            }
        });

        // Botón para eliminar un producto
        ImageButton buttonDelete = findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar Eliminar Activity
                Intent intent = new Intent(ProductActivity.this, Eliminar.class);
                startActivity(intent);
            }
        });

        // Botón adicional para ir al catálogo de productos
        Button buttonNext = findViewById(R.id.button78);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar CarStoreActivity
                Intent intent = new Intent(ProductActivity.this, Catalogo_Productos.class);
                startActivity(intent);
            }
        });
    }
}

