package com.rvcode.E_Service.App.services;

import com.rvcode.E_Service.App.dtoObjects.UserOrElectricianRegistrationDto;
import com.rvcode.E_Service.App.entities.Electrician;
import com.rvcode.E_Service.App.entities.User;
import com.rvcode.E_Service.App.enums.Role;
import com.rvcode.E_Service.App.exception.MyCustomException;
import com.rvcode.E_Service.App.repositories.ElectricianRepository;
import com.rvcode.E_Service.App.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ElectricianRepository electricianRepository;

    public User registerForUserOrElectrician(UserOrElectricianRegistrationDto userDto, Role role){
        try {
            if(userRepository.existsByEmail(userDto.getEmail())){
                throw new MyCustomException("User email already registered.");
            }
            User user = new User();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setPhone(userDto.getPhone());
            user.setCity(userDto.getCity());
            user.setState(userDto.getState());
            user.setPinCode(userDto.getPinCode());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            if(Role.CUSTOMER==role)
                user.setRole(Role.CUSTOMER);
            else{
                user.setRole(Role.ELECTRICIAN);
                Electrician electrician = new Electrician();
                electrician.setUser(user);
                electrician.setAadhaarNumber(userDto.getAadhaarNumber());
                user.setElectrician(electrician);
            }
            User saveduser = userRepository.save(user);
            return saveduser;
        }catch (Exception e){
            throw new MyCustomException("Error on registration of user");
        }
    }

    public User authenticateUser(String email,String password){
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isEmpty())
                return null;
            User user = optionalUser.get();
            if(passwordEncoder.matches(password, user.getPassword()))
                return user;
            return  null;
        }catch (Exception e){
            return null;
        }
    }
}

