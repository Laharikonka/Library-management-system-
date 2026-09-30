package com.library.security;

import com.library.dao.UserDAO;
import com.library.model.User;
import java.sql.SQLException;
import java.util.Optional;

public final class Authentication {
    private Authentication() {}
    public static Optional<User> authenticate(String email, String password) throws SQLException {
        return UserDAO.findByEmail(email).filter(u -> PasswordUtil.verify(password, u.getPasswordHash()));
    }
}
