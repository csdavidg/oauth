package com.david.oauth.demo.client.dto;

import lombok.Getter;

@Getter
public enum CardEnum {

    AUTHORIZATION_CODE(1),
    ACCESS_TOKEN(2),
    REFRESH_TOKEN(3),
    LIST_EMPLOYEES(4);

    private int id;

    CardEnum(int id) {
        this.id = id;
    }

}
