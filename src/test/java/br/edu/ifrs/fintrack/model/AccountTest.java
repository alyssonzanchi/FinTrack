package br.edu.ifrs.fintrack.model;

import br.edu.ifrs.fintrack.exception.InvalidAccountDataException;
import br.edu.ifrs.fintrack.exception.MissingRequiredFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTest {
    private Account account;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("user@test.com", "password123", "User Teste", LocalDate.of(1990, 1, 1), "image.png");
        account = new Account("Conta Teste", BigDecimal.valueOf(100), "icon.png", user);
    }

    @Test
    void testAccountCreationWithValidData() {
        assertEquals("Conta Teste", account.getName());
        assertEquals(BigDecimal.valueOf(100), account.getBalance());
        assertEquals("icon.png", account.getIcon());
        assertEquals(user, account.getUser());
    }

    @Test
    void testAccountCreationWithNullName() {
        Exception exception = assertThrows(MissingRequiredFieldException.class, () ->
                new Account(null, BigDecimal.valueOf(100), "icon.png", user));
        assertEquals("Campo obrigatório ausente: name", exception.getMessage());
    }

    @Test
    void testAccountCreationWithEmptyName() {
        Exception exception = assertThrows(MissingRequiredFieldException.class, () ->
                new Account("", BigDecimal.valueOf(100), "icon.png", user));
        assertEquals("Campo obrigatório ausente: name", exception.getMessage());
    }

    @Test
    void testAccountCreationWithNullUser() {
        Exception exception = assertThrows(InvalidAccountDataException.class, () ->
                new Account("Conta Teste", BigDecimal.valueOf(100), "icon.png", null));
        assertEquals("Dados inválidos para a criação da conta: Usuário não pode ser nulo", exception.getMessage());
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
    void testSetUserWithValidData() {
        User newUser = new User("newuser@test.com", "newpassword123", "New User", LocalDate.of(2000, 8, 12), "user.png");
        account.setUser(newUser);
        assertEquals(newUser, account.getUser());
    }

    @Test
    void testSetUserWithNullValue() {
        Exception exception = assertThrows(InvalidAccountDataException.class, () -> account.setUser(null));
        assertEquals("Dados inválidos para a criação da conta: Usuário não pode ser nulo", exception.getMessage());
    }

    @Test
    void testSetIcon() {
        account.setIcon("new_icon.png");
        assertEquals("new_icon.png", account.getIcon());
    }
}
