package com.rvcode.E_Service.App.controllers;

import com.rvcode.E_Service.App.dtoObjects.UserOrElectricianRegistrationDto;
import com.rvcode.E_Service.App.entities.User;
import com.rvcode.E_Service.App.enums.Role;
import com.rvcode.E_Service.App.response.ErrorResponse;
import com.rvcode.E_Service.App.response.JwtTokenResponse;
import com.rvcode.E_Service.App.services.AuthService;
import com.rvcode.E_Service.App.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authServices;




    @GetMapping("/health-check")
    public String heathCheck(){
        return "Health okk";
    }



    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        JwtTokenResponse tokenResponse = authServices.authenticateUser(email, password);
        if(tokenResponse!=null)
            return ResponseEntity.ok(tokenResponse);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid email or password"));
    }


    @PostMapping("/user-register")
    public ResponseEntity<?> userRegister(@RequestBody UserOrElectricianRegistrationDto myDto){

        JwtTokenResponse tokenResponse = authServices.registerForUserOrElectrician(myDto, Role.CUSTOMER);
        if(tokenResponse!=null)
            return new ResponseEntity<>(tokenResponse, HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



    @PostMapping("/electrician-register")
    public ResponseEntity<?> electricianRegister(@RequestBody UserOrElectricianRegistrationDto myDto){

        JwtTokenResponse tokenResponse = authServices.registerForUserOrElectrician(myDto,Role.ELECTRICIAN);
        if(tokenResponse!=null)
            return new ResponseEntity<>(tokenResponse, HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String,String> request){
        String token = request.get("refreshToken");
        try {
            String newAccessToken = authServices.refreshToken(token);

            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid refresh token"));
        }

    }




}

