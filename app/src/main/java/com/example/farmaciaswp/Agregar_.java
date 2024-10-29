package com.example.farmaciaswp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Agregar_ extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText nombreComercial, nombreGenerico, concentracion, presentacion, registroInvima, lote, valor, molier;
    private Button buttonGuardar, buttonVolver, buttonSeleccionarImagen;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        // Inicializar Firebase Database y Storage
        databaseReference = FirebaseDatabase.getInstance().getReference("productos");
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

        // Botón para volver a la actividad anterior
        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Finaliza la actividad actual y vuelve atrás
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

    // Subir la imagen a Firebase Storage y guardar los datos en Firebase Database
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
                        // Obtener la URL de la imagen subida y guardar los datos en Firebase Database
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

    // Guardar los datos en Firebase Database con la URL de la imagen
    private void guardarDatosEnDatabase(String imageUrl) {
        String id = databaseReference.push().getKey();

        // Convertir el valor de molier a int
        int molierInt = Integer.parseInt(molier.getText().toString());

        // Crear un nuevo objeto Producto
        Producto producto = new Producto(
                nombreComercial.getText().toString(),
                nombreGenerico.getText().toString(),
                concentracion.getText().toString(),
                presentacion.getText().toString(),
                registroInvima.getText().toString(),
                lote.getText().toString(),
                valor.getText().toString(),
                R.drawable.default_image, // Puedes poner un recurso de imagen predeterminado o cambiarlo según tu lógica
                molierInt
        );

        // Guardar el producto en Firebase
        databaseReference.child(id).setValue(producto);
    }
}
