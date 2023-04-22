package com.skopmateusz.backendTask.service;

import com.skopmateusz.backendTask.models.MaxMinAvgRate;
import com.skopmateusz.backendTask.models.AverageRate;
import com.skopmateusz.backendTask.models.BuySellRate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;

@Service
public class NbpService {

    private final NbpExchangeRatesProvider nbpExchangeRatesProvider;

    public NbpService(RestTemplate restTemplate) {
        this.nbpExchangeRatesProvider = new NbpExchangeRatesProvider(restTemplate);
    }

    public Double getAvgRates(String currencyCode, String date) {
        List<AverageRate> rates = nbpExchangeRatesProvider.getAverageRatesOfDate(date, currencyCode);
        return rates.stream()
                .mapToDouble(AverageRate::mid)
                .average()
                .orElse(Double.NaN);
    }

    public MaxMinAvgRate getLastAvgRates(String currency, Integer numOfQuotations) {
        List<AverageRate> rates = nbpExchangeRatesProvider.getLastAverageRates(currency, numOfQuotations);
        rates.sort(Comparator.comparing(AverageRate::mid));
        Double max = rates.get(rates.size()-1).mid();
        Double min = ((AverageRate)rates.get(0)).mid();
        return new MaxMinAvgRate(max, min);
    }

    public Double getLastBuySellRates(String currency, Integer numOfQuotations) {
        List<BuySellRate> rates = nbpExchangeRatesProvider.getLastBuySellRates(currency, numOfQuotations);
        return rates.stream()
                .map(rate -> rate.bid() - rate.ask())
                .max(Double::compareTo)
                .orElse(Double.NaN);
    }
}
