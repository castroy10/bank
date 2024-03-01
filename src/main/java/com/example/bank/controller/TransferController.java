package com.example.bank.controller;

import com.example.bank.dto.transfer.RequestTransferDto;
import com.example.bank.exception.NotEnoughMoneyException;
import com.example.bank.service.TransferService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping()
    public ResponseEntity<?> transfer(@RequestBody @Valid RequestTransferDto requestTransferDto, HttpServletRequest httpServletRequest) throws NotEnoughMoneyException {
        return transferService.transfer(requestTransferDto, httpServletRequest);
    }
}
