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
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;


public class RegisClientActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    private EditText nombreEditText, telefonoEditText, direccionEditText, documentoEditText, emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_regis_client);

        db = FirebaseFirestore.getInstance();

        nombreEditText = findViewById(R.id.editTextText);
        telefonoEditText = findViewById(R.id.editTextPhone);
        direccionEditText = findViewById(R.id.editTextTextPostalAddress);
        documentoEditText = findViewById(R.id.editTextNumberDecimal);
        emailEditText = findViewById(R.id.editTextTextEmailAddress2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClientData();
            }
        });

        Button nextButton = findViewById(R.id.Button18);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisClientActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveClientData() {
        String nombreCliente = nombreEditText.getText().toString().trim();
        String telefono = telefonoEditText.getText().toString().trim();
        String direccion = direccionEditText.getText().toString().trim();
        String documento = documentoEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (nombreCliente.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || documento.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> client = new HashMap<>();
        client.put("nombre", nombreCliente);
        client.put("telefono", telefono);
        client.put("direccion", direccion);
        client.put("documento", documento);
        client.put("email", email);

        db.collection("clientes")
                .add(client)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(RegisClientActivity.this, "Registro guardado exitosamente!", Toast.LENGTH_SHORT).show();
                    limpiarCampos(); // Limpia los campos después de guardar
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisClientActivity.this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void limpiarCampos() {
        nombreEditText.setText("Nombre");
        telefonoEditText.setText("Telefono");
        direccionEditText.setText(" Direccion");
        documentoEditText.setText("Documento");
        emailEditText.setText("Email");
    }}
