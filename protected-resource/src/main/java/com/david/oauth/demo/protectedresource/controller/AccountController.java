package com.david.oauth.demo.protectedresource.controller;

import com.david.oauth.demo.protectedresource.entity.Account;
import com.david.oauth.demo.protectedresource.service.AccountManagement;
import com.david.oauth.demo.protectedresource.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("account")
public class AccountController {

    private AccountManagement accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/all")
    public List<Account> getAllAccounts() {
        return accountService.findAll();
    }
}
