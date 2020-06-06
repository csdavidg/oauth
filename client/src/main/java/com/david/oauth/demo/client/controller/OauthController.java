package com.david.oauth.demo.client.controller;

import com.david.oauth.demo.client.dto.CardEnum;
import com.david.oauth.demo.client.dto.ViewDTO;
import com.david.oauth.demo.client.service.CardService;
import com.david.oauth.demo.client.service.EmployeeService;
import com.david.oauth.demo.client.service.OauthService;
import com.david.oauth.demo.oauthcommons.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OauthController {

    private final OauthService oauthService;
    private final EmployeeService employeeService;
    private final CardService cardService;
    private List<ViewDTO> cards;

    @Autowired
    public OauthController(OauthService oauthService, EmployeeService employeeService, CardService cardService) {
        this.oauthService = oauthService;
        this.employeeService = employeeService;
        this.cardService = cardService;
    }

    @GetMapping("/")
    public String index(Model model) {
        try {
            cards = cardService.buildListByCards(CardEnum.AUTHORIZATION_CODE);
            model.addAttribute("cards", cards);
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Unable to get the authorization code");
            return "index";
        }
    }

    @GetMapping("/callback")
    public String callback(@RequestParam String code, @RequestParam String state, Model model) {
        try {
            oauthService.validateAndSaveAuthorizationCode(code, state);
            this.cards.addAll(cardService.buildListByCards(CardEnum.ACCESS_TOKEN));
            model.addAttribute("cards", cardService.disableUnUsedCardsAndRemoveDuplicate(cards, CardEnum.ACCESS_TOKEN));
        } catch (Exception e) {
            model.addAttribute("error", "Error saving the authorization code");
            return "index";
        }
        return "index";
    }

    @GetMapping("/authorization")
    public String accessToken(Model model) {
        try {
            oauthService.getAccessToken();
            cards.addAll(cardService.buildListByCards(CardEnum.LIST_EMPLOYEES, CardEnum.REFRESH_TOKEN));
            model.addAttribute("cards", cardService.disableUnUsedCardsAndRemoveDuplicate(cards, CardEnum.LIST_EMPLOYEES, CardEnum.REFRESH_TOKEN));
        } catch (Exception e) {
            model.addAttribute("error", "Unable getting access token");
            return "index";
        }
        return "index";
    }

    @GetMapping("/refresh")
    public String refreshToken(Model model) {
        try {
            oauthService.getAccessTokenUsingRefresh();
            model.addAttribute("cards", cards);
        } catch (Exception e) {
            model.addAttribute("error", "Unable getting access token");
            return "index";
        }
        return "index";
    }

    @GetMapping("/protected")
    public String protectedReource(Model model) {
        List<Employee> employeeList = employeeService.getEmployeesFromAPI();
        if (employeeList != null) {

            model.addAttribute("employees", employeeList);
            model.addAttribute("cards", cards);
        } else {

            List<ViewDTO> cards = cardService.buildListByCards(CardEnum.AUTHORIZATION_CODE, CardEnum.ACCESS_TOKEN, CardEnum.REFRESH_TOKEN);
            model.addAttribute("cards", cardService.disableUnUsedCardsAndRemoveDuplicate(cards, CardEnum.REFRESH_TOKEN));
            model.addAttribute("error", "You are not authorized to access this resource");
        }
        return "index";
    }

    @DeleteMapping("/revoke")
    public String revokeAccessToken(Model model) {
        oauthService.revokeAccessToken();
        model.addAttribute("cards", cards);
        return "index";
    }


}
