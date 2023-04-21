package com.skopmateusz.backendTask.controllers;

import com.skopmateusz.backendTask.service.NbpService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@Validated
public class OperationController {

    @Autowired
    private NbpService nbpService;

    @GetMapping("/avg-ex-rate/{date}/{code}")
    public ResponseEntity<String> getAverageExchangeRate(
            @PathVariable("date") @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String date,
            @PathVariable("code") @Pattern(regexp = "[a-zA-Z]{3}") String code
    ) {
        try {
            Double result = nbpService.getAvgRates(code, date);
            return ResponseEntity.ok(result.toString());
        } catch () {
            return ResponseEntity.badRequest();
        }
    }

    @GetMapping("/avg-value/{code}")
    public ResponseEntity<String> getAverageValue(
            @PathVariable("code") @Pattern(regexp = "[a-zA-Z]{3}") String code,
            @RequestParam("quotations") @Min(0) @Max(255) Integer numOfQuotations
    ) {
        Double result = nbpService.getAvgExchangeRates();
        return new ResponseEntity<>("avg-value: " + result, HttpStatus.OK);
    }

    @GetMapping("/buy-ask-diff/{code}")
    public ResponseEntity<String> getBuyAskDifference(
            @PathVariable("code") @Pattern(regexp = "[a-zA-Z]{3}") String code,
            @RequestParam("quotations") @Min(0) @Max(255) Integer numOfQuotations
    ) {
        return new ResponseEntity<>("buy-ask-diff: " + numOfQuotations, HttpStatus.OK);
    }
}
