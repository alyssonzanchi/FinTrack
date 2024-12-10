package br.edu.ifrs.fintrack.dao;

import br.edu.ifrs.fintrack.exception.DataAccessException;
import br.edu.ifrs.fintrack.exception.EntityNotFoundException;
import br.edu.ifrs.fintrack.model.Account;
import br.edu.ifrs.fintrack.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements DAO<Account> {

    private final UserDAO userDAO = new UserDAO();

    @Override
    public boolean insert(Account account) {
        String query = "INSERT INTO \"Accounts\" (name, balance, icon, user_id) VALUES (?,?,?,?)";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); // Solicita as chaves geradas
            pstm.setString(1, account.getName());
            pstm.setBigDecimal(2, account.getBalance());
            pstm.setString(3, account.getIcon());
            pstm.setInt(4, account.getUser().getId());

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected == 0) {
                return false;
            }

            ResultSet generatedKeys = pstm.getGeneratedKeys();
            if (generatedKeys.next()) {
                account.setId(generatedKeys.getInt(1));
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
        String query = "DELETE FROM \"Accounts\" WHERE id = ?";

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
    public boolean update(Account account) {
        String query = "UPDATE \"Accounts\" SET name = ?, balance = ?, icon = ?, user_id = ? WHERE id = ?";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setString(1, account.getName());
            pstm.setBigDecimal(2, account.getBalance());
            pstm.setString(3, account.getIcon());
            pstm.setInt(4, account.getUser().getId());
            pstm.setInt(5, account.getId());

            int rowsAffected = pstm.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Account> list(int limit, int offset) {
        String query = "SELECT * FROM \"Accounts\" LIMIT ? OFFSET ?";
        List<Account> accounts = new ArrayList<>();

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, limit);
            pstm.setInt(2, offset);

            ResultSet rs = pstm.executeQuery();

            while(rs.next()) {
                User user = userDAO.get(rs.getInt("user_id"));
                Account account = new Account(
                        rs.getString("name"),
                        rs.getBigDecimal("balance"),
                        rs.getString("icon"),
                        user
                );
                account.setId(rs.getInt("id"));

                accounts.add(account);
            }

            return accounts;
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao listar contas: " + e.getMessage());
        }
    }

    @Override
    public Account get(int id) {
        String query = "SELECT * FROM \"Accounts\" WHERE id = ?";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            if(rs.next()) {
                User user = userDAO.get(rs.getInt("user_id"));
                Account account = new Account(
                        rs.getString("name"),
                        rs.getBigDecimal("balance"),
                        rs.getString("icon"),
                        user
                );
                account.setId(rs.getInt("id"));

                return account;
            } else {
                throw new EntityNotFoundException("Conta com id " + id + " não encontrada.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao buscar conta: " + e.getMessage());
        }
    }

    public List<Account> listByUser(int userId) {
        String query = "SELECT * FROM \"Accounts\" WHERE user_id = ?";
        List<Account> accounts = new ArrayList<>();

        try (Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, userId);

            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                User user = userDAO.get(rs.getInt("user_id"));
                Account account = new Account(
                        rs.getString("name"),
                        rs.getBigDecimal("balance"),
                        rs.getString("icon"),
                        user
                );
                account.setId(rs.getInt("id"));
                accounts.add(account);
            }

            return accounts;
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao listar contas do usuário com ID " + userId + ": " + e.getMessage());
        }
    }

}
