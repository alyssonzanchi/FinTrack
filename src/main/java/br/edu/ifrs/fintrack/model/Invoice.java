package br.edu.ifrs.fintrack.model;

import br.edu.ifrs.fintrack.exception.InvalidInvoiceDataException;
import br.edu.ifrs.fintrack.exception.MissingInvoiceFieldException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Invoice {
    private int id;
    private CreditCard creditCard;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate dueDate;
    private BigDecimal total;
    private boolean paid;

    public Invoice(CreditCard creditCard, LocalDate startDate, LocalDate endDate, LocalDate dueDate) {
        if (creditCard == null) {
            throw new MissingInvoiceFieldException("creditCard");
        }
        if (startDate == null || endDate == null || dueDate == null) {
            throw new MissingInvoiceFieldException("As datas");
        }
        if (endDate.isBefore(startDate)) {
            throw new InvalidInvoiceDataException("A data de término não pode ser antes da data de início.");
        }

        this.creditCard = creditCard;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dueDate = dueDate;
        this.total = BigDecimal.ZERO;
        this.paid = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        if (creditCard == null) {
            throw new MissingInvoiceFieldException("creditCard");
        }
        this.creditCard = creditCard;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new MissingInvoiceFieldException("startDate");
        }
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new MissingInvoiceFieldException("endDate");
        }
        if (endDate.isBefore(this.startDate)) {
            throw new InvalidInvoiceDataException("A data de término não pode ser antes da data de início.");
        }
        this.endDate = endDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        if (dueDate == null) {
            throw new MissingInvoiceFieldException("dueDate");
        }
        this.dueDate = dueDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        if (total == null || total.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInvoiceDataException("O valor da fatura não pode ser nulo ou negativo.");
        }
        this.total = total;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
