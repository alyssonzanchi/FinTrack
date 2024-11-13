package br.edu.ifrs.fintrack.model;

import br.edu.ifrs.fintrack.exception.InvalidCreditCardDataException;
import br.edu.ifrs.fintrack.exception.MissingCreditCardFieldException;

import java.math.BigDecimal;

public class CreditCard {
    private int id;
    private String name;
    private String icon;
    private BigDecimal limit;
    private int closing;
    private int payment;
    private int userId;
    private int accountId;

    public CreditCard(String name, BigDecimal limit, int closing, int payment, int userId, int accountId, String icon) {
        if (name == null || name.isEmpty()) {
            throw new MissingCreditCardFieldException("name");
        }

        if (limit == null || limit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidCreditCardDataException("O limite deve ser maior que zero.");
        }

        if (closing <= 0 || closing > 31) {
            throw new InvalidCreditCardDataException("O dia de fechamento deve estar entre 1 e 31.");
        }

        if (payment <= 0 || payment > 31) {
            throw new InvalidCreditCardDataException("O dia de pagamento deve estar entre 1 e 31.");
        }

        if (userId <= 0) {
            throw new InvalidCreditCardDataException("O ID do usuário deve ser maior que zero.");
        }

        if (accountId <= 0) {
            throw new InvalidCreditCardDataException("O ID da conta deve ser maior que zero.");
        }

        this.name = name;
        this.limit = limit;
        this.closing = closing;
        this.payment = payment;
        this.userId = userId;
        this.accountId = accountId;
        this.icon = icon != null ? icon : "default";
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
        if (name == null || name.isEmpty()) {
            throw new MissingCreditCardFieldException("name");
        }
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        if (limit == null || limit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidCreditCardDataException("O limite deve ser maior que zero.");
        }
        this.limit = limit;
    }

    public int getClosing() {
        return closing;
    }

    public void setClosing(int closing) {
        if (closing <= 0 || closing > 31) {
            throw new InvalidCreditCardDataException("O dia de fechamento deve estar entre 1 e 31.");
        }
        this.closing = closing;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        if (payment <= 0 || payment > 31) {
            throw new InvalidCreditCardDataException("O dia de pagamento deve estar entre 1 e 31.");
        }
        this.payment = payment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if (userId <= 0) {
            throw new InvalidCreditCardDataException("O ID do usuário deve ser maior que zero.");
        }
        this.userId = userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        if (accountId <= 0) {
            throw new InvalidCreditCardDataException("O ID da conta deve ser maior que zero.");
        }
        this.accountId = accountId;
    }
}
