package com.example.farmaciaswp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Agregar_ extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText nombreComercial, nombreGenerico, concentracion, presentacion, registroInvima, lote, valor, molier;
    private Button buttonGuardar, buttonVolver, buttonSeleccionarImagen;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);


        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("imagenes");

        nombreComercial = findViewById(R.id.nombreComercial);
        nombreGenerico = findViewById(R.id.nombreGenerico);
        concentracion = findViewById(R.id.concentracion);
        presentacion = findViewById(R.id.presentacion);
        registroInvima = findViewById(R.id.registroInvima);
        lote = findViewById(R.id.lote);
        valor = findViewById(R.id.valor);
        molier = findViewById(R.id.molier);


        buttonGuardar = findViewById(R.id.buttonGuardar);
        buttonVolver = findViewById(R.id.buttonVolver);
        buttonSeleccionarImagen = findViewById(R.id.buttonSeleccionarImagen);
        progressBar = findViewById(R.id.progressBar);


        buttonSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    subirImagenYGuardarDatos();
                } else {
                    Toast.makeText(Agregar_.this, "Seleccione una imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void abrirGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // Obtener la URI de la imagen seleccionada
        }
    }


    private void subirImagenYGuardarDatos() {
        progressBar.setVisibility(View.VISIBLE);

        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");


        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String imageUrl = uri.toString();
                        guardarDatosEnDatabase(imageUrl);
                    }
                });
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Agregar_.this, "Producto agregado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(Agregar_.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
        });
    }


    private void guardarDatosEnDatabase(String imageUrl) {

        String molierString = molier.getText().toString();
        int molierInt;


        try {
            molierInt = Integer.parseInt(molierString);
        } catch (NumberFormatException e) {
            Toast.makeText(Agregar_.this, "El ID (molier) debe ser un número válido", Toast.LENGTH_SHORT).show();
            return;
        }


        Producto producto = new Producto(
                nombreComercial.getText().toString(),
                nombreGenerico.getText().toString(),
                concentracion.getText().toString(),
                presentacion.getText().toString(),
                registroInvima.getText().toString(),
                lote.getText().toString(),
                valor.getText().toString(),
                imageUrl,
                molierString
        );


        firestore.collection("PRODUCTOSWP")
                .document(molierString)
                .set(producto)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Agregar_.this, "Producto agregado exitosamente a Firestore", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    finish();
                })
                .addOnFailureListener(e -> {
                    String errorMessage = "Error al guardar en Firestore: " + e.getMessage();
                    Toast.makeText(Agregar_.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", errorMessage, e);
                    progressBar.setVisibility(View.GONE);
                });
    }
}

