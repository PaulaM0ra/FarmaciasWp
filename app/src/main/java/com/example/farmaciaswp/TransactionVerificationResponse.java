package com.example.farmaciaswp;

import com.google.gson.annotations.SerializedName;

public class TransactionVerificationResponse {

    @SerializedName("paymentStatus")
    private String paymentStatus;  // Campo de estado del pago (ej. "COMPLETED")

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
