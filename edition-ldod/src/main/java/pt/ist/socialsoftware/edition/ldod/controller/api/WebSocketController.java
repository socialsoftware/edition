package pt.ist.socialsoftware.edition.ldod.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.socialsoftware.edition.ldod.dto.APIResponse;

@RestController
public class WebSocketController {
    private static Logger logger = LoggerFactory.getLogger(WebSocketController.class);
    @Autowired
    private SimpMessagingTemplate broker;


    @MessageMapping("/hello")
    @SendTo("/topic/ping")
    public @ResponseBody APIResponse ping(@Payload String value) {
        logger.debug("client send hello: " + value);
        return new APIResponse(true,"Hello back from the server");
    }

    @SubscribeMapping("/ping")
    public void subscribeHandler(){
        logger.debug("ldod-game handler");
        //broker.convertAndSend("ldod-game/ping", "Registered");
    }
}
