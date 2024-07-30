package com.danservice.gateway;

import io.micrometer.common.KeyValue;
import io.micrometer.common.KeyValues;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.observation.DefaultServerRequestObservationConvention;
import org.springframework.http.server.reactive.observation.ServerRequestObservationContext;
import org.springframework.http.server.reactive.observation.ServerRequestObservationConvention;

import java.util.Optional;

import static org.springframework.http.server.reactive.observation.ServerHttpObservationDocumentation.LowCardinalityKeyNames.URI;

@Configuration
public class MicrometerConfiguration {
    private static final String KEY_URI = "uri";
    private static final String UNKNOWN = "UNKNOWN";

    @Bean
    public ServerRequestObservationConvention uriTagContributorForObservationApi() {

        /**
         * Fixes the URI {@link org.springframework.http.server.reactive.observation.ServerHttpObservationDocumentation} value when set as <b>UNKNOWN</b> in /metrics API
         * by Spring framework issue. See https://github.com/spring-cloud/spring-cloud-gateway/issues/891
         */
        return new DefaultServerRequestObservationConvention() {
            @Override
            public KeyValues getLowCardinalityKeyValues(ServerRequestObservationContext context) {
                KeyValues lowCardinalityKeyValues = super.getLowCardinalityKeyValues(context);

                if (isUriTagNullOrUnknown(context, lowCardinalityKeyValues)) {
                    return lowCardinalityKeyValues
                            .and(KeyValue.of(KEY_URI, context.getCarrier().getPath().value()));
                }

                return lowCardinalityKeyValues;
            }

            private static boolean isUriTagNullOrUnknown(ServerRequestObservationContext context, KeyValues lowCardinalityKeyValues) {
                Optional<KeyValue> uriKeyValue = lowCardinalityKeyValues.stream()
                        .filter(keyValue -> URI.name()
                                .equals(keyValue.getKey())).findFirst();

                return (uriKeyValue.isEmpty() || UNKNOWN.equals(uriKeyValue.get().getValue()));
            }
        };
    }
}
