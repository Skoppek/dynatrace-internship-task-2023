package com.skopmateusz.backendTask.service;

import com.skopmateusz.backendTask.exceptions.NbpNotFoundException;
import com.skopmateusz.backendTask.errorHandlers.NbpRestTemplateResponseErrorHandler;
import com.skopmateusz.backendTask.models.AverageRate;
import com.skopmateusz.backendTask.models.AverageRatesTable;
import com.skopmateusz.backendTask.models.BuySellRate;
import com.skopmateusz.backendTask.models.BuySellRatesTable;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NbpExchangeRatesProvider {

    final String NBP_EXCHANGE_RATES_API_URL = "http://api.nbp.pl/api/exchangerates/rates/";
    final RestTemplate restTemplate;

    public NbpExchangeRatesProvider() {
        restTemplate = new RestTemplateBuilder()
                .errorHandler(new NbpRestTemplateResponseErrorHandler())
                .build();
    }

    public List<AverageRate> getAverageRates(String queryPath) {
        String endPoint = NBP_EXCHANGE_RATES_API_URL + queryPath;
        ResponseEntity<AverageRatesTable> tableResponse =
                restTemplate.getForEntity(endPoint, AverageRatesTable.class);

        if (tableResponse.getBody() == null) {
            throw new NbpNotFoundException("NBP response is null");
        }
        return tableResponse.getBody().rates();
    }

    public List<BuySellRate> getBuySellRates(String queryPath) {
        String endPoint = NBP_EXCHANGE_RATES_API_URL + queryPath;
        ResponseEntity<BuySellRatesTable> tableResponse =
                restTemplate.getForEntity(endPoint, BuySellRatesTable.class);

        if (tableResponse.getBody() == null) {
            throw new NbpNotFoundException("NBP response is null");
        }
        return tableResponse.getBody().rates();
    }
}
