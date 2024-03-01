package com.example.bank.controller;

import com.example.bank.dto.phone.RequestAddPhoneDto;
import com.example.bank.dto.phone.RequestDeletePhoneDto;
import com.example.bank.dto.phone.RequestUpdatePhoneDto;
import com.example.bank.exception.LastRecordException;
import com.example.bank.exception.NotFoundRecordException;
import com.example.bank.service.PhoneService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/phone")
public class PhoneController {
    private final PhoneService phoneService;

    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @PostMapping("addphone")
    public ResponseEntity<?> addPhone(@RequestBody @Valid RequestAddPhoneDto requestAddPhoneDto, HttpServletRequest httpServletRequest) {
        return phoneService.addPhone(requestAddPhoneDto,httpServletRequest);
    }

    @PostMapping("deletephone")
    public ResponseEntity<?> deletePhone(@RequestBody @Valid RequestDeletePhoneDto requestDeletePhoneDto, HttpServletRequest httpServletRequest) throws LastRecordException, NotFoundRecordException {
        return phoneService.deletePhone(requestDeletePhoneDto,httpServletRequest);
    }

    @PostMapping("updatephone")
    public ResponseEntity<?> updatePhone(@RequestBody @Valid RequestUpdatePhoneDto requestUpdatePhoneDto, HttpServletRequest httpServletRequest) throws NotFoundRecordException {
        return phoneService.updatePhone(requestUpdatePhoneDto, httpServletRequest);
    }
}
