package com.example.bank.dto.phone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestAddPhoneDto {
    @NotNull
    @NotBlank
    private String phone;
}
