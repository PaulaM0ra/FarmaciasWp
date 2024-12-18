package com.example.farmaciaswp;

import com.google.gson.annotations.SerializedName;

public class AccessTokenResponse {

    @SerializedName("scope")
    private String scope;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("expires_in")
    private int expiresIn;

    @SerializedName("app_id")
    private String appId;

    public String getScope() {
        return scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getAppId() {
        return appId;
    }
}
