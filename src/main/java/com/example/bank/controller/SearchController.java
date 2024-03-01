package com.example.bank.controller;

import com.example.bank.dto.search.*;
import com.example.bank.service.SearchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("date")
    public ResponseEntity<?> searchDate(@RequestBody @Valid RequestDateSearchDto requestDateSearchDto) {
        return searchService.searchDate(requestDateSearchDto);
    }

    @PostMapping("phone")
    public ResponseEntity<?> searchPhone(@RequestBody @Valid RequestPhoneSearchDto requestPhoneSearchDto) {
        return searchService.searchPhone(requestPhoneSearchDto);
    }

    @PostMapping("email")
    public ResponseEntity<?> searchEmail(@RequestBody @Valid RequestEmailSearchDto requestEmailSearchDto) {
        return searchService.searchEmail(requestEmailSearchDto);
    }

    @PostMapping("name")
    public ResponseEntity<?> searchName(@RequestBody @Valid RequestNameSearchDto requestNameSearchDto) {
        return searchService.searchName(requestNameSearchDto);
    }

    @PostMapping("full")
    public ResponseEntity<?> searchFull(@RequestBody @Valid RequestFullSearchDto requestFullSearchDto){
        return searchService.searchFull(requestFullSearchDto);
    }
}
