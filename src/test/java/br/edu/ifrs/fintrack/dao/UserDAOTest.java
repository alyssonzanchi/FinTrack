package br.edu.ifrs.fintrack.dao;

import br.edu.ifrs.fintrack.exception.EntityNotFoundException;
import br.edu.ifrs.fintrack.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
        try (Connection con = ConnectionFactory.getConnection()) {
            Statement stmt = con.createStatement();
            stmt.execute("TRUNCATE TABLE \"Users\" RESTART IDENTITY CASCADE"); // Limpa a tabela antes dos testes
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar o banco de dados para os testes.", e);
        }
    }

    @Test
    void testInsert() {
        User user = new User("test@example.com", "strongPassword", "Test User", LocalDate.of(1990, 1, 1), "image.png");
        boolean isInserted = userDAO.insert(user);
        assertTrue(isInserted, "O usuário deveria ter sido inserido com sucesso.");
    }

    @Test
    void testGet_ExistingUser() {
        User user = new User("test@example.com", "strongPassword", "Test User", LocalDate.of(1990, 1, 1), "image.png");
        userDAO.insert(user);

        User retrievedUser = userDAO.get(user.getId());
        assertNotNull(retrievedUser);
        assertEquals(user.getEmail(), retrievedUser.getEmail());
        assertEquals(user.getName(), retrievedUser.getName());
    }

    @Test
    void testGet_NonExistingUser() {
        assertThrows(EntityNotFoundException.class, () -> userDAO.get(999), "Deveria lançar exceção ao buscar usuário inexistente.");
    }

    @Test
    void testDelete_ExistingUser() {
        User user = new User("delete@example.com", "strongPassword", "Delete User", LocalDate.of(1990, 1, 1), "image.png");
        userDAO.insert(user);

        boolean isDeleted = userDAO.delete(user.getId());
        assertTrue(isDeleted, "O usuário deveria ter sido deletado com sucesso.");
        assertThrows(EntityNotFoundException.class, () -> userDAO.get(user.getId()), "Usuário deletado não deveria ser encontrado.");
    }

    @Test
    void testDelete_NonExistingUser() {
        boolean result = userDAO.delete(999);
        assertFalse(result, "A exclusão de um usuário inexistente deveria retornar false.");
    }

    @Test
    void testUpdate_ExistingUser() {
        User user = new User("update@example.com", "strongPassword", "Update User", LocalDate.of(1990, 1, 1), "image.png");
        userDAO.insert(user);

        user.setName("Updated User");
        boolean isUpdated = userDAO.update(user);
        assertTrue(isUpdated, "O usuário deveria ter sido atualizado com sucesso.");

        User updatedUser = userDAO.get(user.getId());
        assertEquals("Updated User", updatedUser.getName(), "O nome do usuário deveria ter sido atualizado.");
    }

    @Test
    void testUpdate_NonExistingUser() {
        User user = new User("nonexistent@example.com", "strongPassword", "Nonexistent User", LocalDate.of(1990, 1, 1), "image.png");
        user.setId(999);

        boolean result = userDAO.update(user);
        assertFalse(result, "A atualização de um usuário inexistente deveria retornar false.");
    }

    @Test
    void testList() {
        User user1 = new User("user1@example.com", "password1", "User One", LocalDate.of(1990, 1, 1), "image1.png");
        User user2 = new User("user2@example.com", "password2", "User Two", LocalDate.of(1992, 2, 2), "image2.png");
        userDAO.insert(user1);
        userDAO.insert(user2);

        List<User> users = userDAO.list(10, 0);
        assertEquals(2, users.size(), "Deveriam ter sido retornados dois usuários.");
    }

    @Test
    void testGetByEmail_ExistingUser() {
        User user = new User("email@example.com", "securePassword", "Email User", LocalDate.of(1990, 1, 1), "email_image.png");
        userDAO.insert(user);

        User retrievedUser = userDAO.getByEmail("email@example.com");
        assertNotNull(retrievedUser, "O usuário deveria ter sido retornado com sucesso.");
        assertEquals(user.getEmail(), retrievedUser.getEmail(), "O e-mail do usuário deveria coincidir.");
        assertEquals(user.getName(), retrievedUser.getName(), "O nome do usuário deveria coincidir.");
    }

    @Test
    void testGetByEmail_NonExistingUser() {
        assertThrows(EntityNotFoundException.class, () -> userDAO.getByEmail("nonexistent@example.com"),
                "Deveria lançar exceção ao buscar um usuário inexistente pelo e-mail.");
    }
}
