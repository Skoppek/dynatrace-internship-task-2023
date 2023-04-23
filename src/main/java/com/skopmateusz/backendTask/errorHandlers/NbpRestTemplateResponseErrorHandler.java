package com.skopmateusz.backendTask.errorHandlers;

import com.skopmateusz.backendTask.exceptions.NbpNotFoundException;
import com.skopmateusz.backendTask.exceptions.NbpWrongParamsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class NbpRestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {

        return httpResponse.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse)
            throws IOException {

        if (httpResponse.getStatusCode().is4xxClientError()) {
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NbpNotFoundException("No data found.");
            }
            if (httpResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new NbpWrongParamsException("Wrong query parameters.");
            }
        }
    }
}
