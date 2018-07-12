package pt.ist.socialsoftware.edition.ldod.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import pt.ist.socialsoftware.edition.ldod.dto.APIResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessagingTemplate broker;
    private Map<String, String> subscribedUsers = new HashMap<>();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        logger.info("Web socket connected {}", headerAccessor.getSessionId());
    }



    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = headerAccessor.getNativeHeader("userId").get(0);
        String destination = headerAccessor.getDestination();
        logger.info("Received a subscribe to: {}, with Id: {}, from user: {}.", destination, headerAccessor.getSubscriptionId(), userId);
        broker.convertAndSend("/topic/ping", new APIResponse(true,"You are subscribed"));
        subscribedUsers.put(destination, userId);

     }

    @EventListener
    public void handleUnSubscribe(SessionUnsubscribeEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        logger.info("Received a unsubscribe: "+ headerAccessor.getSubscriptionId());
        subscribedUsers.remove(headerAccessor.getSubscriptionId());

    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        logger.info("Web socket disconnected {}", headerAccessor.getSessionId());
    }

    public Map<String, String> getSubscribedUsers() {
        return subscribedUsers;
    }

    public void setSubscribedUsers(Map<String, String> subscribedUsers) {
        this.subscribedUsers = subscribedUsers;
    }

}
