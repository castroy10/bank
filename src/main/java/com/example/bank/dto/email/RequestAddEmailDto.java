package com.example.bank.dto.email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestAddEmailDto {
    @NotNull
    @NotBlank
    private String email;
}
