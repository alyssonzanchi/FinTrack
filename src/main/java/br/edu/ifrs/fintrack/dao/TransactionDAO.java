package br.edu.ifrs.fintrack.dao;

import br.edu.ifrs.fintrack.exception.DataAccessException;
import br.edu.ifrs.fintrack.exception.EntityNotFoundException;
import br.edu.ifrs.fintrack.model.*;

import java.sql.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO implements DAO<Transaction> {
    private final UserDAO userDAO = new UserDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final CreditCardDAO creditCardDAO = new CreditCardDAO();
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();

    @Override
    public boolean insert(Transaction transaction) {
        String query = "INSERT INTO \"Transactions\" " +
                "(name, type, amount, due_date, payment_date, paid, description, recurring, fixed_frequency, installment_frequency, installment_count, installment_number, " +
                "user_id, category_id, account_id, credit_card_id, invoice_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, transaction.getName());
            pstm.setString(2, transaction.getType());
            pstm.setBigDecimal(3, transaction.getAmount());
            pstm.setDate(4, Date.valueOf(transaction.getDueDate()));
            pstm.setDate(5, transaction.getPaymentDate() != null ? Date.valueOf(transaction.getPaymentDate()) : null);
            pstm.setBoolean(6, transaction.getPaid());
            pstm.setString(7, transaction.getDescription());
            pstm.setString(8, transaction.getRecurring());
            pstm.setString(9, transaction.getFixedFrequency());
            pstm.setString(10, transaction.getInstallmentFrequency());
            pstm.setObject(11, transaction.getInstallmentCount(), Types.INTEGER);
            pstm.setObject(12, transaction.getInstallmentNumber(), Types.INTEGER);
            pstm.setInt(13, transaction.getUser().getId());
            pstm.setInt(14, transaction.getCategory().getId());
            pstm.setInt(15, transaction.getAccount().getId());
            pstm.setObject(16, transaction.getCreditCard() != null ? transaction.getCreditCard().getId() : null, Types.INTEGER);
            pstm.setObject(17, transaction.getInvoice() != null ? transaction.getInvoice().getId() : null, Types.INTEGER);

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected == 0) {
                return false;
            }

            ResultSet generatedKeys = pstm.getGeneratedKeys();
            if (generatedKeys.next()) {
                transaction.setId(generatedKeys.getInt(1));
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM \"Transactions\" WHERE id = ?";

        try (Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, id);
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Transaction transaction) {
        String query = "UPDATE \"Transactions\" SET " +
                "name = ?, type = ?, amount = ?, due_date = ?, payment_date = ?, paid = ?, description = ?, recurring = ?, fixed_frequency = ?, " +
                "installment_frequency = ?, installment_count = ?, installment_number = ?, user_id = ?, category_id = ?, account_id = ?, " +
                "credit_card_id = ?, invoice_id = ? WHERE id = ?";

        try (Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setString(1, transaction.getName());
            pstm.setString(2, transaction.getType());
            pstm.setBigDecimal(3, transaction.getAmount());
            pstm.setDate(4, Date.valueOf(transaction.getDueDate()));
            pstm.setDate(5, transaction.getPaymentDate() != null ? Date.valueOf(transaction.getPaymentDate()) : null);
            pstm.setBoolean(6, transaction.getPaid());
            pstm.setString(7, transaction.getDescription());
            pstm.setString(8, transaction.getRecurring());
            pstm.setString(9, transaction.getFixedFrequency());
            pstm.setString(10, transaction.getInstallmentFrequency());
            pstm.setObject(11, transaction.getInstallmentCount(), Types.INTEGER);
            pstm.setObject(12, transaction.getInstallmentNumber(), Types.INTEGER);
            pstm.setInt(13, transaction.getUser().getId());
            pstm.setInt(14, transaction.getCategory().getId());
            pstm.setInt(15, transaction.getAccount().getId());
            pstm.setObject(16, transaction.getCreditCard() != null ? transaction.getCreditCard().getId() : null, Types.INTEGER);
            pstm.setObject(17, transaction.getInvoice() != null ? transaction.getInvoice().getId() : null, Types.INTEGER);
            pstm.setInt(18, transaction.getId());

            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Transaction> list(int limit, int offset) throws DataAccessException {
        String query = "SELECT * FROM \"Transactions\" ORDER BY id LIMIT ? OFFSET ?";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, limit);
            pstm.setInt(2, offset);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                User user = userDAO.get(rs.getInt("user_id"));
                Category category = categoryDAO.get(rs.getInt("category_id"));
                Account account = accountDAO.get(rs.getInt("account_id"));
                CreditCard creditCard = rs.getObject("credit_card_id") != null
                        ? creditCardDAO.get(rs.getInt("credit_card_id")) : null;
                Invoice invoice = rs.getObject("invoice_id") != null
                        ? invoiceDAO.get(rs.getInt("invoice_id")) : null;

                Transaction transaction = new Transaction(
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getBigDecimal("amount"),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getDate("payment_date") != null ? rs.getDate("payment_date").toLocalDate() : null,
                        rs.getBoolean("paid"),
                        rs.getString("description"),
                        rs.getString("recurring"),
                        rs.getString("fixed_frequency"),
                        rs.getString("installment_frequency"),
                        rs.getObject("installment_count", Integer.class),
                        rs.getObject("installment_number", Integer.class),
                        user,
                        category,
                        account,
                        creditCard,
                        invoice
                );
                transaction.setId(rs.getInt("id"));

                transactions.add(transaction);
            }
            return transactions;
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao listar transações: " + e.getMessage());
        }
    }

    @Override
    public Transaction get(int id) throws EntityNotFoundException, DataAccessException {
        String query = "SELECT * FROM \"Transactions\" WHERE id = ?";

        try (Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                User user = userDAO.get(rs.getInt("user_id"));
                Category category = categoryDAO.get(rs.getInt("category_id"));
                Account account = accountDAO.get(rs.getInt("account_id"));
                CreditCard creditCard = rs.getObject("credit_card_id") != null
                        ? creditCardDAO.get(rs.getInt("credit_card_id")) : null;
                Invoice invoice = rs.getObject("invoice_id") != null
                        ? invoiceDAO.get(rs.getInt("invoice_id")) : null;

                Transaction transaction = new Transaction(
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getBigDecimal("amount"),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getDate("payment_date") != null ? rs.getDate("payment_date").toLocalDate() : null,
                        rs.getBoolean("paid"),
                        rs.getString("description"),
                        rs.getString("recurring"),
                        rs.getString("fixed_frequency"),
                        rs.getString("installment_frequency"),
                        rs.getObject("installment_count", Integer.class),
                        rs.getObject("installment_number", Integer.class),
                        user,
                        category,
                        account,
                        creditCard,
                        invoice
                );
                transaction.setId(rs.getInt("id"));
                return transaction;
            } else {
                throw new EntityNotFoundException("Transação com id " + id + " não encontrada.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao buscar transação: " + e.getMessage());
        }
    }

    public List<Transaction> listByUser(int userId) {
        String query = "SELECT * FROM \"Transactions\" WHERE user_id = ?";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, userId);

            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                User user = userDAO.get(rs.getInt("user_id"));
                Category category = categoryDAO.get(rs.getInt("category_id"));
                Account account = accountDAO.get(rs.getInt("account_id"));
                CreditCard creditCard = rs.getObject("credit_card_id") != null
                        ? creditCardDAO.get(rs.getInt("credit_card_id")) : null;
                Invoice invoice = rs.getObject("invoice_id") != null
                        ? invoiceDAO.get(rs.getInt("invoice_id")) : null;

                Transaction transaction = new Transaction(
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getBigDecimal("amount"),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getDate("payment_date") != null ? rs.getDate("payment_date").toLocalDate() : null,
                        rs.getBoolean("paid"),
                        rs.getString("description"),
                        rs.getString("recurring"),
                        rs.getString("fixed_frequency"),
                        rs.getString("installment_frequency"),
                        rs.getObject("installment_count", Integer.class),
                        rs.getObject("installment_number", Integer.class),
                        user,
                        category,
                        account,
                        creditCard,
                        invoice
                );
                transaction.setId(rs.getInt("id"));

                transactions.add(transaction);
            }

            return transactions;
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao listar transações do usuário com id " + userId + ": " + e.getMessage());
        }
    }

    public List<Transaction> listByUserAndDueDate(int userId, YearMonth monthYear) {
        String query = "SELECT * FROM \"Transactions\" WHERE user_id = ? AND EXTRACT(YEAR FROM due_date) = ? AND EXTRACT(MONTH FROM due_date) = ?";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, userId);
            pstm.setInt(2, monthYear.getYear());
            pstm.setInt(3, monthYear.getMonthValue());

            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                User user = userDAO.get(rs.getInt("user_id"));
                Category category = categoryDAO.get(rs.getInt("category_id"));
                Account account = accountDAO.get(rs.getInt("account_id"));
                CreditCard creditCard = rs.getObject("credit_card_id") != null
                        ? creditCardDAO.get(rs.getInt("credit_card_id")) : null;
                Invoice invoice = rs.getObject("invoice_id") != null
                        ? invoiceDAO.get(rs.getInt("invoice_id")) : null;

                Transaction transaction = new Transaction(
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getBigDecimal("amount"),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getDate("payment_date") != null ? rs.getDate("payment_date").toLocalDate() : null,
                        rs.getBoolean("paid"),
                        rs.getString("description"),
                        rs.getString("recurring"),
                        rs.getString("fixed_frequency"),
                        rs.getString("installment_frequency"),
                        rs.getObject("installment_count", Integer.class),
                        rs.getObject("installment_number", Integer.class),
                        user,
                        category,
                        account,
                        creditCard,
                        invoice
                );
                transaction.setId(rs.getInt("id"));

                transactions.add(transaction);
            }

            return transactions;
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao listar transações do usuário com id " + userId + ": " + e.getMessage());
        }
    }
}
