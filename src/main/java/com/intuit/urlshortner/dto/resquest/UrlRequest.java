package com.intuit.urlshortner.dto.resquest;

import com.intuit.urlshortner.validation.ValidUrl;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class UrlRequest {
    @NotBlank @ValidUrl private String originalUrl;
    @NotBlank private String userId;
}
