package pt.ist.socialsoftware.edition.ldod.domain;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.bff.user.dtos.SignupDto;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.SocialMediaService;
import pt.ist.socialsoftware.edition.ldod.domain.Role.RoleType;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateUsernameException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class LdoD extends LdoD_Base {
    public static String TWITTER_EDITION_ACRONYM = "LdoD-Twitter";
    public static int TWITTER_EDITION_DAYS = 30;
    private static Logger log = LoggerFactory.getLogger(LdoD.class);

    public LdoD() {
        FenixFramework.getDomainRoot().setLdoD(this);
        setNullEdition(new NullEdition());
        setLastTwitterID(new LastTwitterID()); // check if this is supposed to be here
    }

    public static LdoD getInstance() {
        return FenixFramework.getDomainRoot().getLdoD();
    }

    @Atomic(mode = TxMode.WRITE)
    public static void deleteTweetCitationsWithoutInfoRangeOrTweet() {
        LdoD.getInstance().getAllTwitterCitation().stream()
                .filter(c -> c.getInfoRangeSet().isEmpty() || c.getTweetSet().isEmpty()).forEach(c -> c.remove());
    }

    @Atomic(mode = TxMode.WRITE)
    public static void deleteTweetsWithoutCitation() {
        LdoD.getInstance().getTweetSet().stream().filter(t -> t.getCitation() == null).forEach(t -> t.remove());
    }

    @Atomic(mode = TxMode.WRITE)
    public static void dailyRegenerateTwitterCitationEdition() {
        VirtualEdition twitterEdition = LdoD.getInstance().getVirtualEdition(TWITTER_EDITION_ACRONYM);
        twitterEdition.getAllDepthVirtualEditionInters().forEach(VirtualEditionInter::remove);

        LocalDate editionBeginDateJoda = LocalDate.now().minusDays(TWITTER_EDITION_DAYS);
        if (twitterEdition.getTimeWindow() == null) {
            new TimeWindow(twitterEdition, editionBeginDateJoda, null);
        } else {
            twitterEdition.getTimeWindow().setBeginDate(editionBeginDateJoda);
        }

        LocalDateTime editionBeginDateTime = LocalDateTime.of(editionBeginDateJoda.getYear(),
                editionBeginDateJoda.getMonthOfYear(), editionBeginDateJoda.getDayOfMonth(), 0, 0);

        System.out.println(editionBeginDateJoda.toDate());
        AtomicInteger number = new AtomicInteger(0);


        LdoD.getInstance().getRZEdition().getExpertEditionIntersSet().stream()
                .filter(inter -> inter.getNumberOfTwitterCitationsSince(editionBeginDateTime) > 0)
                .sorted((inter1,
                         inter2) -> Math.toIntExact(inter2.getNumberOfTwitterCitationsSince(editionBeginDateTime)
                        - inter1.getNumberOfTwitterCitationsSince(editionBeginDateTime)))
                .forEach(inter -> twitterEdition.createVirtualEditionInter(inter, number.getAndAdd(1)));

    }


    @Atomic(mode = TxMode.WRITE)
    public static void manageDailyClassificationGames(DateTime initialDate) {
        LdoDUser ars = LdoD.getInstance().getUser("ars");
        VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition("LdoD-Jogo-Class");

        // generate daily games
        for (int i = 0; i < 96; i++) {
            int index = (int) Math.floor(Math.random() * virtualEdition.getAllDepthVirtualEditionInters().size());
            VirtualEditionInter inter = virtualEdition.getAllDepthVirtualEditionInters().stream()
                    .sorted((i1, i2) -> i1.getTitle().compareTo(i2.getTitle())).collect(Collectors.toList()).get(index);
            DateTime date = initialDate.plusMinutes(15 * (i + 48));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            String formatedDate = java.time.LocalDateTime.of(date.getYear(), date.getMonthOfYear(),
                    date.getDayOfMonth(), date.getHourOfDay(), date.getMinuteOfHour()).format(formatter);

            virtualEdition.createClassificationGame("Jogo " + formatedDate, date, inter, ars);
        }

        // delete non-played games
        for (ClassificationGame game : virtualEdition.getClassificationGameSet()) {
            if (game.getDateTime().isBefore(initialDate) && game.canBeRemoved()) {
                game.remove();
            }
        }

    }

    public void remove() {
        setRoot(null);
        getExpertEditionsSet().forEach(e -> e.remove());
        getRolesSet().forEach(r -> r.remove());
        getUsersSet().forEach(u -> u.remove());
        getCitationSet().forEach(c -> c.remove());
        getFragmentsSet().forEach(f -> f.remove());
        getHeteronymsSet().forEach(h -> h.remove());
        getNullEdition().remove();
        getPublicClassificationGames().forEach(g -> g.remove());
        getTokenSet().forEach(t -> t.remove());
        getUserConnectionSet().forEach(c -> c.remove());
        getTweetSet().forEach(t -> t.remove());
        getVirtualEditionsSet().forEach(v -> v.remove());
        getLastTwitterID().remove();

        deleteDomainObject();
    }

    public List<Heteronym> getSortedHeteronyms() {
        return getHeteronymsSet().stream().sorted((h1, h2) -> h1.getName().compareTo(h2.getName()))
                .collect(Collectors.toList());
    }

    public List<ExpertEdition> getSortedExpertEdition() {
        return getExpertEditionsSet().stream().sorted().collect(Collectors.toList());
    }

    public Edition getEdition(String acronym) {
        for (Edition edition : getExpertEditionsSet()) {
            if (edition.getAcronym().equalsIgnoreCase(acronym)) {
                return edition;
            }
        }

        return getVirtualEdition(acronym);
    }

    public ExpertEdition getExpertEdition(String acronym) {
        return getExpertEditionsSet()
                .stream()
                .filter(expertEdition -> expertEdition.getAcronym().equalsIgnoreCase(acronym))
                .findFirst().orElse(null);
    }

    public Optional<VirtualEdition> getOptionalVirtualEdition(String acronym) {
        return getVirtualEditionsSet()
                .stream()
                .filter(edition -> edition.getAcronym().equalsIgnoreCase(acronym))
                .findFirst();
    }

    public VirtualEdition getVirtualEdition(String acronym) {
        return getVirtualEditionsSet()
                .stream()
                .filter(virtualEdition -> virtualEdition.getAcronym().equalsIgnoreCase(acronym))
                .findFirst()
                .orElse(null);
    }

    public LdoDUser getUser(String username) {
        return getUsersSet()
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst().orElse(null);
    }


    public Fragment getFragmentByXmlId(String target) {
        for (Fragment fragment : getFragmentsSet()) {
            if (fragment.getXmlId().equals(target)) {
                return fragment;
            }
        }
        return null;
    }

    public List<VirtualEdition> getVirtualEditions4User(LdoDUser user, LdoDSession session) {
        List<VirtualEdition> manageVE = new ArrayList<>();
        List<VirtualEdition> selectedVE = new ArrayList<>();
        List<VirtualEdition> mineVE = new ArrayList<>();
        List<VirtualEdition> publicVE = new ArrayList<>();

        session.synchronizeSession(user);

        if (user == null) {
            selectedVE.addAll(session.materializeVirtualEditions());
        }

        for (VirtualEdition virtualEdition : getVirtualEditionsSet()) {
            if (user != null && virtualEdition.getSelectedBySet().contains(user)) {
                selectedVE.add(virtualEdition);
            } else if (virtualEdition.getParticipantSet().contains(user)) {
                mineVE.add(virtualEdition);
            } else if (virtualEdition.getPub() && !selectedVE.contains(virtualEdition)) {
                publicVE.add(virtualEdition);
            }
        }

        manageVE.addAll(selectedVE);
        manageVE.addAll(mineVE);
        manageVE.addAll(publicVE);

        return manageVE;
    }

    @Atomic(mode = TxMode.WRITE)
    public VirtualEdition createVirtualEdition(LdoDUser user, String acronym, String title, LocalDate date, boolean pub,
                                               Edition usedEdition) {
        log.debug("createVirtualEdition user:{}, acronym:{}, title:{}", user.getUsername(), acronym, title);
        return new VirtualEdition(this, user, acronym, title, date, pub, usedEdition);
    }

    @Atomic(mode = TxMode.WRITE)
    public RecommendationWeights createRecommendationWeights(LdoDUser user, VirtualEdition virtualEdition) {
        return new RecommendationWeights(user, virtualEdition);
    }

    @Atomic(mode = TxMode.WRITE)
    public void switchAdmin() {
        setAdmin(!getAdmin());
    }

    @Atomic(mode = TxMode.WRITE)
    public LdoDUser createUser(PasswordEncoder passwordEncoder, String username, String password, String firstName,
                               String lastName, String email, SocialMediaService socialMediaService, String socialMediaId) {

        removeOutdatedUnconfirmedUsers();

        if (getUser(username) == null) {
            LdoDUser user = new LdoDUser(this, username, passwordEncoder.encode(password), firstName, lastName, email);
            user.setSocialMediaService(socialMediaService);
            user.setSocialMediaId(socialMediaId);

            Role userRole = Role.getRole(RoleType.ROLE_USER);
            user.addRoles(userRole);
            System.out.println(user.getSocialMediaId());
            return user;
        } else {
            throw new LdoDDuplicateUsernameException(username);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public LdoDUser createUser(PasswordEncoder passwordEncoder, SignupDto signupDto) {
        removeOutdatedUnconfirmedUsers();

        if (getUser(signupDto.getUsername()) == null) {
            LdoDUser user = new LdoDUser(this, passwordEncoder.encode(signupDto.getPassword()), signupDto);
            Role userRole = Role.getRole(RoleType.ROLE_USER);
            user.addRoles(userRole);
            return user;
        } else {
            throw new LdoDDuplicateUsernameException(signupDto.getUsername());
        }
    }

    public UserConnection getUserConnection(String userId, String providerId, String providerUserId) {
        return getUserConnectionSet().stream().filter(uc -> uc.getUserId().equals(userId)
                        && uc.getProviderId().equals(providerId) && uc.getProviderUserId().equals(providerUserId)).findFirst()
                .orElse(null);
    }

    @Atomic(mode = TxMode.WRITE)
    public void createUserConnection(String userId, String providerId, String providerUserId, int rank,
                                     String displayName, String profileUrl, String imageUrl, String accessToken, String secret,
                                     String refreshToken, Long expireTime) {


        new UserConnection(this, userId, providerId, providerUserId, rank, displayName, profileUrl, imageUrl,
                accessToken, secret, refreshToken, expireTime);
    }

    public void createUserConnection(SignupDto signupDto) {
        createUserConnection(signupDto.getUsername(), signupDto.getProviderId(), signupDto.getSocialId(), signupDto.getRank(),
                signupDto.getDisplayName(), "", "", "", "", "", signupDto.getExpireTime());

    }


    public void removeOutdatedUnconfirmedUsers() {
        DateTime now = DateTime.now();
        getTokenSet().stream().filter(t -> t.getExpireTimeDateTime().isBefore(now)).map(RegistrationToken_Base::getUser)
                .forEach(LdoDUser::remove);
    }

    public RegistrationToken getTokenSet(String token) {
        return getTokenSet().stream().filter(t -> t.getToken().equals(token)).findFirst().orElse(null);
    }

    public Set<SourceInter> getFragmentRepresentatives() {
        return getFragmentsSet().stream().map(Fragment::getRepresentativeSourceInter).collect(Collectors.toSet());
    }

    public VirtualEdition getArchiveEdition() {
        return getVirtualEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.ARCHIVE_EDITION_ACRONYM))
                .findFirst().orElse(null);
    }

    public ExpertEdition getJPCEdition() {
        return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.COELHO_EDITION_ACRONYM))
                .findFirst().orElse(null);
    }

    public ExpertEdition getTSCEdition() {
        return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.CUNHA_EDITION_ACRONYM))
                .findFirst().orElse(null);
    }

    public ExpertEdition getRZEdition() {
        return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.ZENITH_EDITION_ACRONYM))
                .findFirst().orElse(null);
    }

    public ExpertEdition getJPEdition() {
        return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.PIZARRO_EDITION_ACRONYM))
                .findFirst().orElse(null);
    }

    public VirtualEdition getVirtualEditionByXmlId(String xmlId) {
        return getVirtualEditionsSet().stream().filter(ve -> ve.getXmlId().equals(xmlId)).findFirst().orElse(null);
    }

    @Atomic(mode = TxMode.WRITE)
    public void createTestUsers(PasswordEncoder passwordEncoder) {
        LdoD ldod = LdoD.getInstance();

        Role userRole = Role.getRole(RoleType.ROLE_USER);
        Role admin = Role.getRole(RoleType.ROLE_ADMIN);

        // the bcrypt generator
        // https://www.dailycred.com/blog/12/bcrypt-calculator
        for (int i = 0; i < 6; i++) {
            String username = "zuser" + Integer.toString(i + 1);
            if (LdoD.getInstance().getUser(username) == null) {
                LdoDUser user = new LdoDUser(ldod, username, passwordEncoder.encode(username), "zuser", "zuser",
                        "zuser" + Integer.toString(i + 1) + "@teste.pt");

                user.setEnabled(true);
                user.addRoles(userRole);
            }
        }
    }

    public Set<TwitterCitation> getAllTwitterCitation() {
        // allTwitterCitations -> all twitter citations in the archive
        return getCitationSet()
                .stream()
                .filter(TwitterCitation.class::isInstance)
                .map(TwitterCitation.class::cast).collect(Collectors.toSet());
    }

    public TwitterCitation getTwitterCitationByTweetID(long id) {
        TwitterCitation result = null;
        Set<TwitterCitation> allTwitterCitations = getAllTwitterCitation();
        for (TwitterCitation tc : allTwitterCitations) {
            if (tc.getTweetID() == id) {
                result = tc;
            }
        }
        return result;
    }

    public Tweet getTweetByTweetID(long id) {
        Tweet result = null;
        Set<Tweet> allTweets = getTweetSet();
        for (Tweet t : allTweets) {
            if (t.getTweetID() == id) {
                result = t;
            }
        }
        return result;
    }

    public boolean checkIfTweetExists(long id) {
        Set<Tweet> allTweets = getTweetSet();
        for (Tweet t : allTweets) {
            if (t.getTweetID() == id) {
                return true;
            }
        }
        return false;
    }

    public Set<Citation> getCitationSet() {
        return getFragmentsSet().stream().flatMap(f -> f.getCitationSet().stream()).collect(Collectors.toSet());
    }

    public Citation getCitationById(long id) {
        return getCitationSet().stream().filter(citation -> citation.getId() == id).findFirst().orElse(null);
    }

    public long getLastTwitterCitationId() {
        long res = 0;
        for (Citation c : this.getCitationSet()) {
            if (c instanceof TwitterCitation) {
                if (((TwitterCitation) c).getTweetID() > res) {
                    res = ((TwitterCitation) c).getTweetID();
                }
            }
        }
        return res;
    }

    public List<Citation> getCitationsWithInfoRanges() {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");

        return LdoD.getInstance().getCitationSet().stream().filter(c -> !c.getInfoRangeSet().isEmpty())
                .sorted((c1, c2) -> java.time.LocalDateTime.parse(c2.getDate(), formater)
                        .compareTo(java.time.LocalDateTime.parse(c1.getDate(), formater)))
                .collect(Collectors.toList());
    }

    public int getNumberOfCitationsWithInfoRanges() {
        return getCitationsWithInfoRanges().size();
    }

    @Atomic(mode = TxMode.WRITE)
    public void removeTweets() {
        getLastTwitterID().resetTwitterIDS();
        getTweetSet().forEach(Tweet::remove);
    }

    @Atomic(mode = TxMode.WRITE)
    public void removeTweetsWithoutCitationsWithInfoRange() {
        getTweetSet().forEach(t -> {
            if (t.getCitation() == null || t.getCitation().getInfoRangeSet().isEmpty()) {
                t.remove();
            }
        });
    }

    public List<VirtualEdition> getVirtualEditions4User(String username) {
        LdoDUser user = getUser(username);
        return LdoD.getInstance().getVirtualEditionsSet().stream()
                .filter(virtualEdition -> virtualEdition.getParticipantList().contains(user))
                .collect(Collectors.toList());

    }

    public Set<ClassificationGame> getPublicClassificationGames() {
        return getVirtualEditionsSet().stream().flatMap(virtualEdition -> virtualEdition.getClassificationGameSet()
                .stream().filter(c -> c.getOpenAnnotation() && c.isActive())).collect(Collectors.toSet());
    }

    public Set<ClassificationGame> getActiveGames4User(String username) {
        Set<VirtualEdition> virtualEditions4User = new HashSet<VirtualEdition>(getVirtualEditions4User(username));
        Set<ClassificationGame> classificationGamesOfUser = virtualEditions4User.stream()
                .flatMap(virtualEdition -> virtualEdition.getClassificationGameSet().stream()
                        .filter(ClassificationGame::isActive))
                .collect(Collectors.toSet());

        Set<ClassificationGame> allClassificationGames4User = new HashSet<>(getPublicClassificationGames());
        allClassificationGames4User.addAll(classificationGamesOfUser);
        return allClassificationGames4User;

    }

    public Map<String, Double> getOverallLeaderboard() {
        List<Map<String, Double>> collect = LdoD.getInstance().getVirtualEditionsSet().stream()
                .flatMap(v -> v.getClassificationGameSet().stream().map(g -> g.getLeaderboard()))
                .collect(Collectors.toList());
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

        return new ArrayList<String>(temp.keySet()).indexOf(username) + 1;
    }
}
