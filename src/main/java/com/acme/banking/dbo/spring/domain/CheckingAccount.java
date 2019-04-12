package com.acme.banking.dbo.spring.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.InputMismatchException;

@Entity
@DiscriminatorValue("C")
public class CheckingAccount extends Account {
    private double overdraft;

    public CheckingAccount() {
    }

    public CheckingAccount(double amount, double overdraft, String email) {
        super(amount, email);
        this.overdraft = overdraft;
    }

    @Override
    public double changeAmount(double deltaAmount) {
        if (deltaAmount < 0 && this.amount + this.overdraft < Math.abs(deltaAmount))
            throw new InputMismatchException("Account has not enough money to withdraw");

        this.amount += deltaAmount;
        return this.amount;
    }
}
