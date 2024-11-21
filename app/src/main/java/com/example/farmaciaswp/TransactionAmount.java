package com.example.farmaciaswp;

import com.google.gson.annotations.SerializedName;

public class TransactionAmount {

    @SerializedName("currency_code")
    private String currencyCode = "USD"; // Cambia si es necesario

    @SerializedName("value")
    private String value;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
