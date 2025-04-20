package com.rvcode.E_Service.App.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokenResponse {
    private String accessToken;
    private String refreshToken;
}
