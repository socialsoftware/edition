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
import pt.ist.socialsoftware.edition.ldod.api.ui.UiInterface;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.dto.MainFragmentDto;
import pt.ist.socialsoftware.edition.ldod.frontend.session.FrontendSession;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.HtmlWriter4Variations;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.utils.AnnotationDTO;
import pt.ist.socialsoftware.edition.ldod.utils.AnnotationSearchJson;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@SessionAttributes({"ldoDSession"})
@RequestMapping("/fragments")
public class FragmentController {
    private static final Logger logger = LoggerFactory.getLogger(FragmentController.class);

    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

    @ModelAttribute("ldoDSession")
    public FrontendSession getLdoDSession() {
        return FrontendSession.getLdoDSession();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getFragmentsList(Model model) {
        logger.debug("getFragmentsList");
        model.addAttribute("jpcEdition", TextModule.getInstance().getExpertEdition(ExpertEdition.COELHO_EDITION_ACRONYM));
        model.addAttribute("tscEdition", TextModule.getInstance().getExpertEdition(ExpertEdition.CUNHA_EDITION_ACRONYM));
        model.addAttribute("rzEdition", TextModule.getInstance().getExpertEdition(ExpertEdition.ZENITH_EDITION_ACRONYM));
        model.addAttribute("jpEdition", TextModule.getInstance().getExpertEdition(ExpertEdition.PIZARRO_EDITION_ACRONYM));
        model.addAttribute("fragments", TextModule.getInstance().getFragmentsSet());

        return "fragment/list";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}")
    public String getFragment(Model model, @PathVariable String xmlId) {
        Fragment fragment = TextModule.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            return "redirect:/error";
        } else {
            model.addAttribute("ldoD", VirtualModule.getInstance());
            model.addAttribute("text", TextModule.getInstance());
            model.addAttribute("user", User.getAuthenticatedUser());
            model.addAttribute("fragment", fragment);
            model.addAttribute("fragmentDto", new MainFragmentDto(fragment));
            model.addAttribute("inters", new ArrayList<ScholarInter>());
            model.addAttribute("uiInterface", new UiInterface());
            return "fragment/main";
        }
    }

    public FragmentController() {
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}")
    @PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
    public String getFragmentWithInterForUrlId(Model model, @ModelAttribute("ldoDSession") FrontendSession frontendSession,
                                               @PathVariable String xmlId, @PathVariable String urlId) {
        logger.debug("getFragmentWithInterForUrlId xmlId:{}, urlId:{} ", xmlId, urlId);

        Fragment fragment = TextModule.getInstance().getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return "redirect:/error";
        }

        PlainHtmlWriter4OneInter writer;
        ScholarInter scholarInter = fragment.getScholarInterByUrlId(urlId);
        if (scholarInter != null) {
            List<ScholarInter> inters = new ArrayList<>();
            writer = new PlainHtmlWriter4OneInter(scholarInter);
            inters.add(scholarInter);
            model.addAttribute("inters", inters);
        } else {
            List<VirtualEditionInter> inters = new ArrayList<>();
            VirtualEditionInter virtualEditionInter = VirtualModule.getInstance().getVirtualEditionInterByUrlId(urlId);
            if (virtualEditionInter == null) {
                return "redirect:/error";
            }
            writer = new PlainHtmlWriter4OneInter(virtualEditionInter.getLastUsed().getXmlId());
            inters.add(virtualEditionInter);
            model.addAttribute("inters", inters);

            // if it is a virtual interpretation check access and set session
            VirtualEdition virtualEdition = virtualEditionInter.getEdition();
            String user = this.userProvidesInterface.getAuthenticatedUser();
            if (virtualEdition.isPublicOrIsParticipant()) {
                if (!frontendSession.hasSelectedVE(virtualEdition.getAcronym())) {
                    frontendSession.toggleSelectedVirtualEdition(user, virtualEdition);
                }
            } else {
                // TODO: a userfriendly reimplementation
                throw new LdoDException("Não tem acesso a esta edição virtual");
            }
        }

        writer.write(false);


        model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("text", TextModule.getInstance());
        model.addAttribute("user", User.getAuthenticatedUser());
        model.addAttribute("fragment", fragment);
        model.addAttribute("writer", writer);
        model.addAttribute("uiInterface", new UiInterface());

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
    public String getTaxonomy(Model model, @ModelAttribute("ldoDSession") FrontendSession frontendSession,
                              @PathVariable String externalId) {

        VirtualEditionInter inter = FenixFramework.getDomainObject(externalId);

        if (inter == null) {
            return "redirect:/error";
        }

        VirtualEdition virtualEdition = inter.getEdition();

        String user = this.userProvidesInterface.getAuthenticatedUser();
        if (virtualEdition.isPublicOrIsParticipant()) {
            if (!frontendSession.hasSelectedVE(virtualEdition.getAcronym())) {
                frontendSession.toggleSelectedVirtualEdition(user, virtualEdition);
            }
        } else {
            // TODO: a userfriendly reimplementation
            throw new LdoDException("Não tem acesso a esta edição virtual");
        }

        List<VirtualEditionInter> inters = new ArrayList<>();
        inters.add(inter);
        model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("text", TextModule.getInstance());
        model.addAttribute("user", User.getAuthenticatedUser());
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

        Fragment fragment = FenixFramework.getDomainObject(externalId);

        List<ScholarInter> scholarInters = new ArrayList<>();
        List<VirtualEditionInter> virtualEditionInters = new ArrayList<>();
        if (intersID != null) {
            for (String interID : intersID) {
                Object inter = FenixFramework.getDomainObject(interID);
                if (inter != null && inter instanceof ScholarInter) {
                    scholarInters.add((ScholarInter) inter);
                }
                if (inter != null && inter instanceof VirtualEditionInter) {
                    virtualEditionInters.add((VirtualEditionInter) inter);
                }
            }
        }

        logger.debug("getInter scholarInters: {}, virtualEditionInters: {}", scholarInters.size(), virtualEditionInters.size());

        model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("text", TextModule.getInstance());
        model.addAttribute("user", User.getAuthenticatedUser());
        model.addAttribute("fragment", fragment);
        if (scholarInters.size() > 0) {
            model.addAttribute("inters", scholarInters);
        } else {
            model.addAttribute("inters", virtualEditionInters);
            scholarInters = virtualEditionInters.stream().map(virtualEditionInter -> TextModule.getInstance().getScholarInterByXmlId(virtualEditionInter.getLastUsed().getXmlId())).collect(Collectors.toList());
        }
        model.addAttribute("uiInterface", new UiInterface());

        if (scholarInters.size() == 1) {
            ScholarInter inter = scholarInters.get(0);
            PlainHtmlWriter4OneInter writer4One = new PlainHtmlWriter4OneInter(inter);
            writer4One.write(false);
            model.addAttribute("writer", writer4One);
        } else if (scholarInters.size() > 1) {
            HtmlWriter2CompInters writer = new HtmlWriter2CompInters(scholarInters);
            Boolean lineByLine = false;
            if (scholarInters.size() > 2) {
                lineByLine = true;
            }

            Map<ScholarInter, HtmlWriter4Variations> variations = new HashMap<>();
            for (ScholarInter inter : scholarInters) {
                variations.put(inter, new HtmlWriter4Variations(inter));
            }

            List<AppText> apps = new ArrayList<>();
            fragment.getTextPortion().putAppTextWithVariations(apps, scholarInters);
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
        ScholarInter inter = FenixFramework.getDomainObject(interID[0]);

        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);
        writer.write(displayDiff);

        List<ScholarInter> inters = new ArrayList<>();
        inters.add(inter);
        model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("text", TextModule.getInstance());
        model.addAttribute("inters", inters);
        model.addAttribute("writer", writer);

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
        SourceInter inter = FenixFramework.getDomainObject(interID[0]);
        PbText pbText = null;
        if (pbTextID != null && !pbTextID.equals("")) {
            pbText = FenixFramework.getDomainObject(pbTextID);
        }

        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);

        List<ScholarInter> inters = new ArrayList<>();
        inters.add(inter);
        model.addAttribute("inters", inters);
        model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("text", TextModule.getInstance());

        if (showFacs) {
            Surface surface = null;
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
        List<ScholarInter> inters = new ArrayList<>();
        for (String interID : intersID) {
            DomainObject inter = FenixFramework.getDomainObject(interID);
            if (inter instanceof ScholarInter) {
                inters.add((ScholarInter) inter);
            } else {
                inters.add(TextModule.getInstance().getScholarInterByXmlId(((VirtualEditionInter) inter).getLastUsed().getXmlId()));
            }
        }

        HtmlWriter2CompInters writer = new HtmlWriter2CompInters(inters);

        if (inters.size() > 2) {
            lineByLine = true;
        }
        writer.write(lineByLine, showSpaces);

        model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("text", TextModule.getInstance());
        model.addAttribute("fragment", inters.get(0).getFragment());
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

        for (Annotation annotation : inter.getAllDepthAnnotations()) {
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
        String user = this.userProvidesInterface.getAuthenticatedUser();

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
        String user = this.userProvidesInterface.getAuthenticatedUser();

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
        String user = this.userProvidesInterface.getAuthenticatedUser();

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
