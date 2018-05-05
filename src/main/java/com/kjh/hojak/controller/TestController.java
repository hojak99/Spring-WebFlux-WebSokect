package com.kjh.hojak.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TestController {

	@GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> getStreaming() throws URISyntaxException {
		WebSocketClient client = new ReactorNettyWebSocketClient();
		
		/**
		 * EmitterProcessor<T>.class
		 * 동기식 drain loop 와 함께 발행-구독을 구현하는 RingBuffer 기반의 메세지 전달 프로세스
		 * 
		 * Flux.map(Function<? super T, ? extends V> mapper)
		 * 각 함목에 동기 함수를 적용해 해당 시퀀스를 변형한다.
		 * 
		 */
		EmitterProcessor<String> output = EmitterProcessor.create();
		
		// subscribe() 를 통해 데이터가 흘러가게 한다. 시퀀스에 대한 Publisher 에게 데이터 생성 시작을 요청한다고 한다.
		Mono<Void> sessionMono = client.execute(
				new URI("ws://localhost:8080/test"),
				session -> session.receive()
					.map(WebSocketMessage::getPayloadAsText)
					.subscribeWith(output).then());
		
		return output.doOnSubscribe(s -> sessionMono.subscribe());			
	}
	
	@GetMapping(path = "/remote", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> getRemoteStreaming() throws URISyntaxException {
		
		Flux<String> input = Flux.<String>generate(sink-> sink.next(
				String.format("{get remote message: 'message', date: '%s'}", new Date())))
				.delayElements(Duration.ofSeconds(1));
		
		WebSocketClient clinet = new ReactorNettyWebSocketClient();
		
		EmitterProcessor<String> output = EmitterProcessor.create();
		
		Mono<Void> sessionMono = clinet.execute(
				URI.create("ws://echo.websocket.org"), 
				session -> session.send(input.map(session::textMessage))
				.thenMany(session.receive().map(
						WebSocketMessage::getPayloadAsText).subscribeWith(output).then()).then());
		
		return output.doOnSubscribe(s -> sessionMono.subscribe());
	}
	
	
	
}
