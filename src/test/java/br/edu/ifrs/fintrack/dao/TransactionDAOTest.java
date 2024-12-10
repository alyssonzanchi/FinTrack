package br.edu.ifrs.fintrack.dao;
import br.edu.ifrs.fintrack.exception.EntityNotFoundException;
import br.edu.ifrs.fintrack.model.Account;
import br.edu.ifrs.fintrack.model.Transaction;
import br.edu.ifrs.fintrack.model.User;
import br.edu.ifrs.fintrack.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionDAOTest {

    private TransactionDAO transactionDAO;
    private User testUser;
    private Account testAccount;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        transactionDAO = new TransactionDAO();
        UserDAO userDAO = new UserDAO();
        AccountDAO accountDAO = new AccountDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        try (Connection con = ConnectionFactory.getConnection()) {
            Statement stmt = con.createStatement();
            stmt.execute("TRUNCATE TABLE \"Transactions\" RESTART IDENTITY CASCADE");
            stmt.execute("TRUNCATE TABLE \"Accounts\" RESTART IDENTITY CASCADE");
            stmt.execute("TRUNCATE TABLE \"Users\" RESTART IDENTITY CASCADE");
            stmt.execute("TRUNCATE TABLE \"CreditCard\" RESTART IDENTITY CASCADE");
            stmt.execute("TRUNCATE TABLE \"Invoices\" RESTART IDENTITY CASCADE");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar o banco de dados para os testes.", e);
        }

        testUser = new User("user@test.com", "password123", "User Teste", LocalDate.of(1990, 1, 1), "image.png");
        userDAO.insert(testUser);

        testAccount = new Account("Nubank", new BigDecimal("1500.00"), "Nubank", testUser);
        accountDAO.insert(testAccount);

        testCategory = new Category("DESPESA", "Supermercado", "icon.png");
        categoryDAO.insert(testCategory);
    }

    @Test
    void testInsert() {
        String name = "Compra Mercado";
        String type = "DESPESA";
        BigDecimal amount = new BigDecimal("150.50");
        LocalDate date = LocalDate.of(2024, 12, 10);
        String description = "Compra no supermercado";
        User user = testUser;
        Category category = testCategory;
        Account account = testAccount;

        Transaction transaction = new Transaction(
                name,
                type,
                amount,
                date,
                description,
                null,
                null,
                null,
                null,
                user,
                category,
                account,
                null,
                null
        );

        boolean isInserted = transactionDAO.insert(transaction);
        assertTrue(isInserted, "A transação deveria ter sido inserida com sucesso.");
    }

    @Test
    void testGet_NonExistingTransaction() {
        assertThrows(EntityNotFoundException.class, () -> transactionDAO.get(999), "Deveria lançar exceção ao buscar transação inexistente.");
    }

    @Test
    void testDelete_ExistingTransaction() {
        String name = "Compra Mercado";
        String type = "DESPESA";
        BigDecimal amount = new BigDecimal("150.50");
        LocalDate date = LocalDate.of(2024, 12, 10);
        String description = "Compra no supermercado";
        User user = testUser;
        Category category = testCategory;
        Account account = testAccount;

        Transaction transaction = new Transaction(
                name,
                type,
                amount,
                date,
                description,
                null,
                null,
                null,
                null,
                user,
                category,
                account,
                null,
                null
        );
        transactionDAO.insert(transaction);

        boolean isDeleted = transactionDAO.delete(transaction.getId());
        assertTrue(isDeleted, "A transação deveria ter sido deletada com sucesso.");
        assertThrows(EntityNotFoundException.class, () -> transactionDAO.get(transaction.getId()), "Transação deletada não deveria ser encontrada.");
    }

    @Test
    void testDelete_NonExistingTransaction() {
        boolean result = transactionDAO.delete(999);
        assertFalse(result, "A exclusão de uma transação inexistente deveria retornar false.");
    }

    @Test
    void testUpdate_ExistingTransaction() {
        String name = "Compra Mercado";
        String type = "DESPESA";
        BigDecimal amount = new BigDecimal("150.50");
        LocalDate date = LocalDate.of(2024, 12, 10);
        String description = "Compra no supermercado";
        User user = testUser;
        Category category = testCategory;
        Account account = testAccount;

        Transaction transaction = new Transaction(
                name,
                type,
                amount,
                date,
                description,
                null,
                null,
                null,
                null,
                user,
                category,
                account,
                null,
                null
        );
        transactionDAO.insert(transaction);

        transaction.setDescription("Compra alterada");
        boolean isUpdated = transactionDAO.update(transaction);
        assertTrue(isUpdated, "A transação deveria ter sido atualizada com sucesso.");

        Transaction updatedTransaction = transactionDAO.get(transaction.getId());
        assertEquals("Compra alterada", updatedTransaction.getDescription(), "A descrição da transação deveria ter sido atualizada.");
    }

    @Test
    void testUpdate_NonExistingTransaction() {
        String name = "Compra Mercado";
        String type = "DESPESA";
        BigDecimal amount = new BigDecimal("150.50");
        LocalDate date = LocalDate.of(2024, 12, 10);
        String description = "Compra no supermercado";
        User user = testUser;
        Category category = testCategory;
        Account account = testAccount;

        Transaction transaction = new Transaction(
                name,
                type,
                amount,
                date,
                description,
                null,
                null,
                null,
                null,
                user,
                category,
                account,
                null,
                null
        );
        transaction.setId(999);

        boolean result = transactionDAO.update(transaction);
        assertFalse(result, "A atualização de uma transação inexistente deveria retornar false.");
    }

    @Test
    void testList() {
        String name1 = "Compra Mercado";
        String type1 = "DESPESA";
        BigDecimal amount1 = new BigDecimal("150.50");
        LocalDate date1 = LocalDate.of(2024, 12, 10);
        String description1 = "Compra no supermercado";

        String name2 = "Aluguel";
        String type2 = "DESPESA";
        BigDecimal amount2 = new BigDecimal("800.00");
        LocalDate date2 = LocalDate.of(2024, 12, 5);
        String description2 = "Pagamento do aluguel";
        String recurring2 = "FIXA";
        String fixedFrequency2 = "Mensal";

        Transaction transaction1 = new Transaction(
                name1,
                type1,
                amount1,
                date1,
                description1,
                null,
                null,
                null,
                null,
                testUser,
                testCategory,
                testAccount,
                null,
                null
        );

        Transaction transaction2 = new Transaction(
                name2,
                type2,
                amount2,
                date2,
                description2,
                recurring2,
                fixedFrequency2,
                null,
                null,
                testUser,
                testCategory,
                testAccount,
                null,
                null
        );

        transactionDAO.insert(transaction1);
        transactionDAO.insert(transaction2);

        List<Transaction> transactions = transactionDAO.list(10, 0);
        assertEquals(2, transactions.size(), "Deveriam ter sido retornadas duas transações.");
    }
}
