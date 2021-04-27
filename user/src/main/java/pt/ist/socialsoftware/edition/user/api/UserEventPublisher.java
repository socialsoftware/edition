package pt.ist.socialsoftware.edition.user.api;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import pt.ist.socialsoftware.edition.notification.event.Event;

import javax.jms.Queue;

@Component
public class UserEventPublisher {

    @Autowired
    private final Queue queue = new ActiveMQQueue("test-queue");

    @Autowired
    private JmsTemplate jmsTemplate;


    @GetMapping("/publishEvent")
    public ResponseEntity<Event> publishEvent(Event event){
        jmsTemplate.convertAndSend(queue, event);
        System.out.println("published Event!");
        return new ResponseEntity(event, HttpStatus.OK);
    }
}
