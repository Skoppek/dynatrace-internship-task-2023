package com.skopmateusz.backendTask.controllers;

import com.skopmateusz.backendTask.models.responses.AverageExchangeRateResponse;
import com.skopmateusz.backendTask.models.responses.BuySellMajorDifferenceResponse;
import com.skopmateusz.backendTask.models.responses.MaxMinAverageExchangeRateResponse;
import com.skopmateusz.backendTask.service.NbpService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


    @Operation(description = "Provides average exchange rate for giver currency {code} on given date {date}")
    @ApiResponse(responseCode = "200", description = "Data successfully retrieved.")
    @ApiResponse(responseCode = "400", description = "Wrong parameters provided")
    @ApiResponse(responseCode = "404", description = "No data found for given parameters")
    @GetMapping(value = "/average-exchange-rate/code/{code}/date/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AverageExchangeRateResponse> getAverageExchangeRate(
            @PathVariable("date") @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",
                    message = "Date format should be: YYYY-MM-DD") String date,
            @PathVariable("code") @Pattern(regexp = "[a-zA-Z]{3}",
                    message = "Provide 3 letter currency code (ISO-4217)") String code
    ) {
        AverageExchangeRateResponse body = nbpService.getAvgRate(code, date);
        return ResponseEntity.ok(body);
    }

    @Operation(description = "Provides max and min average exchange value" +
            " for given currency {code} from last N < 256 days {quotations}")
    @ApiResponse(responseCode = "200", description = "Data successfully retrieved.")
    @ApiResponse(responseCode = "400", description = "Wrong parameters provided")
    @ApiResponse(responseCode = "404", description = "No data found for given parameters")
    @GetMapping(value = "/max-min-average-value/code/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MaxMinAverageExchangeRateResponse> getAverageValue(
            @PathVariable("code") @Pattern(regexp = "[a-zA-Z]{3}",
                    message = "Provide 3 letter currency code (ISO-4217)") String code,
            @RequestParam("quotations") @Min(1) @Max(255) Integer numOfQuotations
    ) {
        MaxMinAverageExchangeRateResponse body = nbpService.getLastMaxMinAvgRates(code, numOfQuotations);
        return ResponseEntity.ok(body);
    }

    @Operation(description = "Provides the major difference between the buy and ask rate" +
            " for given currency {code} from last N < 256 days {quotations}")
    @ApiResponse(responseCode = "200", description = "Data successfully retrieved.")
    @ApiResponse(responseCode = "400", description = "Wrong parameters provided")
    @ApiResponse(responseCode = "404", description = "No data found for given parameters")
    @GetMapping(value = "/buy-sell-difference/code/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BuySellMajorDifferenceResponse> getBuyAskDifference(
            @PathVariable("code") @Pattern(regexp = "^[a-zA-Z]{3}",
                    message = "Provide 3 letter currency code (ISO-4217)") String code,
            @RequestParam("quotations") @Min(1) @Max(255) Integer numOfQuotations
    ) {
        BuySellMajorDifferenceResponse body = nbpService.getLastBuySellDifference(code, numOfQuotations);
        return ResponseEntity.ok(body);
    }
}
