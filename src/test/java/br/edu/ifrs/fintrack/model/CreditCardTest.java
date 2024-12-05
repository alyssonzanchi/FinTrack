package br.edu.ifrs.fintrack.model;

import br.edu.ifrs.fintrack.exception.InvalidCreditCardDataException;
import br.edu.ifrs.fintrack.exception.MissingCreditCardFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class  CreditCardTest {
    private CreditCard creditCard;
    private Account account;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("user@test.com", "password123", "User Teste", LocalDate.of(1990, 1, 1), "image.png");
        account = new Account("Conta Teste", BigDecimal.valueOf(100), "icon.png", user);
        creditCard = new CreditCard("Cartão Teste", BigDecimal.valueOf(5000), 10, 20, user, account, "icon.png");
    }

    @Test
    void testCreditCardCreationWithValidData() {
        assertEquals("Cartão Teste", creditCard.getName());
        assertEquals(BigDecimal.valueOf(5000), creditCard.getLimit());
        assertEquals(10, creditCard.getClosing());
        assertEquals(20, creditCard.getPayment());
        assertEquals(user, creditCard.getUser());
        assertEquals(account, creditCard.getAccount());
        assertEquals("icon.png", creditCard.getIcon());
    }

    @Test
    void testCreditCardCreationWithNullName() {
        Exception exception = assertThrows(MissingCreditCardFieldException.class, () ->
                new CreditCard(null, BigDecimal.valueOf(5000), 10, 20, user, account, "icon.png"));
        assertEquals("Campo obrigatório ausente: name", exception.getMessage());
    }

    @Test
    void testCreditCardCreationWithEmptyName() {
        Exception exception = assertThrows(MissingCreditCardFieldException.class, () ->
                new CreditCard("", BigDecimal.valueOf(5000), 10, 20, user, account, "icon.png"));
        assertEquals("Campo obrigatório ausente: name", exception.getMessage());
    }

    @Test
    void testCreditCardCreationWithNegativeLimit() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () ->
                new CreditCard("Cartão Teste", BigDecimal.valueOf(-500), 10, 20, user, account, "icon.png"));
        assertEquals("Dados inválidos para criação do cartão de crédito: O limite deve ser maior que zero.", exception.getMessage());
    }

    @Test
    void testCreditCardCreationWithInvalidClosingDay() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () ->
                new CreditCard("Cartão Teste", BigDecimal.valueOf(5000), 0, 20, user, account, "icon.png"));
        assertEquals("Dados inválidos para criação do cartão de crédito: O dia de fechamento deve estar entre 1 e 31.", exception.getMessage());
    }

    @Test
    void testCreditCardCreationWithInvalidPaymentDay() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () ->
                new CreditCard("Cartão Teste", BigDecimal.valueOf(5000), 10, 32, user, account, "icon.png"));
        assertEquals("Dados inválidos para criação do cartão de crédito: O dia de pagamento deve estar entre 1 e 31.", exception.getMessage());
    }

    @Test
    void testCreditCardCreationWithNullUser() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () ->
                new CreditCard("Cartão Teste", BigDecimal.valueOf(5000), 10, 20, null, account, "icon.png"));
        assertEquals("Dados inválidos para criação do cartão de crédito: O usuário não pode ser nulo.", exception.getMessage());
    }

    @Test
    void testCreditCardCreationWithNullAccount() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () ->
                new CreditCard("Cartão Teste", BigDecimal.valueOf(5000), 10, 20, user, null, "icon.png"));
        assertEquals("Dados inválidos para criação do cartão de crédito: A conta não pode ser nula.", exception.getMessage());
    }

    @Test
    void testSetNameWithValidData() {
        creditCard.setName("Novo Cartão");
        assertEquals("Novo Cartão", creditCard.getName());
    }

    @Test
    void testSetNameWithNull() {
        Exception exception = assertThrows(MissingCreditCardFieldException.class, () -> creditCard.setName(null));
        assertEquals("Campo obrigatório ausente: name", exception.getMessage());
    }

    @Test
    void testSetLimitWithValidData() {
        creditCard.setLimit(BigDecimal.valueOf(10000));
        assertEquals(BigDecimal.valueOf(10000), creditCard.getLimit());
    }

    @Test
    void testSetLimitWithInvalidData() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () -> creditCard.setLimit(BigDecimal.valueOf(-1)));
        assertEquals("Dados inválidos para criação do cartão de crédito: O limite deve ser maior que zero.", exception.getMessage());
    }

    @Test
    void testSetClosingWithValidData() {
        creditCard.setClosing(15);
        assertEquals(15, creditCard.getClosing());
    }

    @Test
    void testSetClosingWithInvalidData() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () -> creditCard.setClosing(0));
        assertEquals("Dados inválidos para criação do cartão de crédito: O dia de fechamento deve estar entre 1 e 31.", exception.getMessage());
    }

    @Test
    void testSetPaymentWithValidData() {
        creditCard.setPayment(25);
        assertEquals(25, creditCard.getPayment());
    }

    @Test
    void testSetPaymentWithInvalidData() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () -> creditCard.setPayment(32));
        assertEquals("Dados inválidos para criação do cartão de crédito: O dia de pagamento deve estar entre 1 e 31.", exception.getMessage());
    }

    @Test
    void testSetUserWithValidData() {
        User newUser = new User("newuser@test.com", "newpassword123", "New User", LocalDate.of(2000, 8, 12), "user.png");
        creditCard.setUser(newUser);
        assertEquals(newUser, creditCard.getUser());
    }

    @Test
    void testSetUserWithNullValue() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () -> creditCard.setUser(null));
        assertEquals("Dados inválidos para criação do cartão de crédito: O usuário não pode ser nulo.", exception.getMessage());
    }

    @Test
    void testSetAccountWithValidData() {
        Account newAccount = new Account("NuBank", BigDecimal.valueOf(1000), "nubank.png", user);
        creditCard.setAccount(newAccount);
        assertEquals(newAccount, creditCard.getAccount());
    }

    @Test
    void testSetAccountIdWithNullValue() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () -> creditCard.setAccount(null));
        assertEquals("Dados inválidos para criação do cartão de crédito: A conta não pode ser nula", exception.getMessage());
    }

    @Test
    void testSetIcon() {
        creditCard.setIcon("new_icon.png");
        assertEquals("new_icon.png", creditCard.getIcon());
    }
}
