package com.example.farmaciaswp;

import android.content.Intent;  // Asegúrate de importar esta clase
import android.os.Bundle;
import android.view.View; // Importa esta clase para usar OnClickListener
import android.widget.Button; // Importa esta clase para trabajar con los botones
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_regis_client);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurar el botón para ir a MainActivity
        Button nextButton = findViewById(R.id.Button18); // Asegúrate de que este ID sea correcto
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear el Intent para cambiar a MainActivity
                Intent intent = new Intent(RegisClientActivity.this, MainActivity.class);
                startActivity(intent);  // Inicia MainActivity
                finish(); // Opcional: cerrar RegisClientActivity
            }
        });
    }
}
