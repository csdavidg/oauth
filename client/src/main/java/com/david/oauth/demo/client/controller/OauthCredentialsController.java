package com.david.oauth.demo.client.controller;

import com.david.oauth.demo.client.dto.CardEnum;
import com.david.oauth.demo.client.dto.ViewDTO;
import com.david.oauth.demo.client.service.CardService;
import com.david.oauth.demo.client.service.EmployeeService;
import com.david.oauth.demo.client.service.OauthService;
import com.david.oauth.demo.oauthcommons.entity.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/credentials")
public class OauthCredentialsController {

    public final String CLIENT_CREDENTIALS_PAGE = "client-credentials";
    public final String PATH = "/credentials";

    private final CardService cardService;
    private final OauthService oauthService;
    private final EmployeeService employeeService;

    private List<ViewDTO> cards;

    public OauthCredentialsController(CardService cardService, OauthService oauthService, EmployeeService employeeService) {
        this.cardService = cardService;
        this.oauthService = oauthService;
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String index(Model model) {
        try {
            cards = cardService.buildListByCards(CardEnum.CREDENTIALS_ACCESS_TOKEN);
            model.addAttribute("cards", cardService.disableUnUsedCardsAndRemoveDuplicate(cards, PATH, CardEnum.CREDENTIALS_ACCESS_TOKEN));
            return CLIENT_CREDENTIALS_PAGE;
        } catch (Exception e) {
            model.addAttribute("error", "Unable to get the access token");
            return CLIENT_CREDENTIALS_PAGE;
        }
    }

    @GetMapping("/authorization")
    public String accessToken(Model model) {
        try {
            oauthService.getAccessTokenUsingCredentials();
            cards.addAll(cardService.buildListByCards(CardEnum.LIST_EMPLOYEES));
            model.addAttribute("cards", cardService.disableUnUsedCardsAndRemoveDuplicate(cards, PATH, CardEnum.LIST_EMPLOYEES));
        } catch (Exception e) {
            model.addAttribute("error", "Unable getting access token");
            return CLIENT_CREDENTIALS_PAGE;
        }
        return CLIENT_CREDENTIALS_PAGE;
    }

    @GetMapping("/protected")
    public String protectedReource(Model model) {
        List<Employee> employeeList = employeeService.getEmployeesFromAPI();
        if (employeeList != null) {

            model.addAttribute("employees", employeeList);
            model.addAttribute("cards", cardService.disableUnUsedCardsAndRemoveDuplicate(cards, PATH, CardEnum.LIST_EMPLOYEES, CardEnum.REFRESH_TOKEN));
        } else {

            cards = cardService.buildListByCards(CardEnum.AUTHORIZATION_CODE, CardEnum.ACCESS_TOKEN, CardEnum.REFRESH_TOKEN);
            model.addAttribute("cards", cardService.disableUnUsedCardsAndRemoveDuplicate(cards, PATH, CardEnum.REFRESH_TOKEN));
            model.addAttribute("error", "You are not authorized to access this resource");
        }
        return CLIENT_CREDENTIALS_PAGE;
    }

}
