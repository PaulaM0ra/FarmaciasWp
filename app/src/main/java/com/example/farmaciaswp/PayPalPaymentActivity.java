package com.example.farmaciaswp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Collections;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.Intent;
import android.net.Uri;

public class PayPalPaymentActivity extends AppCompatActivity {

    private EditText etPaymentAmount;
    private Button btnProceedPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_payment);

        // Vincular elementos del diseño XML
        etPaymentAmount = findViewById(R.id.etPaymentAmount);
        btnProceedPayment = findViewById(R.id.btnProceedPayment);

        // Recuperar el valor del total enviado desde la actividad anterior
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("total_compra")) {
            double totalCompra = intent.getDoubleExtra("total_compra", 0.0);
            etPaymentAmount.setText(String.format("%.2f", totalCompra)); // Mostrar el monto en el campo de texto
        }

        // Configurar la acción del botón
        btnProceedPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el monto ingresado
                String amount = etPaymentAmount.getText().toString();

                // Verificar si el campo de monto está vacío
                if (amount.isEmpty()) {
                    Log.e("PayPalPayment", "Monto vacío. El usuario no ingresó un monto.");
                    Toast.makeText(PayPalPaymentActivity.this, "Por favor, ingrese un monto", Toast.LENGTH_SHORT).show();
                    return; // Detener el flujo si no hay monto
                }

                // Si el monto es válido, proceder con el pago
                Log.i("PayPalPayment", "Monto ingresado: " + amount);
                processPayment(amount);
            }
        });
    }
    private void processPayment(String amount) {
        // 3. Configurar las credenciales de PayPal (sandbox)
        String clientId = "AdMtfWtNXB_UhPfLe4AN7y24OHk27byiRBan_UurZk41J6eklXwpsIiI_OjMeURk7-CIPjJtBG7BURZ6";
        String clientSecret = "EJq1t4EkbCbiHLV89erKkS-6oNbBuk_qsrJqer9KMEkFif2KW_zA8TPp7aY2MfGUUbTirffcx8JddykI";
        String credentials = clientId + ":" + clientSecret;
        String basicAuth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Log.d("PayPalPayment", "Credenciales generadas: " + basicAuth); // Log para verificar que las credenciales se están generando correctamente.

        // 4. Llamar a la API de autenticación para obtener un token de acceso
        PayPalService service = PayPalClient.getInstance().create(PayPalService.class);
        Call<AccessTokenResponse> call = service.getAccessToken(basicAuth, "client_credentials");

        call.enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String accessToken = response.body().getAccessToken();
                    Log.i("PayPalPayment", "Token de acceso obtenido: " + accessToken); // Log de éxito
                    createOrder(accessToken, amount);
                } else {
                    Log.e("PayPalPayment", "Error al obtener el token de acceso. Código de respuesta: " + response.code());
                    Log.e("PayPalPayment", "Cuerpo de la respuesta: " + response.errorBody()); // Mostrar el error detallado
                    Toast.makeText(PayPalPaymentActivity.this, "Error al obtener el token de acceso", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                Log.e("PayPalPayment", "Fallo en la llamada de autenticación: " + t.getMessage(), t);
                Toast.makeText(PayPalPaymentActivity.this, "Error de conexión con PayPal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createOrder(String accessToken, String amount) {
        // 5. Crear la orden de pago
        PayPalTransactionService transactionService = PayPalClient.getInstance().create(PayPalTransactionService.class);

        TransactionRequest request = new TransactionRequest();
        TransactionPurchaseUnit purchaseUnit = new TransactionPurchaseUnit();
        TransactionAmount transactionAmount = new TransactionAmount();

        transactionAmount.setValue(amount);
        purchaseUnit.setAmount(transactionAmount);

        request.setPurchaseUnits(Collections.singletonList(purchaseUnit));

        Log.i("PayPalPayment", "Creando orden con monto: " + amount);

        // 6. Hacer la llamada para crear la orden
        Call<TransactionResponse> transactionCall = transactionService.createOrder(
                "Bearer " + accessToken,
                request
        );

        transactionCall.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String orderId = response.body().getId();
                    Log.i("PayPalPayment", "Orden creada exitosamente. ID de la orden: " + orderId);
                    Toast.makeText(PayPalPaymentActivity.this, "Orden creada con ID: " + orderId, Toast.LENGTH_SHORT).show();

                    // 7. Redirigir a PayPal para que el usuario complete el pago
                    String approvalLink = response.body().getLinks().stream()
                            .filter(link -> "approve".equals(link.getRel())) // Enlace para aprobación
                            .map(Link::getHref)
                            .findFirst()
                            .orElse(null);

                    if (approvalLink != null) {
                        // Redirigir a PayPal
                        Log.i("PayPalPayment", "Redirigiendo a PayPal: " + approvalLink);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(approvalLink));
                        startActivity(browserIntent); // Redirige al navegador o aplicación de PayPal

                        // 8. Obtener el enlace de captura (si es necesario en tu flujo de trabajo)
                        String captureLink = response.body().getLinks().stream()
                                .filter(link -> "capture".equals(link.getRel())) // Enlace para capturar el pago
                                .map(Link::getHref)
                                .findFirst()
                                .orElse(null);

                        if (captureLink != null) {
                            // Llamar a la función que procesará la captura del pago
                            PayPalPaymentProcessor.capturePayment(captureLink); // Llamada al método para capturar el pago
                        } else {
                            Log.e("PayPalPayment", "No se encontró el enlace de captura");
                        }
                    } else {
                        Log.e("PayPalPayment", "No se encontró el enlace de aprobación");
                    }

                } else {
                    Log.e("PayPalPayment", "Error al crear la orden de pago. Código de respuesta: " + response.code());
                    Log.e("PayPalPayment", "Cuerpo de la respuesta: " + response.errorBody()); // Mostrar el error detallado
                    Toast.makeText(PayPalPaymentActivity.this, "Error al crear la orden de pago", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                Log.e("PayPalPayment", "Fallo en la llamada para crear la orden: " + t.getMessage(), t);
                Toast.makeText(PayPalPaymentActivity.this, "Error al procesar la orden", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri data = getIntent().getData();
        if (data != null && data.toString().contains("paymentId") && data.toString().contains("PayerID")) {
            String paymentId = data.getQueryParameter("paymentId");
            String payerId = data.getQueryParameter("PayerID");

            if (paymentId != null && payerId != null) {
                // Llamar a verifyPayment() con los parámetros obtenidos
                verifyPayment(paymentId, payerId);
            }
        }
    }

    private void verifyPayment(String paymentId, String payerId) {
        // Llamada a la API de PayPal para verificar el estado del pago
        PayPalTransactionService transactionService = PayPalClient.getInstance().create(PayPalTransactionService.class);

        Log.i("PayPalPayment", "Verificando el pago con paymentId: " + paymentId + " y payerId: " + payerId);

        Call<TransactionVerificationResponse> call = transactionService.verifyPayment(paymentId, payerId);

        call.enqueue(new Callback<TransactionVerificationResponse>() {
            @Override
            public void onResponse(Call<TransactionVerificationResponse> call, Response<TransactionVerificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String paymentStatus = response.body().getPaymentStatus();
                    Log.i("PayPalPayment", "Pago verificado con estado: " + paymentStatus);
                    Toast.makeText(PayPalPaymentActivity.this, "Pago completado con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("PayPalPayment", "Error al verificar el pago. Código de respuesta: " + response.code());
                    Toast.makeText(PayPalPaymentActivity.this, "Error al verificar el pago", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransactionVerificationResponse> call, Throwable t) {
                Log.e("PayPalPayment", "Error al verificar el pago: " + t.getMessage(), t);
                Toast.makeText(PayPalPaymentActivity.this, "Error de verificación de pago", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
