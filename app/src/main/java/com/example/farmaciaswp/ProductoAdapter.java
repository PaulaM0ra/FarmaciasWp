package com.example.farmaciaswp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productos;
    private FirebaseFirestore db;

    // Constructor
    public ProductoAdapter(List<Producto> productos) {
        this.productos = productos;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto_card, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);

        // Cargar la imagen usando Glide
        Glide.with(holder.itemView.getContext())
                .load(producto.getImagen())
                .into(holder.imgProducto);

        // Asignar valores a las vistas
        holder.txtNombreComercial.setText(producto.getNombreComercial());
        holder.txtNombreGenerico.setText(producto.getNombreGenerico());
        holder.txtConcentracion.setText(producto.getConcentracion());
        holder.txtPresentacion.setText(producto.getPresentacion());
        holder.txtRegistroInvima.setText(producto.getRegistroInvima());
        holder.txtLote.setText(producto.getLote());
        holder.txtValor.setText(producto.getValor());

        // Configurar el botón "Adquirir"
        holder.buttonAdquirir.setOnClickListener(v -> añadirProductoAlCarrito(v, producto));
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    // Método para añadir productos al carrito
    private void añadirProductoAlCarrito(View v, Producto producto) {
        Map<String, Object> productoMap = new HashMap<>();
        productoMap.put("nombreComercial", producto.getNombreComercial());
        productoMap.put("presentacion", producto.getPresentacion());
        productoMap.put("imagen", producto.getImagen());
        productoMap.put("valor", producto.getValor());

        db.collection("Carrito").add(productoMap)
                .addOnSuccessListener(documentReference -> {
                    guardarProductoLocal(v, productoMap, producto);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(v.getContext(), "Error al añadir producto al carrito: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Método para guardar producto localmente usando SharedPreferences
    private void guardarProductoLocal(View v, Map<String, Object> productoMap, Producto producto) {
        SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("CarritoLocal", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String productoJson = new Gson().toJson(productoMap);
        editor.putString("producto_" + producto.getNombreComercial(), productoJson);
        editor.apply();

        Toast.makeText(v.getContext(), "Producto añadido al carrito: " + producto.getNombreComercial(), Toast.LENGTH_SHORT).show();
    }

    // Método para actualizar la lista de productos en el adaptador
    public void actualizarLista(List<Producto> nuevaLista) {
        this.productos = nuevaLista;
        notifyDataSetChanged();
    }

    // Clase interna para el ViewHolder
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProducto;
        TextView txtNombreComercial;
        TextView txtNombreGenerico;
        TextView txtConcentracion;
        TextView txtPresentacion;
        TextView txtRegistroInvima;
        TextView txtLote;
        TextView txtValor;
        Button buttonAdquirir;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            txtNombreComercial = itemView.findViewById(R.id.txtNombreComercial);
            txtNombreGenerico = itemView.findViewById(R.id.txtNombreGenerico);
            txtConcentracion = itemView.findViewById(R.id.txtConcentracion);
            txtPresentacion = itemView.findViewById(R.id.txtPresentacion);
            txtRegistroInvima = itemView.findViewById(R.id.txtRegistroInvima);
            txtLote = itemView.findViewById(R.id.txtLote);
            txtValor = itemView.findViewById(R.id.txtValor);
            buttonAdquirir = itemView.findViewById(R.id.buttonAdquirir);
        }
    }
}
