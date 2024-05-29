package com.fpt.recruitmentsystem.service;

import com.fpt.recruitmentsystem.dto.LoginDTO;
import com.fpt.recruitmentsystem.dto.RegisterDTO;
import com.fpt.recruitmentsystem.dto.TokenDTO;
import com.fpt.recruitmentsystem.util.ResponseMessage;

import jakarta.servlet.http.HttpServletRequest;

public interface IAuthenticationService {
    ResponseMessage register(RegisterDTO registerDTO, String role);
    ResponseMessage registerAdmin(RegisterDTO registerDTO);
    ResponseMessage activeAccount(String token);
    ResponseMessage resendActiveAccount(String email);
    TokenDTO login(LoginDTO loginDTO);
    ResponseMessage logout(HttpServletRequest request);
    Object refreshToken(HttpServletRequest request);
    ResponseMessage forgotPassword(String email);
    ResponseMessage resetPassword(String token, String password);
    ResponseMessage changePassword(HttpServletRequest request, String password);
}
