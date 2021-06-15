package org.example.app.repository;

import org.apache.log4j.Logger;
import org.example.app.services.LoginService;
import org.example.web.dto.LoginForm;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository<LoginForm> {

    private Logger logger = Logger.getLogger(LoginService.class);
    private final List<LoginForm> users = new ArrayList<>();


    @Override
    public List<LoginForm> usersAll() {
        return new ArrayList<>(users);
    }

    @Override
    public void addUser(LoginForm user) {
        logger.info("add new user -> " + user.toString());
        users.add(user);
    }
}
