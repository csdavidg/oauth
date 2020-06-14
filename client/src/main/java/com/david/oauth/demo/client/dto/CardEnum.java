package com.david.oauth.demo.client.dto;

import lombok.Getter;

@Getter
public enum CardEnum {

    AUTHORIZATION_CODE(1),
    CREDENTIALS_ACCESS_TOKEN(2),
    ACCESS_TOKEN(3),
    REFRESH_TOKEN(4),
    LIST_EMPLOYEES(5);

    private int id;

    CardEnum(int id) {
        this.id = id;
    }

}
