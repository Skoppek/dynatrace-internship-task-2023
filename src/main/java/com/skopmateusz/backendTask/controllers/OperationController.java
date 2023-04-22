package com.skopmateusz.backendTask.controllers;

import com.skopmateusz.backendTask.models.MaxMinAvgRate;
import com.skopmateusz.backendTask.service.NbpService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> getAverageExchangeRate(
            @PathVariable("date") @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String date,
            @PathVariable("code") @Pattern(regexp = "[a-zA-Z]{3}") String code
    ) {
        try {
            Double result = nbpService.getAvgRates(code, date);
            return ResponseEntity.ok(result.toString());
        } catch (RestClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/avg-value/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getAverageValue(
            @PathVariable("code") @Pattern(regexp = "[a-zA-Z]{3}") String code,
            @RequestParam("quotations") @Min(0) @Max(255) Integer numOfQuotations
    ) {
        try {
            MaxMinAvgRate result = nbpService.getLastAvgRates(code, numOfQuotations);
            return ResponseEntity.ok(result.toString());
        } catch (RestClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/buy-ask-diff/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getBuyAskDifference(
            @PathVariable("code") @Pattern(regexp = "[a-zA-Z]{3}") String code,
            @RequestParam("quotations") @Min(0) @Max(255) Integer numOfQuotations
    ) {
        return new ResponseEntity<>("buy-ask-diff: " + numOfQuotations, HttpStatus.OK);
    }
}
