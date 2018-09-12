package pt.ist.socialsoftware.edition.ldod.controller.api;

import java.util.*;
import java.util.function.ToIntFunction;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.dto.APIResponse;
import pt.ist.socialsoftware.edition.ldod.dto.ClassificationGameDto;
import pt.ist.socialsoftware.edition.ldod.dto.GameDTO;
import pt.ist.socialsoftware.edition.ldod.dto.GameTagDto;

@RestController
public class ClassificationGameController {
	private static Logger logger = LoggerFactory.getLogger(ClassificationGameController.class);
	@Autowired
	private SimpMessagingTemplate broker;
	private final Map<String, ClassificationGameDto> gamesNEW = new LinkedHashMap<>();
	private final Map<String, List<GameTagDto>> submittedTagsNEW = new LinkedHashMap<>();


	@GetMapping("/api/services/ldod-game/active")
	public @ResponseBody ResponseEntity<List<ClassificationGameDto>> getActiveGames() {
		/*ClassificationGameDto[] result = LdoD.getInstance().getVirtualEditionsSet().stream()
				.flatMap(ve -> ve.getClassificationGameSet().stream().filter(ClassificationGame::isActive))
				.map(ClassificationGameDto::new).sorted((g1, g2) -> g1.getDateTime().compareTo(g2.getDateTime()))
				.toArray(ClassificationGameDto[]::new);*/

		// CHANGED TO LIST
		List<ClassificationGameDto> result = LdoD.getInstance().getVirtualEditionsSet().stream()
				.flatMap(ve -> ve.getClassificationGameSet().stream().filter(ClassificationGame::isActive))
				.map(ClassificationGameDto::new).sorted((g1, g2) -> g1.getDateTime().compareTo(g2.getDateTime())).collect(Collectors.toList());

		// INITIAL SETUP for GAMES
		for(ClassificationGameDto gameDto : result){
			gamesNEW.put(gameDto.getGameExternalId(), gameDto);
			submittedTagsNEW.put(gameDto.getGameExternalId(),new ArrayList<>());
		}

		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	@PostMapping("/api/services/ldod-game/finish")
	public @ResponseBody ResponseEntity<String> finishGame(@RequestBody ClassificationGameDto classificationGameDto) {
		/*logger.debug("finishGame gameExternalId: {}, tag: {}, winner: {}, players: {}",
				classificationGameDto.getGameExternalId(), classificationGameDto.getWinningTag(),
				classificationGameDto.getWinner(),
				classificationGameDto.getPlayers().stream().collect(Collectors.joining(",")));*/

		ClassificationGame game = FenixFramework.getDomainObject(classificationGameDto.getGameExternalId());

		game.finish(classificationGameDto.getWinner(), classificationGameDto.getWinningTag(),
				classificationGameDto.getPlayers());

		return new ResponseEntity<String>(HttpStatus.OK);

	}

	@MessageMapping("/connect")
	@SendTo("/topic/config")
	public @ResponseBody void handleConnect(@Payload Map<String, String> payload) {
		//logger.debug("connect received: {}", payload.values());
		String userId = payload.get("userId");
		String gameId = payload.get("gameId");
		ClassificationGameDto currentGame = gamesNEW.get(gameId);
		// Inserir jogador, por participar recebe um ponto
		currentGame.addPlayer(userId, 1.0);
		payload.remove("userId");
		payload.remove("gameId");
		payload.put("currentUsers", String.valueOf(currentGame.getPlayers().size()));
		payload.put("command", "ready");
		logger.debug("connect sending {}", payload.values());
		try {
			// Thread.sleep(1 * 60 *1000);
			while (currentGame.getPlayers().size() <= 1) {
				Thread.sleep(1000); // TODO: use the above time
			}
			this.broker.convertAndSend("/topic/config", payload.values());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@GetMapping("/api/services/ldod-game/end/{gameId}")
	public @ResponseBody ResponseEntity<?> handleEnd(@PathVariable(value = "gameId") String gameId) {
		//logger.debug("end of game: {}", gameId);
		// TODO: make transaction,i.e, save to DB
		ClassificationGameDto currentGame = gamesNEW.get(gameId);
		String winner = currentGame.getWinner();
		String winningTag = currentGame.getWinningTag();
		List<Object> response = new ArrayList<>();
		response.add(winner);
		response.add(winningTag);
		response.add(currentGame.getPlayersMap());
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@MessageMapping("/tags")
	@SendTo("/topic/tags")
	public @ResponseBody void handleTags(@Payload Map<String, String> payload) {
		//logger.debug("Tags received {}", payload.values());
		// authorId <=> userId
		String gameId = payload.get("gameId");
		String authorId = payload.get("authorId");
		ClassificationGameDto currentGame = gamesNEW.get(gameId);
		GameTagDto tag = new GameTagDto(authorId, gameId, currentGame.getVirtualEditionInterDto().getUrlId(), payload.get("msg"), 1.0);
		List<GameTagDto> res = submittedTagsNEW.get(gameId);


		if (res.stream().noneMatch(t -> t.getContent().equals(tag.getContent()))) {
			// if tag is submitted for the first we add it havina a value of 2.0
			res.add(tag);
			submittedTagsNEW.put(gameId, res);
			currentGame.editPlayerScore(authorId, 1.0);

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
			currentGame.editPlayerScore(authorId, 1.0);
		}

		this.broker.convertAndSend("/topic/tags", payload.values());

	}

	@MessageMapping("/votes")
	@SendTo("/topic/votes")
	public @ResponseBody void handleVotes(@Payload Map<String, String> payload) {
		// logger.debug("Votes received {}", payload.values());
		String gameId = payload.get("gameId");
		String voterId = payload.get("voterId");
		String tagMsg = payload.get("msg");
		Object vote = payload.get("vote");
		double finalVote = Double.parseDouble((String) vote);
		ClassificationGameDto currentGame = gamesNEW.get(gameId);
		List<GameTagDto> res = submittedTagsNEW.get(gameId);
		res.stream().filter(t -> t.getContent().equals(tagMsg)).forEach(tagDto -> {
			tagDto.setScore(tagDto.getScore() + finalVote);
			tagDto.addVoter(voterId);
			currentGame.editPlayerScore(voterId, finalVote);
			payload.put("vote", String.valueOf(tagDto.getScore()));
		});

		GameTagDto topGameTag = res.stream().sorted(Comparator.comparing(GameTagDto::getScore).reversed()).limit(1).collect(Collectors.toList()).get(0);
		String currentTopTag = topGameTag.getContent();
		String currentWinner = topGameTag.getAuthorId();
		currentGame.setWinner(currentWinner);
		currentGame.setWinningTag(currentTopTag);
		payload.put("top", currentTopTag);
		payload.put("winner", currentWinner);
		//logger.debug("vote sending: {} {}", payload.keySet(), payload.values());
		this.broker.convertAndSend("/topic/votes", payload.values());

	}

	@MessageMapping("/review")
	@SendTo("/topic/review")
	public @ResponseBody void handleReview(@Payload Map<String, String> payload) {
		//logger.debug("Review received {}", payload.values());
		String gameId = payload.get("gameId");
		String voterId = payload.get("voterId");
		Object limit = payload.get("limit");
		int finalLimit = Integer.parseInt((String)limit);
		ClassificationGameDto currentGame = gamesNEW.get(gameId);
		List<GameTagDto> res = submittedTagsNEW.get(gameId);

		List<GameTagDto> topTags = res.stream().sorted((g1, g2) -> (int) g2.getScore()).limit(finalLimit)
				.collect(Collectors.toList());
		//payload.put("topTags", topTags);
		List<Map<String, String>> response = new ArrayList<>();
		for (GameTagDto gameTagDto : topTags) {
			Map<String, String> map = new LinkedHashMap<>();
			map.put("tag", gameTagDto.getContent());
			map.put("vote", String.valueOf(gameTagDto.getScore()));
			response.add(map);
		}
		GameTagDto topGameTag = res.stream().sorted(Comparator.comparing(GameTagDto::getScore).reversed()).limit(1).collect(Collectors.toList()).get(0);
		String currentTopTag = topGameTag.getContent();
		String currentWinner = topGameTag.getAuthorId();
		Map<String, String> map = new LinkedHashMap<>();
		map.put(currentWinner, currentTopTag);
		response.add(map);
		try {
			// Thread.sleep(1 * 60 *1000);
			Thread.sleep(1000); // TODO: use the above time
			//logger.debug("Review sending {} ", response);
			this.broker.convertAndSend("/topic/review", response);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@GetMapping("/api/services/ldod-game/leaderboard")
	public @ResponseBody ResponseEntity<?> getLeaderboard() {
		// logger.debug("get leaderboard");
		List<Object> response = new ArrayList<>();
		for(ClassificationGameDto g: gamesNEW.values()){
			List<String> users = g.getPlayersMap().entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).map(Map.Entry::getKey).collect(Collectors.toList());
			List<Double> scores = g.getPlayersMap().entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).map(Map.Entry::getValue).collect(Collectors.toList());
			response.add(users);
			response.add(scores);
		}
		return new ResponseEntity<>(response.toArray(), HttpStatus.OK);

	}
}
