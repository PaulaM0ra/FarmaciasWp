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


        SharedPreferences sharedPreferences = getSharedPreferences("CarritoLocal", MODE_PRIVATE);
        Gson gson = new Gson();
        for (String clave : sharedPreferences.getAll().keySet()) {
            String productoJson = sharedPreferences.getString(clave, null);
            ProductoCarrito producto = gson.fromJson(productoJson, ProductoCarrito.class);
            if (producto != null) {
                carritoList.add(producto);
                total += producto.getValor() * producto.getCantidad();
            }
        }


        txtTotal.setText("Total: $" + String.format("%.2f", total));
        adaptador.notifyDataSetChanged();


        btnFinalizarCompra.setOnClickListener(v -> finalizarCompra());
    }

    @Override
    public void onProductoEliminado(ProductoCarrito producto) {

        total -= producto.getValor() * producto.getCantidad();
        txtTotal.setText("Total: $" + String.format("%.2f", total));
    }

    @Override
    public void onCantidadCambiada() {

        total = 0;
        for (ProductoCarrito producto : carritoList) {
            total += producto.getValor() * producto.getCantidad();
        }
        txtTotal.setText("Total: $" + String.format("%.2f", total));
    }

    private void finalizarCompra() {

        Toast.makeText(this, "Compra finalizada por un total de $" + String.format("%.2f", total), Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("CarritoLocal", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();


        Intent intent = new Intent(CARRO.this, PayPalPaymentActivity.class);
        startActivity(intent);
        finish();
    }
}
