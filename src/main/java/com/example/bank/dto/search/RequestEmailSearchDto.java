package com.example.bank.dto.search;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestEmailSearchDto {
    @NotNull
    @NotBlank
    private String email;
}
