package br.edu.ifrs.fintrack.dao;

import br.edu.ifrs.fintrack.exception.EntityNotFoundException;
import br.edu.ifrs.fintrack.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountDAOTest {
    private AccountDAO accountDAO;

    @BeforeEach
    void setUp() {
        accountDAO = new AccountDAO();
        try (Connection con = ConnectionFactory.getConnection()) {
            Statement stmt = con.createStatement();
            stmt.execute("TRUNCATE TABLE \"Accounts\" RESTART IDENTITY CASCADE"); // Limpa a tabela antes dos testes
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar o banco de dados para os testes.", e);
        }
    }

    @Test
    void testInsert() {
        Account account = new Account("Nubank", new BigDecimal("1500.00"), "Nubank", 1);
        boolean isInserted = accountDAO.insert(account);
        assertTrue(isInserted, "O usuário deveria ter sido inserido com sucesso.");
    }

    @Test
    void testGet_NonExistingAccount() {
        assertThrows(EntityNotFoundException.class, () -> accountDAO.get(999), "Deveria lançar exceção ao buscar usuário inexistente.");
    }

    @Test
    void testDelete_ExistingAccount() {
        Account account = new Account("Nubank", new BigDecimal("1500.00"), "Nubank", 1);
        accountDAO.insert(account);

        boolean isDeleted = accountDAO.delete(account.getId());
        assertTrue(isDeleted, "A conta deveria ter sido deletada com sucesso.");
        assertThrows(EntityNotFoundException.class, () -> accountDAO.get(account.getId()), "Conta deletada não deveria ser encontrada.");
    }

    @Test
    void testDelete_NonExistingAccount() {
        boolean result = accountDAO.delete(999);
        assertFalse(result, "A exclusão de uma conta inexistente deveria retornar false.");
    }

    @Test
    void testUpdate_ExistingAccount() {
        Account account = new Account("Nubank", new BigDecimal("1500.00"), "Nubank", 1);
        accountDAO.insert(account);

        account.setName("Sicredi");
        boolean isUpdated = accountDAO.update(account);
        assertTrue(isUpdated, "A conta deveria ter sido atualizada com sucesso.");

        Account updatedAccount = accountDAO.get(account.getId());
        assertEquals("Sicredi", updatedAccount.getName(), "O nome da conta deveria ter sido atualizado.");
    }

    @Test
    void testUpdate_NonExistingAccount() {
        Account account = new Account("Nubank", new BigDecimal("1500.00"), "Nubank", 1);
        account.setId(999);

        boolean result = accountDAO.update(account);
        assertFalse(result, "A atualização de uma conta inexistente deveria retornar false.");
    }

    @Test
    void testList() {
        Account account1 = new Account("Nubank", new BigDecimal("1500.00"), "Nubank", 1);
        Account account2 = new Account("Sicredi", new BigDecimal("2000.00"), "Sicredi", 1);
        accountDAO.insert(account1);
        accountDAO.insert(account2);

        List<Account> accounts = accountDAO.list(10, 0);
        assertEquals(2, accounts.size(), "Deveriam ter sido retornadas duas contas.");
    }
}
