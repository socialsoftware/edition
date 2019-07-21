package pt.ist.socialsoftware.edition.ldod.api.event;

import pt.ist.socialsoftware.edition.ldod.game.api.GameRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.recommendation.api.RecommendationRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualRequiresInterface;

public class EventInterface {
    private final VirtualRequiresInterface virtualRequiresInterface = new VirtualRequiresInterface();
    private final GameRequiresInterface gameRequiresInterface = new GameRequiresInterface();
    private final RecommendationRequiresInterface recommendationRequiresInterface = new RecommendationRequiresInterface();

    public void publish(Event event) {
        this.virtualRequiresInterface.notifyEvent(event);
        this.gameRequiresInterface.notifyEvent(event);
        this.recommendationRequiresInterface.notifyEvent(event);
    }
}
