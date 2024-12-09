package br.edu.ifrs.fintrack.model;

import br.edu.ifrs.fintrack.exception.InvalidTransactionDataException;
import br.edu.ifrs.fintrack.exception.MissingTransactionFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionTest {
    private Transaction transaction;
    private Account account;
    private Category category;
    private CreditCard creditCard;
    private Invoice invoice;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("user@test.com", "password123", "User Teste", LocalDate.of(1990, 1, 1), "image.png");
        account = new Account("Conta Teste", BigDecimal.valueOf(100), "icon.png", user);
        category = new Category("RECEITA", "Teste", "icon.png");
        creditCard = new CreditCard("Cartão Teste", "icon.png", BigDecimal.valueOf(5000), 10, 20, new User("user@test.com", "password123", "User Teste", LocalDate.of(1990, 1, 1), "image.png"), account);
        invoice = new Invoice(creditCard, LocalDate.of(2024, 12, 9), LocalDate.of(2024, 12, 19), LocalDate.of(2024, 12, 30));
        transaction = new Transaction("Transação Teste", "Receita", BigDecimal.valueOf(150), LocalDate.of(2024, 12, 9), "Descrição de teste", "Mensal", "Diária", "Semanal", 3, user, category, account, creditCard, invoice);
    }

    @Test
    void testTransactionCreationWithValidData() {
        assertEquals(LocalDate.of(2024, 12, 9), transaction.getDate());
        assertEquals(BigDecimal.valueOf(150), transaction.getAmount());
        assertEquals("Descrição de teste", transaction.getDescription());
        assertEquals(creditCard, transaction.getCreditCard());
    }

    @Test
    void testTransactionCreationWithEmptyDescription() {
        Exception exception = assertThrows(MissingTransactionFieldException.class, () ->
                new Transaction(null, "RECEITA", BigDecimal.valueOf(150), LocalDate.of(2024, 12, 9), "Descrição de teste", "Mensal", "Diária", "Semanal", 3, user, category, account, creditCard, invoice));
        assertEquals("Campo obrigatório ausente: name", exception.getMessage());
    }

    @Test
    void testTransactionCreationWithNullDate() {
        Exception exception = assertThrows(MissingTransactionFieldException.class, () ->
                new Transaction("Transação Teste", "RECEITA", BigDecimal.valueOf(150), null, "Descrição de teste", "Mensal", "Diária", "Semanal", 3, user, category, account, creditCard, invoice));
        assertEquals("Campo obrigatório ausente: date", exception.getMessage());
    }

    @Test
    void testTransactionCreationWithNullAmount() {
        Exception exception = assertThrows(MissingTransactionFieldException.class, () ->
                new Transaction("Transação Teste", "Receita", null, LocalDate.of(2024, 12, 9), "Descrição de teste", "Mensal", "Diária", "Semanal", 3, user, category, account, creditCard, invoice));
        assertEquals("Campo obrigatório ausente: amount", exception.getMessage());
    }

    @Test
    void testTransactionCreationWithNegativeAmount() {
        Exception exception = assertThrows(InvalidTransactionDataException.class, () ->
                new Transaction("Transação Teste", "Receita", BigDecimal.valueOf(-150), LocalDate.of(2024, 12, 9), "Descrição de teste", "Mensal", "Diária", "Semanal", 3, user, category, account, creditCard, invoice));
        assertEquals("Dados inválidos para criação da transação: O valor da transação não pode ser negativo.", exception.getMessage());
    }

    @Test
    void testSetDescriptionWithValidData() {
        creditCard.setName("Novo Cartão");
        assertEquals("Novo Cartão", creditCard.getName());
    }
}
