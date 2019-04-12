package com.acme.banking.dbo.spring;

import com.acme.banking.dbo.spring.dao.AccountRepository;
import com.acme.banking.dbo.spring.domain.Account;
import com.acme.banking.dbo.spring.service.CurrencyService;
import com.acme.banking.dbo.spring.service.ReportingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration({"classpath:test-spring-context.xml", "classpath:spring-context.xml"})
@ActiveProfiles({"test"})
@TestPropertySource("classpath:app.properties")
public class PracticeMockedTest {

    private final String firstEmail = "a@a.ru";
    private final String secondEmail = "b@b.ru";

    @Autowired
    private ReportingService reportingService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private CurrencyService currencyService;

    @Test
    public void transferMoneyMockedTest() {
        // GIVEN
        Account sourceAccountStub = mock(Account.class);
        when(accountRepository.findByEmail(firstEmail)).thenReturn(Collections.singletonList(sourceAccountStub));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccountStub));

        Account targetAccountStub = mock(Account.class);
        when(accountRepository.findByEmail(secondEmail)).thenReturn(Collections.singletonList(targetAccountStub));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(targetAccountStub));


        // first account
        when(sourceAccountStub.getId()).thenReturn(1L);
        when(sourceAccountStub.changeAmount(anyDouble())).thenReturn(10d);
        // second account
        when(targetAccountStub.getId()).thenReturn(2L);
        when(targetAccountStub.changeAmount(anyDouble())).thenReturn(20d);


        // WHEN
        Account firstAccount = accountRepository.findByEmail(firstEmail).get(0);
        Account secondAccount = accountRepository.findByEmail(secondEmail).get(0);
        reportingService.transferMoneyFromOneAccountToAnother(firstAccount.getId(), secondAccount.getId(), 100.23);

        // THEN
        verify(sourceAccountStub, times(1)).changeAmount(-100.23);
        verify(targetAccountStub, times(1)).changeAmount(100.23);

//        verify(sourceAccountStub, times(1)).getId();
//
//        verify(targetAccountStub, times(1)).getId();
    }

    @Test
    public void convertToUsdMockedTest() {
        Account accountStub = mock(Account.class);
        when(accountRepository.findByEmail(firstEmail)).thenReturn(Collections.singletonList(accountStub));
        when (accountRepository.findById(anyLong())).thenReturn(Optional.of(accountStub));
        when (accountStub.getAmount()).thenReturn(120d);

        when(currencyService.getUsdRateForRur()).thenReturn(20d);

        // WHEN
        double usdAmount = reportingService.getUsdAmountFor(30L);

        // THEN
        verify(accountStub,times(1)).getAmount();
        verify(accountRepository,times(1)).findById(anyLong());
        assertThat(usdAmount).isEqualTo(120d / 20d);
    }
}
