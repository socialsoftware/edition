package pt.ist.socialsoftware.edition.virtual.api;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import pt.ist.socialsoftware.edition.notification.event.Event;

import javax.jms.Queue;
import javax.jms.Topic;

@Component
public class VirtualEventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(VirtualEventPublisher.class);


    @Autowired
    private final Topic queue = new ActiveMQTopic("test-topic");

    @Autowired
    private JmsTemplate jmsTemplate;


    @GetMapping("/publishEvent")
    public ResponseEntity<Event> publishEvent(Event event){
        jmsTemplate.convertAndSend(queue, event);
        logger.debug("published Event!");
        return new ResponseEntity(event, HttpStatus.OK);
    }

}
