package com.example.bank.controller;

import com.example.bank.dto.appuser.AppUserRequestRegisterDto;
import com.example.bank.dto.appuser.AppUserRequestTokenDto;
import com.example.bank.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final AppUserService appUserService;

    public UserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody @Valid AppUserRequestRegisterDto appUserRequestRegisterDto) {
        return appUserService.register(appUserRequestRegisterDto);
    }

    @PostMapping("login")
    public ResponseEntity<?> getToken(@RequestBody AppUserRequestTokenDto appUserRequestTokenDto) {
        return appUserService.getToken(appUserRequestTokenDto);
    }
}
