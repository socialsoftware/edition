package pt.ist.socialsoftware.edition.ldod.domain;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VirtualModule extends VirtualModule_Base {
    private static final Logger log = LoggerFactory.getLogger(VirtualModule.class);

    public static VirtualModule getInstance() {
        return FenixFramework.getDomainRoot().getVirtualModule();
    }

    public VirtualModule() {
        FenixFramework.getDomainRoot().setVirtualModule(this);
        setLastTwitterID(new LastTwitterID()); // check if this is supposed to be here
    }

    public void remove() {
        getCitationSet().forEach(c -> c.remove());
        getTweetSet().forEach(t -> t.remove());
        getVirtualEditionsSet().forEach(v -> v.remove());
        getLastTwitterID().remove();
        setRoot(null);

        deleteDomainObject();
    }

    public VirtualEdition getVirtualEdition(String acronym) {
        for (VirtualEdition edition : getVirtualEditionsSet()) {
            if (edition.getAcronym().toUpperCase().equals(acronym.toUpperCase())) {
                return edition;
            }
        }

        return null;
    }

    public List<VirtualEdition> getVirtualEditionsUserIsParticipant(
            String user, LdoDSession session) {
        List<VirtualEdition> manageVE = new ArrayList<>();
        List<VirtualEdition> selectedVE = new ArrayList<>();
        List<VirtualEdition> mineVE = new ArrayList<>();
        List<VirtualEdition> publicVE = new ArrayList<>();

        session.synchronizeSession(user);

        if (user == null) {
            selectedVE.addAll(session.materializeVirtualEditions());
        }

        for (VirtualEdition virtualEdition : getVirtualEditionsSet()) {
            if (user != null
                    && virtualEdition.getSelectedBySet().stream().anyMatch(selectedBy -> selectedBy.getUser().equals(user))) {
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
    public VirtualEdition createVirtualEdition(String user, String acronym, String title, LocalDate date, boolean pub,
                                               String acronymOfUsed) {
        log.debug("createVirtualEdition user:{}, acronym:{}, title:{}", user, acronym, title);
        return new VirtualEdition(this, user, acronym, title, date, pub, acronymOfUsed);
    }

    @Atomic(mode = TxMode.WRITE)
    public RecommendationWeights createRecommendationWeights(String user, VirtualEdition virtualEdition) {
        return new RecommendationWeights(user, virtualEdition);
    }

    public VirtualEdition getArchiveEdition() {
        return getVirtualEditionsSet().stream().filter(ve -> ve.getAcronym().equals(ExpertEdition.ARCHIVE_EDITION_ACRONYM))
                .findAny().orElse(null);
    }

    public VirtualEdition getVirtualEditionByXmlId(String xmlId) {
        return getVirtualEditionsSet().stream().filter(ve -> ve.getXmlId().equals(xmlId)).findFirst().orElse(null);
    }

    public VirtualEditionInter getVirtualEditionInterByUrlId(String urlId) {
        return getVirtualEditionsSet().stream().map(virtualEdition -> virtualEdition.getFragInterByUrlId(urlId))
                .filter(Objects::nonNull).map(VirtualEditionInter.class::cast).findAny().orElse(null);
    }

    public VirtualEditionInter getVirtualEditionInterByXmlId(String xmlId) {
        return getVirtualEditionsSet().stream().map(virtualEdition -> virtualEdition.getFragInterByXmlId(xmlId))
                .filter(Objects::nonNull).map(VirtualEditionInter.class::cast).findAny().orElse(null);
    }

    public Set<VirtualEditionInter> getVirtualEditionInterSet() {
        return getVirtualEditionsSet().stream().flatMap(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters().stream()).collect(Collectors.toSet());
    }

    public Set<VirtualEditionInter> getVirtualEditionInterSet(String fragmentXmlId) {
        return getVirtualEditionInterSet().stream().filter(virtualEditionInter -> virtualEditionInter.getFragmentXmlId().equals(fragmentXmlId)).collect(Collectors.toSet());
    }

    public Set<TwitterCitation> getAllTwitterCitation() {
        // allTwitterCitations -> all twitter citations in the archive
        Set<TwitterCitation> allTwitterCitations = getCitationSet().stream().filter(TwitterCitation.class::isInstance)
                .map(TwitterCitation.class::cast).collect(Collectors.toSet());
        return allTwitterCitations;
    }

    @Atomic(mode = TxMode.WRITE)
    public static void deleteTweetCitationsWithoutInfoRangeOrTweet() {
        VirtualModule.getInstance().getAllTwitterCitation().stream()
                .filter(c -> c.getInfoRangeSet().isEmpty() || c.getTweetSet().isEmpty()).forEach(c -> c.remove());
    }

    @Atomic(mode = TxMode.WRITE)
    public static void deleteTweetsWithoutCitation() {
        VirtualModule.getInstance().getTweetSet().stream().filter(t -> t.getCitation() == null).forEach(t -> t.remove());
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
        return TextModule.getInstance().getFragmentsSet().stream().flatMap(f -> f.getCitationSet().stream()).collect(Collectors.toSet());
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

        return VirtualModule.getInstance().getCitationSet().stream().filter(c -> !c.getInfoRangeSet().isEmpty())
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
        getTweetSet().forEach(t -> t.remove());
    }

    @Atomic(mode = TxMode.WRITE)
    public void removeTweetsWithoutCitationsWithInfoRange() {
        getTweetSet().forEach(t -> {
            if (t.getCitation() == null || t.getCitation().getInfoRangeSet().isEmpty()) {
                t.remove();
            }
        });
    }

    public static String TWITTER_EDITION_ACRONYM = "VirtualModule-Twitter";
    public static int TWITTER_EDITION_DAYS = 30;

    @Atomic(mode = TxMode.WRITE)
    public static void dailyRegenerateTwitterCitationEdition() {
        VirtualEdition twitterEdition = VirtualModule.getInstance().getVirtualEdition(TWITTER_EDITION_ACRONYM);

        twitterEdition.getAllDepthVirtualEditionInters().stream().forEach(inter -> inter.remove());

        LocalDate editionBeginDateJoda = LocalDate.now().minusDays(TWITTER_EDITION_DAYS);
        if (twitterEdition.getTimeWindow() == null) {
            new TimeWindow(twitterEdition, editionBeginDateJoda, null);
        } else {
            twitterEdition.getTimeWindow().setBeginDate(editionBeginDateJoda);
        }

        LocalDateTime editionBeginDateTime = LocalDateTime.of(editionBeginDateJoda.getYear(),
                editionBeginDateJoda.getMonthOfYear(), editionBeginDateJoda.getDayOfMonth(), 0, 0);
        int number = 0;

        TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

        textProvidesInterface.getScholarInterDtoListTwitterEdition(editionBeginDateTime);

        for (ScholarInterDto scholarInterDto : textProvidesInterface.getScholarInterDtoListTwitterEdition(editionBeginDateTime)) {
            twitterEdition.createVirtualEditionInter(scholarInterDto, ++number);
        }
    }

    public List<VirtualEdition> getVirtualEditionsUserIsParticipant(String username) {
        return getVirtualEditionsSet().stream()
                .filter(virtualEdition -> virtualEdition.getParticipantSet().contains(username))
                .collect(Collectors.toList());
    }

    public List<VirtualEditionInter> getVirtualEditionIntersUserIsContributor(String username) {
        Set<VirtualEditionInter> virtualEditionIntersPublicAndParticipant =
                getVirtualEditionsSet().stream()
                        .filter(virtualEdition -> virtualEdition.getPub() || virtualEdition.getParticipantSet().contains(username))
                        .flatMap(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters().stream())
                        .collect(Collectors.toSet());

        Stream<VirtualEditionInter> virtualEditionIntersAnnotated = virtualEditionIntersPublicAndParticipant.stream()
                .filter(virtualEditionInter -> virtualEditionInter.getAnnotationSet().stream()
                        .anyMatch(annotation -> annotation.getUser().equals(username)));

        Stream<VirtualEditionInter> virtualEditionIntersTagged = virtualEditionIntersPublicAndParticipant.stream()
                .filter(virtualEditionInter -> virtualEditionInter.getTagSet().stream()
                        .anyMatch(tag -> tag.getContributor().equals(username)));

        return Stream.concat(virtualEditionIntersAnnotated, virtualEditionIntersTagged).distinct()
                .sorted(Comparator.comparing(VirtualEditionInter::getTitle)).collect(Collectors.toList());
    }

    public List<VirtualEdition> getPublicVirtualEditionsOrUserIsParticipant(String username) {
        return getVirtualEditionsSet().stream()
                .filter(virtualEdition -> virtualEdition.getPub() || virtualEdition.getParticipantSet().contains(username))
                .distinct()
                .sorted(Comparator.comparing(VirtualEdition::getTitle))
                .collect(Collectors.toList());
    }

    public Set<VirtualEdition> getSelectedVirtualEditionsByUser(String username) {
        return getVirtualEditionsSet().stream()
                .filter(virtualEdition -> virtualEdition.getSelectedBySet().stream().
                        anyMatch(selectedBy -> selectedBy.getUser().equals(username)))
                .collect(Collectors.toSet());
    }

}
