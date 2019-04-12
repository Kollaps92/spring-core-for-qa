package com.acme.banking.dbo.spring.service;

import com.acme.banking.dbo.spring.dao.AccountRepository;
import com.acme.banking.dbo.spring.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
public class ReportingService {
    @Autowired(required = false)
    private CurrencyService currencyService;

    @Resource /** Like @Autowired but with JNDI support */
    private AccountRepository accountRepository;

    @PostConstruct
    public void onCreate() {
        System.out.println("ReportingService created");
    }

    @PreDestroy
    public void onShutDown() {
        System.out.println("ReportingService shut down");
    }

    //task #2
    public double getUsdAmountFor(long accountId) {
        double rurAmount = accountRepository.findById(accountId).get().getAmount();
        double resultInUsd = rurAmount / currencyService.getUsdRateForRur();
        System.out.println(">>>>> Amount in USD for fiven account : " + resultInUsd);
        return resultInUsd;
    }

    //task # 1

    @Transactional
    public void transferMoneyFromOneAccountToAnother(long sourceId, long targetId, double amount) {
        Account source = accountRepository.findById(sourceId).get();
        Account target = accountRepository.findById(targetId).get();

        source.changeAmount(-amount);
        accountRepository.saveAndFlush(source);

        target.changeAmount(amount);
        accountRepository.saveAndFlush(target);
    }
}
