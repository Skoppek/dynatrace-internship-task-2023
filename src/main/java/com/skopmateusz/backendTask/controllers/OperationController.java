package com.skopmateusz.backendTask.controllers;

import com.skopmateusz.backendTask.models.responses.AverageExchangeRateResponse;
import com.skopmateusz.backendTask.models.responses.BuySellMajorDifferenceResponse;
import com.skopmateusz.backendTask.models.responses.MaxMinAverageExchangeRateResponse;
import com.skopmateusz.backendTask.service.NbpService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@Validated
public class OperationController {

    private NbpService nbpService;

    @Autowired
    public OperationController(NbpService nbpService) {
        this.nbpService = nbpService;
    }

    @GetMapping(value = "/average-exchange-rate/code/{code}/date/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAverageExchangeRate(
            @PathVariable("date") @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",
                    message = "Date format should be: YYYY-MM-DD") String date,
            @PathVariable("code") @Pattern(regexp = "[a-zA-Z]{3}",
                    message = "Provide 3 letter currency code (ISO-4217)") String code
    ) {
        AverageExchangeRateResponse body = nbpService.getAvgRate(code, date);
        return ResponseEntity.ok(body);
    }

    @GetMapping(value = "/max-min-average-value/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAverageValue(
            @PathVariable("code") @Pattern(regexp = "[a-zA-Z]{3}",
                    message = "Provide 3 letter currency code (ISO-4217)") String code,
            @RequestParam("quotations") @Min(1) @Max(255) Integer numOfQuotations
    ) {
        MaxMinAverageExchangeRateResponse body = nbpService.getLastMaxMinAvgRates(code, numOfQuotations);
        return ResponseEntity.ok(body);
    }

    @GetMapping(value = "/buy-sell-difference/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getBuyAskDifference(
            @PathVariable("code") @Pattern(regexp = "^[a-zA-Z]{3}",
                    message = "Provide 3 letter currency code (ISO-4217)") String code,
            @RequestParam("quotations") @Min(1) @Max(255) Integer numOfQuotations
    ) {
        BuySellMajorDifferenceResponse body = nbpService.getLastBuySellDifference(code, numOfQuotations);
        return ResponseEntity.ok(body);
    }

    @RequestMapping("/error")
    public String handleError() {
        return "Something went wrong...";
    }
}
