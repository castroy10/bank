package com.example.bank.dto.search;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestFullSearchDto {
    @Size(min = 1)
    private String date;
    @Size(min = 1)
    private String phone;
    @Size(min = 1)
    private String name;
    @Size(min = 1)
    private String email;
}
