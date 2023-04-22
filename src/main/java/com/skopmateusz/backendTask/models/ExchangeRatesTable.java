package com.skopmateusz.backendTask.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeRatesTable(String table, String currency, String code, List<AverageRate> rates) {
}
