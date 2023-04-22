package com.skopmateusz.backendTask.service;

import com.skopmateusz.backendTask.models.responses.AverageExchangeRateResponse;
import com.skopmateusz.backendTask.models.responses.BuySellMajorDifferenceResponse;
import com.skopmateusz.backendTask.models.responses.MaxMinAverageExchangeRateResponse;
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

    public AverageExchangeRateResponse getAvgRate(String currencyCode, String date) {
        List<AverageRate> rates = nbpExchangeRatesProvider.getAverageRatesOfDate(date, currencyCode);
        Double average = rates.stream()
                .mapToDouble(AverageRate::mid)
                .average()
                .orElse(Double.NaN);
        return new AverageExchangeRateResponse(average);
    }

    public MaxMinAverageExchangeRateResponse getLastMaxMinAvgRates(String currency, Integer numOfQuotations) {
        List<AverageRate> rates = nbpExchangeRatesProvider.getLastAverageRates(currency, numOfQuotations);
        rates.sort(Comparator.comparing(AverageRate::mid));
        Double max = rates.get(rates.size()-1).mid();
        Double min = rates.get(0).mid();
        return new MaxMinAverageExchangeRateResponse(max, min);
    }

    public BuySellMajorDifferenceResponse getLastBuySellDifference(String currency, Integer numOfQuotations) {
        List<BuySellRate> rates = nbpExchangeRatesProvider.getLastBuySellRates(currency, numOfQuotations);
        Double difference =  rates.stream()
                .map(rate -> rate.bid() - rate.ask())
                .max(Double::compareTo)
                .orElse(Double.NaN);
        return new BuySellMajorDifferenceResponse(difference);
    }
}
