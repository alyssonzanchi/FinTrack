package br.edu.ifrs.fintrack.model;

import br.edu.ifrs.fintrack.exception.MissingRequiredFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CategoryTest {
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("RECEITA", "Teste", "icon.png");
    }

    @Test
    void testCategoryCreationWithValidData() {
        assertEquals("RECEITA", category.getType());
        assertEquals("Teste", category.getName());
        assertEquals("icon.png", category.getIcon());
    }

    @Test
    void testCategoryCreationWithNullName() {
        Exception exception = assertThrows(MissingRequiredFieldException.class, () ->
                new Category("RECEITA", null, "icon.png"));
        assertEquals("Campo obrigatório ausente: name", exception.getMessage());
    }

    @Test
    void testCategoryCreationWithEmptyName() {
        Exception exception = assertThrows(MissingRequiredFieldException.class, () ->
                new Category("RECEITA", "", "icon.png"));
        assertEquals("Campo obrigatório ausente: name", exception.getMessage());
    }

    @Test
    void testCategoryCreationWithNullType() {
        Exception exception = assertThrows(MissingRequiredFieldException.class, () ->
                new Category(null, "Teste", "icon.png"));
        assertEquals("Campo obrigatório ausente: type", exception.getMessage());
    }

    @Test
    void testCategoryCreationWithEmptyType() {
        Exception exception = assertThrows(MissingRequiredFieldException.class, () ->
                new Category("", "Teste", "icon.png"));
        assertEquals("Campo obrigatório ausente: type", exception.getMessage());
    }

    @Test
    void testSetNameWithValidData() {
        category.setName("Teste2");
        assertEquals("Teste2", category.getName());
    }

    @Test
    void testSetNameWithNull() {
        Exception exception = assertThrows(MissingRequiredFieldException.class, () -> category.setName(null));
        assertEquals("Campo obrigatório ausente: name", exception.getMessage());
    }

    @Test
    void testSetNameWithEmptyString() {
        Exception exception = assertThrows(MissingRequiredFieldException.class, () -> category.setName(""));
        assertEquals("Campo obrigatório ausente: name", exception.getMessage());
    }

    @Test
    void testSetTypeWithValidData() {
        category.setType("DESPESA");
        assertEquals("DESPESA", category.getType());
    }

    @Test
    void testSetTypeWithNull() {
        Exception exception = assertThrows(MissingRequiredFieldException.class, () -> category.setType(null));
        assertEquals("Campo obrigatório ausente: type", exception.getMessage());
    }

    @Test
    void testSetTypeWithEmptyString() {
        Exception exception = assertThrows(MissingRequiredFieldException.class, () -> category.setType(""));
        assertEquals("Campo obrigatório ausente: type", exception.getMessage());
    }

    @Test
    void testSetIcon() {
        category.setIcon("new_icon.png");
        assertEquals("new_icon.png", category.getIcon());
    }
}
