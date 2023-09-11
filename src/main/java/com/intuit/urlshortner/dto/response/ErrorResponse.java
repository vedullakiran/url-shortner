package com.intuit.urlshortner.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorResponse{
    private String errorMessage;
}
