package br.edu.ifrs.fintrack.model;

import br.edu.ifrs.fintrack.exception.InvalidEmailException;
import br.edu.ifrs.fintrack.exception.InvalidPasswordException;
import br.edu.ifrs.fintrack.util.EmailUtils;
import br.edu.ifrs.fintrack.util.PasswordUtils;

import java.time.LocalDate;
import java.util.Objects;

public class User {
    private int id;
    private String email;
    private String password;
    private String name;
    private LocalDate dateOfBirth;
    private String image;

    public User(String email, String password, String name, LocalDate dateOfBirth, String image) {
        if (!EmailUtils.isValidEmail(email)) {
            throw new InvalidEmailException("Email inválido.");
        }

        if (password == null || password.length() < 8) {
            throw new InvalidPasswordException("A senha deve ter pelo menos 8 caracteres.");
        }

        this.email = email;
        this.password = PasswordUtils.hashPassword(password);
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(!EmailUtils.isValidEmail(email)) {
            throw new InvalidEmailException("Email inválido.");
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password == null || password.length() < 8) {
            throw new InvalidPasswordException("A senha deve ter pelo menos 8 caracteres.");
        }
        this.password = PasswordUtils.hashPassword(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean validatePassword(String inputPassword) {
        return PasswordUtils.checkPassword(inputPassword, this.password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(dateOfBirth, user.dateOfBirth) && Objects.equals(image, user.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, name, dateOfBirth, image);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", image='" + image + '\'' +
                '}';
    }
}
