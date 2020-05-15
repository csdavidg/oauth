package com.david.oauth.demo.protectedresource.dao;

import com.david.oauth.demo.protectedresource.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDAO extends JpaRepository<Account, Long> {
}
