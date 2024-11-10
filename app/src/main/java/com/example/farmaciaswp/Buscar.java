
package com.example.farmaciaswp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Buscar extends AppCompatActivity {

    private EditText inputMolier;
    private Button buttonBuscar, buttonVolverBuscar;
    private LinearLayout resultadosContainer;
    private TextView resultadoBuscar, informacionProducto;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        firestore = FirebaseFirestore.getInstance();


        inputMolier = findViewById(R.id.inputMolier);
        buttonBuscar = findViewById(R.id.buttonBuscar);
        buttonVolverBuscar = findViewById(R.id.buttonVolverBuscar);
        resultadosContainer = findViewById(R.id.resultadosContainer);
        resultadoBuscar = findViewById(R.id.resultadoBuscar);
        informacionProducto = findViewById(R.id.informacionProducto);
        progressBar = findViewById(R.id.progressBar8);

        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarProducto();
            }
        });


        buttonVolverBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void buscarProducto() {
        String molierInput = inputMolier.getText().toString().trim();


        if (molierInput.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese un número molier", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        resultadosContainer.setVisibility(View.GONE);

        firestore.collection("PRODUCTOSWP")
                .document(molierInput)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            Producto producto = documentSnapshot.toObject(Producto.class);
                            mostrarInformacionProducto(producto);
                        } else {
                            Toast.makeText(Buscar.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = "Error al buscar en Firestore: " + e.getMessage();
                        Toast.makeText(Buscar.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("FirestoreError", errorMessage, e);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void mostrarInformacionProducto(Producto producto) {
        resultadosContainer.setVisibility(View.VISIBLE);
        resultadoBuscar.setText(getString(R.string.resultado_de_la_b_squeda) + ": " + producto.getNombreComercial());
        informacionProducto.setText("Nombre Genérico: " + producto.getNombreGenerico() + "\n" +
                "Concentración: " + producto.getConcentracion() + "\n" +
                "Presentación: " + producto.getPresentacion() + "\n" +
                "Registro INVIMA: " + producto.getRegistroInvima() + "\n" +
                "Lote: " + producto.getLote() + "\n" +
                "Valor: " + producto.getValor() + "\n" +
                "ID (Molier): " + producto.getMolier());
    }
}
