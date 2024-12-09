package br.edu.ifrs.fintrack.util;

import br.edu.ifrs.fintrack.model.User;

public class LoggedUser {
    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        LoggedUser.user = user;
    }

    public static void clear() {
        user = null;
    }
}
