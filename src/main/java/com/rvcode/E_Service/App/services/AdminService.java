package com.rvcode.E_Service.App.services;

import com.rvcode.E_Service.App.entities.User;
import com.rvcode.E_Service.App.enums.Role;
import com.rvcode.E_Service.App.exception.MyCustomException;
import com.rvcode.E_Service.App.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAllRegisteredUser(Role role){
        try {
            List<User> list = userRepository.findAllByRole(role);
            return list;

        }catch (Exception e){
            throw new MyCustomException("Error fetching the Registered user :-> "+ e.getMessage());
        }
    }
}

