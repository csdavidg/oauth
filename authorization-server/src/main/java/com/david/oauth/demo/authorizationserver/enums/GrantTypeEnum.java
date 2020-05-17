package com.david.oauth.demo.authorizationserver.enums;

public enum GrantTypeEnum {

    AUTHORIZATION_CODE("authorization_code");

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
