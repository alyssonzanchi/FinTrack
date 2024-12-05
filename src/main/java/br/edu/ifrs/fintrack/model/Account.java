package br.edu.ifrs.fintrack.model;

import br.edu.ifrs.fintrack.exception.InvalidAccountDataException;
import br.edu.ifrs.fintrack.exception.MissingRequiredFieldException;

import java.math.BigDecimal;

public class Account {
    private int id;
    private String name;
    private BigDecimal balance;
    private String icon;
    private User user;

    public Account(String name, BigDecimal balance, String icon, User user) {
        if(name == null || name.isEmpty()) {
            throw new MissingRequiredFieldException("name");
        }

        if(user == null) {
            throw new InvalidAccountDataException("Usuário não pode ser nulo");
        }

        this.name = name;
        this.balance = balance;
        this.icon = icon;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if(user == null) {
            throw new InvalidAccountDataException("Usuário não pode ser nulo");
        }
        this.user = user;
    }
}
