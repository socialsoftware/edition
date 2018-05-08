package pt.ist.socialsoftware.edition.core.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {
	private static Logger log = LoggerFactory.getLogger(GameController.class);
	private final AtomicLong counter = new AtomicLong();
	private static final String template = "Hello, %s!";

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(method = RequestMethod.GET, value = "/game")
	public Game greeting(@RequestParam(required=true, defaultValue="DefaultUser") String username, @RequestParam(required=true, defaultValue="DefaultVirtualEdition") String virtualEdition) {
		return new Game(counter.incrementAndGet(),"teste", username, virtualEdition);
	}
}
