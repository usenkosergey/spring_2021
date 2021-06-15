package org.example.app.repository;

import java.util.List;

public interface UserRepository<T> {
    List<T> usersAll();

    void addUser(T user);
}
