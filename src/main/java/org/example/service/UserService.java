package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getUserByUsernameAndPassword(String username, String password){
        User user = userDao.getUserByUsernameAndPassword(username, password);
        return user;

    }

    public List<User> getAllUsers(){

       return  userDao.getAll();
    }
}
