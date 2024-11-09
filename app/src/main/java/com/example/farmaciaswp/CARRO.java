package com.example.farmaciaswp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class CARRO extends AppCompatActivity implements Adaptador.OnProductoEliminadoListener, Adaptador.OnCantidadCambiadaListener {

    private RecyclerView recyclerView;
    private Adaptador adaptador;
    private List<ProductoCarrito> carritoList;
    private TextView txtTotal;
    private Button btnFinalizarCompra;
    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carro);

        recyclerView = findViewById(R.id.recyclerViewCarrito);
        txtTotal = findViewById(R.id.txtTotal);
        btnFinalizarCompra = findViewById(R.id.btnFinalizarCompra);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        carritoList = new ArrayList<>();
        adaptador = new Adaptador(this, carritoList, this, this); // Pasamos la referencia de la actividad
        recyclerView.setAdapter(adaptador);

        // Cargar productos desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("CarritoLocal", MODE_PRIVATE);
        Gson gson = new Gson();
        for (String clave : sharedPreferences.getAll().keySet()) {
            String productoJson = sharedPreferences.getString(clave, null);
            ProductoCarrito producto = gson.fromJson(productoJson, ProductoCarrito.class);
            if (producto != null) {
                carritoList.add(producto);
                total += producto.getValor() * producto.getCantidad(); // Calculamos el total con la cantidad
            }
        }

        // Mostrar el total calculado
        txtTotal.setText("Total: $" + String.format("%.2f", total));
        adaptador.notifyDataSetChanged();

        // Configuración del botón "Finalizar Compra"
        btnFinalizarCompra.setOnClickListener(v -> finalizarCompra());
    }

    @Override
    public void onProductoEliminado(ProductoCarrito producto) {
        // Actualizar el total al eliminar un producto
        total -= producto.getValor() * producto.getCantidad(); // Descontamos la cantidad del total
        txtTotal.setText("Total: $" + String.format("%.2f", total));
    }

    @Override
    public void onCantidadCambiada() {
        // Actualizar el total cuando cambie la cantidad de cualquier producto
        total = 0; // Reiniciamos el total
        for (ProductoCarrito producto : carritoList) {
            total += producto.getValor() * producto.getCantidad(); // Recalculamos el total
        }
        txtTotal.setText("Total: $" + String.format("%.2f", total));
    }

    private void finalizarCompra() {
        // Mostrar mensaje de confirmación y cambiar de actividad
        Toast.makeText(this, "Compra finalizada por un total de $" + String.format("%.2f", total), Toast.LENGTH_SHORT).show();

        // Borrar todos los productos del carrito en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("CarritoLocal", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Limpia todo el almacenamiento del carrito
        editor.apply();

        // Redirigir a la actividad de registro del cliente
        Intent intent = new Intent(CARRO.this, RegisClientActivity.class);
        startActivity(intent);
        finish(); // Cierra la actividad actual
    }
}
