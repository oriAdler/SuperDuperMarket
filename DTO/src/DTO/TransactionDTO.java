package DTO;

import java.util.Date;

public class TransactionDTO {
    final private String type;
    final private Date date;
    final private double amount;
    final private double balanceBefore;
    final private double balanceAfter;

    public TransactionDTO(String type, Date date, double amount, double balanceBefore, double balanceAfter) {
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
    }

    public String getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceBefore() {
        return balanceBefore;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }
}
