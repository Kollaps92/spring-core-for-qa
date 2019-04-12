package com.acme.banking.dbo.spring.domain;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.InputMismatchException;

@Entity
@Inheritance
@DiscriminatorColumn(name="ACCOUNT_TYPE")
public abstract class Account {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;
    protected double amount;
    @Email @Size(max = 50) private String email;

    public Account() {
    }

    public Account(double amount, String email) {
        this.amount = amount;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public double changeAmount(double deltaAmount) {
        if (deltaAmount < 0 && this.amount < Math.abs(deltaAmount))
            throw new InputMismatchException("Account has not enough money to withdraw");

        this.amount += deltaAmount;
        return this.amount;
    }
}
