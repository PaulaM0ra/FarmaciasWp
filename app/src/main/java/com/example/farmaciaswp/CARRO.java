package com.example.farmaciaswp;

import android.content.SharedPreferences;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carro);

        recyclerView = findViewById(R.id.recyclerViewCarrito);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        carritoList = new ArrayList<>();  // Inicializamos la lista
        adaptador = new Adaptador(this, carritoList);  // Adaptador para mostrar los productos, pasando el contexto y la lista
        recyclerView.setAdapter(adaptador);

        // Recuperar productos guardados en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("CarritoLocal", MODE_PRIVATE);
        Gson gson = new Gson();
        for (String clave : sharedPreferences.getAll().keySet()) {
            String productoJson = sharedPreferences.getString(clave, null);
            ProductoCarrito producto = gson.fromJson(productoJson, ProductoCarrito.class);
            if (producto != null) {
                carritoList.add(producto);
            }
        }

        adaptador.notifyDataSetChanged();  // Actualizar el RecyclerView con los productos
    }

    // Método para eliminar el producto
    private void eliminarProducto(ProductoCarrito producto) {
        // Eliminar de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("CarritoLocal", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(producto.getNombreComercial());  // Usa la clave única para el producto
        editor.apply();  // Aplicar cambios

        // Eliminar del carrito en la lista
        carritoList.remove(producto);
        adaptador.notifyDataSetChanged();  // Actualizar la vista
        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
    }
}
