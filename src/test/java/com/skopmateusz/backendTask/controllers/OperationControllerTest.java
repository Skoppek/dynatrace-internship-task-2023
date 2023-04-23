package com.skopmateusz.backendTask.controllers;

import com.skopmateusz.backendTask.service.NbpService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class OperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(strings = {"2022.09.09", "09-09-2022", "010101"})
    void wrongDateFormatTest(String date) throws Exception {
        mockMvc.perform(get(String.format("/avg-ex-rate/%s/usd", date)))
                .andExpect(status().isBadRequest());
    }

    // 2022-01-01 was saturday -> there is no data for weekends
    @ParameterizedTest
    @CsvSource({"2022-01-03,xxx", "2022-01-01,usd"})
    void notFoundDataTest(String date, String code) throws Exception {
        mockMvc.perform(get(String.format("/avg-ex-rate/%s/%s", date, code)))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"USDD", "E3R", "P", "PL"})
    void wrongCurrencyCodeFormatTest(String code) throws Exception {
        mockMvc.perform(get(String.format("/avg-ex-rate/2023-04-21/%s", code)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, Integer.MIN_VALUE, 256, Integer.MAX_VALUE})
    void wrongQuotationsTest(int quotations) throws Exception {
        mockMvc.perform(get(String.format("/avg-value/usd?quotations=%d", quotations)))
                .andExpect(status().isBadRequest());
    }
}