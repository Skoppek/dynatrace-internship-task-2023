package com.skopmateusz.backendTask.controllers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@Validated
public class OperationController {

    @GetMapping("/avg-ex-rate/{date}/{code}")
    public ResponseEntity<String> getAverageExchangeRate(
            @PathVariable("date") @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String date,
            @PathVariable("code") @Pattern(regexp = "[a-zA-Z]{3}") String code
    ) {
        return new ResponseEntity<>("avg-ex-rate", HttpStatus.OK);
    }

    @GetMapping("/avg-value/{code}")
    public ResponseEntity<String> getAverageValue(
            @PathVariable("code") @Pattern(regexp = "[a-zA-Z]{3}") String code,
            @RequestParam("quotations") @Min(0) @Max(255) Integer numOfQuotations
    ) {
        return new ResponseEntity<>("avg-value: " + numOfQuotations, HttpStatus.OK);
    }

    @GetMapping("/buy-ask-diff/{code}")
    public ResponseEntity<String> getBuyAskDifference(
            @PathVariable("code") @Pattern(regexp = "[a-zA-Z]{3}") String code,
            @RequestParam("quotations") @Min(0) @Max(255) Integer numOfQuotations
    ) {
        return new ResponseEntity<>("buy-ask-diff: " + numOfQuotations, HttpStatus.OK);
    }
}
