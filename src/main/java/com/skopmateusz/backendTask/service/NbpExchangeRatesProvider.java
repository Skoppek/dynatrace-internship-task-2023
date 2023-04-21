package com.skopmateusz.backendTask.service;

import com.skopmateusz.backendTask.models.ExchangeTable;
import com.skopmateusz.backendTask.models.Rate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class NbpExchangeRatesProvider {
    final String NBP_EXCHANGE_RATES_API_URL = "http://api.nbp.pl/api/exchangerates/rates/";
    final String exchangeTableName = "A";
    final String buySellTableName = "C";
    final RestTemplate restTemplate;

    public NbpExchangeRatesProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Rate> getAverageRatesOfDate(String date, String currency) {
        String queryPath = String.format("%s/%s/%s", exchangeTableName, currency, date);
        return queryNbp(queryPath);
    }

    public List<Rate> getLastAverageRates(String currency, Integer numOfQuotations) {
        String queryPath = String.format("%s/%s/last/%s", exchangeTableName, currency, numOfQuotations);
        return queryNbp(queryPath);
    }

    public List<Rate> getLastBuySellRates(String currency, Integer numOfQuotations) {
        String queryPath = String.format("%s/%s/last/%s", buySellTableName, currency, numOfQuotations);
        return queryNbp(queryPath);
    }

    private List<Rate> queryNbp(String queryPath) {
        String endPoint = NBP_EXCHANGE_RATES_API_URL + queryPath;
        ResponseEntity<ExchangeTable> tableResponse =
                restTemplate.exchange(endPoint, HttpMethod.GET, null, ExchangeTable.class);
        return tableResponse.getBody().rates();
    }
}
