package com.github.hal4j.test.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

public class Money implements Serializable {

    public final BigDecimal amount;

    public final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    @JsonCreator
    public static Money fromString(String value) {
        int idx = value.indexOf(' ');
        if (idx < 1 || idx >= value.length() - 1) {
            throw new IllegalArgumentException(value);
        }
        BigDecimal amount = new BigDecimal(value.substring(0, idx));
        Currency currency = Currency.getInstance(value.substring(idx + 1));
        return new Money(amount, currency);
    }

    @JsonValue
    @Override
    public String toString() {
        return amount.toString() + " " + currency.getCurrencyCode();
    }

}
