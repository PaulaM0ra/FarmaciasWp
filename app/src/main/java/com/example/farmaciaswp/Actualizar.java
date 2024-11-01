package com.example.farmaciaswp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class Actualizar extends AppCompatActivity {
    private EditText molierEditText, nombreGenericoEditText, nombreComercialEditText,
            concentracionEditText, presentacionEditText, loteEditText, registroInvimaEditText;
    private ProgressBar progressBar;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        db = FirebaseFirestore.getInstance();

        molierEditText = findViewById(R.id.molier);
        nombreGenericoEditText = findViewById(R.id.nombre_generico);
        nombreComercialEditText = findViewById(R.id.nombre_comercial);
        concentracionEditText = findViewById(R.id.concentracion);
        presentacionEditText = findViewById(R.id.presentacion);
        loteEditText = findViewById(R.id.lote);
        registroInvimaEditText = findViewById(R.id.registro_invima);
        progressBar = findViewById(R.id.progressBar);

        Button buttonBuscarMolier = findViewById(R.id.buttonBuscarMolier);
        buttonBuscarMolier.setOnClickListener(v -> buscarProducto());

        Button buttonActualizar = findViewById(R.id.buttonActualizar);
        buttonActualizar.setOnClickListener(v -> actualizarProducto());


        Button buttonGuardarBD = findViewById(R.id.buttonGuardarBD);
        buttonGuardarBD.setOnClickListener(v -> guardarEnBD());
        // Botón para volver a ProductActivity
        Button buttonVolver = findViewById(R.id.buttonVolver);
        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar ProductActivity
                Intent intent = new Intent(Actualizar.this, ProductActivity.class);
                startActivity(intent);
            }
        });

    }

    private void buscarProducto() {
        String molier = molierEditText.getText().toString().trim();
        if (molier.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el número de Molier", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        db.collection("PRODUCTOSWP").document(molier)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Cargar los datos en los EditTexts
                            nombreGenericoEditText.setText(document.getString("nombreGenerico"));
                            nombreComercialEditText.setText(document.getString("nombreComercial"));
                            concentracionEditText.setText(document.getString("concentracion"));
                            presentacionEditText.setText(document.getString("presentacion"));
                            loteEditText.setText(document.getString("lote"));
                            registroInvimaEditText.setText(document.getString("registroInvima"));
                        } else {
                            Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error al buscar el producto", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void actualizarProducto() {

    }


    private void guardarEnBD() {
        String molier = molierEditText.getText().toString().trim();
        String nombreGenerico = nombreGenericoEditText.getText().toString().trim();
        String nombreComercial = nombreComercialEditText.getText().toString().trim();
        String concentracion = concentracionEditText.getText().toString().trim();
        String presentacion = presentacionEditText.getText().toString().trim();
        String lote = loteEditText.getText().toString().trim();
        String registroInvima = registroInvimaEditText.getText().toString().trim();

        if (molier.isEmpty() || nombreGenerico.isEmpty() || nombreComercial.isEmpty() ||
                concentracion.isEmpty() || presentacion.isEmpty() || lote.isEmpty() || registroInvima.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        db.collection("PRODUCTOSWP").document(molier)
                .update("nombreGenerico", nombreGenerico,
                        "nombreComercial", nombreComercial,
                        "concentracion", concentracion,
                        "presentacion", presentacion,
                        "lote", lote,
                        "registroInvima", registroInvima)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Error al actualizar el producto", Toast.LENGTH_SHORT).show();
                });
    }
}
