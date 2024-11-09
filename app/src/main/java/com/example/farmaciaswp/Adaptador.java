package com.example.farmaciaswp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ProductoViewHolder> {

    private List<ProductoCarrito> carritoList;
    private Context context;

    // Constructor del adaptador que recibe el contexto y la lista de productos
    public Adaptador(Context context, List<ProductoCarrito> carritoList) {
        this.context = context;
        this.carritoList = carritoList;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout item_carrito.xml
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new ProductoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        ProductoCarrito producto = carritoList.get(position);

        // Establecer los valores del producto en las vistas
        holder.txtNombreComercial.setText(producto.getNombreComercial());
        holder.txtPresentacion.setText(producto.getPresentacion());
        holder.txtValor.setText(String.valueOf(producto.getValor()));

        // Cargar la imagen usando Glide
        Glide.with(holder.imgProducto.getContext())
                .load(producto.getImagen())
                .into(holder.imgProducto);

        // Configurar el botón de eliminar
        holder.buttonEliminar.setOnClickListener(v -> {
            // Eliminar el producto de la lista y de SharedPreferences
            eliminarProductoDeStorage(producto);
            carritoList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return carritoList.size();
    }

    // Método para eliminar el producto de SharedPreferences
    private void eliminarProductoDeStorage(ProductoCarrito producto) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CarritoLocal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(producto.getNombreComercial());
        editor.apply();
    }

    // ViewHolder que contiene las vistas de cada elemento del RecyclerView
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreComercial, txtPresentacion, txtValor;
        ImageView imgProducto;
        Button buttonEliminar;

        public ProductoViewHolder(View itemView) {
            super(itemView);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            txtNombreComercial = itemView.findViewById(R.id.txtNombreComercial);
            txtPresentacion = itemView.findViewById(R.id.txtPresentacion);
            txtValor = itemView.findViewById(R.id.txtValor);
            buttonEliminar = itemView.findViewById(R.id.buttonEliminar);
        }
    }
}
