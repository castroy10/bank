package com.example.bank.dto.transfer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestTransferDto {
    @NotNull
    @NotBlank
    private String username;
    @Positive
    private BigDecimal sum;
}
