package com.kjh.hojak;

import java.net.URI;
import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringWebFluxWebSocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebFluxWebSocketApplication.class, args);
	}
}
