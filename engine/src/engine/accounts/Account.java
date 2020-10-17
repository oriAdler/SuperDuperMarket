package engine.accounts;

import DTO.TransactionDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Account {
    private double currentBalance;
    private double oldBalance;
    final private List<Transaction> transactionList;

    public Account() {
        currentBalance = 0;
        oldBalance = 0;
        transactionList = new ArrayList<>();
    }

    public void addTransaction(String type, LocalDate date, double amount){
        if(type.equals("Charge")){
            amount *= (-1);
        }
        oldBalance = currentBalance;
        currentBalance += amount;
        transactionList.add(new Transaction(type, date, amount, oldBalance, currentBalance));
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public double getOldBalance() {
        return oldBalance;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public List<TransactionDTO> getTransactionDTOList(){
        List<TransactionDTO> transactionDTOList = new ArrayList<>();

        this.transactionList.forEach(transaction -> transactionDTOList.add(new TransactionDTO(
                transaction.getType(),
                transaction.getDate(),
                transaction.getAmount(),
                transaction.getBalanceBefore(),
                transaction.balanceAfter)));

        return transactionDTOList;
    }

    public static class Transaction {
        final private String type;
        final private LocalDate date;
        final private double amount;
        final private double balanceBefore;
        final private double balanceAfter;

        public Transaction(String type, LocalDate date, double amount, double balanceBefore, double balanceAfter) {
            this.type = type;
            this.date = date;
            this.amount = amount;
            this.balanceBefore = balanceBefore;
            this.balanceAfter = balanceAfter;
        }

        public String getType() {
            return type;
        }

        public LocalDate getDate() {
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
}
