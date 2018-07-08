package pt.ist.socialsoftware.edition.ldod.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class APIChatController {

    private static Logger logger = LoggerFactory.getLogger(APIChatController.class);

    @MessageMapping("/chat")
    @SendTo("/")
    public Map<String, String> post(@Payload Map<String, String> message) {
        logger.debug("received a message {}", message);
        message.put("timestamp", Long.toString(System.currentTimeMillis()));
        return message;
    }
}
