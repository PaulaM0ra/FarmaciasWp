package com.example.farmaciaswp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TransactionResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("status")
    private String status;

    @SerializedName("links")
    private List<Link> links;  // Agregar la lista de enlaces

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public List<Link> getLinks() {
        return links;
    }
}
