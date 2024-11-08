package com.example.farmaciaswp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private List<ProductoCarrito> productosCarrito;

    public CarritoAdapter(List<ProductoCarrito> productosCarrito) {
        this.productosCarrito = productosCarrito;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        ProductoCarrito producto = productosCarrito.get(position);

        Glide.with(holder.itemView.getContext())
                .load(producto.getImagen())
                .into(holder.imgProducto);

        holder.txtNombreComercial.setText(producto.getNombreComercial());
        holder.txtPresentacion.setText(producto.getPresentacion());
        holder.txtValor.setText("Precio: " + producto.getValor());

        holder.editCantidad.setText(String.valueOf(producto.getCantidad()));
        holder.editCantidad.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                int cantidad = Integer.parseInt(holder.editCantidad.getText().toString());
                producto.setCantidad(cantidad);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productosCarrito.size();
    }

    public static class CarritoViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProducto;
        TextView txtNombreComercial;
        TextView txtPresentacion;
        TextView txtValor;
        EditText editCantidad;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            txtNombreComercial = itemView.findViewById(R.id.txtNombreComercial);
            txtPresentacion = itemView.findViewById(R.id.txtPresentacion);
            txtValor = itemView.findViewById(R.id.txtValor);
            editCantidad = itemView.findViewById(R.id.editCantidad);
        }
    }
}
