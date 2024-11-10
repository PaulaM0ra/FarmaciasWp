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


    public Adaptador(Context context, List<ProductoCarrito> carritoList, OnProductoEliminadoListener listener, OnCantidadCambiadaListener cantidadCambiadaListener) {
        this.context = context;
        this.carritoList = carritoList;
        this.productoEliminadoListener = listener;
        this.cantidadCambiadaListener = cantidadCambiadaListener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new ProductoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        ProductoCarrito producto = carritoList.get(position);


        holder.txtNombreComercial.setText(producto.getNombreComercial());
        holder.txtPresentacion.setText(producto.getPresentacion());
        holder.txtValor.setText("$" + String.format("%.2f", producto.getValor() * producto.getCantidad()));


        Glide.with(holder.imgProducto.getContext())
                .load(producto.getImagen())
                .into(holder.imgProducto);


        holder.txtCantidad.setText(String.valueOf(producto.getCantidad()));


        holder.buttonEliminar.setOnClickListener(v -> {

            eliminarProductoDeStorage(producto);
            carritoList.remove(position);
            notifyItemRemoved(position);

            if (productoEliminadoListener != null) {
                productoEliminadoListener.onProductoEliminado(producto);
            }
        });


        holder.btnMenos.setOnClickListener(v -> {
            if (producto.getCantidad() > 1) {
                producto.setCantidad(producto.getCantidad() - 1);
                holder.txtCantidad.setText(String.valueOf(producto.getCantidad()));
                notifyItemChanged(position);


                if (cantidadCambiadaListener != null) {
                    cantidadCambiadaListener.onCantidadCambiada();
                }
            }
        });


        holder.btnMas.setOnClickListener(v -> {
            producto.setCantidad(producto.getCantidad() + 1);
            holder.txtCantidad.setText(String.valueOf(producto.getCantidad()));
            notifyItemChanged(position);


            if (cantidadCambiadaListener != null) {
                cantidadCambiadaListener.onCantidadCambiada();
            }
        });
    }

    @Override
    public int getItemCount() {
        return carritoList.size();
    }


    private void eliminarProductoDeStorage(ProductoCarrito producto) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CarritoLocal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(producto.getNombreComercial());
        editor.apply();
    }

    public interface OnProductoEliminadoListener {
        void onProductoEliminado(ProductoCarrito producto);
    }


    public interface OnCantidadCambiadaListener {
        void onCantidadCambiada();
    }


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

