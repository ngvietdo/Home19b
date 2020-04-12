package com.home19b.controller;

import com.home19b.domain.request.LoginRequest;
import com.home19b.domain.response.BaseResponse;
import com.home19b.services.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logIn")
public class LoginController {

    final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/")
    public BaseResponse login(@RequestBody LoginRequest request) {
        return loginService.logIn(request);
    }
}
