package com.example.farmaciaswp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productos;

    // Constructor del adaptador que recibe la lista de productos
    public ProductoAdapter(List<Producto> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño del item de producto
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto_card, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        // Obtener el producto en la posición actual
        Producto producto = productos.get(position);


        // Usar Glide para cargar la imagen desde la URL
        Glide.with(holder.itemView.getContext())
                .load(producto.getImagen()) // Aquí se usa la URL de la imagen
                .into(holder.imgProducto); // Carga la imagen en el ImageView

        // Asignar los valores del producto a las vistas correspondientes

        holder.txtNombreComercial.setText(producto.getNombreComercial());
        holder.txtNombreGenerico.setText(producto.getNombreGenerico());
        holder.txtConcentracion.setText(producto.getConcentracion());
        holder.txtPresentacion.setText(producto.getPresentacion());
        holder.txtRegistroInvima.setText(producto.getRegistroInvima());
        holder.txtLote.setText(producto.getLote());
        holder.txtValor.setText(producto.getValor());

        // Configurar el listener del botón "Adquirir"
        holder.buttonAdquirir.setOnClickListener(v -> {
            // Lógica para adquirir el producto
            Toast.makeText(v.getContext(), "Producto adquirido: " + producto.getNombreComercial(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        // Devolver el número de productos en la lista
        return productos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        // Definición de las vistas en el item de producto
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
