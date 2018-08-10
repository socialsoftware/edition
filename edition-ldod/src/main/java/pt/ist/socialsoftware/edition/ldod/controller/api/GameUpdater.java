package pt.ist.socialsoftware.edition.ldod.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pt.ist.socialsoftware.edition.ldod.dto.APIResponse;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class GameUpdater{
    private static final Logger logger = LoggerFactory.getLogger(GameUpdater.class);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private SimpMessagingTemplate broker;

    @Autowired
    public GameUpdater(final SimpMessagingTemplate broker) {
        this.broker = broker;
    }

    @Scheduled(fixedRate = 30000)
    public void run() {
        String time = LocalTime.now().format(TIME_FORMAT);
        //broker.convertAndSend("/topic/tags", new APIResponse(true,"Current time is " + time));
    }
}