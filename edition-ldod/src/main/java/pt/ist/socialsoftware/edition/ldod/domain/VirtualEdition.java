package pt.ist.socialsoftware.edition.ldod.domain;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.ldod.dto.WeightsDto;
import pt.ist.socialsoftware.edition.ldod.recommendation.VSMVirtualEditionInterRecommender;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class VirtualEdition extends VirtualEdition_Base {
    private static final Logger logger = LoggerFactory.getLogger(VirtualEdition.class);

    public static String ACRONYM_PREFIX = "LdoD-";

    @Override
    public String getTitle() {
        return StringEscapeUtils.escapeHtml(super.getTitle());
    }

    @Override
    public String getSynopsis() {
        return StringEscapeUtils.escapeHtml(super.getSynopsis());
    }

    @Override
    public void setAcronym(String acronym) {
        if (!acronym.matches("^[A-Za-z0-9\\-]+$")) {
            throw new LdoDException("acronym");
        }

        // cannot change acronym of the archive edition
        if (getAcronym() == null || !getAcronym().equals(ARCHIVE_EDITION_ACRONYM)) {
            super.setAcronym(acronym);
        }
    }

    @Override
    public String getXmlId() {
        return "ED.VIRT." + getAcronym();
    }

    public VirtualEdition(LdoD ldod, LdoDUser participant, String acronym, String title, LocalDate date, Boolean pub,
                          Edition usedEdition) {
        setLdoD4Virtual(ldod);
        new Member(this, participant, Member.MemberRole.ADMIN, true);
        setXmlId("ED.VIRT." + acronym);
        setAcronym(acronym);
        setTitle(title);
        setDate(date);
        setPub(pub);
        setTaxonomy(new Taxonomy());
        createSection(Section.DEFAULT, 0);
        if (usedEdition != null) {
            for (FragInter inter : usedEdition.getIntersSet()) {
                createVirtualEditionInter(inter, inter.getNumber());
            }
        }
    }

    @Override
    @Atomic(mode = TxMode.WRITE)
    public void remove() {
        // delete directory and all its files if it exists
        String path = PropertiesManager.getProperties().getProperty("corpus.dir");
        File directory = new File(path + getExternalId());
        if (directory.exists()) {
            try {
                FileUtils.deleteDirectory(directory);
            } catch (IOException e) {
                // Unable to delete directory
                e.printStackTrace();
            }
        }

        setLdoD4Virtual(null);
        getClassificationGameSet().forEach(ClassificationGame::remove);
        getTaxonomy().remove();
        getMemberSet().forEach(Member::remove);
        getCriteriaSet().forEach(SocialMediaCriteria::remove);
        getSelectedBySet().forEach(this::removeSelectedBy);
        getSectionsSet().forEach(Section::remove);
        getAllDepthVirtualEditionInters().forEach(VirtualEditionInter::remove);
        getRecommendationWeightsSet().forEach(RecommendationWeights::remove);
        super.remove();
    }

    @Override
    public void setPub(Boolean pub) {
        if (!pub) {
            Set<LdoDUser> participants = getParticipantSet();
            for (LdoDUser user : getSelectedBySet()) {
                if (!participants.contains(user)) {
                    this.removeSelectedBy(user);
                }
            }
        }
        super.setPub(pub);
    }

    @Override
    public EditionType getSourceType() {
        return EditionType.VIRTUAL;
    }

    public List<VirtualEditionInter> getSortedInter4Frag(Fragment fragment) {
        List<VirtualEditionInter> interps = new ArrayList<>();

        for (FragInter inter : fragment.getFragmentInterSet()) {
            if (inter.getSourceType() == EditionType.VIRTUAL
                    && ((VirtualEditionInter) inter).getVirtualEdition() == this) {
                interps.add((VirtualEditionInter) inter);
            }
        }

        Collections.sort(interps);

        return interps;
    }

    public List<VirtualEditionInter> getSortedInter4VirtualFrag(Fragment fragment) {
        return fragment.getFragmentInterSet()
                .stream()
                .filter(fragInter -> fragInter.getSourceType().equals(EditionType.VIRTUAL) && ((VirtualEditionInter) fragInter).getVirtualEdition().equals(this))
                .map(fragInter -> ((VirtualEditionInter) fragInter))
                .sorted()
                .collect(Collectors.toList());

    }

    // determines if the fragment can have more interpretations for this virtual
    // edition, deals with the case of a fragment having two interpretations
    // for the same expert edition
    public Boolean canAddFragInter(FragInter addInter) {
        Fragment fragment = addInter.getFragment();
        FragInter usedAddInter = addInter.getLastUsed();
        for (VirtualEditionInter inter : fragment.getVirtualEditionInters(this)) {
            FragInter usedInter = inter.getLastUsed();
            if (isSameInterpretation(usedAddInter, usedInter)) {
                return false;
            }

            if (atLeastOneIsSourceInterpretation(usedAddInter, usedInter)) {
                return false;
            }

            if (belongToDifferentExpertEditions(usedAddInter, usedInter)) {
                return false;
            }

            ExpertEdition expertEdition = ((ExpertEditionInter) usedInter).getExpertEdition();
            int numberOfInter4Expert = fragment.getNumberOfInter4Edition(expertEdition);
            int numberOfInter4Virtual = fragment.getNumberOfInter4Edition(this);
            return numberOfInter4Expert > numberOfInter4Virtual;
        }
        return true;
    }

    private boolean belongToDifferentExpertEditions(FragInter usedAddInter, FragInter usedInter) {
        ExpertEdition addExpertEdition = ((ExpertEditionInter) usedAddInter).getExpertEdition();
        ExpertEdition expertEdition = ((ExpertEditionInter) usedInter).getExpertEdition();
        return addExpertEdition != expertEdition;
    }

    public boolean atLeastOneIsSourceInterpretation(FragInter usedAddInter, FragInter usedInter) {
        return usedInter instanceof SourceInter || usedAddInter instanceof SourceInter;
    }

    public boolean isSameInterpretation(FragInter usedAddInter, FragInter usedInter) {
        return usedAddInter == usedInter;
    }

    public int getMaxFragNumber() {
        int max = 0;
        for (FragInter inter : getAllDepthVirtualEditionInters()) {
            max = inter.getNumber() > max ? inter.getNumber() : max;
        }

        return max;
    }

    @Override
    public Set<FragInter> getIntersSet() {
        return new HashSet<>(getAllDepthVirtualEditionInters());
    }

    @Override
    public String getReference() {
        return getAcronym();
    }

    public String getShortAcronym() {
        return getAcronym().substring(ACRONYM_PREFIX.length());
    }

    // TODO corrigir o caso dos parâmetros vazios e também o new e o remove
    @Atomic(mode = TxMode.WRITE)
    public void edit(String acronym, String title, String synopsis, boolean pub, boolean openManagement,
                     boolean openVocabulary, boolean openAnnotation, String mediaSource, String beginDate, String endDate,
                     String geoLocation, String frequency) {
        setPub(pub);
        setTitle(title);
        if (synopsis.length() > 1500) {
            setSynopsis(synopsis.substring(0, 1499));
        } else {
            setSynopsis(synopsis);
        }
        setAcronym(acronym);
        getTaxonomy().edit(openManagement, openVocabulary, openAnnotation);

        MediaSource medSource = this.getMediaSource();
        // creates
        if (medSource == null) {
            if (mediaSource.equals("noMediaSource")) {
                // do nothing
            } else if (mediaSource.equals("Twitter")) {
                new MediaSource(this, mediaSource);
            }
        }
        // removes or edits
        else {
            if (mediaSource.equals("noMediaSource")) {
                medSource.remove();
            } else if (mediaSource.equals("Twitter")) {
                medSource.edit(mediaSource);
            }
        }

        TimeWindow timeWindow = this.getTimeWindow();
        LocalDate bDate = null;
        LocalDate eDate = null;
        // creates
        if (timeWindow == null) {
            if (!beginDate.equals("") || !endDate.equals("")) {
                if (!beginDate.equals("")) {
                    bDate = new LocalDate(beginDate);
                }
                if (!endDate.equals("")) {
                    eDate = new LocalDate(endDate);
                }
                new TimeWindow(this, bDate, eDate);
            }
        }
        // removes or edits
        else {
            if (beginDate.equals("") && endDate.equals("")) {
                timeWindow.remove();
            } else {
                if (!beginDate.equals("")) {
                    bDate = new LocalDate(beginDate);
                }
                if (!endDate.equals("")) {
                    eDate = new LocalDate(endDate);
                }
                timeWindow.edit(bDate, eDate);
            }
        }

        // remover o noCountry!! fazer a comparaçao com a String vazia
        GeographicLocation geographicLocation = this.getGeographicLocation();
        // creates
        if (geographicLocation == null) {
            if (geoLocation.equals("")) {
                // do nothing
            } else {
                new GeographicLocation(this, geoLocation);
            }
        }
        // removes or edits
        else {
            if (geoLocation.equals("")) {
                geographicLocation.remove();
            } else {
                geographicLocation.edit(geoLocation);
            }
        }

        Frequency freq = this.getFrequency();
        // creates
        if (freq == null) {
            if (frequency.equals("") || frequency.equals("0")) {
                // do nothing
            } else {
                new Frequency(this, Integer.parseInt(frequency));
            }
        }
        // removes or edits
        else {
            if (frequency.equals("") || frequency.equals("0")) {
                freq.remove();
            } else {
                freq.edit(Integer.parseInt(frequency));
            }
        }

        geographicLocation = this.getGeographicLocation();
        if (geographicLocation != null) {
            logger.debug(geographicLocation.getCountry());
            String[] split = geographicLocation.getCountry().split(",");
            logger.debug("size: " + split.length);
            for (String s : split) {
                logger.debug(s);
            }
        }

    }

    @Atomic(mode = TxMode.WRITE)
    public void updateVirtualEditionInters(String fraginters) {
        // logger.debug("updateVirtualEditionInters fragintters:{}", fraginters);

        List<String> fragInterList = Arrays
                .stream(fraginters.trim().split(";"))
                .map(String::trim)
                .filter(item -> !item.equals(""))
                .collect(Collectors.toList());


        // Frag inter initialization
        List<String> newFragList = fragInterList
                .stream()
                .map(id -> ((FragInter) FenixFramework.getDomainObject(id)).getLastUsed().getExternalId())
                .collect(Collectors.toList());

        List<String> actualFragList = getAllDepthVirtualEditionInters()
                .stream()
                .map(inter -> inter.getLastUsed().getExternalId())
                .collect(Collectors.toList());


        String fragVirtualInterId = "";
        // remove os fragmentos que não se encontram na nova lista
        for (VirtualEditionInter inter : getAllDepthVirtualEditionInters()) {

            System.out.println(inter.getExternalId() + " " + inter.getLastUsed().getExternalId() + " "
                    + inter.getTitle() + " " + inter.getNumber());

            // fragVirtualInterId = inter.getFragment().getExternalId();

            System.out.println("V1");
            String id = inter.getLastUsed().getExternalId();
            System.out.println("V2");
            actualFragList.add(id);

            if (!newFragList.contains(id)) {
                inter.remove();
            }
        }

        // adicionar os novos fragmentos
        /*
         * int i = 0; for (String fragId : newFragList) { if
         * (!actualFragList.contains(fragId)) { FragInter inter =
         * FenixFramework.getDomainObject(fragInterList .get(i)); VirtualEditionInter
         * addInter = createVirtualEditionInter(inter, i); } i++; }
         */

        System.out.println("V3");

        int i = 0;
        for (String fragInter : newFragList) {
            System.out.println("V4 " + fragInter);
            if (!actualFragList.contains(fragInter)) {
                System.out.println("V5 " + fragInter);
                FragInter inter = FenixFramework.getDomainObject(fragInter);
                createVirtualEditionInter(inter, i + 1);
                System.out.println("V6 " + fragInter);
            }
            i++;
        }

        System.out.println("VirtualEditionInter INDEX UPDATE -----------------");
        // actualiza indices dos fragmentos da edicao virtual
        int n = 0;
        for (VirtualEditionInter inter : getAllDepthVirtualEditionInters()) {

            fragVirtualInterId = inter.getLastUsed().getExternalId();

            if (newFragList.contains(fragVirtualInterId)) {
                n = newFragList.indexOf(fragVirtualInterId) + 1;
                inter.setNumber(n);
                System.out.println(inter.getTitle() + " " + n);
            } else {
                System.out.println("NOT " + inter.getTitle() + " " + inter.getNumber());
            }

        }

        System.out.println("UPDATE2");

        /*
         * int i = 1;
         *
         * StringTokenizer st = new StringTokenizer(fraginters, ";"); while
         * (st.hasMoreElements()) { String fraginterId = st.nextElement().toString(); if
         * (fraginterId.length() > 0) { FragInter inter =
         * FenixFramework.getDomainObject(fraginterId); VirtualEditionInter addInter =
         * createVirtualEditionInter(inter, i); i++; }
         *
         * }
         */

    }

    // Default section
    @Atomic(mode = TxMode.WRITE)
    public VirtualEditionInter createVirtualEditionInter(FragInter inter, int number) {
        // logger.debug("createVirtualEditionInter inter:{}, number:{}", inter, number);
        VirtualEditionInter virtualInter = null;

        if (canAddFragInter(inter)) {
            if (getSectionsSet().isEmpty()) {
                Section section = new Section(this, Section.DEFAULT, 0);
                virtualInter = new VirtualEditionInter(section, inter, number);
                section.addVirtualEditionInter(virtualInter);
                addSections(section);
            } else {
                Section section = getSectionsSet().iterator().next();
                virtualInter = new VirtualEditionInter(section, inter, number);
            }
        }

        return virtualInter;
    }

    @Atomic(mode = TxMode.WRITE)
    public VirtualEditionInter createVirtualEditionInter(Section section, FragInter inter, int number) {
        VirtualEditionInter virtualInter = null;
        if (canAddFragInter(inter)) {
            virtualInter = new VirtualEditionInter(section, inter, number);
            section.addVirtualEditionInter(virtualInter);
            addSections(section);
        }
        return virtualInter;
    }

    public boolean checkAccess() {
        return getPub() || getParticipantSet().contains(LdoDUser.getAuthenticatedUser());
    }

    public boolean checkAccess(LdoDUser user) {
        return getPub() || getParticipantSet().contains(user);
    }

    public Optional<VirtualEditionInter> getInterById(String externalId) {
        return getAllDepthVirtualEditionInters()
                .stream()
                .filter(inter -> inter.getExternalId().equals(externalId))
                .findFirst();
    }

    public List<VirtualEditionInter> getAllDepthVirtualEditionInters() {

        return getSectionsSet()
                .stream()
                .flatMap(section -> section.getAllDepthVirtualEditionInterSet().stream())
                .sorted()
                .collect(Collectors.toList());
    }

    @Atomic(mode = TxMode.WRITE)
    public Section createSection(String title, int number) {
        return new Section(this, title, number);
    }

    public boolean hasMultipleSections() {
        return getSectionsSet().size() > 1;
    }

    public Section getSection(String title) {
        for (Section section : getSectionsSet()) {
            if (section.getTitle().equals(title)) {
                return section;
            }
        }
        return null;
    }

    public Section createSection(String title) {
        int number = getSectionsSet().size();
        return createSection(title, number);
    }

    public int getSectionDepth() {
        int max = 0;
        for (Section section : getSectionsSet()) {
            int depth = section.getDepth();
            if (max < depth) {
                max = depth;
            }
        }
        return max;
    }

    public List<Section> getSortedSections() {
        List<Section> sortedList = new ArrayList<>(getSectionsSet());
        Collections.sort(sortedList);
        return sortedList;
    }

    @Atomic(mode = TxMode.WRITE)
    public void clearEmptySections() {
        for (Section section : getSectionsSet()) {
            section.clearEmptySections();
        }

        for (Section section : getSectionsSet()) {
            if (section.getAllDepthVirtualEditionInterSet().size() == 0) {
                section.remove();
            }
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public void removeMember(LdoDUser user) {
        getMemberSet().stream().filter(m -> m.getUser() == user).forEach(Member::remove);
        removeSelectedBy(user);
    }

    public Member getMember(LdoDUser user) {
        return getMemberSet().stream().filter(m -> m.getUser() == user).findFirst().orElse(null);
    }

    @Atomic(mode = TxMode.WRITE)
    public void addMember(LdoDUser user, Member.MemberRole role, boolean active) {
        if (getMemberSet().stream().noneMatch(m -> m.getUser() == user)) {
            new Member(this, user, role, active);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public void cancelParticipationSubmission(LdoDUser user) {
        getMemberSet().stream().filter(m -> !m.getActive() && m.getUser() == user).findFirst().ifPresent(Member::remove);
    }

    @Atomic(mode = TxMode.WRITE)
    public void addApprove(LdoDUser user) {
        getMemberSet().stream().filter(m -> !m.getActive() && m.getUser() == user).findFirst().ifPresent(member -> member.setActive(true));
    }

    @Atomic(mode = TxMode.WRITE)
    public void switchRole(LdoDUser user) {
        getMemberSet()
                .stream()
                .filter(m -> m.getUser() == user)
                .findFirst()
                .ifPresent(member -> {
                    if (member.getRole().equals(Member.MemberRole.ADMIN))
                        member.setRole(Member.MemberRole.MEMBER);
                    else
                        member.setRole(Member.MemberRole.ADMIN);
                });
    }

    public Set<LdoDUser> getParticipantSet() {
        return getMemberSet().stream().filter(Member_Base::getActive).map(Member_Base::getUser).collect(Collectors.toSet());
    }

    public List<LdoDUser> getParticipantList() {
        return getParticipantSet().stream().sorted(Comparator.comparing(LdoDUser_Base::getFirstName))
                .collect(Collectors.toList());
    }

    public Set<Member> getActiveMemberSet() {
        return getMemberSet().stream().filter(Member_Base::getActive).collect(Collectors.toSet());
    }

    public Set<LdoDUser> getAdminSet() {
        return getMemberSet().stream().filter(m -> m.getRole().equals(Member.MemberRole.ADMIN) && m.getActive())
                .map(Member_Base::getUser).collect(Collectors.toSet());
    }

    private Set<Member> getAdminMemberSet() {
        return getMemberSet().stream().filter(m -> m.getRole().equals(Member.MemberRole.ADMIN) && m.getActive())
                .collect(Collectors.toSet());
    }

    public Set<LdoDUser> getPendingSet() {
        return getMemberSet().stream().filter(m -> !m.getActive()).map(Member_Base::getUser).collect(Collectors.toSet());
    }

    public Set<Member> getPendingMemberSet() {
        return getMemberSet().stream().filter(m -> !m.getActive()).collect(Collectors.toSet());
    }

    public boolean canRemoveMember(LdoDUser actor, LdoDUser user) {
        if (getMember(actor).getRole().equals(Member.MemberRole.ADMIN)
                && (getAdminMemberSet().size() > 1
                || (getAdminMemberSet().size() == 1 && actor != user)))
            return true;
        return actor == user;
    }

    public boolean canSwitchRole(LdoDUser actor, LdoDUser user) {
        return getMember(actor).getRole().equals(Member.MemberRole.ADMIN)
                && (getAdminMemberSet().size() > 1
                || (getAdminMemberSet().size() == 1 && actor != user));
    }

    public List<InterIdDistancePairDto> getIntersByDistance(VirtualEditionInter virtualEditionInter, WeightsDto weights) {
        List<VirtualEditionInter> inters = getAllDepthVirtualEditionInters();
        VSMVirtualEditionInterRecommender recommender = new VSMVirtualEditionInterRecommender();

        inters.remove(virtualEditionInter);

        List<InterIdDistancePairDto> recommendedEdition = new ArrayList<>();

        List<Property> properties = weights.getProperties(virtualEditionInter.getVirtualEdition());
        for (VirtualEditionInter inter : inters) {
            recommendedEdition.add(new InterIdDistancePairDto(inter.getExternalId(),
                    recommender.calculateSimilarity(virtualEditionInter, inter, properties)));
        }

        recommendedEdition = recommendedEdition.stream().sorted(Comparator.comparing(InterIdDistancePairDto::getDistance).reversed()).collect(Collectors.toList());

        recommendedEdition.add(0, new InterIdDistancePairDto(virtualEditionInter.getExternalId(), 1.0d));

        return recommendedEdition;
    }

    public List<VirtualEditionInter> generateRecommendation(VirtualEditionInter inter,
                                                            RecommendationWeights recommendationWeights) {
        List<VirtualEditionInter> inters = getAllDepthVirtualEditionInters();
        VSMVirtualEditionInterRecommender recommender = new VSMVirtualEditionInterRecommender();

        inters.remove(inter);

        List<Property> properties = recommendationWeights.getPropertiesWithStoredWeights();
        List<VirtualEditionInter> recommendedEdition = new ArrayList<>();
        recommendedEdition.add(inter);
        recommendedEdition.addAll(recommender.getMostSimilarItemsAsList(inter, inters, properties));
        return recommendedEdition;
    }

    public Set<Category> getAllDepthCategories() {
        Set<Category> result = new HashSet<>(getTaxonomy().getCategoriesSet());

        Edition usedEdition = getUses();
        while (usedEdition instanceof VirtualEdition) {
            result.addAll(((VirtualEdition) usedEdition).getTaxonomy().getCategoriesSet());
            usedEdition = ((VirtualEdition) usedEdition).getUses();
        }

        return result;
    }

    public List<Category> getAllDepthSortedCategories() {
        return getAllDepthCategories().stream().sorted((c1, c2) -> c1.getName().compareTo(c2.getName()))
                .collect(Collectors.toList());
    }

    // Foi alterado por causa das human annotations
    public List<HumanAnnotation> getAnnotationList() {
        return getAllDepthVirtualEditionInters().stream().flatMap(i -> i.getAnnotationSet().stream())
                .filter(HumanAnnotation.class::isInstance).map(HumanAnnotation.class::cast)
                .collect(Collectors.toList());
    }

    public List<String> getAnnotationTextList() {
        return getAnnotationList().stream().filter(a -> a.getText() != null && !a.getText().isEmpty())
                .map(Annotation::getText).sorted().collect(Collectors.toList());
    }

    public boolean isSAVE() {
        // old version
        // if (!this.getCriteriaSet().isEmpty()) {
        // return true;
        // }
        // return false;

        // new version
        if (this.getMediaSource() != null && !this.getMediaSource().getName().equals("noMediaSource")) {
            return true;
        }
        return false;
    }

    public MediaSource getMediaSource() {
        for (SocialMediaCriteria criteria : this.getCriteriaSet()) {
            if (criteria instanceof MediaSource) {
                return (MediaSource) criteria;
            }
        }
        return null;
    }

    public TimeWindow getTimeWindow() {
        for (SocialMediaCriteria criteria : this.getCriteriaSet()) {
            if (criteria instanceof TimeWindow) {
                return (TimeWindow) criteria;
            }
        }
        return null;
    }

    public GeographicLocation getGeographicLocation() {
        for (SocialMediaCriteria criteria : this.getCriteriaSet()) {
            if (criteria instanceof GeographicLocation) {
                return (GeographicLocation) criteria;
            }
        }
        return null;
    }

    public Frequency getFrequency() {
        for (SocialMediaCriteria criteria : this.getCriteriaSet()) {
            if (criteria instanceof Frequency) {
                return (Frequency) criteria;
            }
        }
        return null;
    }

    @Atomic(mode = TxMode.WRITE)
    public void createClassificationGame(String description, DateTime date, VirtualEditionInter inter, LdoDUser user) {
        new ClassificationGame(this, description, date, inter, user);
    }

}