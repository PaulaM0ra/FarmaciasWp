package com.example.farmaciaswp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class CarStoreActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<ProductoCarrito> productosCarrito;
    private RecyclerView recyclerView;
    private CarritoAdapter carritoAdapter;
    private TextView txtTotalCompra;
    private Button btnFinalizarCompra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_store);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerCarrito);
        txtTotalCompra = findViewById(R.id.txtTotalCompra);
        btnFinalizarCompra = findViewById(R.id.btnFinalizarCompra);

        // Configuración de RecyclerView
        productosCarrito = new ArrayList<>();
        carritoAdapter = new CarritoAdapter(productosCarrito);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(carritoAdapter);

        // Cargar los productos del carrito desde Firestore
        cargarProductosCarrito();

        // Finalizar compra
        btnFinalizarCompra.setOnClickListener(v -> {
            double total = calcularTotal();
            txtTotalCompra.setText("Total: " + total);
            Toast.makeText(this, "Compra finalizada: " + total, Toast.LENGTH_SHORT).show();
            // Redirigir al cliente para registrar sus datos
            Intent intent = new Intent(CarStoreActivity.this, RegisClientActivity.class);
            startActivity(intent);
        });
    }

    private void cargarProductosCarrito() {
        db.collection("Carrito")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productosCarrito.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombreComercial = document.getString("nombreComercial");
                            String presentacion = document.getString("presentacion");
                            String imagen = document.getString("imagen");
                            String molier = document.getString("molier");
                            double valor = document.getDouble("valor");
                            productosCarrito.add(new ProductoCarrito(nombreComercial, presentacion, imagen, molier, valor, 1));
                        }
                        carritoAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error al cargar carrito", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private double calcularTotal() {
        double total = 0;
        for (ProductoCarrito producto : productosCarrito) {
            total += producto.getValor() * producto.getCantidad();
        }
        return total;
    }
}
