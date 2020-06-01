package com.david.oauth.demo.oauthcommons.enums;

public enum ResponseTypeEnum {

    CODE("code");

    private String type;

    ResponseTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ResponseTypeEnum getValueFromString(String type) {
        return ResponseTypeEnum.valueOf(type.toUpperCase());
    }
}
