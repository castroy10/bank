package com.example.bank.controller;

import com.example.bank.dto.email.RequestAddEmailDto;
import com.example.bank.dto.email.RequestDeleteEmailDto;
import com.example.bank.dto.email.RequestUpdateEmailDto;
import com.example.bank.exception.LastRecordException;
import com.example.bank.exception.NotFoundRecordException;
import com.example.bank.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("addemail")
    public ResponseEntity<?> addEmail(@RequestBody @Valid RequestAddEmailDto requestAddEmailDto, HttpServletRequest httpServletRequest) {
        return emailService.addEmail(requestAddEmailDto, httpServletRequest);
    }

    @PostMapping("deleteemail")
    public ResponseEntity<?> deleteEmail(@RequestBody @Valid RequestDeleteEmailDto requestDeleteEmailDto, HttpServletRequest httpServletRequest) throws LastRecordException, NotFoundRecordException {
        return emailService.deleteEmail(requestDeleteEmailDto, httpServletRequest);
    }

    @PostMapping("updateemail")
    public ResponseEntity<?> updateEmail(@RequestBody @Valid RequestUpdateEmailDto requestUpdateEmailDto, HttpServletRequest httpServletRequest) throws NotFoundRecordException {
        return emailService.updateEmail(requestUpdateEmailDto, httpServletRequest);
    }
}
