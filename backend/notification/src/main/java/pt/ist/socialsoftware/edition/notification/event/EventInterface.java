package pt.ist.socialsoftware.edition.notification.event;

//import pt.ist.socialsoftware.edition.ldod.frontend.user.session.SessionRequiresInterface;
//import pt.ist.socialsoftware.edition.game.api.GameRequiresInterface;
//import pt.ist.socialsoftware.edition.recommendation.api.RecommendationRequiresInterface;
//import pt.ist.socialsoftware.edition.virtual.api.VirtualRequiresInterface;

import groovy.transform.Synchronized;

import java.util.HashSet;
import java.util.Set;


public class EventInterface {

    private static EventInterface instance;

    public static EventInterface getInstance() {
        if (instance == null) {
            instance = new EventInterface();
        }
        return instance;
    }

    protected EventInterface() {}


    public Set<SubscribeInterface> subscribers = new HashSet<>();
//    private final VirtualRequiresInterface virtualRequiresInterface = new VirtualRequiresInterface();
//    private final GameRequiresInterface gameRequiresInterface = new GameRequiresInterface();
//    private final RecommendationRequiresInterface recommendationRequiresInterface = new RecommendationRequiresInterface();
//    private final SessionRequiresInterface sessionRequiresInterface = new SessionRequiresInterface();

    public void subscribe(SubscribeInterface subscriber){
        subscribers.add(subscriber);
    }


    public void publish(Event event) {
        for (int i = 0; i < subscribers.size(); i++) {
            ((SubscribeInterface) subscribers.toArray()[i]).notify(event);
        }

  //      subscribers.forEach(subscribeInterface -> subscribeInterface.notify(event));

//        this.virtualRequiresInterface.notifyEvent(event);
//        this.gameRequiresInterface.notifyEvent(event);
//        this.recommendationRequiresInterface.notifyEvent(event);
//        this.sessionRequiresInterface.notifyEvent(event);
    }
}
