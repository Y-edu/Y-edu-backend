package com.yedu.paymint.api.config;

import com.yedu.common.webclient.WebClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "paymint")
public record PaymintWebClientProperties(WebClientProperties webClientProperties) {}
