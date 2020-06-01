package com.david.oauth.demo.oauthcommons.enums;

public enum GrantTypeEnum {

    AUTHORIZATION_CODE("authorization_code"),
    REFRESH_TOKEN("refresh_token");

    private String type;

    GrantTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static GrantTypeEnum getValueFromString(String type) {
        try {
            return GrantTypeEnum.valueOf(type.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
