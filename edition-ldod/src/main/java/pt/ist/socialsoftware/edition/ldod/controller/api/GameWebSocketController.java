package pt.ist.socialsoftware.edition.ldod.controller.api;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.dto.APIResponse;
import pt.ist.socialsoftware.edition.ldod.dto.GameTagDto;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class GameWebSocketController {
    private static Logger logger = LoggerFactory.getLogger(GameWebSocketController.class);
    @Autowired
    private SimpMessagingTemplate broker;
    private Map<String, List<GameTagDto>> submittedTags = new HashMap<>();
    private Map<String, Integer> participants = new HashMap<>();

    @GetMapping("/api/services/ldod-game/ready/{user}/{acronym}")
    public @ResponseBody ResponseEntity<?> handleReady(@PathVariable(value = "user") String user, @PathVariable(value = "acronym") String acronym){
        VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(acronym);
        List<String> res = virtualEdition.getIntersSet().stream().sorted().map(FragInter::getUrlId).collect(Collectors.toList());
        for(String id: res){
            submittedTags.put(id, new ArrayList<>());
        }
        participants.put(user, 0);
        return new ResponseEntity<>(new APIResponse(true, "starting game"), HttpStatus.OK);

    }

    @MessageMapping("/start")
    @SendTo("/topic/config")
    public @ResponseBody void handleStart(@Payload Map<String,String> payload) {
        /* TODO: Queria chamar o método handleReady mas da erro se for um metodo da classe e nao um getMapping, coniderar portar
            algum codigo para o dominio
         */
        /*String user = payload.get("userId");
        String acronym = payload.get("virtualEdition");*/
        broker.convertAndSend("/topic/config", participants.size());
    }

    @GetMapping("/api/services/ldod-game/end")
    public @ResponseBody ResponseEntity<?> handleEnd() {
        logger.debug("end of game");
        Map<String, List<GameTagDto>> result = new HashMap<>();
        Set<String> fragmentsTagged = submittedTags.keySet();
        for(String fragment: fragmentsTagged){
            List<GameTagDto> top3Tags = submittedTags.get(fragment).stream().sorted(((g1, g2) -> g2.getScore())).limit(3).collect(Collectors.toList());

            result.put(fragment, top3Tags);
        }
        //return new ResponseEntity<>(new APIResponse(true, "ending game"), HttpStatus.OK);
        List<Object> response = new ArrayList<>();
        response.add(result);
        response.add(participants);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @MessageMapping("/tags")
    @SendTo("/topic/tags")
    public @ResponseBody void handleTags(@Payload Map<String, String> payload) {
        String urlId = payload.get("urlId");
        String authorId = payload.get("authorId");
        GameTagDto tag = new GameTagDto(authorId, urlId, payload.get("msg"), 1);
        List<GameTagDto> res = submittedTags.get(urlId);
        if(res.stream().noneMatch(t -> t.getContent().equals(tag.getContent()))){
            // if tag is submitted for the first we add it
            res.add(tag);
            submittedTags.put(urlId, res);
            participants.put(authorId, participants.get(authorId) + 1);
        }
        else{
            // if tag already exists increment score and update
            res.stream().filter(t -> t.getContent().equals(tag.getContent())).forEach(t->{
                t.setScore(tag.getScore()+t.getScore());
                payload.put("vote", String.valueOf(t.getScore()));
                // since the tag has been suggested, it now has a co-author / voter because of the suggestion
                t.addCoAuthor(authorId);
                t.addVoter(authorId);
                participants.put(authorId, participants.get(authorId) + 1);
            });

            submittedTags.put(urlId, res);
        }
        broker.convertAndSend("/topic/tags", payload.values());

    }

    @MessageMapping("/votes")
    @SendTo("/topic/votes")
    public @ResponseBody void handleVotes(@Payload Map<String,String> payload) {
        String urlId = payload.get("urlId");
        String voterId = payload.get("voterId");
        List<GameTagDto> res = submittedTags.get(urlId);
        String tagMsg = payload.get("msg");
        Object vote = payload.get("vote");
        res.stream().filter(t -> t.getContent().equals(tagMsg)).
                forEach(tagDto ->{
                    tagDto.setScore(tagDto.getScore() + (int) vote);
                    tagDto.addVoter(voterId);
                    participants.put(tagDto.getAuthorId(), participants.get(tagDto.getAuthorId()) + (int) vote);
                    payload.put("vote", String.valueOf(tagDto.getScore()));
                    logger.debug("tag: {} voters: {}", tagDto.getContent(), tagDto.getVoters().toArray());
                });

        broker.convertAndSend("/topic/votes", payload.values());

    }

    @GetMapping("/api/services/ldod-game/leaderboard")
    public @ResponseBody ResponseEntity<?> getLeaderboard() {
        logger.debug("get leaderboard");
        List<String> users = participants.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        List<Integer> scores = participants.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        List<Object> response = new ArrayList<>();
        response.add(users);
        response.add(scores);
        return new ResponseEntity<>(response.toArray(), HttpStatus.OK);
    }
}
