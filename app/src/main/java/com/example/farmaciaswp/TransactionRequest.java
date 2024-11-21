package com.example.farmaciaswp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TransactionRequest {

    @SerializedName("intent")
    private String intent = "CAPTURE"; // PayPal admite "CAPTURE" o "AUTHORIZE"

    @SerializedName("purchase_units")
    private List<TransactionPurchaseUnit> purchaseUnits;

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public List<TransactionPurchaseUnit> getPurchaseUnits() {
        return purchaseUnits;
    }

    public void setPurchaseUnits(List<TransactionPurchaseUnit> purchaseUnits) {
        this.purchaseUnits = purchaseUnits;
    }
}
