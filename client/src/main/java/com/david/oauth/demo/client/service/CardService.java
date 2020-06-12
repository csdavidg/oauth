package com.david.oauth.demo.client.service;

import com.david.oauth.demo.client.dto.CardEnum;
import com.david.oauth.demo.client.dto.ViewDTO;
import com.david.oauth.demo.oauthcommons.enums.ResponseTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final OauthService oauthService;

    @Autowired
    public CardService(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    public List<ViewDTO> buildListByCards(CardEnum... cardEnums) {

        List<ViewDTO> cards = new ArrayList<>();

        for (CardEnum cardEnum : cardEnums) {
            switch (cardEnum) {
                case AUTHORIZATION_CODE:
                    cards.add(ViewDTO.builder()
                            .cardEnum(CardEnum.AUTHORIZATION_CODE)
                            .cardTitle("Authorization Code")
                            .cardDescription("Click this button to get an authorization code")
                            .buttonAction(oauthService.getAuthorizationCodeURI(ResponseTypeEnum.CODE))
                            .buttonMessage("Authorization Code")
                            .imagePath("/img/seq-1")
                            .imageAlt("Sequence diagram 1")
                            .build());
                    break;
                case ACCESS_TOKEN:
                    cards.add(ViewDTO.builder()
                            .cardEnum(CardEnum.ACCESS_TOKEN)
                            .cardTitle("Access Token")
                            .cardDescription("Click this button to get the access token")
                            .buttonAction("/authorization")
                            .buttonMessage("Access Token")
                            .imagePath("/img/seq-2")
                            .imageAlt("Sequence diagram 2")
                            .build());
                    break;
                case REFRESH_TOKEN:
                    cards.add(ViewDTO.builder()
                            .cardEnum(CardEnum.REFRESH_TOKEN)
                            .cardTitle("Refresh Token")
                            .cardDescription("Click this button to get a new access token using refresh token")
                            .buttonAction("/refresh")
                            .buttonMessage("Refresh Token")
                            .imagePath("/img/seq-3")
                            .imageAlt("Sequence diagram 3")
                            .build());
                    break;
                case LIST_EMPLOYEES:
                    cards.add(ViewDTO.builder()
                            .cardEnum(CardEnum.LIST_EMPLOYEES)
                            .cardTitle("Protected Resource")
                            .cardDescription("Click this button to get an employees list from a protected resource")
                            .buttonAction("/protected")
                            .buttonMessage("Employees")
                            .imagePath("/img/seq-4")
                            .imageAlt("Sequence diagram 4")
                            .build());
                    break;
                default:
                    break;
            }
        }

        return cards.stream().sorted(Comparator.comparing(ViewDTO::getCardEnum)).collect(Collectors.toList());
    }

    public List<ViewDTO> disableUnUsedCardsAndRemoveDuplicate(List<ViewDTO> cards, CardEnum... cardEnums) {
        cards.stream().filter(a -> !Arrays.asList(cardEnums).contains(a.getCardEnum()))
                .forEach(b -> b.setButtonClass("disabled"));
        return cards.stream().distinct().collect(Collectors.toList());
    }


}
