package pt.ist.socialsoftware.edition.ldod.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.socialsoftware.edition.ldod.dto.APIResponse;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;

import java.security.Principal;
import java.util.Map;

@RestController
public class WebSocketController {
    private static Logger logger = LoggerFactory.getLogger(WebSocketController.class);
    @Autowired
    private SimpMessagingTemplate broker;


    @MessageMapping("/tags")
    @SendTo("/topic/tags")
    public @ResponseBody void handleTags(@Payload Map<String,String> value) {
        logger.debug("client sent:{}", value.values());
        broker.convertAndSend("/topic/tags", value);
    }

    @SubscribeMapping("/ping")
    public void subscribeHandler(){
        logger.debug("ldod-game handler");
        //broker.convertAndSend("ldod-game/ping", "Registered");
    }
}
