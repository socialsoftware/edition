package pt.ist.socialsoftware.edition.game.domain;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.game.api.GameRequiresInterface;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;


import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ClassificationModule extends ClassificationModule_Base {
    private static final Logger logger = LoggerFactory.getLogger(ClassificationModule.class);
    public static String USER_ARS = "ars";


    public static ClassificationModule getInstance() {
        return FenixFramework.getDomainRoot().getClassificationModule();
    }
    
    public ClassificationModule() {
        FenixFramework.getDomainRoot().setClassificationModule(this);
        GameRequiresInterface.getInstance();
    }

    public void remove() {
        getClassificationGameSet().forEach(classificationGame -> classificationGame.remove());

        getPlayerSet().stream().forEach(player -> player.remove());

        setRoot(null);

        deleteDomainObject();
    }

    public Player getPlayerByUsername(String user) {
        return getPlayerSet().stream().filter(player -> player.getUser().equals(user)).findAny().orElse(null);
    }

    public Set<ClassificationGame> getClassificationGamesForEdition(String acronym){
        return getClassificationGameSet().stream().filter(g -> g.getEditionId().equals(acronym)).collect(Collectors.toSet());
    }

    public Set<ClassificationGame> getClassificationGamesForInter(String xmlId){
        return getClassificationGameSet().stream().filter(g -> g.getInterId().equals(xmlId)).collect(Collectors.toSet());
    }

    public Set<ClassificationGame> getPublicClassificationGames() {
        return getClassificationGameSet()
                .stream().filter(c -> c.getOpenAnnotation() && c.isActive()).collect(Collectors.toSet());
    }

    public Set<ClassificationGame> getActiveGames4User(String username) {
        //GameRequiresInterface gameRequiresInterface = new GameRequiresInterface();

        Set<VirtualEditionDto> virtualEditions4User = GameRequiresInterface.getInstance().getVirtualEditionUserIsParticipant(username);
        Set<String> virtualEditionIds = virtualEditions4User.stream().map(VirtualEditionDto::getAcronym)
                .collect(Collectors.toSet());
        Set<ClassificationGame> classificationGamesOfUser = ClassificationModule.getInstance().getClassificationGameSet().stream()
                .filter(classificationGame -> virtualEditionIds.contains(classificationGame.getEditionId()))
                .filter(ClassificationGame::isActive)
                .collect(Collectors.toSet());

        Set<ClassificationGame> allClassificationGames4User = new HashSet<>(getPublicClassificationGames());
        allClassificationGames4User.addAll(classificationGamesOfUser);
        return allClassificationGames4User;
    }

    public Map<String, Double> getOverallLeaderboard() {
        List<Map<String, Double>> collect = getClassificationGameSet().stream()
                .map(g -> g.getLeaderboard()).collect(Collectors.toList());
        Map<String, Double> result = new LinkedHashMap<>();
        for (Map<String, Double> m : collect) {
            for (Map.Entry<String, Double> e : m.entrySet()) {
                String key = e.getKey();
                Double value = result.get(key);
                result.put(key, value == null ? e.getValue() : e.getValue() + value);
            }
        }
        return result;
    }

    public int getOverallUserPosition(String username) {
        if (!getOverallLeaderboard().containsKey(username)) {
            return -1;
        }

        Map<String, Double> temp = getOverallLeaderboard().entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return new ArrayList<>(temp.keySet()).indexOf(username) + 1;
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void manageDailyClassificationGames(DateTime initialDate) {
       // GameRequiresInterface gameRequiresInterface = new GameRequiresInterface();

        String acronym = "VirtualModule-Jogo-Class";
        VirtualEditionDto virtualEdition = GameRequiresInterface.getInstance().getVirtualEdition(acronym);

        // generate daily games
        for (int i = 0; i < 96; i++) {
            int index = (int) Math.floor(Math.random() * GameRequiresInterface.getInstance().getVirtualEditionInterDtoSet(acronym).size());
            VirtualEditionInterDto inter = GameRequiresInterface.getInstance().getVirtualEditionInterDtoSet(acronym).stream()
                    .sorted((i1, i2) -> i1.getTitle().compareTo(i2.getTitle())).collect(Collectors.toList()).get(index);
            DateTime date = initialDate.plusMinutes(15 * (i + 48));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            String formatedDate = java.time.LocalDateTime.of(date.getYear(), date.getMonthOfYear(),
                    date.getDayOfMonth(), date.getHourOfDay(), date.getMinuteOfHour()).format(formatter);

           createClassificationGame(virtualEdition,"Jogo " + formatedDate, date, inter, USER_ARS);
        }

        Set<ClassificationGame> games = ClassificationModule.getInstance().getClassificationGameSet().stream()
                .filter(game -> game.getEditionId().equals(acronym)).collect(Collectors.toSet());

        // delete non-played games
        for (ClassificationGame game : games) {
            if (game.getDateTime().isBefore(initialDate) && game.canBeRemoved()) {
                game.remove();
            }
        }

    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void createClassificationGame(VirtualEditionDto virtualEdition, String description, DateTime date, VirtualEditionInterDto inter, String
            user) {
        new ClassificationGame(virtualEdition,description,date,inter,user);
    }

}
