package com.yedu.api.global.config;

import com.yedu.api.global.config.AppConfig.PaymentConfig;
import com.yedu.common.webclient.WebClientProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@EnableConfigurationProperties(PaymentConfig.class)
@Configuration
public class AppConfig {

  @Bean
  public WebClient paymentWebClient(PaymentConfig config) {
    WebClientProperties props = config.webClientProperties;

    HttpClient httpClient =
        HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, props.connectionTimeOut() * 1000)
            .doOnConnected(
                conn ->
                    conn.addHandlerLast(
                            new ReadTimeoutHandler(props.readTimeOut(), TimeUnit.SECONDS))
                        .addHandlerLast(
                            new WriteTimeoutHandler(props.writeTimeOut(), TimeUnit.SECONDS)));

    return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
  }

  @RequiredArgsConstructor
  @ConfigurationProperties(prefix = "app.payment-api")
  public static class PaymentConfig {
    private final WebClientProperties webClientProperties;
  }
}
