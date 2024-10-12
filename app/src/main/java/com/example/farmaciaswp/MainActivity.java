package com.example.farmaciaswp;

import android.content.Intent;  // Asegúrate de importar esta clase
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View; // Importa esta clase para usar OnClickListener
import android.widget.Button; // Importa esta clase para trabajar con los botones

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Asegúrate de que esto funcione con tu layout actual
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Aquí añadimos el código para el botón "NEXT"
        Button nextButton = findViewById(R.id.button12);  // Encuentra el botón "NEXT"
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear el Intent para cambiar a ProductActivity
                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                startActivity(intent);  // Inicia ProductActivity
            }
        });
    }
}
