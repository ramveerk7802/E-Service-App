package com.rvcode.E_Service.App.dtoObjects;

import lombok.Data;

@Data
public class UserOrElectricianRegistrationDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String aadhaarNumber;
    private String city;
    private String state;
    private String pinCode;
}
