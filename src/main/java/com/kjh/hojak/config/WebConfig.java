package com.kjh.hojak.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.TomcatRequestUpgradeStrategy;

import com.kjh.hojak.handler.MyWebSocketHandler;

@Configuration
public class WebConfig {
	
	@Bean
	public HandlerMapping handlerMapping() {
		Map<String, WebSocketHandler> map = new HashMap<>();
		map.put("/test", new MyWebSocketHandler());
		
		
		/**
		 * SimpleUrlHandlerMapping.class
		 * 
		 * 요청 URL 과 비교해 매칭되는 패턴에 대한 handler 를 호출한다.
		 */
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setUrlMap(map);
		mapping.setOrder(1);		// before annotated controllers
		return mapping;
	}
	
	
	/**
	 * Spring WebFlux 는 WebSokcet 요청을 adapt 하고 MyWebSocketHandler 를 사용하여 결과 WebSocket 세션을 처리할 수 있는
	 * WebSocketHandlerAdapter 를 제공한다.Adapter 가 Bean 으로 등록된 후 SimpleUrlHandlerMapping 을 사용하여  요청을 handler 에 매핑할 수 있다.
	 * 
	 * @return
	 */
	@Bean
	public WebSocketHandlerAdapter handlerAdapter() {
		return new WebSocketHandlerAdapter();
	}
	
}
