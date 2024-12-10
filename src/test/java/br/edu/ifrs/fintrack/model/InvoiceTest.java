package br.edu.ifrs.fintrack.model;

import br.edu.ifrs.fintrack.exception.InvalidInvoiceDataException;
import br.edu.ifrs.fintrack.exception.MissingInvoiceFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceTest {
    private Invoice invoice;
    private CreditCard creditCard;

    @BeforeEach
    void setUp() {
        creditCard = new CreditCard("Cartão Teste", "icon.png", BigDecimal.valueOf(5000), 10, 20, new User("user@test.com", "password123", "User Teste", LocalDate.of(1990, 1, 1), "image.png"), new Account("Conta Teste", BigDecimal.valueOf(100), "icon.png", new User("user@test.com", "password123", "User Teste", LocalDate.of(1990, 1, 1), "image.png")));
        invoice = new Invoice(creditCard, LocalDate.of(2024, 12, 9), LocalDate.of(2024, 12, 19), LocalDate.of(2024, 12, 30), new BigDecimal(200), false);
    }

    @Test
    void testInvoiceCreationWithValidData() {
        assertEquals(creditCard, invoice.getCreditCard());
        assertEquals(LocalDate.of(2024, 12, 9), invoice.getStartDate());
        assertEquals(LocalDate.of(2024, 12, 19), invoice.getEndDate());
        assertEquals(LocalDate.of(2024, 12, 30), invoice.getDueDate());
        assertEquals(BigDecimal.valueOf(200), invoice.getTotal());
        assertFalse(invoice.isPaid());
    }

    @Test
    void testInvoiceCreationWithNullCreditCard() {
        Exception exception = assertThrows(MissingInvoiceFieldException.class, () ->
                new Invoice(null, LocalDate.of(2024, 12, 9), LocalDate.of(2024, 12, 19), LocalDate.of(2024, 12, 30), new BigDecimal(200), false));
        assertEquals("O campo 'creditCard' é obrigatório.", exception.getMessage());
    }

    @Test
    void testInvoiceCreationWithNullDates() {
        Exception exception = assertThrows(MissingInvoiceFieldException.class, () ->
                new Invoice(creditCard, null, LocalDate.of(2024, 12, 19), LocalDate.of(2024, 12, 30), new BigDecimal(200), false));
        assertEquals("O campo 'startDate' é obrigatório.", exception.getMessage());
    }

    @Test
    void testInvoiceCreationWithNullStartDate() {
        Exception exception = assertThrows(MissingInvoiceFieldException.class, () ->
                new Invoice(creditCard, null, LocalDate.of(2024, 12, 19), LocalDate.of(2024, 12, 30), new BigDecimal(200), false));
        assertEquals("O campo 'startDate' é obrigatório.", exception.getMessage());
    }

    @Test
    void testInvoiceCreationWithNullEndDate() {
        Exception exception = assertThrows(MissingInvoiceFieldException.class, () ->
                new Invoice(creditCard, LocalDate.of(2024, 12, 9), null, LocalDate.of(2024, 12, 30), new BigDecimal(200), false));
        assertEquals("O campo 'endDate' é obrigatório.", exception.getMessage());
    }

    @Test
    void testInvoiceCreationWithNullDueDate() {
        Exception exception = assertThrows(MissingInvoiceFieldException.class, () ->
                new Invoice(creditCard, LocalDate.of(2024, 12, 9), LocalDate.of(2024, 12, 19), null, new BigDecimal(200), false));
        assertEquals("O campo 'dueDate' é obrigatório.", exception.getMessage());
    }

    @Test
    void testInvoiceCreationWithEndDateBeforeStartDate() {
        Exception exception = assertThrows(InvalidInvoiceDataException.class, () ->
                new Invoice(creditCard, LocalDate.of(2024, 12, 19), LocalDate.of(2024, 12, 9), LocalDate.of(2024, 12, 30), new BigDecimal(200), false));
        assertEquals("A data de término não pode ser antes da data de início.", exception.getMessage());
    }

    @Test
    void testSetTotalWithNullValue() {
        Exception exception = assertThrows(InvalidInvoiceDataException.class, () ->
                invoice.setTotal(null));
        assertEquals("O valor da fatura não pode ser nulo ou negativo.", exception.getMessage());
    }

    @Test
    void testSetTotalWithNegativeValue() {
        Exception exception = assertThrows(InvalidInvoiceDataException.class, () ->
                invoice.setTotal(BigDecimal.valueOf(-100)));
        assertEquals("O valor da fatura não pode ser nulo ou negativo.", exception.getMessage());
    }

    @Test
    void testSetCreditCardWithNullValue() {
        Exception exception = assertThrows(MissingInvoiceFieldException.class, () ->
                invoice.setCreditCard(null));
        assertEquals("O campo 'creditCard' é obrigatório.", exception.getMessage());
    }

    @Test
    void testSetStartDateWithNullValue() {
        Exception exception = assertThrows(MissingInvoiceFieldException.class, () ->
                invoice.setStartDate(null));
        assertEquals("O campo 'startDate' é obrigatório.", exception.getMessage());
    }

    @Test
    void testSetEndDateWithNullValue() {
        Exception exception = assertThrows(MissingInvoiceFieldException.class, () ->
                invoice.setEndDate(null));
        assertEquals("O campo 'endDate' é obrigatório.", exception.getMessage());
    }

    @Test
    void testSetDueDateWithNullValue() {
        Exception exception = assertThrows(MissingInvoiceFieldException.class, () ->
                invoice.setDueDate(null));
        assertEquals("O campo 'dueDate' é obrigatório.", exception.getMessage());
    }

    @Test
    void testSetEndDateBeforeStartDate() {
        Exception exception = assertThrows(InvalidInvoiceDataException.class, () ->
                invoice.setEndDate(LocalDate.of(2024, 12, 8)));
        assertEquals("A data de término não pode ser antes da data de início.", exception.getMessage());
    }
}
