package com.skopmateusz.backendTask.service;

import com.skopmateusz.backendTask.errorHandlers.NbpRestTemplateResponseErrorHandler;
import com.skopmateusz.backendTask.models.responses.AverageExchangeRateResponse;
import com.skopmateusz.backendTask.models.responses.BuySellMajorDifferenceResponse;
import com.skopmateusz.backendTask.models.responses.MaxMinAverageExchangeRateResponse;
import com.skopmateusz.backendTask.models.AverageRate;
import com.skopmateusz.backendTask.models.BuySellRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class NbpService {

    private final NbpExchangeRatesProvider nbpExchangeRatesProvider;
    private final RestTemplate restTemplate;

    @Autowired
    public NbpService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .errorHandler(new NbpRestTemplateResponseErrorHandler())
                .build();
        this.nbpExchangeRatesProvider =
                new NbpExchangeRatesProvider("http://api.nbp.pl/api/exchangerates/rates/", restTemplate);
    }

    public AverageExchangeRateResponse getAvgRate(String currency, String date) {
        List<AverageRate> rates =
                nbpExchangeRatesProvider.getAverageRates(String.format("A/%s/%s", currency, date)).stream()
                        .map(obj -> (AverageRate)obj)
                        .toList();
        Double average = rates.stream()
                .mapToDouble(AverageRate::mid)
                .average()
                .orElse(Double.NaN);
        return new AverageExchangeRateResponse(average);
    }

    public MaxMinAverageExchangeRateResponse getLastMaxMinAvgRates(String currency, Integer numOfQuotations) {
        List<AverageRate> rates = new ArrayList<>(
                nbpExchangeRatesProvider.getAverageRates(String.format("A/%s/last/%d", currency, numOfQuotations))
        );
        rates.sort(Comparator.comparing(AverageRate::mid));
        Double max = rates.get(rates.size()-1).mid();
        Double min = rates.get(0).mid();
        return new MaxMinAverageExchangeRateResponse(max, min);
    }

    public BuySellMajorDifferenceResponse getLastBuySellDifference(String currency, Integer numOfQuotations) {
        List<BuySellRate> rates =
                nbpExchangeRatesProvider.getBuySellRates(String.format("C/%s/last/%d", currency, numOfQuotations));
        Double difference =  rates.stream()
                .map(rate -> rate.bid() - rate.ask())
                .max(Double::compareTo)
                .orElse(Double.NaN);
        return new BuySellMajorDifferenceResponse(difference);
    }
}
