package com.example.farmaciaswp;

import android.util.Base64;
import okhttp3.*;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;

public class PayPalAuthenticator {

    private static final String CLIENT_ID = "AdMtfWtNXB_UhPfLe4AN7y24OHk27byiRBan_UurZk41J6eklXwpsIiI_OjMeURk7-CIPjJtBG7BURZ6";  // Reemplaza con tu Client ID
    private static final String SECRET = "EJq1t4EkbCbiHLV89erKkS-6oNbBuk_qsrJqer9KMEkFif2KW_zA8TPp7aY2MfGUUbTirffcx8JddykI";  // Reemplaza con tu Secret

    // Obtén el access token de PayPal
    public static String getAccessToken() throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Cuerpo de la solicitud para obtener el token
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();

        // Solicitud a PayPal para obtener el token
        Request request = new Request.Builder()
                .url("https://api.sandbox.paypal.com/v1/oauth2/token")
                .header("Authorization", "Basic " + getBase64EncodedCredentials())
                .post(body)
                .build();

        // Ejecución de la solicitud
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            // Parsear la respuesta JSON y obtener el token
            String jsonResponse = response.body().string();
            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                return jsonObject.getString("access_token");
            } catch (JSONException e) {
                throw new IOException("Error al procesar la respuesta JSON", e);
            }
        } else {
            throw new IOException("Failed to get access token");
        }
    }

    // Codificar las credenciales en Base64
    private static String getBase64EncodedCredentials() {
        String credentials = CLIENT_ID + ":" + SECRET;
        return Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }
}

