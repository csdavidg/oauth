package com.david.oauth.demo.client.controller;

import com.david.oauth.demo.client.config.OauthConfig;
import com.david.oauth.demo.client.dto.ViewDTO;
import com.david.oauth.demo.client.service.EmployeeService;
import com.david.oauth.demo.client.service.OauthService;
import com.david.oauth.demo.oauthcommons.entity.Employee;
import com.david.oauth.demo.oauthcommons.managers.KeyStoreManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OauthController {

    private static final String AS_AUTHORIZATION_CODE = "as_authorization_code";

    @Resource
    private OauthConfig oauthConfig;

    private final OauthService oauthService;
    private final KeyStoreManager keyStoreManager;
    private final EmployeeService employeeService;
    private List<ViewDTO> cards;

    @Autowired
    public OauthController(OauthService oauthService, KeyStoreManager keyStoreManager, EmployeeService employeeService) {
        this.oauthService = oauthService;
        this.keyStoreManager = keyStoreManager;
        this.employeeService = employeeService;
        this.cards = new ArrayList<>();
    }

    @GetMapping("/")
    public String index(Model model) {
        try {
            ViewDTO authorizationCodeCard = ViewDTO.builder()
                    .id(1)
                    .cardTitle("Authorization Code")
                    .cardDescription("Click this button to get an authorization code")
                    .buttonAction(oauthService.getAuthorizationCodeURI())
                    .buttonMessage("Authorization Code")
                    .imagePath("/img/seq-1.png")
                    .imageAlt("Sequence diagram 1")
                    .build();
            model.addAttribute("cards", this.buildCardList(authorizationCodeCard));
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

            ViewDTO accessTokenCard = ViewDTO.builder()
                    .id(2)
                    .cardTitle("Access Token")
                    .cardDescription("Click this button to get the access token")
                    .buttonAction("/authorization")
                    .buttonMessage("Access Token")
                    .imagePath("/img/seq-2.png")
                    .imageAlt("Sequence diagram 2")
                    .build();
            model.addAttribute("cards", this.buildCardList(accessTokenCard));
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
            ViewDTO protectedResourceCard = ViewDTO.builder()
                    .id(3)
                    .cardTitle("Protected Resource")
                    .cardDescription("Click this button to get an employees list from a protected resource")
                    .buttonAction("/protected")
                    .buttonMessage("Employees")
                    .imagePath("/img/seq-3.png")
                    .imageAlt("Sequence diagram 3")
                    .build();
            model.addAttribute("cards", this.buildCardList(protectedResourceCard));
        } catch (Exception e) {
            model.addAttribute("error", "Unable getting access token");
            return "index";
        }
        return "index";
    }

    @GetMapping("/protected")
    public String protectedReource(Model model) {
        List<Employee> employeeList = this.employeeService.getEmployeesFromAPI();
        model.addAttribute("cards", this.cards);
        if (employeeList != null) {
            model.addAttribute("employees", employeeList);
        } else {
            model.addAttribute("error", "You are not authorized to access this resource");
        }
        return "index";
    }

    private List<ViewDTO> buildCardList(ViewDTO card) {
        this.cards.remove(card);
        this.cards.add(card);
        this.cards.stream().filter(a -> a.getId() != card.getId()).forEach(b -> b.setButtonClass("disabled"));
        return this.cards.stream().sorted(Comparator.comparing(ViewDTO::getId)).collect(Collectors.toList());
    }


}
