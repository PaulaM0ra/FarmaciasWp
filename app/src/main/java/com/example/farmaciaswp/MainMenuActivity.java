package com.example.farmaciaswp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); // Asegúrate de usar el diseño correcto

        // Inicializar los botones después de setContentView
        Button btnDatabase = findViewById(R.id.btnDatabase);
        Button btnMaps = findViewById(R.id.btnMaps);
        Button btnCatalog = findViewById(R.id.btnCatalog);
        Button btnRegisterClient = findViewById(R.id.btnRegisterClient);

        // Configurar listeners para cada botón
        btnDatabase.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, ProductActivity.class);
            startActivity(intent);
        });

        btnMaps.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, LocationActivity.class);
            startActivity(intent);
        });

        btnCatalog.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, Catalogo_Productos.class);
            startActivity(intent);
        });

        btnRegisterClient.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, RegisClientActivity.class);
            startActivity(intent);
        });
    }
}
