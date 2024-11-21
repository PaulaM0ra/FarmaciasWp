package com.example.farmaciaswp;

import com.google.gson.annotations.SerializedName;

public class TransactionPurchaseUnit {

    @SerializedName("amount")
    private TransactionAmount amount;

    public TransactionAmount getAmount() {
        return amount;
    }

    public void setAmount(TransactionAmount amount) {
        this.amount = amount;
    }
}
