package com.skopmateusz.backendTask.service;

import com.skopmateusz.backendTask.errorHandlers.NbpException;
import com.skopmateusz.backendTask.models.AverageRate;
import com.skopmateusz.backendTask.models.AverageRatesTable;
import com.skopmateusz.backendTask.models.BuySellRate;
import com.skopmateusz.backendTask.models.BuySellRatesTable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class NbpExchangeRatesProvider {
    final String NBP_EXCHANGE_RATES_API_URL;
    final RestTemplate restTemplate;

    public NbpExchangeRatesProvider(String nbpUrl, RestTemplate restTemplate) {
        this.NBP_EXCHANGE_RATES_API_URL = nbpUrl;
        this.restTemplate = restTemplate;
    }

    public List<AverageRate> getAverageRates(String queryPath) {
        String endPoint = NBP_EXCHANGE_RATES_API_URL + queryPath;
        ResponseEntity<AverageRatesTable> tableResponse =
                restTemplate.getForEntity(endPoint, AverageRatesTable.class);

        if (tableResponse.getBody() == null) {
            throw new NbpException(HttpStatus.INTERNAL_SERVER_ERROR, "NBP response is null");
        }
        return tableResponse.getBody().rates();
    }

    public List<BuySellRate> getBuySellRates(String queryPath) {
        String endPoint = NBP_EXCHANGE_RATES_API_URL + queryPath;
        ResponseEntity<BuySellRatesTable> tableResponse =
                restTemplate.getForEntity(endPoint, BuySellRatesTable.class);

        if (tableResponse.getBody() == null) {
            throw new NbpException(HttpStatus.INTERNAL_SERVER_ERROR, "NBP response is null");
        }
        return tableResponse.getBody().rates();
    }
}
