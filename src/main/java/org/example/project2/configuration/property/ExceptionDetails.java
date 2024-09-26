package org.example.project2.configuration.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.project2.enums.ExceptionEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties
@Getter
@Setter
public class ExceptionDetails {
    private Map<ExceptionEnum, ExceptionProperties> errorDetails;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExceptionProperties {
        private String code;
        private String message;
        private int httpCode;
    }
}
