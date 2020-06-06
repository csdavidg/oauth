package com.david.oauth.demo.client.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ViewDTO {

    private CardEnum cardEnum;
    private String cardTitle;
    private String cardDescription;
    private String cardError;

    private String buttonAction;
    private String buttonClass;
    private String buttonMessage;

    private String imagePath;
    private String imageAlt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ViewDTO viewDTO = (ViewDTO) o;

        if (cardEnum != viewDTO.cardEnum) return false;
        return cardTitle != null ? cardTitle.equals(viewDTO.cardTitle) : viewDTO.cardTitle == null;
    }

    @Override
    public int hashCode() {
        int result = cardEnum.getId();
        result = 31 * result + (cardTitle != null ? cardTitle.hashCode() : 0);
        return result;
    }
}
