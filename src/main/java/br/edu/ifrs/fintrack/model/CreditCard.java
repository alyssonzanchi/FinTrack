package br.edu.ifrs.fintrack.model;

import br.edu.ifrs.fintrack.exception.InvalidCreditCardDataException;
import br.edu.ifrs.fintrack.exception.MissingCreditCardFieldException;

import java.math.BigDecimal;
import java.util.List;

public class CreditCard {
    private int id;
    private String name;
    private String icon;
    private BigDecimal limit;
    private int closing;
    private int payment;
    private User user;
    private Account account;
    private List<Invoice> invoices;

    public CreditCard(String name, String icon, BigDecimal limit, int closing, int payment, User user, Account account) {
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

        if (user == null) {
            throw new InvalidCreditCardDataException("O usuário não pode ser nulo.");
        }

        if (account == null) {
            throw new InvalidCreditCardDataException("A conta não pode ser nula.");
        }

        this.name = name;
        this.limit = limit;
        this.closing = closing;
        this.payment = payment;
        this.user = user;
        this.account = account;
        this.icon = icon;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new InvalidCreditCardDataException("O usuário não pode ser nulo.");
        }
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        if (account == null) {
            throw new InvalidCreditCardDataException("A conta não pode ser nula");
        }
        this.account = account;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }
}
