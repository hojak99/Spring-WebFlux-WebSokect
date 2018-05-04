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
		
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setUrlMap(map);
		mapping.setOrder(-1);		// before annotated controllers
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
	
	
	/**
	 * 각 서버의 RequestUpgradeStrategy 는 기본 WebSocket 엔진에서 사용 WebSocket 관련 구성 옵션을 제공한다.
	 * 아래는 Tomcat 에서 실행될 때 WebScoket 옵션을 설정하는 코드이다.
	 * (Tomcat 과 Jetty 만 제공)
	 * 
	 * @return
	 */
	@Bean
	public WebSocketService webSocketService() {
		TomcatRequestUpgradeStrategy strategy = new TomcatRequestUpgradeStrategy();
		strategy.setMaxSessionIdleTimeout(0L);
	
		/**
		 * HandShakeWebSocketService 는 WebSocket 요청에 대한 기본 검사를 수행하고 서버 별로 RequestUpgradeStrategy에 위임한다.
		 */
		return new HandshakeWebSocketService(strategy);
	}
	
	
}
