package br.edu.ifrs.fintrack.dao;

import br.edu.ifrs.fintrack.exception.DataAccessException;
import br.edu.ifrs.fintrack.exception.EntityNotFoundException;
import br.edu.ifrs.fintrack.model.Account;
import br.edu.ifrs.fintrack.model.CreditCard;
import br.edu.ifrs.fintrack.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CreditCardDAO implements DAO<CreditCard> {

    private final UserDAO userDAO = new UserDAO();
    private final AccountDAO accountDAO = new AccountDAO();

    @Override
    public boolean insert(CreditCard creditCard) {

        String query = "INSERT INTO \"CreditCard\" (name, icon, \"limit\", closing, payment, user_id, account_id) VALUES (?,?,?,?,?,?,?)";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, creditCard.getName());
            pstm.setString(2, creditCard.getIcon());
            pstm.setBigDecimal(3, creditCard.getLimit());
            pstm.setInt(4, creditCard.getClosing());
            pstm.setInt(5, creditCard.getPayment());
            pstm.setInt(6, creditCard.getUser().getId());
            pstm.setInt(7, creditCard.getAccount().getId());

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected == 0) {
                return false;
            }

            ResultSet generatedKeys = pstm.getGeneratedKeys();
            if (generatedKeys.next()) {
                creditCard.setId(generatedKeys.getInt(1));
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
        String query = "DELETE FROM \"CreditCard\" WHERE id = ?";

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
    public boolean update(CreditCard creditCard) {
        String query = "UPDATE \"CreditCard\" SET name = ?, icon = ?, \"limit\" = ?, closing = ?, payment = ?, user_id = ?, account_id = ? WHERE id = ?";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setString(1, creditCard.getName());
            pstm.setString(2, creditCard.getIcon());
            pstm.setBigDecimal(3, creditCard.getLimit());
            pstm.setInt(4, creditCard.getClosing());
            pstm.setInt(5, creditCard.getPayment());
            pstm.setInt(6, creditCard.getUser().getId());
            pstm.setInt(7, creditCard.getAccount().getId());
            pstm.setInt(8, creditCard.getId());

            int rowsAffected = pstm.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<CreditCard> list(int limit, int offset) {
        String query = "SELECT * FROM \"CreditCard\" LIMIT ? OFFSET ?";
        List<CreditCard> creditCards = new ArrayList<>();

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, limit);
            pstm.setInt(2, offset);

            ResultSet rs = pstm.executeQuery();

            while(rs.next()) {
                User user = userDAO.get(rs.getInt("user_id"));
                Account account = accountDAO.get(rs.getInt("account_id"));

                CreditCard creditCard = new CreditCard(
                        rs.getString("name"),
                        rs.getString("icon"),
                        rs.getBigDecimal("limit"),
                        rs.getInt("closing"),
                        rs.getInt("payment"),
                        user,
                        account
                );
                creditCard.setId(rs.getInt("id"));

                creditCards.add(creditCard);
            }

            return creditCards;
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao listar cartões de crédito: " + e.getMessage());
        }
    }

    @Override
    public CreditCard get(int id) {
        String query = "SELECT * FROM \"CreditCard\" WHERE id = ?";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            if(rs.next()) {
                User user = userDAO.get(rs.getInt("user_id"));
                Account account = accountDAO.get(rs.getInt("account_id"));
                CreditCard creditCard = new CreditCard(
                        rs.getString("name"),
                        rs.getString("icon"),
                        rs.getBigDecimal("limit"),
                        rs.getInt("closing"),
                        rs.getInt("payment"),
                        user,
                        account
                );
                creditCard.setId(rs.getInt("id"));

                return creditCard;
            } else {
                throw new EntityNotFoundException("Cartão de crédito com id " + id + " não encontrado.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao buscar cartão de crédito: " + e.getMessage());
        }
    }

    public List<CreditCard> listByUser(int userId) {
        String query = "SELECT * FROM \"CreditCard\" WHERE user_id = ?";
        List<CreditCard> creditCards = new ArrayList<>();

        try (Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, userId);

            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                User user = userDAO.get(rs.getInt("user_id"));
                Account account = accountDAO.get(rs.getInt("account_id"));

                CreditCard creditCard = new CreditCard(
                        rs.getString("name"),
                        rs.getString("icon"),
                        rs.getBigDecimal("limit"),
                        rs.getInt("closing"),
                        rs.getInt("payment"),
                        user,
                        account
                );
                creditCard.setId(rs.getInt("id"));

                creditCards.add(creditCard);
            }

            return creditCards;
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao listar cartões de crédito para o usuário com id " + userId + ": " + e.getMessage());
        }
    }

}
