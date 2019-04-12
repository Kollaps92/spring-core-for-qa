package com.acme.banking.dbo.spring;

import com.acme.banking.dbo.spring.dao.AccountRepository;
import com.acme.banking.dbo.spring.domain.CheckingAccount;
import com.acme.banking.dbo.spring.service.CurrencyService;
import com.acme.banking.dbo.spring.service.ReportingService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration({"classpath:test-spring-context.xml", "classpath:spring-context.xml"})
@ActiveProfiles({"test","system-test"})
@TestPropertySource("classpath:app.properties")
@Transactional
@Ignore
public class DemoSystemTest {
    @Autowired private ReportingService reportingService;
    @Autowired private AccountRepository accountRepository;
    @Autowired ApplicationContext context;

    @Test @DirtiesContext //Performance issue
    public void shouldCreateContext() {
        CurrencyService currencyService = context.getBean(CurrencyService.class);
        currencyService.setRurToUsdRate(60);

        assertThat(reportingService.getUsdAmountFor(1)).isEqualTo(60);
    }

    @Test
    public void shouldRecreateContextForEachTestRunWhenDirtiesContext() {
        assertThat(reportingService.getUsdAmountFor(1)).isEqualTo(30);
    }

    @Test @Rollback(true)
    public void shouldNotLeaveSideEffectInDb() {
        CheckingAccount justSavedAccount = accountRepository.save(new CheckingAccount(100, 200, "xx@xx.ru"));
        assertThat(
                accountRepository.findById(justSavedAccount.getId()).get().getId()
        ).isEqualTo(justSavedAccount.getId());
    }
}
