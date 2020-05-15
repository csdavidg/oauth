package com.david.oauth.demo.protectedresource.service;

import com.david.oauth.demo.protectedresource.entity.Account;

import java.util.List;

public interface AccountManagement {

    List<Account> findAll();

    Account save(Account employee);

    Account findById(Long id);

    void deleteById(Long id);

}
