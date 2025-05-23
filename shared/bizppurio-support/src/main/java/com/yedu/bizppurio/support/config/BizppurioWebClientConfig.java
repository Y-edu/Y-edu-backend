package com.yedu.bizppurio.support.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.function.Function;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ReactorResourceFactory;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@EnableConfigurationProperties(BizppurioProperties.class)
public class BizppurioWebClientConfig {
  @Bean
  public ReactorResourceFactory resourceFactory() {
    return new ReactorResourceFactory();
  }

  @Bean
  public WebClient webClient() {
    Function<HttpClient, HttpClient> mapper =
        client ->
            HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                .doOnConnected(
                    connection ->
                        connection
                            .addHandlerLast(new ReadTimeoutHandler(10))
                            .addHandlerLast(new WriteTimeoutHandler(10)))
                .responseTimeout(Duration.ofSeconds(10));

    ClientHttpConnector connector = new ReactorClientHttpConnector(resourceFactory(), mapper);
    return WebClient.builder().clientConnector(connector).build();
  }
}
