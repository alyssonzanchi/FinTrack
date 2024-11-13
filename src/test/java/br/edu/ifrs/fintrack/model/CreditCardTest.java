package br.edu.ifrs.fintrack.model;

import br.edu.ifrs.fintrack.exception.InvalidCreditCardDataException;
import br.edu.ifrs.fintrack.exception.MissingCreditCardFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreditCardTest {
    private CreditCard creditCard;

    @BeforeEach
    void setUp() {
        creditCard = new CreditCard("Cartão Teste", BigDecimal.valueOf(5000), 10, 20, 1, 1, "icon.png");
    }

    @Test
    void testCreditCardCreationWithValidData() {
        assertEquals("Cartão Teste", creditCard.getName());
        assertEquals(BigDecimal.valueOf(5000), creditCard.getLimit());
        assertEquals(10, creditCard.getClosing());
        assertEquals(20, creditCard.getPayment());
        assertEquals(1, creditCard.getUserId());
        assertEquals(1, creditCard.getAccountId());
        assertEquals("icon.png", creditCard.getIcon());
    }

    @Test
    void testCreditCardCreationWithNullName() {
        Exception exception = assertThrows(MissingCreditCardFieldException.class, () ->
                new CreditCard(null, BigDecimal.valueOf(5000), 10, 20, 1, 1, "icon.png"));
        assertEquals("Campo obrigatório ausente: name", exception.getMessage());
    }

    @Test
    void testCreditCardCreationWithEmptyName() {
        Exception exception = assertThrows(MissingCreditCardFieldException.class, () ->
                new CreditCard("", BigDecimal.valueOf(5000), 10, 20, 1, 1, "icon.png"));
        assertEquals("Campo obrigatório ausente: name", exception.getMessage());
    }

    @Test
    void testCreditCardCreationWithNegativeLimit() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () ->
                new CreditCard("Cartão Teste", BigDecimal.valueOf(-500), 10, 20, 1, 1, "icon.png"));
        assertEquals("Dados inválidos para criação do cartão de crédito: O limite deve ser maior que zero.", exception.getMessage());
    }

    @Test
    void testCreditCardCreationWithInvalidClosingDay() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () ->
                new CreditCard("Cartão Teste", BigDecimal.valueOf(5000), 0, 20, 1, 1, "icon.png"));
        assertEquals("Dados inválidos para criação do cartão de crédito: O dia de fechamento deve estar entre 1 e 31.", exception.getMessage());
    }

    @Test
    void testCreditCardCreationWithInvalidPaymentDay() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () ->
                new CreditCard("Cartão Teste", BigDecimal.valueOf(5000), 10, 32, 1, 1, "icon.png"));
        assertEquals("Dados inválidos para criação do cartão de crédito: O dia de pagamento deve estar entre 1 e 31.", exception.getMessage());
    }

    @Test
    void testCreditCardCreationWithInvalidUserId() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () ->
                new CreditCard("Cartão Teste", BigDecimal.valueOf(5000), 10, 20, 0, 1, "icon.png"));
        assertEquals("Dados inválidos para criação do cartão de crédito: O ID do usuário deve ser maior que zero.", exception.getMessage());
    }

    @Test
    void testCreditCardCreationWithInvalidAccountId() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () ->
                new CreditCard("Cartão Teste", BigDecimal.valueOf(5000), 10, 20, 1, 0, "icon.png"));
        assertEquals("Dados inválidos para criação do cartão de crédito: O ID da conta deve ser maior que zero.", exception.getMessage());
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
    void testSetUserIdWithValidData() {
        creditCard.setUserId(2);
        assertEquals(2, creditCard.getUserId());
    }

    @Test
    void testSetUserIdWithInvalidData() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () -> creditCard.setUserId(0));
        assertEquals("Dados inválidos para criação do cartão de crédito: O ID do usuário deve ser maior que zero.", exception.getMessage());
    }

    @Test
    void testSetAccountIdWithValidData() {
        creditCard.setAccountId(2);
        assertEquals(2, creditCard.getAccountId());
    }

    @Test
    void testSetAccountIdWithInvalidData() {
        Exception exception = assertThrows(InvalidCreditCardDataException.class, () -> creditCard.setAccountId(0));
        assertEquals("Dados inválidos para criação do cartão de crédito: O ID da conta deve ser maior que zero.", exception.getMessage());
    }

    @Test
    void testSetIcon() {
        creditCard.setIcon("new_icon.png");
        assertEquals("new_icon.png", creditCard.getIcon());
    }
}
