package com.example.farmaciaswp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class CARRO extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adaptador adaptador;
    private List<ProductoCarrito> carritoList;  // Lista para los productos del carrito
    private TextView txtTotal;  // TextView para mostrar el total del carrito

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carro);

        // Inicializar componentes de la vista
        recyclerView = findViewById(R.id.recyclerViewCarrito);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        txtTotal = findViewById(R.id.txtTotal);  // Referencia al TextView para el total

        // Inicializar lista y adaptador
        carritoList = new ArrayList<>();
        adaptador = new Adaptador(this, carritoList);
        recyclerView.setAdapter(adaptador);

        // Cargar productos del carrito desde SharedPreferences
        cargarProductosCarrito();

        // Actualizar el total cuando se carga la lista
        actualizarTotal();

        adaptador.notifyDataSetChanged();
    }

    // Método para cargar productos desde SharedPreferences
    private void cargarProductosCarrito() {
        SharedPreferences sharedPreferences = getSharedPreferences("CarritoLocal", MODE_PRIVATE);
        Gson gson = new Gson();
        for (String clave : sharedPreferences.getAll().keySet()) {
            String productoJson = sharedPreferences.getString(clave, null);
            ProductoCarrito producto = gson.fromJson(productoJson, ProductoCarrito.class);
            if (producto != null) {
                carritoList.add(producto);
            }
        }
    }

    // Método para calcular y actualizar el total del carrito
    private void actualizarTotal() {
        double total = 0.0;
        for (ProductoCarrito producto : carritoList) {
            total += producto.getValor();  // Sumar el valor de cada producto en la lista
        }
        txtTotal.setText("Total: $" + String.format("%.2f", total));  // Mostrar el total en el TextView
    }

    // Método para eliminar un producto del carrito
    private void eliminarProducto(ProductoCarrito producto) {
        // Eliminar de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("CarritoLocal", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(producto.getNombreComercial());
        editor.apply();

        // Eliminar de la lista y actualizar la vista
        carritoList.remove(producto);
        adaptador.notifyDataSetChanged();

        // Actualizar el total después de eliminar el producto
        actualizarTotal();
        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
    }
}
