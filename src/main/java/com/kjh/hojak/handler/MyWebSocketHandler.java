package com.kjh.hojak.handler;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Mono;

public class MyWebSocketHandler implements WebSocketHandler {

	@Override
	public Mono<Void> handle(WebSocketSession arg0) {
		return Mono.create(s -> {
			System.out.println("Hello");
			s.success();
		});
	}
	
}
