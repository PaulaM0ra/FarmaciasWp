package com.example.farmaciaswp;
import android.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Collections;


public class PayPalManager {
    private static final String CLIENT_ID = "AdMtfWtNXB_UhPfLe4AN7y24OHk27byiRBan_UurZk41J6eklXwpsIiI_OjMeURk7-CIPjJtBG7BURZ6";
    private static final String CLIENT_SECRET = "EJq1t4EkbCbiHLV89erKkS-6oNbBuk_qsrJqer9KMEkFif2KW_zA8TPp7aY2MfGUUbTirffcx8JddykI";
    private static final String BASE_URL = "https://api-m.sandbox.paypal.com/";

    public void authenticateAndPay(String amount, PayPalCallback callback) {
        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        String basicAuth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        PayPalService service = PayPalClient.getInstance().create(PayPalService.class);
        Call<AccessTokenResponse> call = service.getAccessToken(basicAuth, "client_credentials");

        call.enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                if (response.isSuccessful()) {
                    String accessToken = response.body().getAccessToken();
                    createTransaction(amount, accessToken, callback);
                } else {
                    callback.onFailure("Error autenticando con PayPal");
                }
            }

            @Override
            public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                callback.onFailure("Fallo en la conexión: " + t.getMessage());
            }
        });
    }

    private void createTransaction(String amount, String accessToken, PayPalCallback callback) {
        PayPalTransactionService transactionService = PayPalClient.getInstance().create(PayPalTransactionService.class);

        TransactionAmount transactionAmount = new TransactionAmount();
        transactionAmount.setValue(amount);

        TransactionPurchaseUnit purchaseUnit = new TransactionPurchaseUnit();
        purchaseUnit.setAmount(transactionAmount);

        TransactionRequest request = new TransactionRequest();
        request.setPurchaseUnits(Collections.singletonList(purchaseUnit));


        String bearerToken = "Bearer " + accessToken;

        Call<TransactionResponse> transactionCall = transactionService.createOrder(bearerToken, request);

        transactionCall.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                if (response.isSuccessful()) {
                    String orderId = response.body().getId();
                    callback.onSuccess(orderId);
                } else {
                    callback.onFailure("Error procesando el pago");
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                callback.onFailure("Fallo en la conexión: " + t.getMessage());
            }
        });
    }
}
