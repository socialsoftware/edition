package pt.ist.socialsoftware.edition.ldod.controller.api;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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


import java.util.*;

@RestController
public class WebSocketController {
    private static Logger logger = LoggerFactory.getLogger(WebSocketController.class);
    @Autowired
    private SimpMessagingTemplate broker;
    private Multimap<Object, Object> tagMap = ArrayListMultimap.create();
    private Map<String, Integer> votingMap = new HashMap<>();

    @MessageMapping("/tags")
    @SendTo("/topic/tags")
    public @ResponseBody void handleTags(@Payload Map<String,String> value) {
        tagMap.put(value.values().toArray()[0], value.values().toArray()[1]);
        logger.debug("tagMap received: {} {}", tagMap.keySet(), tagMap.values());
        votingMap.put((String) value.values().toArray()[1], 1);
        broker.convertAndSend("/topic/tags", value.values());
    }

    @MessageMapping("/votes")
    @SendTo("/topic/votes")
    public @ResponseBody void handleVotes(@Payload Map<String,String> value) {
        String authorId = (String) value.values().toArray()[0];
        Object response = value.values().toArray()[1];
        HashMap<String,Integer> votes = (HashMap<String, Integer>) response;
        int res = votingMap.get(votes.get("tag")) + votes.get("vote");
        logger.debug("teste {}", res);
        votingMap.put(String.valueOf(votes.get("tag")), votingMap.get(votes.get("tag")) + votes.get("vote"));
        logger.debug("votingMap keys: {} values {}", votingMap.keySet(), votingMap.values());
        broker.convertAndSend("/topic/votes", value.values());
    }

    @SubscribeMapping("/ping")
    public void subscribeHandler(){
        logger.debug("ldod-game handler");
        //broker.convertAndSend("ldod-game/ping", "Registered");
    }
}
