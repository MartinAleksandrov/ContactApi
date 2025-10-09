package com.example.contactapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.List;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;


@Configuration
public class CorsConfig {

    // This filter will intercept incoming HTTP requests and apply the defined CORS settings.
    @Bean
    public CorsFilter corsFilter() {

        // Creates a configuration source object that maps URL patterns to CORS configurations.
        var urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

        // Creates a CORS configuration instance where we specify allowed origins, headers, and methods.
        var corsConfiguration = new CorsConfiguration();

        // Allows credentials (cookies, authorization headers, etc.) to be included in CORS requests.
        corsConfiguration.setAllowCredentials(true);

        // Specifies which origins are allowed to access this API.
        // Typically, these are frontend apps running on different ports.
        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:3000",  // React app
                "http://localhost:4200"   // Angular app
        ));

        // Lists all HTTP headers that can be used in the actual request.
        corsConfiguration.setAllowedHeaders(List.of(
                ORIGIN,
                ACCESS_CONTROL_ALLOW_ORIGIN,
                CONTENT_TYPE,
                ACCEPT,
                AUTHORIZATION,
                ACCESS_CONTROL_REQUEST_METHOD,
                ACCESS_CONTROL_REQUEST_HEADERS,
                ACCESS_CONTROL_ALLOW_CREDENTIALS
        ));

        // Lists headers that the client (browser) is allowed to access from the response.
        corsConfiguration.setExposedHeaders(List.of(
                ORIGIN,
                ACCESS_CONTROL_ALLOW_ORIGIN,
                CONTENT_TYPE,
                ACCEPT,
                AUTHORIZATION,
                ACCESS_CONTROL_REQUEST_METHOD,
                ACCESS_CONTROL_REQUEST_HEADERS,
                ACCESS_CONTROL_ALLOW_CREDENTIALS
        ));

        // Specifies the allowed HTTP methods for cross-origin requests.
        corsConfiguration.setAllowedMethods(List.of(
                GET.name(),
                POST.name(),
                PUT.name(),
                PATCH.name(),
                DELETE.name(),
                OPTIONS.name()
        ));

        // Registers this CORS configuration to apply to all endpoints in the application.
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        // Returns a new CorsFilter that uses the above configuration.
        // Spring Boot automatically adds it to the filter chain.
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}

