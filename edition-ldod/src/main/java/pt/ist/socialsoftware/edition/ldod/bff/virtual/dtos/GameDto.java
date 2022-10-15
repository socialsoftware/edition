package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import java.util.List;

public class GameDto {
    private boolean publicAnnotation;
    private List<VirtualEditionInterDto> inters;

    private List<ClassGameDto> games;

    public GameDto(boolean publicAnnotation, List<VirtualEditionInterDto> inters, List<ClassGameDto> games) {
        setInters(inters);
        setPublicAnnotation(publicAnnotation);
        setGames(games);
    }

    public boolean isPublicAnnotation() {
        return publicAnnotation;
    }

    public List<ClassGameDto> getGames() {
        return games;
    }

    public void setGames(List<ClassGameDto> games) {
        this.games = games;
    }

    public void setPublicAnnotation(boolean publicAnnotation) {
        this.publicAnnotation = publicAnnotation;
    }

    public List<VirtualEditionInterDto> getInters() {
        return inters;
    }

    public void setInters(List<VirtualEditionInterDto> inters) {
        this.inters = inters;
    }
}
