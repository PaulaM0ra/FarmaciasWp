package com.example.farmaciaswp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class Eliminar extends AppCompatActivity {

    private EditText editTextMolier;
    private Button buttonEliminar, buttonVolver;
    private ProgressBar progressBar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eliminar);

        db = FirebaseFirestore.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextMolier = findViewById(R.id.editTextMolier);
        buttonEliminar = findViewById(R.id.buttonEliminar);
        buttonVolver = findViewById(R.id.buttonVolver);
        progressBar = findViewById(R.id.progressBar10);

        buttonEliminar.setOnClickListener(v -> eliminarProducto());

        buttonVolver.setOnClickListener(v -> {
            Intent intent = new Intent(Eliminar.this, ProductActivity.class);
            startActivity(intent);
        });
    }

    private void eliminarProducto() {
        String molier = editTextMolier.getText().toString().trim();
        if (molier.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el número de Molier", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE); // Mostrar el ProgressBar durante la operación
        db.collection("PRODUCTOSWP").document(molier)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Error al eliminar el producto", Toast.LENGTH_SHORT).show();
                });
    }
}
