package com.example.farmaciaswp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Agregar_ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        // Botón "Volver"
        Button buttonVolver = findViewById(R.id.buttonVolver);
        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar ProductActivity
                Intent intent = new Intent(Agregar_.this, ProductActivity.class);
                startActivity(intent);
                // Finaliza la actividad actual si no quieres que esté en la pila de actividades
                finish();
            }
        });
    }
}
