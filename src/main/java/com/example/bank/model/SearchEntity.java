package com.example.bank.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchEntity {
    private LocalDate date;
    private String phone;
    private String name;
    private String email;
}
