package com.david.oauth.demo.client.controller;

import com.david.oauth.demo.client.config.OauthConfig;
import com.david.oauth.demo.client.service.OauthService;
import com.david.oauth.demo.oauthcommons.entity.ResponseToken;
import com.david.oauth.demo.oauthcommons.jwt.JwtTokenGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.time.LocalDate;

@Controller
public class OauthController {

    @Resource
    private OauthConfig oauthConfig;

    private OauthService oauthService;

    private JwtTokenGenerator jwtTokenGenerator;

    private String state;

    public OauthController(OauthService oauthService, JwtTokenGenerator jwtTokenGenerator) {
        this.oauthService = oauthService;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    @GetMapping("/")
    public String index(Model model) {
        state = jwtTokenGenerator.generateState();
        model.addAttribute("date", LocalDate.now());
        model.addAttribute("client", oauthConfig.getClient());
        model.addAttribute("callback", oauthConfig.getCallback());
        model.addAttribute("state", state);
        return "index";
    }

    @GetMapping("/callback")
    public String callback(@RequestParam String code, @RequestParam String state, Model model) throws Exception {
        ResponseToken token = oauthService.getToken(code, this.state, state);
        model.addAttribute("code", token);
        return "callback";
    }


}
