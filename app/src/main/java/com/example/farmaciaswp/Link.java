package com.example.farmaciaswp;

import com.google.gson.annotations.SerializedName;

public class Link {

    @SerializedName("href")
    private String href;

    @SerializedName("rel")
    private String rel;

    public String getHref() {
        return href;
    }

    public String getRel() {
        return rel;
    }
}
