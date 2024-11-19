package br.edu.ifrs.fintrack.model;

import br.edu.ifrs.fintrack.exception.InvalidEmailException;
import br.edu.ifrs.fintrack.exception.InvalidPasswordException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private static User user;

    @BeforeAll
    static void setUp() {
        // Cria um usuário válido antes de cada teste
        user = new User("test@example.com", "strongPassword", "Test User", LocalDate.of(1990, 1, 1), "image.png");
    }

    @Test
    void testUserCreation_ValidData() {
        // Cenário válido
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getName());
        assertEquals(LocalDate.of(1990, 1, 1), user.getDateOfBirth());
        assertEquals("image.png", user.getImage());
    }

    @Test
    void testUserCreation_InvalidEmail() {
        // Cenário de email inválido
        Exception exception = assertThrows(InvalidEmailException.class, () -> new User("invalidEmail", "strongPassword", "Test User", LocalDate.of(1990, 1, 1), "image.png"));
        assertEquals("Email inválido.", exception.getMessage());
    }

    @Test
    void testUserCreation_ShortPassword() {
        // Cenário de senha muito curta
        Exception exception = assertThrows(InvalidPasswordException.class, () -> new User("test@example.com", "short", "Test User", LocalDate.of(1990, 1, 1), "image.png"));
        assertEquals("A senha deve ter pelo menos 8 caracteres.", exception.getMessage());
    }

    @Test
    void testSetEmail_InvalidEmail() {
        // Cenário de email inválido ao atualizar
        Exception exception = assertThrows(InvalidEmailException.class, () -> user.setEmail("invalidEmail"));
        assertEquals("Email inválido.", exception.getMessage());
    }

    @Test
    void testSetPassword_ShortPassword() {
        // Cenário de senha muito curta ao atualizar
        Exception exception = assertThrows(InvalidPasswordException.class, () -> user.setPassword("short"));
        assertEquals("A senha deve ter pelo menos 8 caracteres.", exception.getMessage());
    }

    @Test
    void testSetName() {
        String newName = "New Test User";
        user.setName(newName);
        assertEquals(newName, user.getName());
    }

    @Test
    void testSetDateOfBirth() {
        LocalDate newDate = LocalDate.of(2000, 1, 1);
        user.setDateOfBirth(newDate);
        assertEquals(newDate, user.getDateOfBirth());
    }

    @Test
    void testSetImage() {
        String newImage = "newImage.png";
        user.setImage(newImage);
        assertEquals(newImage, user.getImage());
    }

    @Test
    void testSetId() {
        user.setId(1);
        assertEquals(1, user.getId());
    }
}
