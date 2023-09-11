package com.intuit.urlshortner.dto.resquest;

import com.intuit.urlshortner.validation.ValidCustomUrl;
import com.intuit.urlshortner.validation.ValidUrl;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class CustomUrlRequest {
    @ValidUrl private String originalUrl;
    @ValidCustomUrl private String customUrl;
    @NotBlank private String userId;
}
