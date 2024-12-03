package br.edu.ifrs.fintrack.dao;

import br.edu.ifrs.fintrack.exception.EntityNotFoundException;
import br.edu.ifrs.fintrack.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryDAOTest {
    private CategoryDAO categoryDAO;

    @BeforeEach
    void setUp() {
        categoryDAO = new CategoryDAO();
        try (Connection con = ConnectionFactory.getConnection()) {
            Statement stmt = con.createStatement();
            stmt.execute("TRUNCATE TABLE \"Categories\" RESTART IDENTITY CASCADE");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar o banco de dados para os testes.", e);
        }
    }

    @Test
    void testInsert() {
        Category category = new Category("RECEITA", "Salário", "dolar.png");
        boolean isInserted = categoryDAO.insert(category);
        assertTrue(isInserted, "A categoria deveria ter sido inserida com sucesso.");
    }

    @Test
    void testGet_NonExistingCategory() {
        assertThrows(EntityNotFoundException.class, () -> categoryDAO.get(999), "Deveria lançar exceção ao buscar categoria inexistente.");
    }

    @Test
    void testDelete_ExistingCategory() {
        Category category = new Category("RECEITA", "Salário", "dolar.png");
        categoryDAO.insert(category);

        boolean isDeleted = categoryDAO.delete(category.getId());
        assertTrue(isDeleted, "A categoria deveria ter sido deletada com sucesso.");
        assertThrows(EntityNotFoundException.class, () -> categoryDAO.get(category.getId()), "Categoria deletada não deveria ser encontrada.");
    }

    @Test
    void testDelete_NonExistingCategory() {
        boolean result = categoryDAO.delete(999);
        assertFalse(result, "A exclusão de uma categoria inexistente deveria retornar false.");
    }

    @Test
    void testUpdate_ExistingCategory() {
        Category category = new Category("RECEITA", "Salário", "dolar.png");
        categoryDAO.insert(category);

        category.setName("Investimento");
        boolean isUpdated = categoryDAO.update(category);
        assertTrue(isUpdated, "A categoria deveria ter sido atualizada com sucesso.");

        Category updatedCategory = categoryDAO.get(category.getId());
        assertEquals("Investimento", updatedCategory.getName(), "O nome da categoria deveria ter sido atualizado.");
    }

    @Test
    void testUpdate_NonExistingCategory() {
        Category category = new Category("RECEITA", "Salário", "dolar.png");
        category.setId(999);

        boolean result = categoryDAO.update(category);
        assertFalse(result, "A atualização de uma categoria inexistente deveria retornar false.");
    }

    @Test
    void testList() {
        Category category1 = new Category("RECEITA", "Salário", "dolar.png");
        Category category2 = new Category("RECEITA", "Investimento", "dolar.png");
        categoryDAO.insert(category1);
        categoryDAO.insert(category2);

        List<Category> categories = categoryDAO.list(10, 0);
        assertEquals(2, categories.size(), "Deveriam ter sido retornadas duas categorias.");
    }
}
