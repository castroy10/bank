package com.example.bank.dto.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestUpdateEmailDto {
    @NotNull
    @NotBlank
    @Email
    private String OldEmail;
    @NotNull
    @NotBlank
    @Email
    private String NewEmail;
}
