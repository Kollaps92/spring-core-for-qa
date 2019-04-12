package com.acme.banking.dbo.spring;

import com.acme.banking.dbo.spring.dao.AccountRepository;
import com.acme.banking.dbo.spring.domain.Account;
import com.acme.banking.dbo.spring.domain.SavingAccount;
import com.acme.banking.dbo.spring.service.ReportingService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@ContextConfiguration({"classpath:test-spring-context.xml", "classpath:spring-context.xml"})
@ActiveProfiles({"test","system-test"})
@TestPropertySource("classpath:app.properties")
@DirtiesContext
public class PracticeTest {

    @Autowired
    private ReportingService reportingService;

    @Autowired
    private AccountRepository accountRepository;

    private final String firstEmail = "a@a.ru";
    private final String secondEmail = "b@b.ru";
    private final double amount = 100.23;

    @Test
    public void firstTaskTest() {
        // GIVEN
        Account firstAccount = accountRepository.findByEmail(firstEmail).get(0);
        Account secondAccount = accountRepository.findByEmail(secondEmail).get(0);

        // WHEN
        reportingService.transferMoneyFromOneAccountToAnother(firstAccount.getId(), secondAccount.getId(), amount);
    }

    @Test
    public void secondTaskTest() {
        Account account = accountRepository.findByEmail(firstEmail).get(0);
        reportingService.getUsdAmountFor(account.getId());
    }
}
