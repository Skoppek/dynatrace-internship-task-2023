package com.skopmateusz.backendTask.controllers;

import com.skopmateusz.backendTask.models.responses.AverageExchangeRateResponse;
import com.skopmateusz.backendTask.models.responses.BuySellMajorDifferenceResponse;
import com.skopmateusz.backendTask.models.responses.MaxMinAverageExchangeRateResponse;
import com.skopmateusz.backendTask.service.NbpService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

@RestController
@RequestMapping("/")
@Validated
public class OperationController {

    @Autowired
    private NbpService nbpService;

    @GetMapping(value = "/avg-ex-rate/{date}/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> getAverageExchangeRate(
            @PathVariable("date") @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String date,
            @PathVariable("code") @Pattern(regexp = "[a-zA-Z]{3}") String code
    ) {
        try {
            AverageExchangeRateResponse body = nbpService.getAvgRate(code, date);
            return ResponseEntity.ok(body);
        } catch (RestClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/avg-value/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> getAverageValue(
            @PathVariable("code") @Pattern(regexp = "[a-zA-Z]{3}") String code,
            @RequestParam("quotations") @Min(0) @Max(255) Integer numOfQuotations
    ) {
        try {
            MaxMinAverageExchangeRateResponse body = nbpService.getLastMaxMinAvgRates(code, numOfQuotations);
            return ResponseEntity.ok(body);
        } catch (RestClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/buy-ask-diff/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> getBuyAskDifference(
            @PathVariable("code") @Pattern(regexp = "[a-zA-Z]{3}") String code,
            @RequestParam("quotations") @Min(0) @Max(255) Integer numOfQuotations
    ) {
        try {
            BuySellMajorDifferenceResponse body = nbpService.getLastBuySellDifference(code, numOfQuotations);
            return ResponseEntity.ok(body);
        } catch (RestClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
