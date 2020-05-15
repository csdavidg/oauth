package com.david.oauth.demo.protectedresource.service;

import com.david.oauth.demo.protectedresource.dao.AccountDAO;
import com.david.oauth.demo.protectedresource.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService implements AccountManagement {

    private AccountDAO accountDAO;

    @Autowired
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public List<Account> findAll() {
        return accountDAO.findAll();
    }

    @Override
    public Account save(Account account) {
        return accountDAO.save(account);
    }

    @Override
    public Account findById(Long id) {
        return accountDAO.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        accountDAO.deleteById(id);
    }
}
