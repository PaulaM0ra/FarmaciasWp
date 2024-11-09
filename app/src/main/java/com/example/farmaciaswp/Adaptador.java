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
    private OnProductoEliminadoListener productoEliminadoListener;
    private OnCantidadCambiadaListener cantidadCambiadaListener;

    // Constructor del adaptador que recibe el contexto, la lista de productos, el listener para eliminación y el listener para cantidad
    public Adaptador(Context context, List<ProductoCarrito> carritoList, OnProductoEliminadoListener listener, OnCantidadCambiadaListener cantidadCambiadaListener) {
        this.context = context;
        this.carritoList = carritoList;
        this.productoEliminadoListener = listener;
        this.cantidadCambiadaListener = cantidadCambiadaListener;
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
        holder.txtValor.setText("$" + String.format("%.2f", producto.getValor() * producto.getCantidad()));

        // Cargar la imagen usando Glide
        Glide.with(holder.imgProducto.getContext())
                .load(producto.getImagen())
                .into(holder.imgProducto);

        // Mostrar la cantidad en el TextView
        holder.txtCantidad.setText(String.valueOf(producto.getCantidad()));

        // Configurar el botón de eliminar
        holder.buttonEliminar.setOnClickListener(v -> {
            // Eliminar el producto de la lista y de SharedPreferences
            eliminarProductoDeStorage(producto);
            carritoList.remove(position);
            notifyItemRemoved(position);

            // Notificar a la actividad para que actualice el total
            if (productoEliminadoListener != null) {
                productoEliminadoListener.onProductoEliminado(producto);
            }
        });

        // Configurar el botón de "Menos"
        holder.btnMenos.setOnClickListener(v -> {
            if (producto.getCantidad() > 1) {
                producto.setCantidad(producto.getCantidad() - 1);
                holder.txtCantidad.setText(String.valueOf(producto.getCantidad()));
                notifyItemChanged(position);

                // Notificar a la actividad sobre el cambio de cantidad
                if (cantidadCambiadaListener != null) {
                    cantidadCambiadaListener.onCantidadCambiada();
                }
            }
        });

        // Configurar el botón de "Más"
        holder.btnMas.setOnClickListener(v -> {
            producto.setCantidad(producto.getCantidad() + 1);
            holder.txtCantidad.setText(String.valueOf(producto.getCantidad()));
            notifyItemChanged(position);

            // Notificar a la actividad sobre el cambio de cantidad
            if (cantidadCambiadaListener != null) {
                cantidadCambiadaListener.onCantidadCambiada();
            }
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
        editor.remove(producto.getNombreComercial()); // Usa el nombre como clave
        editor.apply();
    }

    // Interfaz para notificar a la actividad que un producto ha sido eliminado
    public interface OnProductoEliminadoListener {
        void onProductoEliminado(ProductoCarrito producto);
    }

    // Interfaz para notificar a la actividad sobre el cambio de cantidad
    public interface OnCantidadCambiadaListener {
        void onCantidadCambiada();
    }

    // ViewHolder que contiene las vistas de cada elemento del RecyclerView
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreComercial, txtPresentacion, txtValor, txtCantidad;
        ImageView imgProducto;
        Button buttonEliminar, btnMas, btnMenos;

        public ProductoViewHolder(View itemView) {
            super(itemView);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            txtNombreComercial = itemView.findViewById(R.id.txtNombreComercial);
            txtPresentacion = itemView.findViewById(R.id.txtPresentacion);
            txtValor = itemView.findViewById(R.id.txtValor);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            buttonEliminar = itemView.findViewById(R.id.buttonEliminar);
            btnMas = itemView.findViewById(R.id.btnMas);
            btnMenos = itemView.findViewById(R.id.btnMenos);
        }
    }
}

