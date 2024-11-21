package com.example.farmaciaswp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PayPalTransactionService {

    @POST("v2/checkout/orders")
    Call<TransactionResponse> createOrder(
            @Header("Authorization") String authorization, // Token de acceso
            @Body TransactionRequest request // Detalles de la transacción
    );

    // Nuevo método para verificar el pago
    @GET("v1/payments/payment")
    Call<TransactionVerificationResponse> verifyPayment(
            @Query("paymentId") String paymentId,
            @Query("payerId") String payerId
    );
}
