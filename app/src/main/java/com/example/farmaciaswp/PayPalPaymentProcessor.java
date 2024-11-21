package com.example.farmaciaswp;

import okhttp3.*;
import android.util.Log;
import java.io.IOException;

public class PayPalPaymentProcessor {

    // Método para capturar el pago
    public static void capturePayment(String captureLink) {
        OkHttpClient client = new OkHttpClient();

        try {
            // Obtener el token de acceso de PayPal
            String accessToken = PayPalAuthenticator.getAccessToken();

            // Crear la solicitud para capturar el pago
            Request request = new Request.Builder()
                    .url(captureLink)  // La URL de captura
                    .header("Authorization", "Bearer " + accessToken)  // Usamos el token obtenido
                    .post(RequestBody.create("", MediaType.get("application/json; charset=utf-8")))  // Cuerpo vacío
                    .build();

            // Ejecutar la solicitud de manera asíncrona
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // Manejo de error en la captura
                    Log.e("PayPalPayment", "Error al capturar el pago: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        // Procesar la respuesta si la captura es exitosa
                        String responseBody = response.body().string();
                        Log.d("PayPalPayment", "Pago capturado exitosamente: " + responseBody);
                    } else {
                        // Manejo de error si la captura falla
                        Log.e("PayPalPayment", "Error en la captura del pago: " + response.code());
                    }
                }
            });

        } catch (IOException e) {
            // Manejar el error al obtener el token de acceso
            Log.e("PayPalPayment", "Error al obtener el token de acceso: " + e.getMessage());
        }
    }
}
