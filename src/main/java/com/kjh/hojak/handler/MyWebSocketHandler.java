package com.kjh.hojak.handler;

import java.time.Duration;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MyWebSocketHandler implements WebSocketHandler {
	
	@Override
	public Mono<Void> handle(WebSocketSession session) {
		
		/**
		 * Flux.generate(Consumner<SynchroniusSink<T>> generator)
		 * 
		 * consumer 콜백을 통해 신호를 하나씩 생성하여 Flux 를 생성한다.
		 */
		return session.send(
				Flux.<String> generate(sink -> sink.next(
						String.format("{ message: 'got local message', data: '%s'}", new Date())))
				.delayElements(Duration.ofSeconds(1)).map(session::textMessage));
	}
	
}
