package com.acme.banking.dbo.spring.dao;

import com.acme.banking.dbo.spring.domain.Account;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Profile({"prod", "system-test"})
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByEmail(String email);
}
