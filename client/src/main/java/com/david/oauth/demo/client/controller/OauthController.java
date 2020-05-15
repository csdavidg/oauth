package com.david.oauth.demo.client.controller;

import java.time.LocalDate;
import java.util.UUID;

import com.david.oauth.demo.client.config.OauthConfig;
import com.david.oauth.demo.client.service.OauthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
public class OauthController {

    @Resource
    private OauthConfig oauthConfig;

    private OauthService oauthService;

    private Long state = UUID.randomUUID().getMostSignificantBits();

    public OauthController(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("date", LocalDate.now());
        model.addAttribute("client", oauthConfig.getClient());
        model.addAttribute("callback", oauthConfig.getCallback());
        model.addAttribute("state", state);
        return "index";
    }

    @GetMapping("/callback")
    public String callback(@RequestParam String code, Model model) throws Exception {
        String token = oauthService.getToken(code);
        model.addAttribute("code", token);
        return "callback";
    }


}
