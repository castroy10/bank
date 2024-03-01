package com.example.bank.dto.appuser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class AppUserRequestRegisterDto {
    private String lastName;
    private String firstName;
    private String middleName;
    private String birthday;
    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    private String password;
    @Positive
    private Double balance;
    private List<String> phones;
    private List<String> emails;
}
