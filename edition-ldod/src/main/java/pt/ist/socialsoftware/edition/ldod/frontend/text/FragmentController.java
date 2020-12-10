package pt.ist.socialsoftware.edition.ldod.frontend.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.dto.MainFragmentDto;
import pt.ist.socialsoftware.edition.ldod.frontend.user.session.FrontendSession;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.HtmlWriter4Variations;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.utils.AnnotationDTO;
import pt.ist.socialsoftware.edition.ldod.utils.AnnotationSearchJson;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@SessionAttributes({"frontendSession"})
@RequestMapping("/fragments")
public class FragmentController {
    private static final Logger logger = LoggerFactory.getLogger(FragmentController.class);

    private final FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();

    @ModelAttribute("frontendSession")
    public FrontendSession getFrontendSession() {
        return FrontendSession.getFrontendSession();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getFragmentsList(Model model) {
        logger.debug("getFragmentsList");
        model.addAttribute("jpcEdition", this.feTextRequiresInterface.getExpertEditionByAcronym(ExpertEdition.COELHO_EDITION_ACRONYM));
        model.addAttribute("tscEdition", this.feTextRequiresInterface.getExpertEditionByAcronym(ExpertEdition.CUNHA_EDITION_ACRONYM));
        model.addAttribute("rzEdition", this.feTextRequiresInterface.getExpertEditionByAcronym(ExpertEdition.ZENITH_EDITION_ACRONYM));
        model.addAttribute("jpEdition", this.feTextRequiresInterface.getExpertEditionByAcronym(ExpertEdition.PIZARRO_EDITION_ACRONYM));
        model.addAttribute("fragments", this.feTextRequiresInterface.getFragmentDtosWithScholarInterDtos());

        return "fragment/list";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}")
    public String getFragment(Model model, @PathVariable String xmlId) {
        Fragment fragment = TextModule.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            return "redirect:/error";
        } else {
            model.addAttribute("ldoD", VirtualModule.getInstance());
            model.addAttribute("expertEditions", this.feTextRequiresInterface.getSortedExpertEditionsDto());
            model.addAttribute("user", this.feTextRequiresInterface.getAuthenticatedUser());
            model.addAttribute("ldoDArchiveEdition", this.feTextRequiresInterface.getVirtualEditionByAcronym(VirtualEdition.ARCHIVE_EDITION_ACRONYM));
            model.addAttribute("fragment", fragment);
            model.addAttribute("fragmentDto", new MainFragmentDto(fragment));
            model.addAttribute("inters", new ArrayList<ScholarInterDto>());
            return "fragment/main";
        }
    }

    public FragmentController() {
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}")
    @PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
    public String getFragmentWithInterForUrlId(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                               @PathVariable String xmlId, @PathVariable String urlId) {
        logger.debug("getFragmentWithInterForUrlId xmlId:{}, urlId:{} ", xmlId, urlId);

        FragmentDto fragmentDto = this.feTextRequiresInterface.getFragmentByXmlId(xmlId);
        if (fragmentDto == null) {
            return "redirect:/error";
        }

        PlainHtmlWriter4OneInter writer;
        ScholarInterDto scholarInterDto = fragmentDto.getScholarInterByUrlId(urlId);
        if (scholarInterDto != null) {
            List<ScholarInterDto> inters = new ArrayList<>();
            writer = new PlainHtmlWriter4OneInter(scholarInterDto.getXmlId());
            inters.add(scholarInterDto);
            model.addAttribute("inters", inters);
        } else {
            List<VirtualEditionInterDto> inters = new ArrayList<>();
            VirtualEditionInterDto virtualEditionInterDto = this.feTextRequiresInterface.getVirtualEditionInterByUrlId(urlId);
            if (virtualEditionInterDto == null) {
                return "redirect:/error";
            }
            writer = new PlainHtmlWriter4OneInter(virtualEditionInterDto.getLastUsed().getXmlId());
            inters.add(virtualEditionInterDto);
            model.addAttribute("inters", inters);
        }

        writer.write(false);


        model.addAttribute("expertEditions", this.feTextRequiresInterface.getSortedExpertEditionsDto());
        model.addAttribute("user", this.feTextRequiresInterface.getAuthenticatedUser());
        model.addAttribute("ldoDArchiveEdition", this.feTextRequiresInterface.getVirtualEditionByAcronym(VirtualEdition.ARCHIVE_EDITION_ACRONYM));
        model.addAttribute("fragment", fragmentDto);
        model.addAttribute("writer", writer);

        return "fragment/main";
    }

    // added until the controller below is removed
    @ExceptionHandler({Exception.class})
    public String handleException(Exception ex) {
        return "redirect:/error";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/inter/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'fragInter.public')")
    public String getFragmentWithInter(Model model, @PathVariable String externalId) {
        logger.debug("getFragmentWithInter externalId:{}", externalId);

        DomainObject inter = FenixFramework.getDomainObject(externalId);

        if (inter == null) {
            return "redirect:/error";
        } else if (inter instanceof ScholarInter) {
            ScholarInter scholarInter = (ScholarInter) inter;
            return "redirect:/fragments/fragment/" + scholarInter.getFragment().getXmlId() + "/inter/" + scholarInter.getUrlId();
        } else {
            VirtualEditionInter virtualEditionInter = (VirtualEditionInter) inter;
            return "redirect:/fragments/fragment/" + virtualEditionInter.getFragmentXmlId() + "/inter/" + virtualEditionInter.getUrlId();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/inter/{externalId}/taxonomy")
    @PreAuthorize("hasPermission(#externalId, 'fragInter.public')")
    public String getTaxonomy(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                              @PathVariable String externalId) {

        VirtualEditionInterDto inter = this.feTextRequiresInterface.getVirtualEditionInterByExternalId(externalId);

        if (inter == null) {
            return "redirect:/error";
        }

        List<VirtualEditionInterDto> inters = new ArrayList<>();
        inters.add(inter);
        model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("expertEditions", this.feTextRequiresInterface.getSortedExpertEditionsDto());
        model.addAttribute("user", this.feTextRequiresInterface.getAuthenticatedUser());
        model.addAttribute("ldoDArchiveEdition", this.feTextRequiresInterface.getVirtualEditionByAcronym(VirtualEdition.ARCHIVE_EDITION_ACRONYM));
        model.addAttribute("inters", inters);

        return "fragment/taxonomy";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}/next")
    @PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
    public String getNextFragmentWithInter(Model model, @PathVariable String xmlId, @PathVariable String urlId) {
        Fragment fragment = TextModule.getInstance().getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return "redirect:/error";
        }

        ExpertEditionInter inter = (ExpertEditionInter) fragment.getScholarInterByUrlId(urlId);
        if (inter != null) {
            inter = inter.getNextNumberInter();

            return "redirect:/fragments/fragment/" + inter.getFragment().getXmlId() + "/inter/" + inter.getUrlId();
        }

        VirtualEditionInter virtualEditionInter = VirtualModule.getInstance().getVirtualEditionInterByUrlId(urlId);
        if (virtualEditionInter != null) {
            virtualEditionInter = virtualEditionInter.getNextNumberInter();

            return "redirect:/fragments/fragment/" + virtualEditionInter.getFragmentXmlId() + "/inter/" + virtualEditionInter.getUrlId();
        }
        return "redirect:/error";

    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}/prev")
    @PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
    public String getPrevFragmentWithInter(Model model, @PathVariable String xmlId, @PathVariable String urlId) {
        Fragment fragment = TextModule.getInstance().getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return "redirect:/error";
        }

        ExpertEditionInter inter = (ExpertEditionInter) fragment.getScholarInterByUrlId(urlId);
        if (inter != null) {
            inter = inter.getPrevNumberInter();

            return "redirect:/fragments/fragment/" + inter.getFragment().getXmlId() + "/inter/" + inter.getUrlId();
        }

        VirtualEditionInter virtualEditionInter = VirtualModule.getInstance().getVirtualEditionInterByUrlId(urlId);
        if (virtualEditionInter != null) {
            virtualEditionInter = virtualEditionInter.getPrevNumberInter();

            return "redirect:/fragments/fragment/" + virtualEditionInter.getFragmentXmlId() + "/inter/" + virtualEditionInter.getUrlId();
        }
        return "redirect:/error";
    }


    @RequestMapping(method = RequestMethod.GET, value = "/fragment/inter")
    public String getInter(Model model, @RequestParam(value = "fragment", required = true) String externalId,
                           @RequestParam(value = "inters[]", required = false) String[] intersID) {
        logger.debug("getInter externalId: {}, inters: {}", externalId, intersID);

        FragmentDto fragmentDto = this.feTextRequiresInterface.getFragmentByExternalId(externalId);

        List<ScholarInterDto> scholarInters = new ArrayList<>();
        List<VirtualEditionInterDto> virtualEditionInters = new ArrayList<>();
        if (intersID != null) {
            for (String interID : intersID) {
                ScholarInterDto scholarInterDto = this.feTextRequiresInterface.getScholarInterByExternalId(interID);
                if (scholarInterDto != null) {
                    scholarInters.add(scholarInterDto);
                } else {
                    VirtualEditionInterDto virtualEditionInterDto = this.feTextRequiresInterface.getVirtualEditionInterByExternalId(interID);
                    if (virtualEditionInterDto != null) {
                        virtualEditionInters.add(virtualEditionInterDto);
                    }
                }
            }
        }

        logger.debug("getInter scholarInters: {}, virtualEditionInters: {}", scholarInters.size(), virtualEditionInters.size());

        model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("expertEditions", this.feTextRequiresInterface.getSortedExpertEditionsDto());
        model.addAttribute("user", this.feTextRequiresInterface.getAuthenticatedUser());
        model.addAttribute("ldoDArchiveEdition", this.feTextRequiresInterface.getVirtualEditionByAcronym(VirtualEdition.ARCHIVE_EDITION_ACRONYM));
        model.addAttribute("fragment", fragmentDto);
        if (scholarInters.size() > 0) {
            model.addAttribute("inters", scholarInters);
        } else {
            model.addAttribute("inters", virtualEditionInters);
            scholarInters = virtualEditionInters.stream().map(virtualEditionInter -> virtualEditionInter.getLastUsed()).collect(Collectors.toList());
        }

        if (scholarInters.size() == 1) {
            ScholarInterDto inter = scholarInters.get(0);
            PlainHtmlWriter4OneInter writer4One = new PlainHtmlWriter4OneInter(inter.getXmlId());
            writer4One.write(false);
            model.addAttribute("writer", writer4One);
        } else if (scholarInters.size() > 1) {
            HtmlWriter2CompInters writer = new HtmlWriter2CompInters(scholarInters);
            Boolean lineByLine = false;
            if (scholarInters.size() > 2) {
                lineByLine = true;
            }

            Map<ScholarInterDto, HtmlWriter4Variations> variations = new HashMap<>();
            for (ScholarInterDto inter : scholarInters) {
                variations.put(inter, new HtmlWriter4Variations(inter.getXmlId()));
            }

            List<AppText> apps = new ArrayList<>();
            // TODO: remove this dependence
            Fragment fragment = FenixFramework.getDomainObject(externalId);
            fragment.getTextPortion().putAppTextWithVariations(apps,
                    scholarInters.stream().map(scholarInterDto -> TextModule.getInstance().getScholarInterByXmlId(scholarInterDto.getXmlId())).collect(Collectors.toList()));
            // TODO: remove this dependence

            Collections.reverse(apps);

            writer.write(lineByLine, false);
            model.addAttribute("lineByLine", lineByLine);
            model.addAttribute("writer", writer);
            model.addAttribute("variations", variations);
            model.addAttribute("apps", apps);
        }

        return "fragment/main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/inter/editorial")
    public String getInterEditorial(@RequestParam(value = "interp[]", required = true) String[] interID,
                                    @RequestParam(value = "diff", required = true) boolean displayDiff, Model model) {
        ScholarInterDto scholarInterDto = this.feTextRequiresInterface.getScholarInterByExternalId(interID[0]);

        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(scholarInterDto.getXmlId());
        writer.write(displayDiff);

        List<ScholarInterDto> inters = new ArrayList<>();
        inters.add(scholarInterDto);
        model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("expertEditions", this.feTextRequiresInterface.getSortedExpertEditionsDto());
        model.addAttribute("inters", inters);
        model.addAttribute("writer", writer);
        model.addAttribute("ldoDArchiveEdition", this.feTextRequiresInterface.getVirtualEditionByAcronym(VirtualEdition.ARCHIVE_EDITION_ACRONYM));


        return "fragment/transcription";

    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/inter/authorial")
    public String getInterAuthorial(@RequestParam(value = "interp[]", required = true) String[] interID,
                                    @RequestParam(value = "diff", required = true) boolean displayDiff,
                                    @RequestParam(value = "del", required = true) boolean displayDel,
                                    @RequestParam(value = "ins", required = true) boolean highlightIns,
                                    @RequestParam(value = "subst", required = true) boolean highlightSubst,
                                    @RequestParam(value = "notes", required = true) boolean showNotes,
                                    @RequestParam(value = "facs", required = true) boolean showFacs,
                                    @RequestParam(value = "pb", required = false) String pbTextID, Model model) {
        // TODO: to be removed
        SourceInter inter = FenixFramework.getDomainObject(interID[0]);

        ScholarInterDto scholarInterDto = this.feTextRequiresInterface.getScholarInterByExternalId(interID[0]);

        PbText pbText = null;
        if (pbTextID != null && !pbTextID.equals("")) {
            pbText = FenixFramework.getDomainObject(pbTextID);
        }

        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(scholarInterDto.getXmlId());

        List<ScholarInterDto> inters = new ArrayList<>();
        inters.add(scholarInterDto);
        model.addAttribute("inters", inters);
        model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("expertEditions", this.feTextRequiresInterface.getSortedExpertEditionsDto());
        model.addAttribute("ldoDArchiveEdition", this.feTextRequiresInterface.getVirtualEditionByAcronym(VirtualEdition.ARCHIVE_EDITION_ACRONYM));

        if (showFacs) {
            Surface surface;
            if (pbText == null) {
                surface = inter.getSource().getFacsimile().getFirstSurface();
            } else {
                surface = pbText.getSurface();
            }

            writer.write(displayDiff, displayDel, highlightIns, highlightSubst, showNotes, showFacs, pbText);
            model.addAttribute("surface", surface);
            model.addAttribute("prevsurface", inter.getPrevSurface(pbText));
            model.addAttribute("nextsurface", inter.getNextSurface(pbText));
            model.addAttribute("prevpb", inter.getPrevPbText(pbText));
            model.addAttribute("nextpb", inter.getNextPbText(pbText));
            model.addAttribute("writer", writer);

            return "fragment/facsimile";
        } else {
            writer.write(displayDiff, displayDel, highlightIns, highlightSubst, showNotes, showFacs, null);
            model.addAttribute("writer", writer);
            return "fragment/transcription";
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/inter/compare")
    public String getInterCompare(@RequestParam(value = "inters[]", required = true) String[] intersID,
                                  @RequestParam(value = "line") boolean lineByLine,
                                  @RequestParam(value = "spaces", required = true) boolean showSpaces, Model model) {
        List<ScholarInterDto> inters = new ArrayList<>();
        for (String interID : intersID) {
            ScholarInterDto scholarInterDto = this.feTextRequiresInterface.getScholarInterByExternalId(interID);
            if (scholarInterDto != null) {
                inters.add(scholarInterDto);
            } else {
                VirtualEditionInterDto virtualEditionInterDto = this.feTextRequiresInterface.getVirtualEditionInterByExternalId(interID);
                inters.add(virtualEditionInterDto.getLastUsed());
            }
        }

        HtmlWriter2CompInters writer = new HtmlWriter2CompInters(inters);

        if (inters.size() > 2) {
            lineByLine = true;
        }
        writer.write(lineByLine, showSpaces);

        model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("expertEditions", this.feTextRequiresInterface.getSortedExpertEditionsDto());
        model.addAttribute("ldoDArchiveEdition", this.feTextRequiresInterface.getVirtualEditionByAcronym(VirtualEdition.ARCHIVE_EDITION_ACRONYM));
        model.addAttribute("fragment", inters.get(0).getFragmentDto());
        model.addAttribute("lineByLine", lineByLine);
        model.addAttribute("inters", inters);
        model.addAttribute("writer", writer);

        if (lineByLine) {
            return "fragment/inter2CompareLineByLine";
        } else {
            return "fragment/inter2CompareSideBySide";
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/search")
    public @ResponseBody
    AnnotationSearchJson searchAnnotations(Model model, @RequestParam int limit,
                                           @RequestParam String uri) {
        // código alterado para funcionar com os dois tipos de anotações
        logger.debug("searchAnnotations uri: " + uri);
        List<AnnotationDTO> annotations = new ArrayList<>();

        VirtualEditionInter inter = FenixFramework.getDomainObject(uri);

        for (Annotation annotation : inter.getAllDepthAnnotationsAccessibleByUser(this.feTextRequiresInterface.getAuthenticatedUser())) {
            AnnotationDTO annotationJson = new AnnotationDTO(annotation);
            annotations.add(annotationJson);
        }

        return new AnnotationSearchJson(annotations);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fragment/annotations")
    public @ResponseBody
    ResponseEntity<AnnotationDTO> createAnnotation(Model model,
                                                   @RequestBody AnnotationDTO annotationJson, HttpServletRequest request) {
        VirtualEditionInter inter = FenixFramework.getDomainObject(annotationJson.getUri());
        VirtualEdition virtualEdition = (VirtualEdition) inter.getEdition();
        String user = this.feTextRequiresInterface.getAuthenticatedUser();

        HumanAnnotation annotation;
        if (HumanAnnotation.canCreate(virtualEdition, user)) {
            annotation = inter.createHumanAnnotation(annotationJson.getQuote(), annotationJson.getText(), user,
                    annotationJson.getRanges(), annotationJson.getTags());

            annotationJson.setId(annotation.getExternalId());

            return new ResponseEntity<>(new AnnotationDTO(annotation), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/annotations/{id}")
    public @ResponseBody
    ResponseEntity<AnnotationDTO> getAnnotation(Model model, @PathVariable String id) {

        HumanAnnotation annotation = FenixFramework.getDomainObject(id);
        if (annotation != null) {
            return new ResponseEntity<>(new AnnotationDTO(annotation), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/fragment/annotations/{id}")
    public @ResponseBody
    ResponseEntity<AnnotationDTO> updateAnnotation(Model model, @PathVariable String id,
                                                   @RequestBody AnnotationDTO annotationJson) {

        HumanAnnotation annotation = FenixFramework.getDomainObject(id);
        String user = this.feTextRequiresInterface.getAuthenticatedUser();

        if (annotation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (annotation.canUpdate(user)) {
            annotation.update(annotationJson.getText(), annotationJson.getTags());
            return new ResponseEntity<>(new AnnotationDTO(annotation), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/fragment/annotations/{id}")
    public @ResponseBody
    ResponseEntity<AnnotationDTO> deleteAnnotation(Model model, @PathVariable String id,
                                                   @RequestBody AnnotationDTO annotationJson) {

        HumanAnnotation annotation = FenixFramework.getDomainObject(id);
        String user = this.feTextRequiresInterface.getAuthenticatedUser();

        if (annotation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (annotation.canDelete(user)) {
            annotation.remove();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/annotation/{annotationId}/categories")
    public @ResponseBody
    ResponseEntity<String[]> getAnnotationInter(Model model, @PathVariable String annotationId) {

        HumanAnnotation annotation = FenixFramework.getDomainObject(annotationId);

        List<Category> listCategories = annotation.getCategories();

        String[] categories = listCategories.stream()
                .map(c -> c.getNameInEditionContext(annotation.getVirtualEditionInter().getVirtualEdition()))
                .toArray(size -> new String[size]);

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

}
