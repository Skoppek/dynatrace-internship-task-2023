package com.skopmateusz.backendTask.service;

import com.skopmateusz.backendTask.models.AverageRate;
import com.skopmateusz.backendTask.models.BuySellRate;
import com.skopmateusz.backendTask.models.responses.AverageExchangeRateResponse;
import com.skopmateusz.backendTask.models.responses.BuySellMajorDifferenceResponse;
import com.skopmateusz.backendTask.models.responses.MaxMinAverageExchangeRateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NbpServiceTest {
    private static final String CURRENCY = "USD";
    private static final String DATE = "2022-09-09";
    private static final Integer NUM_OF_QUOTATIONS = 15;

    @Mock
    private NbpExchangeRatesProvider nbpExchangeRatesProvider;

    @InjectMocks
    private NbpService nbpService;

    @Test
    public void shouldGetAverageRate() {
        List<AverageRate> rates = List.of(
                new AverageRate(2.0),
                new AverageRate(12.0)
        );
        // given
        when(nbpExchangeRatesProvider.getAverageRates(String.format("A/%s/%s", CURRENCY, DATE)))
                .thenReturn(rates);
        // when
        AverageExchangeRateResponse response = nbpService.getAvgRate(CURRENCY, DATE);
        // then
        assertEquals( 7.0, response.averageExchangeRate());
    }

    @Test
    public void shouldGetMinMaxRate() {
        List<AverageRate> rates = List.of(
                new AverageRate(2.0),
                new AverageRate(6.53),
                new AverageRate(0.0),
                new AverageRate(-1.0),
                new AverageRate(45.422),
                new AverageRate(12.0)
        );
        // given
        when(nbpExchangeRatesProvider.getAverageRates(String.format("A/%s/last/%d", CURRENCY, NUM_OF_QUOTATIONS)))
                .thenReturn(rates);
        // when
        MaxMinAverageExchangeRateResponse response = nbpService.getLastMaxMinAvgRates(CURRENCY, NUM_OF_QUOTATIONS);
        // then
        assertEquals(45.422, response.maxValue());
        assertEquals(-1.0, response.minValue());
    }

    @Test
    public void shouldGreatestDifferenceRate() {
        List<BuySellRate> rates = List.of(
                new BuySellRate(0.45, 0.12),
                new BuySellRate(1.5, 0.0),
                new BuySellRate(2.6, 3.0),
                new BuySellRate(10.0, 1.0),
                new BuySellRate(-0.5, 0.12),
                new BuySellRate(0.0, 0.0)
        );
        // given
        when(nbpExchangeRatesProvider.getBuySellRates(String.format("C/%s/last/%d", CURRENCY, NUM_OF_QUOTATIONS)))
                .thenReturn(rates);
        // when
        BuySellMajorDifferenceResponse response = nbpService.getLastBuySellDifference(CURRENCY, NUM_OF_QUOTATIONS);
        // then
        assertEquals(9.0, response.difference() );
    }
}