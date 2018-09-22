package pt.ist.socialsoftware.edition.ldod.controller.api;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.*;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.dto.ClassificationGameDto;
import pt.ist.socialsoftware.edition.ldod.dto.GameTagDto;

@RestController
@RequestMapping("/api/services/ldod-game")
public class ClassificationGameController {
	private static Logger logger = LoggerFactory.getLogger(ClassificationGameController.class);

	@Autowired
	private SimpMessagingTemplate broker;

	private Map<String, ClassificationGameDto> gamesMapDto = new LinkedHashMap<>(100);
	private Map<String, List<GameTagDto>> tagsMapDto = new LinkedHashMap<>(100);


	@GetMapping("/active")
	public @ResponseBody ResponseEntity<List<ClassificationGameDto>> getActiveGames() {
		List<ClassificationGameDto> result = LdoD.getInstance().getVirtualEditionsSet().stream()
				.flatMap(ve -> ve.getClassificationGameSet().stream().filter(ClassificationGame::isActive))
				.map(ClassificationGameDto::new).sorted(Comparator.comparingLong(ClassificationGameDto::getDateTime)).collect(Collectors.toList());

		for (ClassificationGameDto gameDto : result) {
			if (!gamesMapDto.containsKey(gameDto.getGameExternalId())) {
				gamesMapDto.put(gameDto.getGameExternalId(), gameDto);
				tagsMapDto.put(gameDto.getGameExternalId(), new ArrayList<>(100));
			}
		}

		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	@GetMapping("/end/{gameId}")
	public @ResponseBody ResponseEntity<?> end(@PathVariable(value = "gameId") String gameId) {
		//logger.debug("end: {}", gameId);

		ClassificationGameDto currentGame = gamesMapDto.get(gameId);
		String winner = currentGame.getWinner();
		String winningTag = currentGame.getWinningTag();
		List<Object> response = new ArrayList<>();
		response.add(winner);
		response.add(winningTag);
		response.add(currentGame.getPlayersMap());
		currentGame.setEnded(true);

		ClassificationGame game = FenixFramework.getDomainObject(gameId);
		game.finish(winner, winningTag, currentGame.getPlayersMap());
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("/leaderboard")
	public @ResponseBody ResponseEntity<?> getLeaderboard() {
		List<Object> response = new ArrayList<>();

		List<Player> players = LdoD.getInstance().getUsersSet().stream().map(LdoDUser::getPlayer).collect(Collectors.toList());

		Map<String, Double> playersScores = new LinkedHashMap<>();
		for (Player p: players) {
			if (p != null) {
				playersScores.put(p.getUser().getUsername(), p.getScore());
			}
		}

		List<String> users = playersScores.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry
				.comparingByValue())).map(Map.Entry::getKey).collect(Collectors.toList());
		List<Double> scores = playersScores.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry
				.comparingByValue())).map(Map.Entry::getValue).collect(Collectors.toList());

		response.add(users);
		response.add(scores);

		return new ResponseEntity<>(response.toArray(), HttpStatus.OK);

	}

	@MessageMapping("/connect")
	@SendTo("/topic/config")
	public synchronized @ResponseBody void handleConnect(@Payload Map<String, String> payload) {
		//logger.debug(" handleConnect keys: {}, value: {}", payload.keySet(), payload.values());

		String userId = payload.get("userId");
		String gameId = payload.get("gameId");

		ClassificationGameDto currentGame = gamesMapDto.get(gameId);
		currentGame.addPlayer(userId, 1.0);

		createGameParticipant(gameId, userId);

		/*ClassificationGame.getUserArray().add(userId);
		ClassificationGame.getGameBlockingMap().put(gameId, ClassificationGame.getUserArray());*/
		//logger.debug("users {} {}", ClassificationGame.getUsers(gameId), ClassificationGame.getUsers(gameId).size());
	}

	@Atomic(mode = TxMode.WRITE)
	private void createGameParticipant(String gameId, String userId) {
		ClassificationGame game = FenixFramework.getDomainObject(gameId);
		game.addParticipant(userId);
	}

	@MessageMapping("/tags")
	@SendTo("/topic/tags")
	public @ResponseBody void handleTags(@Payload Map<String, String> payload) {
		//logger.debug("handleTags keys: {}, values: {}", payload.keySet(), payload.values());

		// authorId <=> userId
		String gameId = payload.get("gameId");
		String authorId = payload.get("authorId");
		ClassificationGameDto currentGame = gamesMapDto.get(gameId);
		GameTagDto tag = new GameTagDto(authorId, gameId, currentGame.getVirtualEditionInterDto().getUrlId(), payload.get("msg"), 1.0);
		List<GameTagDto> res = tagsMapDto.get(gameId);


		if (res.stream().noneMatch(t -> t.getContent().equals(tag.getContent()))) {
			// if tag is submitted for the first we add it havina a value of 2.0
			res.add(tag);
			tagsMapDto.put(gameId, res);
			// bug was occuring game only had 1 player
			boolean op = currentGame.updatePlayerScore(authorId, 2.0);
			//logger.debug("handleTags -> updatePlayerScore occured: {} ", op );

		} else {
			// if tag already exists increment score and update +1
			res.stream().filter(t -> t.getContent().equals(tag.getContent())).forEach(t -> {
				t.setScore(tag.getScore() + t.getScore());
				payload.put("vote", String.valueOf(t.getScore()));
				// since the tag has been suggested, it now has a co-author / voter because of
				// the suggestion
				t.addCoAuthor(authorId);//suggested
				t.addVoter(authorId); // suggested <=> voted
			});
			boolean op = currentGame.updatePlayerScore(authorId, 1.0);
			//logger.debug("handleTags -> updatePlayerScore occured: {} ", op );
		}

		this.broker.convertAndSend("/topic/tags", payload.values());
	}

	@MessageMapping("/votes")
	@SendTo("/topic/votes")
	public @ResponseBody void handleVotes(@Payload Map<String, String> payload) {
		//logger.debug("handleVotes keys: {}, values: {}", payload.keySet(), payload.values());

		String gameId = payload.get("gameId");
		String voterId = payload.get("voterId");
		String tagMsg = payload.get("msg");
		Object vote = payload.get("vote");
		double finalVote = Double.parseDouble((String) vote);
		ClassificationGameDto currentGame = gamesMapDto.get(gameId);
		List<GameTagDto> res = tagsMapDto.get(gameId);

		if (res == null) {
			return;
		}
		res.stream().filter(t -> t.getContent().equals(tagMsg)).forEach(tagDto -> {
			tagDto.setScore(tagDto.getScore() + finalVote);
			tagDto.addVoter(voterId);
			currentGame.updatePlayerScore(voterId, finalVote);
			payload.put("vote", String.valueOf(tagDto.getScore()));
		});

		GameTagDto topGameTag;
		if (!res.isEmpty()) {
			topGameTag = res.stream().sorted(Comparator.comparing(GameTagDto::getScore).reversed()).limit(1).collect(Collectors.toList()).get(0);
		}
		else {
			topGameTag = new GameTagDto("system", gameId, currentGame.getVirtualEditionInterDto().getUrlId(), "no tags submitted", 0);
		}

		String currentTopTag = topGameTag.getContent();
		String currentWinner = topGameTag.getAuthorId();
		currentGame.setWinner(currentWinner);
		currentGame.setWinningTag(currentTopTag);

		payload.put("top", currentTopTag);
		payload.put("winner", currentWinner);

		this.broker.convertAndSend("/topic/votes", payload.values());
	}

	@MessageMapping("/review")
	@SendTo("/topic/review")
	public @ResponseBody void handleReview(@Payload Map<String, String> payload) {
		//logger.debug("handleReview keys: {}, values: {}", payload.keySet(), payload.values());

		String gameId = payload.get("gameId");
		String voterId = payload.get("voterId");
		Object limit = payload.get("limit");
		int finalLimit = Integer.parseInt((String)limit);
		ClassificationGameDto currentGame = gamesMapDto.get(gameId);
		List<GameTagDto> res = tagsMapDto.get(gameId);

		if (res == null) {
			logger.debug("No tags submitted");
			return;
		}


		//List<GameTagDto> topTags = res.stream().sorted((g1, g2) -> (int) g2.getScore()).limit(finalLimit).collect(Collectors.toList());
		// TODO: check me, removed limit

		List<GameTagDto> topTags = res.stream().sorted(Comparator.comparing(GameTagDto::getScore).reversed()).collect(Collectors.toList());

		//payload.put("topTags", topTags);
		List<Map<String, String>> response = new ArrayList<>();
		for (GameTagDto gameTagDto : topTags) {
			Map<String, String> map = new LinkedHashMap<>();
			map.put("tag", gameTagDto.getContent());
			map.put("vote", String.valueOf(gameTagDto.getScore()));
			response.add(map);
		}

		GameTagDto topGameTag;
		if (!res.isEmpty()) {
			topGameTag = topTags.get(0);
		}
		else {

			topGameTag = new GameTagDto("system", gameId, currentGame.getVirtualEditionInterDto().getUrlId(), "no tags submitted", 0);
		}

		String currentTopTag = topGameTag.getContent();
		String currentWinner = topGameTag.getAuthorId();

		Map<String, String> map = new LinkedHashMap<>();
		map.put(currentWinner, currentTopTag);
		response.add(map);

		this.broker.convertAndSend("/topic/review", response);
	}

	/*@MessageMapping("/register")
	@SendTo("/topic/config")
	@Atomic(mode = TxMode.READ)
	public @ResponseBody void handleRegister(@Payload Map<String, String> payload) {
		//logger.debug("handleRegister keys: {}, values: {}", payload.keySet(), payload.values());

		String gameId = payload.get("gameId");
		payload.remove("userId");
		payload.remove("gameId");

		Thread t = new Thread(new Runnable() {
			@Atomic(mode = TxMode.READ)
			public void run() {
				while (true) {
					ClassificationGame game = FenixFramework.getDomainObject(gameId);
					if (game != null && game.hasStarted()) {

						payload.put("currentUsers", String.valueOf(ClassificationGame.getUsers(gameId).size()));
						payload.put("command", "ready");
						broker.convertAndSend("/topic/config", payload.values());
						break;
					}
				}
			}
		});
   		t.start();
	}*/

}
