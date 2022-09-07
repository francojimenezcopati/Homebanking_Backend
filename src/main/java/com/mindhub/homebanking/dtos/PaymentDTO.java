package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Payment;

public class PaymentDTO {
    private long id;
    private String accountNumber;
    private String cardNumber;
    private int CVV;
    private double amount;
    private String description;

    public PaymentDTO() {
    }

    public PaymentDTO(Payment payment) {
        this.id = payment.getId();
        this.accountNumber = payment.getAccountNumber();
        this.cardNumber = payment.getCardNumber();
        this.CVV = payment.getCVV();
        this.amount = payment.getAmount();
        this.description = payment.getDescription();
    }

    public long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getCVV() {
        return CVV;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
