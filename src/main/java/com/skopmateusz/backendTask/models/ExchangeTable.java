package com.skopmateusz.backendTask.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeTable(String table, String currency, String code, List<Rate> rates) {
}
