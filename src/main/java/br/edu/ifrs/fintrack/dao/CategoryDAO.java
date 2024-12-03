package br.edu.ifrs.fintrack.dao;

import br.edu.ifrs.fintrack.exception.DataAccessException;
import br.edu.ifrs.fintrack.exception.EntityNotFoundException;
import br.edu.ifrs.fintrack.model.Category;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements DAO<Category> {
    @Override
    public boolean insert(Category category) {
        String query = "INSERT INTO \"Categories\" (type, name, icon) VALUES (?, ?, ?)";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, category.getType());
            pstm.setString(2, category.getName());
            pstm.setString(3, category.getIcon());

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected == 0) {
                return false;
            }

            ResultSet generatedKeys = pstm.getGeneratedKeys();
            if (generatedKeys.next()) {
                category.setId(generatedKeys.getInt(1));
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
        String query = "DELETE FROM \"Categories\" WHERE id = ?";

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
    public boolean update(Category category) {
        String query = "UPDATE \"Categories\" SET type = ?, name = ?, icon = ? WHERE id = ?";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setString(1, category.getType());
            pstm.setString(2, category.getName());
            pstm.setString(3, category.getIcon());
            pstm.setInt(4, category.getId());

            int rowsAffected = pstm.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Category> list(int limit, int offset) throws DataAccessException {
        String query = "SELECT * FROM \"Categories\" ORDER BY id LIMIT ? OFFSET ?";
        List<Category> categories = new ArrayList<>();

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, limit);
            pstm.setInt(2, offset);

            ResultSet rs = pstm.executeQuery();

            while(rs.next()) {
                Category category = new Category(
                        rs.getString("type"),
                        rs.getString("name"),
                        rs.getString("icon")
                );
                category.setId(rs.getInt("id"));

                categories.add(category);
            }

            return categories;
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao listar categorias: " + e.getMessage());
        }
    }

    @Override
    public Category get(int id) throws EntityNotFoundException, DataAccessException {
        String query = "SELECT * FROM \"Categories\" WHERE id = ?";

        try(Connection con = ConnectionFactory.getConnection()) {
            var pstm = con.prepareStatement(query);
            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            if(rs.next()) {
                Category category = new Category(
                        rs.getString("type"),
                        rs.getString("name"),
                        rs.getString("icon")
                );
                category.setId(rs.getInt("id"));

                return category;
            } else {
                throw new EntityNotFoundException("Categoria com id " + id + " não encontrada.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao buscar categoria: " + e.getMessage());
        }
    }
}
