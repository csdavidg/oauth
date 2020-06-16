package com.david.oauth.demo.client.service;

import com.david.oauth.demo.client.dto.CardEnum;
import com.david.oauth.demo.client.dto.ViewDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    public List<ViewDTO> buildListByCards(CardEnum... cardEnums) {

        List<ViewDTO> cards = new ArrayList<>();

        for (CardEnum cardEnum : cardEnums) {
            switch (cardEnum) {
                case AUTHORIZATION_CODE:
                    cards.add(ViewDTO.builder()
                            .cardEnum(CardEnum.AUTHORIZATION_CODE)
                            .cardTitle("Authorization Code")
                            .cardDescription("Click this button to get an authorization code")
                            .buttonAction("/authorization")
                            .buttonMessage("Authorization Code")
                            .imagePath("/img/seq-1")
                            .imageAlt("Sequence diagram authorization code")
                            .build());
                    break;
                case ACCESS_TOKEN:
                    cards.add(ViewDTO.builder()
                            .cardEnum(CardEnum.ACCESS_TOKEN)
                            .cardTitle("Access Token")
                            .cardDescription("Click this button to get the access token")
                            .buttonAction("/token")
                            .buttonMessage("Access Token")
                            .imagePath("/img/seq-2")
                            .imageAlt("Sequence diagram access token")
                            .build());
                    break;
                case CREDENTIALS_ACCESS_TOKEN:
                    cards.add(ViewDTO.builder()
                            .cardEnum(CardEnum.CREDENTIALS_ACCESS_TOKEN)
                            .cardTitle("Access Token")
                            .cardDescription("Click this button to get the access token")
                            .buttonAction("/authorization")
                            .buttonMessage("Access Token")
                            .imagePath("/img/seq-credentials-1")
                            .imageAlt("Sequence diagram access token with credentials")
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
                            .imageAlt("Sequence diagram refresh token")
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
                            .imageAlt("Sequence diagram list employees")
                            .build());
                    break;
                default:
                    break;
            }
        }

        return cards.stream().sorted(Comparator.comparing(ViewDTO::getCardEnum)).collect(Collectors.toList());
    }

    public List<ViewDTO> disableUnUsedCardsAndRemoveDuplicate(List<ViewDTO> cards, String path, CardEnum... cardEnums) {
        cards.stream().filter(a -> !Arrays.asList(cardEnums).contains(a.getCardEnum()))
                .forEach(b -> b.setButtonClass("disabled"));
        cards.stream().filter(c -> !c.getButtonAction().startsWith(path)).forEach(c -> c.setButtonAction(path.concat(c.getButtonAction())));
        return cards.stream().distinct().collect(Collectors.toList());
    }


}
