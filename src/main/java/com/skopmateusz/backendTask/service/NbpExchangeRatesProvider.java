package com.skopmateusz.backendTask.service;

import com.skopmateusz.backendTask.models.BuySellRatesTable;
import com.skopmateusz.backendTask.models.ExchangeRatesTable;
import com.skopmateusz.backendTask.models.AverageRate;
import com.skopmateusz.backendTask.models.BuySellRate;
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

    public List<AverageRate> getAverageRatesOfDate(String date, String currency) {
        String queryPath = String.format("%s/%s/%s", exchangeTableName, currency, date);
        return getExchangeRatesTable(queryPath);
    }

    public List<AverageRate> getLastAverageRates(String currency, Integer numOfQuotations) {
        String queryPath = String.format("%s/%s/last/%s", exchangeTableName, currency, numOfQuotations);
        return getExchangeRatesTable(queryPath);
    }

    public List<BuySellRate> getLastBuySellRates(String currency, Integer numOfQuotations) {
        String queryPath = String.format("%s/%s/last/%s", buySellTableName, currency, numOfQuotations);
        return getBuySellTable(queryPath);
    }

    private List<AverageRate> getExchangeRatesTable(String queryPath) {
        String endPoint = NBP_EXCHANGE_RATES_API_URL + queryPath;
        ResponseEntity<ExchangeRatesTable> tableResponse =
                restTemplate.getForEntity(endPoint, ExchangeRatesTable.class);
        return tableResponse.getBody().rates();
    }

    private List<BuySellRate> getBuySellTable(String queryPath) {
        String endPoint = NBP_EXCHANGE_RATES_API_URL + queryPath;
        ResponseEntity<BuySellRatesTable> tableResponse =
                restTemplate.getForEntity(endPoint, BuySellRatesTable.class);
        return tableResponse.getBody().rates();
    }
}
