package pt.ist.socialsoftware.edition.ldod.dto;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.joda.time.DateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.controller.api.ClassificationGameController;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame;

public class ClassificationGameDto {
	private String gameExternalId;
	private String virtualEditionAcronym;
	private String virtualEditionTitle;
	private String description;
	private boolean openAnnotation;
	private boolean ended;
	private long dateTime;
	private VirtualEditionInterDto virtualEditionInterDto;
	private List<LdoDUserDto> members;
	private Map<String, List<GameTagDto>> submittedTags = new ConcurrentHashMap<>();
	private String winningTag;
	private String winner;
	private  Map<String, Double> players = new LinkedHashMap<>(100);

	public ClassificationGameDto(){

	}

	public ClassificationGameDto(ClassificationGame game) {
		setGameExternalId(game.getExternalId());

		setVirtualEditionAcronym(game.getVirtualEdition().getAcronym());
		setVirtualEditionTitle(game.getVirtualEdition().getTitle());
		setDescription(game.getDescription());
		setOpenAnnotation(game.getOpenAnnotation());
		setDateTime(game.getDateTime().getMillis());
		setVirtualEditionInterDto(new VirtualEditionInterDto(game.getVirtualEditionInter()));

		this.setMembers(game.getVirtualEdition().getActiveMemberSet().stream()
				.map(member -> new LdoDUserDto(member.getUser())).collect(Collectors.toList()));

	}

	public String getGameExternalId() {
		return this.gameExternalId;
	}

	public void setGameExternalId(String gameExternalId) {
		this.gameExternalId = gameExternalId;
	}

	public String getVirtualEditionAcronym() {
		return this.virtualEditionAcronym;
	}

	public void setVirtualEditionAcronym(String virtualEditionAcronym) {
		this.virtualEditionAcronym = virtualEditionAcronym;
	}

	public String getVirtualEditionTitle() {
		return this.virtualEditionTitle;
	}

	public void setVirtualEditionTitle(String virtualEditionTitle) {
		this.virtualEditionTitle = virtualEditionTitle;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isOpenAnnotation() {
		return this.openAnnotation;
	}

	public void setOpenAnnotation(boolean openAnnotation) {
		this.openAnnotation = openAnnotation;
	}

	public long getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	public VirtualEditionInterDto getVirtualEditionInterDto() {
		return this.virtualEditionInterDto;
	}

	public void setVirtualEditionInterDto(VirtualEditionInterDto virtualEditionInterDto) {
		this.virtualEditionInterDto = virtualEditionInterDto;
	}

	public List<LdoDUserDto> getMembers() {
		return this.members;
	}

	public void setMembers(List<LdoDUserDto> members) {
		this.members = members;
	}

	public String getWinningTag() {
		return this.winningTag;
	}

	public void setWinningTag(String winningTag) {
		this.winningTag = winningTag;
	}

	public String getWinner() {
		return this.winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public Map<String, Double> getPlayersMap() {
		return this.players;
	}

	public void setPlayersMap(HashMap<String,Double> players) {
		this.players = players;
	}

	public Set<String> getPlayers(){
		return this.players.keySet();
	}

	public void addPlayer(String player, double score){
		Double scoreObj = new Double(score);
		Double put = this.players.put(player, scoreObj);
	}

	public boolean updatePlayerScore(String player, Double score){
		if (this.players.containsKey(player)){
			this.players.put(player,this.players.get(player) + score);
			return true;
		}
		return false;

	}

	public void removePlayer(String player){
		this.players.remove(player);
	}

	public Map<String, List<GameTagDto>> getSubmittedTagsMap() {
		return submittedTags;
	}

	public void setSubmittedTags(HashMap<String,List<GameTagDto>> submittedTags) {
		this.submittedTags = submittedTags;
	}

	public boolean hasEnded() {
		return ended;
	}

	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	/*public void addTag(GameTagDto tag){
		if (this.submittedTags.containsKey(tag)) {
			return;
		}
		this.submittedTags.put(tag.getContent(), tag.getAuthorId());
	}*/

}
