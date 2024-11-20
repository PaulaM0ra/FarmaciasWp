package com.example.farmaciaswp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Catalogo_Productos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductoAdapter productoAdapter;
    private List<Producto> listaProductos;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catalogo_productos);


        db = FirebaseFirestore.getInstance();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaProductos = new ArrayList<>();
        cargarProductos();

        productoAdapter = new ProductoAdapter(listaProductos);
        recyclerView.setAdapter(productoAdapter);
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Opcional: Acción al presionar "Buscar"
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarProductos(newText); // Filtra en tiempo real
                return true;
            }
        });

        Button buttonCRUD = findViewById(R.id.button5);
        buttonCRUD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Catalogo_Productos.this, ProductActivity.class);
                startActivity(intent);
            }
        });

        Button buttonCarrito = findViewById(R.id.button3);
        buttonCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Catalogo_Productos.this, CARRO.class);
                startActivity(intent);
            }
        });

        Button buttonmapa = findViewById(R.id.button6);
        buttonmapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Catalogo_Productos.this, LocationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void filtrarProductos(String texto) {
        List<Producto> productosFiltrados = new ArrayList<>();
        for (Producto producto : listaProductos) {
            if (producto.getNombreComercial().toLowerCase().contains(texto.toLowerCase())
                    || producto.getNombreGenerico().toLowerCase().contains(texto.toLowerCase())) {
                productosFiltrados.add(producto);
            }
        }

        productoAdapter.actualizarLista(productosFiltrados);
    }

    private void cargarProductos() {
        CollectionReference productosRef = db.collection("PRODUCTOSWP");

        productosRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    Producto producto = document.toObject(Producto.class);
                    producto.setMolier(document.getId());
                    listaProductos.add(producto);
                }
                productoAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(Catalogo_Productos.this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
