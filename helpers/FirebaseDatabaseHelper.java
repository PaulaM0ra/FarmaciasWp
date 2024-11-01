public class FirebaseDatabaseHelper {
    private DatabaseReference databaseReference;

    public FirebaseDatabaseHelper() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("productos");
    }

    // Método para buscar producto por número Molier
    public void obtenerProductoPorMolier(String molier, final ProductoCallback callback) {
        databaseReference.orderByChild("molier").equalTo(molier)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot productoSnapshot : snapshot.getChildren()) {
                                Producto producto = productoSnapshot.getValue(Producto.class);
                                callback.onCallback(producto, productoSnapshot.getKey());
                            }
                        } else {
                            callback.onCallback(null, null); // No se encontró el producto
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Error al obtener el producto", error.toException());
                    }
                });
    }

    // Método para actualizar un producto en Firebase
    public void actualizarProducto(String key, Producto producto) {
        databaseReference.child(key).setValue(producto)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Producto actualizado exitosamente"))
                .addOnFailureListener(e -> Log.e("Firebase", "Error al actualizar el producto", e));
    }

    public interface ProductoCallback {
        void onCallback(Producto producto, String key);
    }
}
