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

        // Inicializar Firestore y Storage
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("imagenes");

        // Inicializar EditTexts
        nombreComercial = findViewById(R.id.nombreComercial);
        nombreGenerico = findViewById(R.id.nombreGenerico);
        concentracion = findViewById(R.id.concentracion);
        presentacion = findViewById(R.id.presentacion);
        registroInvima = findViewById(R.id.registroInvima);
        lote = findViewById(R.id.lote);
        valor = findViewById(R.id.valor);
        molier = findViewById(R.id.molier);

        // Inicializar Buttons y ProgressBar
        buttonGuardar = findViewById(R.id.buttonGuardar);
        buttonVolver = findViewById(R.id.buttonVolver);
        buttonSeleccionarImagen = findViewById(R.id.buttonSeleccionarImagen);
        progressBar = findViewById(R.id.progressBar);

        // Seleccionar imagen
        buttonSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        // Guardar datos y subir imagen a Firebase
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

    // Método para abrir la galería y seleccionar imagen
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

    // Subir la imagen a Firebase Storage y guardar los datos en Firestore
    private void subirImagenYGuardarDatos() {
        progressBar.setVisibility(View.VISIBLE);

        // Crear una referencia para la imagen en Firebase Storage
        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

        // Subir la imagen a Firebase Storage
        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Obtener la URL de la imagen subida y guardar los datos en Firestore
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

    // Guardar los datos en Firestore con la URL de la imagen
    private void guardarDatosEnDatabase(String imageUrl) {
        // Convertir el valor de molier a int
        String molierString = molier.getText().toString(); // Obtener el valor de molier como String
        int molierInt;

        // Validar si el molier se puede convertir a entero
        try {
            molierInt = Integer.parseInt(molierString); // Intentar convertir a int
        } catch (NumberFormatException e) {
            Toast.makeText(Agregar_.this, "El ID (molier) debe ser un número válido", Toast.LENGTH_SHORT).show();
            return; // Salir si la conversión falla
        }

        // Crear un nuevo objeto Producto con los datos ingresados
        Producto producto = new Producto(
                nombreComercial.getText().toString(),
                nombreGenerico.getText().toString(),
                concentracion.getText().toString(),
                presentacion.getText().toString(),
                registroInvima.getText().toString(),
                lote.getText().toString(),
                valor.getText().toString(),
                imageUrl,  // Usa la URL de la imagen
                molierString // Mantén molier como String para el objeto Producto
        );

        // Guardar el producto en Firestore en la colección "PRODUCTOSWP" usando el molier como ID
        firestore.collection("PRODUCTOSWP")
                .document(molierString) // Usar molier como ID del documento
                .set(producto)  // Guarda el objeto producto con el ID especificado
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Agregar_.this, "Producto agregado exitosamente a Firestore", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    String errorMessage = "Error al guardar en Firestore: " + e.getMessage();
                    Toast.makeText(Agregar_.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", errorMessage, e); // Agrega el objeto de excepción para más detalles
                    progressBar.setVisibility(View.GONE);
                });
    }
}

