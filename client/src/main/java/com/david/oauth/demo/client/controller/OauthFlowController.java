package com.david.oauth.demo.client.controller;

import com.david.oauth.demo.client.dto.CardEnum;
import com.david.oauth.demo.client.dto.ViewDTO;
import com.david.oauth.demo.client.service.CardService;
import com.david.oauth.demo.client.service.EmployeeService;
import com.david.oauth.demo.client.service.OauthService;
import com.david.oauth.demo.oauthcommons.entity.Employee;
import com.david.oauth.demo.oauthcommons.enums.ResponseTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/code")
public class OauthFlowController {

    private final String AUTHORIZATION_CODE_PAGE = "authorization-code";
    private final String PATH = "/code";

    private final OauthService oauthService;
    private final EmployeeService employeeService;
    private final CardService cardService;
    private List<ViewDTO> cards;

    @Autowired
    public OauthFlowController(OauthService oauthService, EmployeeService employeeService, CardService cardService) {
        this.oauthService = oauthService;
        this.employeeService = employeeService;
        this.cardService = cardService;
    }

    @GetMapping("/")
    public String index(Model model) {
        try {
            cards = cardService.buildListByCards(CardEnum.AUTHORIZATION_CODE);
            model.addAttribute("cards", cardService.disableUnUsedCardsAndRemoveDuplicate(cards, PATH, CardEnum.AUTHORIZATION_CODE));
        } catch (Exception e) {
            model.addAttribute("error", "Error loading authorization code flow page");
        }
        return AUTHORIZATION_CODE_PAGE;
    }

    @GetMapping("/authorization")
    public RedirectView authorizationCode(Model model) {
        try {
            String authorizationCodeUri = oauthService.getAuthorizationCodeURI(ResponseTypeEnum.CODE);
            return new RedirectView(authorizationCodeUri);
        } catch (Exception e) {
            model.addAttribute("error", "Unable to get the authorization code");
            return new RedirectView(AUTHORIZATION_CODE_PAGE);
        }
    }


    @GetMapping("/callback")
    public String callback(@RequestParam String code, @RequestParam String state, Model model) {
        try {
            oauthService.validateAndSaveAuthorizationCode(code, state);
            this.cards.addAll(cardService.buildListByCards(CardEnum.ACCESS_TOKEN));
            model.addAttribute("cards", cardService.disableUnUsedCardsAndRemoveDuplicate(cards, PATH, CardEnum.ACCESS_TOKEN));
        } catch (Exception e) {
            cards = cardService.buildListByCards(CardEnum.AUTHORIZATION_CODE);
            model.addAttribute("cards", cardService.disableUnUsedCardsAndRemoveDuplicate(cards, PATH, CardEnum.AUTHORIZATION_CODE));
            model.addAttribute("error", "Error saving the authorization code");
        }
        return AUTHORIZATION_CODE_PAGE;
    }

    @GetMapping("/token")
    public String accessToken(Model model) {

        try {
            oauthService.getAccessTokenUsingCode();
            cards.addAll(cardService.buildListByCards(CardEnum.LIST_EMPLOYEES, CardEnum.REFRESH_TOKEN));
            model.addAttribute("cards", cardService.disableUnUsedCardsAndRemoveDuplicate(cards, PATH, CardEnum.LIST_EMPLOYEES, CardEnum.REFRESH_TOKEN));
        } catch (Exception e) {
            cards = cardService.buildListByCards(CardEnum.AUTHORIZATION_CODE);
            model.addAttribute("cards", cardService.disableUnUsedCardsAndRemoveDuplicate(cards, PATH, CardEnum.AUTHORIZATION_CODE));
            model.addAttribute("error", "Unable getting access token");
        }
        return AUTHORIZATION_CODE_PAGE;
    }

    @GetMapping("/refresh")
    public String refreshToken(Model model) {
        try {
            oauthService.getAccessTokenUsingRefresh();
            cards.addAll(cardService.buildListByCards(CardEnum.LIST_EMPLOYEES, CardEnum.REFRESH_TOKEN));
            model.addAttribute("cards", cardService.disableUnUsedCardsAndRemoveDuplicate(cards, PATH, CardEnum.LIST_EMPLOYEES, CardEnum.REFRESH_TOKEN));
        } catch (Exception e) {
            cards = cardService.buildListByCards(CardEnum.AUTHORIZATION_CODE);
            model.addAttribute("cards", cardService.disableUnUsedCardsAndRemoveDuplicate(cards, PATH, CardEnum.AUTHORIZATION_CODE));
            model.addAttribute("error", "Unable getting access token");
        }
        return AUTHORIZATION_CODE_PAGE;
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
        return AUTHORIZATION_CODE_PAGE;
    }

    @DeleteMapping("/revoke")
    public String revokeAccessToken(Model model) {
        oauthService.revokeAccessToken();
        model.addAttribute("cards", cards);
        return AUTHORIZATION_CODE_PAGE;
    }


}
