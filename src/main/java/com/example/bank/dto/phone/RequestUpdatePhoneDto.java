package com.example.bank.dto.phone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestUpdatePhoneDto {
    @NotNull
    @NotBlank
    private String oldPhone;
    @NotNull
    @NotBlank
    private String newPhone;
}
