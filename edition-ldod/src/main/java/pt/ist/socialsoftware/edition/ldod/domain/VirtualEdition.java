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
import pt.ist.socialsoftware.edition.ldod.api.text.TextInterface;
import pt.ist.socialsoftware.edition.ldod.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.ldod.dto.WeightsDto;
import pt.ist.socialsoftware.edition.ldod.recommendation.VSMVirtualEditionInterRecommender;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateAcronymException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        if (getAcronym() != null && !getAcronym().toUpperCase().equals(acronym.toUpperCase()) || getAcronym() == null) {

            if (!acronym.matches("^[A-Za-z0-9\\-]+$")) {
                throw new LdoDException("acronym");
            }

            // cannot change acronym of the archive edition
            if (getAcronym() == null || !getAcronym().equals(ARCHIVE_EDITION_ACRONYM)) {

                TextInterface textInterface = new TextInterface();

                if (textInterface.acronymExists(acronym)) {
                    throw new LdoDDuplicateAcronymException();
                }

                for (VirtualEdition edition : LdoD.getInstance().getVirtualEditionsSet()) {
                    if (edition.getAcronym() != null && acronym.toUpperCase().equals(edition.getAcronym().toUpperCase())) {
                        throw new LdoDDuplicateAcronymException();
                    }
                }

                super.setAcronym(acronym);
            }
        }
    }

    @Override
    public String getXmlId() {
        return "ED.VIRT." + getAcronym();
    }

    public VirtualEdition(LdoD ldod, LdoDUser participant, String acronym, String title, LocalDate date, Boolean pub,
                          String acronymOfUsed) {
        setLdoD4Virtual(ldod);
        new Member(this, participant, Member.MemberRole.ADMIN, true);
        setXmlId("ED.VIRT." + acronym);
        setAcronym(acronym);
        setTitle(title);
        setDate(date);
        setPub(pub);
        setTaxonomy(new Taxonomy());
        createSection(Section.DEFAULT, 0);
        if (acronymOfUsed != null) {
            VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(acronymOfUsed);
            if (virtualEdition != null) {
                for (VirtualEditionInter inter : virtualEdition.getIntersSet()) {
                    createVirtualEditionInter(inter, inter.getNumber());
                }
            } else {
                TextInterface textInterface = new TextInterface();
                ExpertEdition expertEdition = textInterface.getExpertEdition(acronymOfUsed);
                for (ScholarInter inter : expertEdition.getIntersSet()) {
                    createVirtualEditionInter(inter, inter.getNumber());
                }
            }
        }
    }

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

        getClassificationGameSet().stream().forEach(g -> g.remove());

        getTaxonomy().remove();

        getMemberSet().stream().forEach(m -> m.remove());

        getCriteriaSet().stream().forEach(c -> c.remove());

        for (LdoDUser user : getSelectedBySet()) {
            removeSelectedBy(user);
        }

        for (Section section : getSectionsSet()) {
            section.remove();
        }

        for (VirtualEditionInter inter : getAllDepthVirtualEditionInters()) {
            inter.remove();
        }

        for (RecommendationWeights weights : getRecommendationWeightsSet()) {
            weights.remove();
        }

        deleteDomainObject();
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
        return getAllDepthVirtualEditionInters().stream().filter(i -> i.getFragment() == fragment).sorted().collect(Collectors.toList());
    }

    // determines if the fragment can have more interpretations for this virtual
    // edition, deals with the the case of a fragment having two interpretations
    // for the same expert edition
    public boolean canAddFragInter(ScholarInter addInter) {
        Fragment fragment = addInter.getFragment();

        for (VirtualEditionInter inter : getVirtualEditionInterSetForFragment(fragment)) {
            ScholarInter usedInter = inter.getLastUsed();
            if (isSameInterpretation((ScholarInter) addInter.getLastUsed(), usedInter)) {
                return false;
            }

            if (atLeastOneIsSourceInterpretation((ScholarInter) addInter.getLastUsed(), usedInter)) {
                return false;
            }

            if (belongToDifferentExpertEditions((ScholarInter) addInter.getLastUsed(), usedInter)) {
                return false;
            }
        }
        return true;
    }

    private boolean belongToDifferentExpertEditions(ScholarInter usedAddInter, ScholarInter usedInter) {
        ExpertEdition addExpertEdition = ((ExpertEditionInter) usedAddInter).getExpertEdition();
        ExpertEdition expertEdition = ((ExpertEditionInter) usedInter).getExpertEdition();
        return addExpertEdition != expertEdition;
    }

    private boolean atLeastOneIsSourceInterpretation(ScholarInter usedAddInter, ScholarInter usedInter) {
        return usedInter instanceof SourceInter || usedAddInter instanceof SourceInter;
    }

    private boolean isSameInterpretation(ScholarInter usedAddInter, ScholarInter usedInter) {
        return usedAddInter.getXmlId().equals(usedInter.getXmlId());
    }

    public int getMaxFragNumber() {
        int max = 0;
        for (VirtualEditionInter inter : getAllDepthVirtualEditionInters()) {
            max = inter.getNumber() > max ? inter.getNumber() : max;
        }

        return max;
    }

    public Set<VirtualEditionInter> getIntersSet() {
        return new HashSet<>(getAllDepthVirtualEditionInters());
    }

    public Set<VirtualEditionInter> getVirtualEditionInterSetForFragment(Fragment fragment) {
        return getAllDepthVirtualEditionInters().stream().filter(virtualEditionInter -> virtualEditionInter.getFragment() == fragment)
                .collect(Collectors.toSet());
    }

    public List<VirtualEditionInter> getSortedInterps() {
        return getIntersSet().stream().map(VirtualEditionInter.class::cast).sorted().collect(Collectors.toList());
    }

    public VirtualEditionInter getFragInterByUrlId(String urlId) {
        return getIntersSet().stream().filter(i -> i.getUrlId().equals(urlId)).findFirst().orElse(null);
    }

    public VirtualEditionInter getFragInterByXmlId(String xmlId) {
        return getIntersSet().stream().filter(i -> i.getXmlId().equals(xmlId)).findFirst().orElse(null);
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

        List<String> fragInterList = Arrays.stream(fraginters.trim().split(";")).map(item -> item.trim())
                .filter(item -> !item.equals("")).collect(Collectors.toList());
        List<String> newFragList = new ArrayList<>();
        List<String> actualFragList = new ArrayList<>();

        // inicializa lista de frags
        for (String temp : fragInterList) {
            ScholarInter inter = FenixFramework.getDomainObject(temp);

            // logger.debug("updateVirtualEditionInters temp:{} interLastUsed:{}
            // interTitle:{} interSourceType:{}", temp,
            // inter.getLastUsed().getExternalId(), inter.getTitle(),
            // inter.getSourceType());

            newFragList.add(inter.getLastUsed().getExternalId());
        }

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
                ScholarInter inter = FenixFramework.getDomainObject(fragInter);
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
    public VirtualEditionInter createVirtualEditionInter(VirtualEditionInter inter, int number) {
        // logger.debug("createVirtualEditionInter inter:{}, number:{}", inter, number);
        VirtualEditionInter virtualInter = null;

        if (canAddFragInter(inter.getLastUsed())) {
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

    // Default section
    @Atomic(mode = TxMode.WRITE)
    public VirtualEditionInter createVirtualEditionInter(ScholarInter inter, int number) {
        // logger.debug("createVirtualEditionInter inter:{}, number:{}", inter, number);
        VirtualEditionInter virtualInter = null;

        if (canAddFragInter((ScholarInter) inter.getLastUsed())) {
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

    public boolean checkAccess() {
        return getPub() || getParticipantSet().contains(LdoDUser.getAuthenticatedUser());
    }

    public List<VirtualEditionInter> getAllDepthVirtualEditionInters() {
        List<VirtualEditionInter> inters = new ArrayList<>();
        for (Section section : getSectionsSet()) {
            inters.addAll(section.getAllDepthVirtualEditionInterSet());
        }
        Collections.sort(inters);
        return inters;
    }

    @Atomic(mode = TxMode.WRITE)
    public Section createSection(String title, int number) {
        Section section = new Section(this, title, number);
        return section;
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
        getMemberSet().stream().filter(m -> m.getUser() == user).forEach(m -> m.remove());
        removeSelectedBy(user);
    }

    public Member getMember(LdoDUser user) {
        return getMemberSet().stream().filter(m -> m.getUser() == user).findFirst().orElse(null);
    }

    @Atomic(mode = TxMode.WRITE)
    public void addMember(LdoDUser user, Member.MemberRole role, boolean active) {
        if (!getMemberSet().stream().filter(m -> m.getUser() == user).findFirst().isPresent()) {
            new Member(this, user, role, active);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public void cancelParticipationSubmission(LdoDUser user) {
        Member member = getMemberSet().stream().filter(m -> !m.getActive() && m.getUser() == user).findFirst()
                .orElse(null);
        if (member != null) {
            member.remove();
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public void addApprove(LdoDUser user) {
        Member member = getMemberSet().stream().filter(m -> !m.getActive() && m.getUser() == user).findFirst()
                .orElse(null);
        if (member != null) {
            member.setActive(true);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public void switchRole(LdoDUser user) {
        Member member = getMemberSet().stream().filter(m -> m.getUser() == user).findFirst().orElse(null);
        if (member != null) {
            if (member.getRole().equals(Member.MemberRole.ADMIN)) {
                member.setRole(Member.MemberRole.MEMBER);
            } else {
                member.setRole(Member.MemberRole.ADMIN);
            }
        }
    }

    public Set<LdoDUser> getParticipantSet() {
        return getMemberSet().stream().filter(m -> m.getActive()).map(m -> m.getUser()).collect(Collectors.toSet());
    }

    public List<LdoDUser> getParticipantList() {
        return getParticipantSet().stream().sorted((u1, u2) -> u1.getFirstName().compareTo(u2.getFirstName()))
                .collect(Collectors.toList());
    }

    public Set<Member> getActiveMemberSet() {
        return getMemberSet().stream().filter(m -> m.getActive()).collect(Collectors.toSet());
    }

    public Set<LdoDUser> getAdminSet() {
        return getMemberSet().stream().filter(m -> m.getRole().equals(Member.MemberRole.ADMIN) && m.getActive())
                .map(m -> m.getUser()).collect(Collectors.toSet());
    }

    public Set<Member> getAdminMemberSet() {
        return getMemberSet().stream().filter(m -> m.getRole().equals(Member.MemberRole.ADMIN) && m.getActive())
                .collect(Collectors.toSet());
    }

    public Set<LdoDUser> getPendingSet() {
        return getMemberSet().stream().filter(m -> !m.getActive()).map(m -> m.getUser()).collect(Collectors.toSet());
    }

    public Set<Member> getPendingMemberSet() {
        return getMemberSet().stream().filter(m -> !m.getActive()).collect(Collectors.toSet());
    }

    public boolean canRemoveMember(LdoDUser actor, LdoDUser user) {
        Member.MemberRole roleActor = getMemberSet().stream().filter(m -> m.getUser() == actor).map(m -> m.getRole())
                .findFirst().get();

        if (roleActor.equals(Member.MemberRole.ADMIN) && getAdminMemberSet().size() > 1) {
            return true;
        }

        if (roleActor.equals(Member.MemberRole.ADMIN) && getAdminMemberSet().size() == 1 && actor != user) {
            return true;
        }

        if (roleActor.equals(Member.MemberRole.MEMBER) && actor == user) {
            return true;
        }

        return false;
    }

    public boolean canSwitchRole(LdoDUser actor, LdoDUser user) {
        Member.MemberRole roleActor = getMemberSet().stream().filter(m -> m.getUser() == actor).map(m -> m.getRole())
                .findFirst().get();

        if (roleActor.equals(Member.MemberRole.ADMIN) && getAdminMemberSet().size() > 1) {
            return true;
        }

        if (roleActor.equals(Member.MemberRole.ADMIN) && getAdminMemberSet().size() == 1 && actor != user) {
            return true;
        }

        return false;
    }

    public List<InterIdDistancePairDto> getIntersByDistance(VirtualEditionInter virtualEditionInter, WeightsDto weights) {
        List<VirtualEditionInter> inters = getAllDepthVirtualEditionInters();
        VSMVirtualEditionInterRecommender recommender = new VSMVirtualEditionInterRecommender();

        inters.remove(virtualEditionInter);

        List<InterIdDistancePairDto> recommendedEdition = new ArrayList<>();

        recommendedEdition.add(new InterIdDistancePairDto(virtualEditionInter.getExternalId(), 1.0d));
        List<Property> properties = weights.getProperties(virtualEditionInter.getVirtualEdition());
        for (VirtualEditionInter inter : inters) {
            recommendedEdition.add(new InterIdDistancePairDto(inter.getExternalId(),
                    recommender.calculateSimilarity(virtualEditionInter, inter, properties)));
        }

        return recommendedEdition.stream().sorted(Comparator.comparing(InterIdDistancePairDto::getDistance).reversed()).collect(Collectors.toList());
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
        while (usedEdition != null) {
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
                .map(a -> a.getText()).sorted().collect(Collectors.toList());
    }

    /**
     * Utility method that builds for this Virtual Edition it's corresponding List
     * of DTO fragments. Adapdted from VEController
     *
     * @return List of FragmentDTO of the Virtual Edition
     */
    public List<FragmentDto> buildEditionDTO() {
        List<FragmentDto> fragments = new ArrayList<>();
        String intersFilesPath = PropertiesManager.getProperties().getProperty("inters.dir");
        for (VirtualEditionInter inter : this.getAllDepthVirtualEditionInters()) {
            ScholarInter lastInter = inter.getLastUsed();
            String text;
            try {
                text = new String(Files.readAllBytes(Paths.get(intersFilesPath + lastInter.getExternalId() + ".txt")));
            } catch (IOException e) {
                throw new LdoDException("VirtualEditionController::getTranscriptions IOException");
            }

            FragmentDto fragment = new FragmentDto(inter, text);

            fragments.add(fragment);
        }
        return fragments;
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

    public boolean isLdoDEdition() {
        return getAcronym().equals(Edition.ARCHIVE_EDITION_ACRONYM);
    }

}