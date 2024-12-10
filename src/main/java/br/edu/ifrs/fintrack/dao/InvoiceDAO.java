package br.edu.ifrs.fintrack.dao;

import br.edu.ifrs.fintrack.exception.DataAccessException;
import br.edu.ifrs.fintrack.exception.EntityNotFoundException;
import br.edu.ifrs.fintrack.model.CreditCard;
import br.edu.ifrs.fintrack.model.Invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO implements DAO<Invoice> {

    private final CreditCardDAO creditCardDAO = new CreditCardDAO();

    @Override
    public boolean insert(Invoice invoice) {
        String query = "INSERT INTO \"Invoices\" (credit_card_id, start_date, end_date, due_date, total, paid) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, invoice.getCreditCard().getId());
            pstm.setDate(2, Date.valueOf(invoice.getStartDate()));
            pstm.setDate(3, Date.valueOf(invoice.getEndDate()));
            pstm.setDate(4, Date.valueOf(invoice.getDueDate()));
            pstm.setBigDecimal(5, invoice.getTotal());
            pstm.setBoolean(6, invoice.isPaid());

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected == 0) {
                return false;
            }

            ResultSet generatedKeys = pstm.getGeneratedKeys();
            if (generatedKeys.next()) {
                invoice.setId(generatedKeys.getInt(1));
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM \"Invoices\" WHERE id = ?";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, id);

            int rowsAffected = pstm.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Invoice invoice) {
        String query = "UPDATE \"Invoices\" SET credit_card_id = ?, start_date = ?, end_date = ?, due_date = ?, " +
                "total = ?, paid = ? WHERE id = ?";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, invoice.getCreditCard().getId());
            pstm.setDate(2, Date.valueOf(invoice.getStartDate()));
            pstm.setDate(3, Date.valueOf(invoice.getEndDate()));
            pstm.setDate(4, Date.valueOf(invoice.getDueDate()));
            pstm.setBigDecimal(5, invoice.getTotal());
            pstm.setBoolean(6, invoice.isPaid());
            pstm.setInt(7, invoice.getId());

            int rowsAffected = pstm.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Invoice> list(int limit, int offset) throws DataAccessException {
        String query = "SELECT * FROM \"Invoices\" ORDER BY id LIMIT ? OFFSET ?";
        List<Invoice> invoices = new ArrayList<>();

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, limit);
            pstm.setInt(2, offset);

            ResultSet rs = pstm.executeQuery();

            while(rs.next()) {
                CreditCard creditCard = creditCardDAO.get(rs.getInt("credit_card_id"));
                Invoice invoice = new Invoice(
                        creditCard,
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getBigDecimal("total"),
                        rs.getBoolean("paid")
                );
                invoice.setId(rs.getInt("id"));

                invoices.add(invoice);
            }

            return invoices;
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao listar faturas: " + e.getMessage());
        }
    }

    @Override
    public Invoice get(int id) throws EntityNotFoundException, DataAccessException {
        String query = "SELECT * FROM \"Invoices\" WHERE id = ?";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            if(rs.next()) {
                CreditCard creditCard = creditCardDAO.get(rs.getInt("credit_card_id"));

                Invoice invoice = new Invoice(
                        creditCard,
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getBigDecimal("total"),
                        rs.getBoolean("paid")
                );
                invoice.setId(rs.getInt("id"));

                return invoice;
            } else {
                throw new EntityNotFoundException("Fatura com id " + id + " não encontrada.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao buscar fatura: " + e.getMessage());
        }
    }

    public List<Invoice> listByCreditCard(int creditCardId) {
        String query = "SELECT * FROM \"Invoices\" WHERE credit_card_id = ?";
        List<Invoice> invoices = new ArrayList<>();

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, creditCardId);

            ResultSet rs = pstm.executeQuery();

            while(rs.next()) {
                CreditCard creditCard = creditCardDAO.get(rs.getInt("credit_card_id"));
                Invoice invoice = new Invoice(
                        creditCard,
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getBigDecimal("total"),
                        rs.getBoolean("paid")
                );
                invoice.setId(rs.getInt("id"));

                invoices.add(invoice);
            }

            return invoices;
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao listar faturas: " + e.getMessage());
        }
    }
}
