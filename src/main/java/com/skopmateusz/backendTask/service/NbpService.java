package com.skopmateusz.backendTask.service;

import com.skopmateusz.backendTask.models.responses.AverageExchangeRateResponse;
import com.skopmateusz.backendTask.models.responses.BuySellMajorDifferenceResponse;
import com.skopmateusz.backendTask.models.responses.MaxMinAverageExchangeRateResponse;
import com.skopmateusz.backendTask.models.AverageRate;
import com.skopmateusz.backendTask.models.BuySellRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class NbpService {

    private final NbpExchangeRatesProvider nbpExchangeRatesProvider;

    @Autowired
    public NbpService(NbpExchangeRatesProvider nbpExchangeRatesProvider) {
        this.nbpExchangeRatesProvider = nbpExchangeRatesProvider;
    }

    public AverageExchangeRateResponse getAvgRate(String currency, String date) {
        List<AverageRate> rates =
                nbpExchangeRatesProvider.getAverageRates(String.format("A/%s/%s", currency, date));
        Double average = rates.stream()
                .mapToDouble(AverageRate::mid)
                .average()
                .orElse(Double.NaN);
        average = roundDouble(average);
        return new AverageExchangeRateResponse(average);
    }

    public MaxMinAverageExchangeRateResponse getLastMaxMinAvgRates(String currency, Integer numOfQuotations) {
        List<AverageRate> rates = new ArrayList<>(
                nbpExchangeRatesProvider.getAverageRates(String.format("A/%s/last/%d", currency, numOfQuotations))
        );
        rates.sort(Comparator.comparing(AverageRate::mid));
        Double max = rates.get(rates.size()-1).mid();
        Double min = rates.get(0).mid();
        max = roundDouble(max);
        min = roundDouble(min);
        return new MaxMinAverageExchangeRateResponse(max, min);
    }

    public BuySellMajorDifferenceResponse getLastBuySellDifference(String currency, Integer numOfQuotations) {
        List<BuySellRate> rates =
                nbpExchangeRatesProvider.getBuySellRates(String.format("C/%s/last/%d", currency, numOfQuotations));
        Double difference =  rates.stream()
                .map(rate -> rate.bid() - rate.ask())
                .max(Double::compareTo)
                .orElse(Double.NaN);
        difference = roundDouble(difference);
        return new BuySellMajorDifferenceResponse(difference);
    }

    private Double roundDouble(Double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(4, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
