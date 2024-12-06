package br.edu.ifrs.fintrack.dao;

import br.edu.ifrs.fintrack.exception.EntityNotFoundException;
import br.edu.ifrs.fintrack.model.Account;
import br.edu.ifrs.fintrack.model.CreditCard;
import br.edu.ifrs.fintrack.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CreditCardDAOTest {
    private AccountDAO accountDAO;
    private UserDAO userDAO;
    private CreditCardDAO creditCardDAO;
    private User testUser;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        accountDAO = new AccountDAO();
        userDAO = new UserDAO();
        creditCardDAO = new CreditCardDAO();
        try (Connection con = ConnectionFactory.getConnection()) {
            Statement stmt = con.createStatement();
            stmt.execute("TRUNCATE TABLE \"CreditCard\" RESTART IDENTITY CASCADE");
            stmt.execute("TRUNCATE TABLE \"Accounts\" RESTART IDENTITY CASCADE");
            stmt.execute("TRUNCATE TABLE \"Users\" RESTART IDENTITY CASCADE");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar o banco de dados para os testes.", e);
        }

        testUser = new User("user@test.com", "password123", "User Teste", LocalDate.of(1990, 1, 1), "image.png");
        testAccount = new Account("Nubank", new BigDecimal("1500.00"), "Nubank", testUser);
        userDAO.insert(testUser);
        accountDAO.insert(testAccount);
    }

    @Test
    void testInsert() {
        CreditCard creditCard = new CreditCard("MasterCard", "icon.png", new BigDecimal("1500"), 4, 10, testUser, testAccount);
        boolean isInserted = creditCardDAO.insert(creditCard);
        assertTrue(isInserted, "O cartão de crédito deveria ter sido inserido com sucesso.");
    }

    @Test
    void testGet_NonExistingCreditCard() {
        assertThrows(EntityNotFoundException.class, () -> creditCardDAO.get(999), "Deveria lançar exceção ao buscar cartão de crédito inexistente.");
    }

    @Test
    void testDelete_ExistingCreditCard() {
        CreditCard creditCard = new CreditCard("MasterCard", "icon.png", new BigDecimal("1500.00"), 4, 10, testUser, testAccount);
        creditCardDAO.insert(creditCard);

        boolean isDeleted = creditCardDAO.delete(creditCard.getId());
        assertTrue(isDeleted, "O cartão de crédito deveria ter sido deletado com sucesso.");
        assertThrows(EntityNotFoundException.class, () -> creditCardDAO.get(creditCard.getId()), "Cartão de crédito deletado não deveria ser encontrado.");
    }

    @Test
    void testDelete_NonExistingCreditCard() {
        boolean result = creditCardDAO.delete(999);
        assertFalse(result, "A exclusão de um cartão de crédito inexistente deveria retornar false.");
    }

    @Test
    void testUpdate_ExistingCreditCard() {
        CreditCard creditCard = new CreditCard("MasterCard", "icon.png", new BigDecimal("1500.00"), 4, 10, testUser, testAccount);
        creditCardDAO.insert(creditCard);

        creditCard.setName("Visa");
        boolean isUpdated = creditCardDAO.update(creditCard);
        assertTrue(isUpdated, "O cartão de crédito deveria ter sido atualizado com sucesso.");

        CreditCard updatedCreditCard = creditCardDAO.get(creditCard.getId());
        assertEquals("Visa", updatedCreditCard.getName(), "O nome do cartão de crédito deveria ter sido atualizado.");
    }

    @Test
    void testUpdate_NonExistingCreditCard() {
        CreditCard creditCard = new CreditCard("MasterCard", "icon.png", new BigDecimal("1500.00"), 4, 10, testUser, testAccount);
        creditCard.setId(999);

        boolean result = creditCardDAO.update(creditCard);
        assertFalse(result, "A atualização de cartão de crédito inexistente deveria retornar false.");
    }

    @Test
    void testList() {
        CreditCard creditCard1 = new CreditCard("MasterCard", "icon.png", new BigDecimal("1500.00"), 4, 10, testUser, testAccount);
        CreditCard creditCard2 = new CreditCard("Visa", "icon.png", new BigDecimal("4000.00"), 4, 10, testUser, testAccount);
        creditCardDAO.insert(creditCard1);
        creditCardDAO.insert(creditCard2);

        List<CreditCard> creditCards = creditCardDAO.list(10, 0);
        assertEquals(2, creditCards.size(), "Deveriam ter sido retornados dois cartões de crédito.");
    }
}
