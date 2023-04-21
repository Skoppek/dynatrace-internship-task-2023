package com.skopmateusz.backendTask.service;

import com.skopmateusz.backendTask.models.MaxMinAvgRate;
import com.skopmateusz.backendTask.models.Rate;
import com.skopmateusz.backendTask.models.RateA;
import com.skopmateusz.backendTask.models.RateC;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Service
public class NbpService {

    private final NbpExchangeRatesProvider nbpExchangeRatesProvider;

    public NbpService(RestTemplate restTemplate) {
        this.nbpExchangeRatesProvider = new NbpExchangeRatesProvider(restTemplate);
    }

    public Double getAvgRates(String currencyCode, String date) {
        List<Rate> rates = nbpExchangeRatesProvider.getAverageRatesOfDate(date, currencyCode);
        return rates.stream()
                .mapToDouble(x -> ((RateA)x).mid())
                .average()
                .orElse(Double.NaN);
    }

    public MaxMinAvgRate getLastAvgRates(String currency, Integer numOfQuotations) {
        List<Rate> rates = nbpExchangeRatesProvider.getLastAverageRates(currency, numOfQuotations);
        rates.sort(Comparator.comparing(rate -> ((RateA) rate).mid()));
        Double max = ((RateA)rates.get(rates.size()-1)).mid();
        Double min = ((RateA)rates.get(0)).mid();
        return new MaxMinAvgRate(max, min);
    }

    public Double getLastBuySellRates(String currency, Integer numOfQuotations) {
        List<Rate> rates = nbpExchangeRatesProvider.getLastBuySellRates(currency, numOfQuotations);
        return rates.stream()
                .map(rate -> ((RateC)rate).bid() - ((RateC)rate).ask())
                .max(Double::compareTo)
                .orElse(Double.NaN);
    }
}
