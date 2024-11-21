package com.example.farmaciaswp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Field;

public interface PayPalService {
    // Endpoint para obtener el token de acceso
    @FormUrlEncoded
    @POST("v1/oauth2/token")
    Call<AccessTokenResponse> getAccessToken(
            @Header("Authorization") String basicAuth,
            @Field("grant_type") String grantType
    );

    // Endpoint para crear una orden
    @POST("v2/checkout/orders")
    Call<TransactionResponse> createOrder(
            @Header("Authorization") String bearerToken,
            @Body TransactionRequest request
    );
}
