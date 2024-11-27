package br.edu.ifrs.fintrack.model;

import br.edu.ifrs.fintrack.exception.InvalidAccountDataException;
import br.edu.ifrs.fintrack.exception.MissingRequiredFieldException;

import java.math.BigDecimal;

public class Account {
    private int id;
    private String name;
    private BigDecimal balance;
    private String icon;
    private int userId;

    public Account(String name, BigDecimal balance, String icon, int userId) {
        if(name == null || name.isEmpty()) {
            throw new MissingRequiredFieldException("name");
        }

        if(userId <= 0) {
            throw new InvalidAccountDataException("O ID do usuário deve ser maior que zero");
        }

        this.name = name;
        this.balance = balance;
        this.icon = icon != null ? icon : "default";
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null || name.isEmpty()) {
            throw new MissingRequiredFieldException("name");
        }
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if(userId <= 0) {
            throw new InvalidAccountDataException("O ID do usuário deve ser maior que zero");
        }
        this.userId = userId;
    }
}
