package com.rvcode.E_Service.App.services;

import com.rvcode.E_Service.App.CustomUserDetail;
import com.rvcode.E_Service.App.dtoObjects.UserOrElectricianRegistrationDto;
import com.rvcode.E_Service.App.entities.Electrician;
import com.rvcode.E_Service.App.entities.User;
import com.rvcode.E_Service.App.enums.Role;
import com.rvcode.E_Service.App.exception.MyCustomException;
import com.rvcode.E_Service.App.exception.UserAlreadyRegister;
import com.rvcode.E_Service.App.repositories.ElectricianRepository;
import com.rvcode.E_Service.App.repositories.UserRepository;
import com.rvcode.E_Service.App.response.JwtTokenResponse;
import com.rvcode.E_Service.App.utility.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ElectricianRepository electricianRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailServiceImp customUserDetailServiceImp;
    @Autowired
    private Validator validator;

    public JwtTokenResponse registerForUserOrElectrician(UserOrElectricianRegistrationDto userDto, Role role){
        try {
            if(userRepository.existsByEmail(userDto.getEmail())){
                throw new UserAlreadyRegister("User email already register");
            }
            if(!validator.emailValidator(userDto.getEmail()) || !validator.phoneNumberValidator(userDto.getPhone()) || !validator.pinCodeValidator(userDto.getPinCode()))
                throw new MyCustomException("Enter the valid data");
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
            UserDetails userDeatails = customUserDetailServiceImp.loadUserByUsername(saveduser.getEmail());
            return getJwtToken(userDeatails);

        }catch (Exception e){
            throw e;
        }
    }

    public JwtTokenResponse authenticateUser(String email,String password){
        try {

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            if(authentication.isAuthenticated()){
                UserDetails userDetails = customUserDetailServiceImp.loadUserByUsername(email);
                return getJwtToken(userDetails);
            }
        }catch (AuthenticationException e){
            log.error("Failed to Authentication : "+e.getMessage());
        }
        return null;
    }

    public String refreshToken(String token){
        try {
            String email = jwtService.extractUserName(token);
            UserDetails userDetails = customUserDetailServiceImp.loadUserByUsername(email);
            String newAccessToken = jwtService.generateAccessToken(userDetails);
            return newAccessToken;
        }catch (Exception e){
            return null;
        }
    }





    private JwtTokenResponse getJwtToken(UserDetails userDetails){
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        return new JwtTokenResponse(accessToken,refreshToken);
    }
}

