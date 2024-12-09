package br.edu.ifrs.fintrack.model;

import br.edu.ifrs.fintrack.exception.InvalidTransactionDataException;
import br.edu.ifrs.fintrack.exception.MissingTransactionFieldException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Transaction {
    private int id;
    private String name;
    private String type;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private String recurring;
    private String fixedFrequency;
    private String installmentFrequency;
    private Integer installmentCount;
    private Category category;
    private Account account;
    private CreditCard creditCard;
    private Invoice invoice;

    public Transaction(
            String name,
            String type,
            BigDecimal amount,
            LocalDate date,
            String description,
            String recurring,
            String fixedFrequency,
            String installmentFrequency,
            Integer installmentCount,
            Category category,
            Account account,
            CreditCard creditCard,
            Invoice invoice) {
        if (name == null || name.isEmpty()) {
            throw new MissingTransactionFieldException("name");
        }
        if (type == null || type.isEmpty()) {
            throw new MissingTransactionFieldException("type");
        }
        if (amount == null) {
            throw new MissingTransactionFieldException("amount");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionDataException("O valor da transação não pode ser negativo.");
        }
        if (date == null) {
            throw new MissingTransactionFieldException("date");
        }

        this.name = name;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.recurring = recurring;
        this.fixedFrequency = fixedFrequency;
        this.installmentFrequency = installmentFrequency;
        this.installmentCount = installmentCount;
        this.category = category;
        this.account = account;
        this.creditCard = creditCard;
        this.invoice = invoice;
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
            throw new MissingTransactionFieldException("name");
        }
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type == null || type.isEmpty()) {
            throw new MissingTransactionFieldException("type");
        }
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount == null) {
            throw new MissingTransactionFieldException("amount");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionDataException("O valor da transação não pode ser negativo.");
        }
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new MissingTransactionFieldException("date");
        }
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecurring() {
        return recurring;
    }

    public void setRecurring(String recurring) {
        this.recurring = recurring;
    }

    public String getFixedFrequency() {
        return fixedFrequency;
    }

    public void setFixedFrequency(String fixedFrequency) {
        this.fixedFrequency = fixedFrequency;
    }

    public String getInstallmentFrequency() {
        return installmentFrequency;
    }

    public void setInstallmentFrequency(String installmentFrequency) {
        this.installmentFrequency = installmentFrequency;
    }

    public Integer getInstallmentCount() {
        return installmentCount;
    }

    public void setInstallmentCount(Integer installmentCount) {
        this.installmentCount = installmentCount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id && Objects.equals(type, that.type) && Objects.equals(amount, that.amount) && Objects.equals(date, that.date) && Objects.equals(description, that.description) && Objects.equals(recurring, that.recurring) && Objects.equals(fixedFrequency, that.fixedFrequency) && Objects.equals(installmentFrequency, that.installmentFrequency) && Objects.equals(installmentCount, that.installmentCount) && Objects.equals(category, that.category) && Objects.equals(account, that.account) && Objects.equals(creditCard, that.creditCard) && Objects.equals(invoice, that.invoice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, amount, date, description, recurring, fixedFrequency, installmentFrequency, installmentCount, category, account, creditCard, invoice);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", recurring='" + recurring + '\'' +
                ", fixedFrequency='" + fixedFrequency + '\'' +
                ", installmentFrequency='" + installmentFrequency + '\'' +
                ", installmentCount=" + installmentCount +
                ", category=" + category +
                ", account=" + account +
                ", creditCard=" + creditCard +
                ", invoice=" + invoice +
                '}';
    }
}
