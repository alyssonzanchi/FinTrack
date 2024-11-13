package br.edu.ifrs.fintrack.model;

import br.edu.ifrs.fintrack.exception.InvalidAccountDataException;
import br.edu.ifrs.fintrack.exception.MissingRequiredFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTest {
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("Conta Teste", BigDecimal.valueOf(100), "icon.png", 1);
    }

    @Test
    void testAccountCreationWithValidData() {
        assertEquals("Conta Teste", account.getName());
        assertEquals(BigDecimal.valueOf(100), account.getBalance());
        assertEquals("icon.png", account.getIcon());
        assertEquals(1, account.getUserId());
    }

    @Test
    void testAccountCreationWithNullName() {
        Exception exception = assertThrows(MissingRequiredFieldException.class, () ->
                new Account(null, BigDecimal.valueOf(100), "icon.png", 1));
        assertEquals("Campo obrigatório ausente: name", exception.getMessage());
    }

    @Test
    void testAccountCreationWithEmptyName() {
        Exception exception = assertThrows(MissingRequiredFieldException.class, () ->
                new Account("", BigDecimal.valueOf(100), "icon.png", 1));
        assertEquals("Campo obrigatório ausente: name", exception.getMessage());
    }

    @Test
    void testAccountCreationWithNegativeBalance() {
        Exception exception = assertThrows(InvalidAccountDataException.class, () ->
                new Account("Conta Teste", BigDecimal.valueOf(-1), "icon.png", 1));
        assertEquals("Dados inválidos para a criação da conta: O saldo deve ser positivo", exception.getMessage());
    }

    @Test
    void testAccountCreationWithInvalidUserId() {
        Exception exception = assertThrows(InvalidAccountDataException.class, () ->
                new Account("Conta Teste", BigDecimal.valueOf(100), "icon.png", 0));
        assertEquals("Dados inválidos para a criação da conta: O ID do usuário deve ser maior que zero", exception.getMessage());
    }

    @Test
    void testSetNameWithValidData() {
        account.setName("Nova Conta");
        assertEquals("Nova Conta", account.getName());
    }

    @Test
    void testSetNameWithNull() {
        Exception exception = assertThrows(MissingRequiredFieldException.class, () -> account.setName(null));
        assertEquals("Campo obrigatório ausente: name", exception.getMessage());
    }

    @Test
    void testSetNameWithEmptyString() {
        Exception exception = assertThrows(MissingRequiredFieldException.class, () -> account.setName(""));
        assertEquals("Campo obrigatório ausente: name", exception.getMessage());
    }

    @Test
    void testSetBalanceWithValidData() {
        account.setBalance(BigDecimal.valueOf(200));
        assertEquals(BigDecimal.valueOf(200), account.getBalance());
    }

    @Test
    void testSetBalanceWithNegativeValue() {
        Exception exception = assertThrows(InvalidAccountDataException.class, () -> account.setBalance(BigDecimal.valueOf(-10)));
        assertEquals("Dados inválidos para a criação da conta: O saldo deve ser positivo", exception.getMessage());
    }

    @Test
    void testSetUserIdWithValidData() {
        account.setUserId(2);
        assertEquals(2, account.getUserId());
    }

    @Test
    void testSetUserIdWithInvalidValue() {
        Exception exception = assertThrows(InvalidAccountDataException.class, () -> account.setUserId(0));
        assertEquals("Dados inválidos para a criação da conta: O ID do usuário deve ser maior que zero", exception.getMessage());
    }

    @Test
    void testSetIcon() {
        account.setIcon("new_icon.png");
        assertEquals("new_icon.png", account.getIcon());
    }
}
