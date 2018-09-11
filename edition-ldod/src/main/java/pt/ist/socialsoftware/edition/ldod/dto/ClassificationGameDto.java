package pt.ist.socialsoftware.edition.ldod.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.DateTime;

import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame;

public class ClassificationGameDto {
	private String gameExternalId;
	private String virtualEditionAcronym;
	private String virtualEditionTitle;
	private String description;
	private boolean openAnnotation;
	private DateTime dateTime;
	private VirtualEditionInterDto virtualEditionInterDto;
	private List<LdoDUserDto> members;

	private String tag;
	private String winner;
	private Set<String> players;

	public ClassificationGameDto(ClassificationGame game) {
		setGameExternalId(game.getExternalId());
		setVirtualEditionAcronym(game.getVirtualEdition().getAcronym());
		setVirtualEditionTitle(game.getVirtualEdition().getTitle());
		setDescription(game.getDescription());
		setOpenAnnotation(game.getOpenAnnotation());
		setDateTime(game.getDateTime());
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

	public DateTime getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(DateTime dateTime) {
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

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getWinner() {
		return this.winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public Set<String> getPlayers() {
		return this.players;
	}

	public void setPlayers(Set<String> players) {
		this.players = players;
	}

}
