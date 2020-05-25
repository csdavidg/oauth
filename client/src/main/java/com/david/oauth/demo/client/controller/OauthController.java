package com.david.oauth.demo.client.controller;

import com.david.oauth.demo.client.config.OauthConfig;
import com.david.oauth.demo.client.service.EmployeeService;
import com.david.oauth.demo.client.service.OauthService;
import com.david.oauth.demo.client.service.TokenService;
import com.david.oauth.demo.oauthcommons.entity.Employee;
import com.david.oauth.demo.oauthcommons.entity.ResponseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Controller
public class OauthController {

    @Resource
    private OauthConfig oauthConfig;

    private final OauthService oauthService;
    private final TokenService tokenService;
    private final EmployeeService employeeService;

    @Autowired
    public OauthController(OauthService oauthService, TokenService tokenService, EmployeeService employeeService) {
        this.oauthService = oauthService;
        this.tokenService = tokenService;
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String index(Model model) {
        try {
            model.addAttribute("date", LocalDate.now());
            model.addAttribute("client", oauthConfig.getClient());
            model.addAttribute("callback", oauthConfig.getCallback());
            model.addAttribute("state", tokenService.createAndSaveRequestState());
            return "index";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/callback")
    public String callback(@RequestParam String code, @RequestParam String state, Model model) throws Exception {
        ResponseToken token = oauthService.getToken(code, state);
        model.addAttribute("code", token);
        return "callback";
    }

    @GetMapping("/protected")
    public String protectedReource(Model model) {
        List<Employee> employeeList = this.employeeService.getEmployeesFromAPI();
        if(employeeList != null){
            model.addAttribute("employeesList", employeeList);
        }else{
            model.addAttribute("error", "You are not authorized to access this resource");
        }
        return "list-employees";
    }


}
