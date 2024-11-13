package br.edu.ifrs.fintrack.dao;

import br.edu.ifrs.fintrack.exception.DataAccessException;
import br.edu.ifrs.fintrack.exception.DatabaseIntegrityException;
import br.edu.ifrs.fintrack.exception.EntityNotFoundException;
import br.edu.ifrs.fintrack.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAO<User> {
    @Override
    public boolean insert(User user) {
        String query = "INSERT INTO \"Users\" (email, password, name, date_of_birth, image) VALUES (?,?,?,?,?)";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); // Solicita as chaves geradas
            pstm.setString(1, user.getEmail());
            pstm.setString(2, user.getPassword());
            pstm.setString(3, user.getName());
            pstm.setDate(4, java.sql.Date.valueOf(user.getDateOfBirth()));
            pstm.setString(5, user.getImage());

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected == 0) {
                throw new DataAccessException("Erro ao inserir usuário: nenhum registro foi inserido.");
            }

            // Captura o ID gerado automaticamente
            ResultSet generatedKeys = pstm.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1)); // Define o ID no objeto User
                return true;
            } else {
                throw new DataAccessException("Erro ao inserir usuário: nenhum ID gerado.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao inserir usuário: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM \"Users\" WHERE id = ?";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, id);

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected == 0) {
                throw new EntityNotFoundException("Usuário com id " + id + " não encontrado.");
            }
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DatabaseIntegrityException("Violação de integridade: não é possível excluir o usuário.");
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao excluir usuário: " + e.getMessage());
        }
    }

    @Override
    public boolean update(User user) {
        String query = "UPDATE \"Users\" SET email = ?, password = ?, name = ?, date_of_birth = ?, image = ? WHERE id = ?";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setString(1, user.getEmail());
            pstm.setString(2, user.getPassword());
            pstm.setString(3, user.getName());
            pstm.setDate(4, java.sql.Date.valueOf(user.getDateOfBirth()));
            pstm.setString(5, user.getImage());
            pstm.setInt(6, user.getId());

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected == 0) {
                throw new EntityNotFoundException("Usuário com id " + user.getId() + " não encontrado.");
            }
            return true;
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    @Override
    public List<User> list(int limit, int offset) {
        String query = "SELECT * FROM \"Users\" LIMIT ? OFFSET ?";
        List<User> users = new ArrayList<>();

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, limit);
            pstm.setInt(2, offset);

            ResultSet rs = pstm.executeQuery();

            while(rs.next()) {
                User user = new User(
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getDate("date_of_birth").toLocalDate(),
                        rs.getString("image")
                );
                user.setId(rs.getInt("id"));

                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao listar usuários: " + e.getMessage());
        }
    }

    @Override
    public User get(int id) {
        String query = "SELECT * FROM \"Users\" WHERE id = ?";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            if(rs.next()) {
                User user = new User(
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getDate("date_of_birth").toLocalDate(),
                        rs.getString("image")
                );
                user.setId(rs.getInt("id"));

                return user;
            } else {
                throw new EntityNotFoundException("Usuário com id " + id + " não encontrado.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao buscar usuário: " + e.getMessage());
        }
    }
}
