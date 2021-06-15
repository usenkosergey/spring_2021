package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.app.repository.UserRepositoryImpl;
import org.example.web.dto.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final Logger logger = Logger.getLogger(LoginService.class);
    private final UserRepositoryImpl userRepositoryImpl;

    @Autowired
    public LoginService(UserRepositoryImpl userRepositoryImp) {
        this.userRepositoryImpl = userRepositoryImp;
    }


    public boolean authenticate(LoginForm loginFrom) {
        logger.info("try auth with user-form: " + loginFrom);
        for (LoginForm l : userRepositoryImpl.usersAll()) {
            if (l.getPassword().equals(loginFrom.getPassword()) && l.getUsername().equals(loginFrom.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public boolean addUser(LoginForm loginForm){
        logger.info("user verification ");
        logger.info("all users - " + userRepositoryImpl.usersAll().size());
        for (LoginForm l : userRepositoryImpl.usersAll()) {
            if (l.getUsername().equals(loginForm.getUsername())) {
                return true;
            }
        }
        return false;
    }
}
