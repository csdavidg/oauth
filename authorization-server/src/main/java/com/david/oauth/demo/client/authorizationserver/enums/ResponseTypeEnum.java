package com.david.oauth.demo.client.authorizationserver.enums;

public enum ResponseTypeEnum {

    CODE("code");

    private String type;

    ResponseTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
