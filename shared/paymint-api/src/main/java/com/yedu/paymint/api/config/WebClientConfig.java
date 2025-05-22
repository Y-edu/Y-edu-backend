package com.yedu.paymint.api.config;

import com.yedu.common.webclient.WebClientProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@EnableConfigurationProperties(PaymintWebClientProperties.class)
public class WebClientConfig {

  @Bean
  public WebClient paymintWebClient(PaymintWebClientProperties properties) {
    WebClientProperties webClientProperties = properties.webClientProperties();
    HttpClient httpClient =
        HttpClient.create()
            .option(
                ChannelOption.CONNECT_TIMEOUT_MILLIS,
                webClientProperties.connectionTimeOut() * 1000)
            .doOnConnected(
                conn ->
                    conn.addHandlerLast(
                            new ReadTimeoutHandler(
                                webClientProperties.readTimeOut(), TimeUnit.SECONDS))
                        .addHandlerLast(
                            new WriteTimeoutHandler(
                                webClientProperties.writeTimeOut(), TimeUnit.SECONDS)));

    return WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .build();
  }

}
