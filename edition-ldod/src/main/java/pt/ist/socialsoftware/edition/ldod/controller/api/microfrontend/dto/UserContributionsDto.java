package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;

import java.util.List;
import java.util.stream.Collectors;

public class UserContributionsDto {


    private String userFirst;
    private String userLast;
    private String username;
    private List<PublicEditionListDto> publicEditionList;
    private List<GameDto> games;
    private double score;
    private int position;
    private int fragInterSize;
    private List<FragInterDto> fragInterSet;

    public UserContributionsDto(LdoDUser ldodUser) {
        this.setUserFirst(ldodUser.getFirstName());
        this.setUserLast(ldodUser.getLastName());
        this.setUsername(ldodUser.getUsername());
        this.setPublicEditionList(ldodUser.getPublicEditionList().stream().map(PublicEditionListDto::new).collect(Collectors.toList()));

        if (ldodUser.getPlayer() != null) {
            List<ClassificationGame> games = ldodUser.getPlayer().getClassificationGameParticipantSet().stream().map
                    (ClassificationGameParticipant::getClassificationGame).collect(Collectors.toList());
            this.setGames(games.stream().map(GameDto::new).collect(Collectors.toList()));
            this.setScore(ldodUser.getPlayer().getScore());
        }


        this.setPosition(LdoD.getInstance().getOverallUserPosition(ldodUser.getUsername()));
        this.setFragInterSize(ldodUser.getFragInterSet().size());
        this.setFragInterSet(ldodUser.getFragInterSet().stream().map(FragInter -> new FragInterDto(FragInter, ldodUser)).collect(Collectors.toList()));
    }

    public String getUserFirst() {
        return userFirst;
    }

    public void setUserFirst(String userFirst) {
        this.userFirst = userFirst;
    }

    public String getUserLast() {
        return userLast;
    }

    public void setUserLast(String userLast) {
        this.userLast = userLast;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<PublicEditionListDto> getPublicEditionList() {
        return publicEditionList;
    }

    public void setPublicEditionList(List<PublicEditionListDto> publicEditionList) {
        this.publicEditionList = publicEditionList;
    }

    public List<GameDto> getGames() {
        return games;
    }

    public void setGames(List<GameDto> games) {
        this.games = games;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getFragInterSize() {
        return fragInterSize;
    }

    public void setFragInterSize(int fragInterSize) {
        this.fragInterSize = fragInterSize;
    }

    public List<FragInterDto> getFragInterSet() {
        return fragInterSet;
    }

    public void setFragInterSet(List<FragInterDto> fragInterSet) {
        this.fragInterSet = fragInterSet;
    }
}
