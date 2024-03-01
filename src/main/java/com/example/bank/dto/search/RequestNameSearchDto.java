package com.example.bank.dto.search;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestNameSearchDto {
    @NotNull
    @NotBlank
    private String name;
}
