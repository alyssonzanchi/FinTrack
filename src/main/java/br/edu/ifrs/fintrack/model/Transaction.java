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
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private boolean paid;
    private String description;
    private String recurring;
    private String fixedFrequency;
    private String installmentFrequency;
    private Integer installmentCount;
    private Integer installmentNumber;
    private User user;
    private Category category;
    private Account account;
    private CreditCard creditCard;
    private Invoice invoice;

    public Transaction(
            String name,
            String type,
            BigDecimal amount,
            LocalDate dueDate,
            LocalDate paymentDate,
            boolean paid,
            String description,
            String recurring,
            String fixedFrequency,
            String installmentFrequency,
            Integer installmentCount,
            Integer installmentNumber,
            User user,
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
        if (dueDate == null) {
            throw new MissingTransactionFieldException("dueDate");
        }

        this.name = name;
        this.type = type;
        this.amount = amount;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
        this.paid = paid;
        this.description = description;
        this.recurring = recurring;
        this.fixedFrequency = fixedFrequency;
        this.installmentFrequency = installmentFrequency;
        this.installmentCount = installmentCount;
        this.installmentNumber = installmentNumber;
        this.user = user;
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
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        if (dueDate == null) {
            throw new MissingTransactionFieldException("dueDate");
        }
        this.dueDate = dueDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public boolean getPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
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

    public Integer getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(Integer installmentNumber) {
        this.installmentNumber = installmentNumber;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id && Objects.equals(type, that.type) && Objects.equals(amount, that.amount) && Objects.equals(dueDate, that.dueDate) && Objects.equals(paymentDate, that.paymentDate) && Objects.equals(paid, that.paid) && Objects.equals(description, that.description) && Objects.equals(recurring, that.recurring) && Objects.equals(fixedFrequency, that.fixedFrequency) && Objects.equals(installmentFrequency, that.installmentFrequency) && Objects.equals(installmentCount, that.installmentCount) && Objects.equals(category, that.category) && Objects.equals(account, that.account) && Objects.equals(creditCard, that.creditCard) && Objects.equals(invoice, that.invoice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, amount, dueDate, paymentDate, paid, description, recurring, fixedFrequency, installmentFrequency, installmentCount, category, account, creditCard, invoice);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", dueDate=" + dueDate +
                ", paymentDate=" + paymentDate +
                ", paid=" + paid +
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
