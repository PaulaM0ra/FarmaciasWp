package com.example.farmaciaswp;

public interface PayPalCallback {
    void onSuccess(String orderId);
    void onFailure(String errorMessage);
}
