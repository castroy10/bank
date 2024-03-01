package com.example.bank.dto.appuser;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AppUserResponseFullDto {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate birthday;
    private String username;
    private Double balance;
}
