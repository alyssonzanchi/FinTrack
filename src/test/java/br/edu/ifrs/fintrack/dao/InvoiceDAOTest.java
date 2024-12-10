package br.edu.ifrs.fintrack.dao;

import br.edu.ifrs.fintrack.exception.EntityNotFoundException;
import br.edu.ifrs.fintrack.model.Account;
import br.edu.ifrs.fintrack.model.CreditCard;
import br.edu.ifrs.fintrack.model.Invoice;
import br.edu.ifrs.fintrack.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceDAOTest {

    private InvoiceDAO invoiceDAO;
    private CreditCard testCreditCard;

    @BeforeEach
    void setUp() {
        CreditCardDAO creditCardDAO = new CreditCardDAO();
        invoiceDAO = new InvoiceDAO();
        UserDAO userDAO = new UserDAO();
        AccountDAO accountDAO = new AccountDAO();
        try (Connection con = ConnectionFactory.getConnection()) {
            Statement stmt = con.createStatement();
            stmt.execute("TRUNCATE TABLE \"Invoices\" RESTART IDENTITY CASCADE");
            stmt.execute("TRUNCATE TABLE \"CreditCard\" RESTART IDENTITY CASCADE");
            stmt.execute("TRUNCATE TABLE \"Accounts\" RESTART IDENTITY CASCADE");
            stmt.execute("TRUNCATE TABLE \"Users\" RESTART IDENTITY CASCADE");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar o banco de dados para os testes.", e);
        }

        User testUser = new User("user@test.com", "password123", "User Teste", LocalDate.of(1990, 1, 1), "image.png");
        Account testAccount = new Account("Nubank", new BigDecimal("1500.00"), "Nubank", testUser);
        userDAO.insert(testUser);
        accountDAO.insert(testAccount);

        testCreditCard = new CreditCard("MasterCard", "icon.png", new BigDecimal("1500"), 4, 10, testUser, testAccount);
        creditCardDAO.insert(testCreditCard);
    }

    @Test
    void testInsert() {
        Invoice invoice = new Invoice(
                testCreditCard,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 20),
                new BigDecimal("200.00"),
                false
        );
        boolean isInserted = invoiceDAO.insert(invoice);
        assertTrue(isInserted, "A fatura deveria ter sido inserida com sucesso.");
    }

    @Test
    void testGet_NonExistingInvoice() {
        assertThrows(EntityNotFoundException.class, () -> invoiceDAO.get(999), "Deveria lançar exceção ao buscar fatura inexistente.");
    }

    @Test
    void testDelete_ExistingInvoice() {
        Invoice invoice = new Invoice(
                testCreditCard,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 20),
                new BigDecimal("200.00"),
                false
        );
        invoiceDAO.insert(invoice);

        boolean isDeleted = invoiceDAO.delete(invoice.getId());
        assertTrue(isDeleted, "A fatura deveria ter sido deletada com sucesso.");
        assertThrows(EntityNotFoundException.class, () -> invoiceDAO.get(invoice.getId()), "Fatura deletada não deveria ser encontrada.");
    }

    @Test
    void testDelete_NonExistingInvoice() {
        boolean result = invoiceDAO.delete(999);
        assertFalse(result, "A exclusão de uma fatura inexistente deveria retornar false.");
    }

    @Test
    void testUpdate_ExistingInvoice() {
        Invoice invoice = new Invoice(
                testCreditCard,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 20),
                new BigDecimal("200.00"),
                false
        );
        invoiceDAO.insert(invoice);

        invoice.setPaid(true);
        boolean isUpdated = invoiceDAO.update(invoice);
        assertTrue(isUpdated, "A fatura deveria ter sido atualizada com sucesso.");

        Invoice updatedInvoice = invoiceDAO.get(invoice.getId());
        assertTrue(updatedInvoice.isPaid(), "A fatura deveria estar marcada como paga.");
    }

    @Test
    void testUpdate_NonExistingInvoice() {
        Invoice invoice = new Invoice(
                testCreditCard,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 20),
                new BigDecimal("200.00"),
                false
        );
        invoice.setId(999);

        boolean result = invoiceDAO.update(invoice);
        assertFalse(result, "A atualização de uma fatura inexistente deveria retornar false.");
    }

    @Test
    void testList() {
        Invoice invoice1 = new Invoice(
                testCreditCard,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 20),
                new BigDecimal("200.00"),
                false
        );
        Invoice invoice2 = new Invoice(
                testCreditCard,
                LocalDate.of(2024, 1, 2),
                LocalDate.of(2024, 1, 16),
                LocalDate.of(2024, 1, 21),
                new BigDecimal("300.00"),
                false
        );
        invoiceDAO.insert(invoice1);
        invoiceDAO.insert(invoice2);

        List<Invoice> invoices = invoiceDAO.list(10, 0);
        assertEquals(2, invoices.size(), "Deveriam ter sido retornadas duas faturas.");
    }

    @Test
    void testListByCreditCard() {
        Invoice invoice1 = new Invoice(
                testCreditCard,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 20),
                new BigDecimal("200.00"),
                false
        );
        Invoice invoice2 = new Invoice(
                testCreditCard,
                LocalDate.of(2024, 1, 2),
                LocalDate.of(2024, 1, 16),
                LocalDate.of(2024, 1, 21),
                new BigDecimal("300.00"),
                false
        );

        invoiceDAO.insert(invoice1);
        invoiceDAO.insert(invoice2);

        List<Invoice> invoices = invoiceDAO.listByCreditCard(testCreditCard.getId());

        assertEquals(2, invoices.size(), "Deveriam ter sido retornadas duas faturas para o cartão de crédito especificado.");
    }
}
