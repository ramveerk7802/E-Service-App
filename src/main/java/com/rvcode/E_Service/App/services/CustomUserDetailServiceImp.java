package com.rvcode.E_Service.App.services;

import com.rvcode.E_Service.App.CustomUserDetail;
import com.rvcode.E_Service.App.entities.User;
import com.rvcode.E_Service.App.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailServiceImp implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty())
            throw new UsernameNotFoundException("User Not Found");

        User user = optionalUser.get();
        return new CustomUserDetail(user);

    }
}

