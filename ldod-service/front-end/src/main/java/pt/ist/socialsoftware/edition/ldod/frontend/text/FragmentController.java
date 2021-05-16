package pt.ist.socialsoftware.edition.ldod.frontend.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import pt.ist.socialsoftware.edition.ldod.frontend.text.textDto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.frontend.text.textDto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.frontend.text.textDto.SurfaceDto;
import pt.ist.socialsoftware.edition.ldod.frontend.user.session.FrontendSession;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.AnnotationDTO;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.AnnotationSearchJson;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.FeVirtualRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.AnnotationDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.HumanAnnotationDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionInterDto;


import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@SessionAttributes({"frontendSession"})
@RequestMapping("/fragments")
public class FragmentController {
    private static final Logger logger = LoggerFactory.getLogger(FragmentController.class);

    private static final String COELHO_EDITION_ACRONYM = "JPC";
    private static final String CUNHA_EDITION_ACRONYM = "TSC";
    private static final String ZENITH_EDITION_ACRONYM = "RZ";
    private static final String PIZARRO_EDITION_ACRONYM = "JP";
    private static final String ARCHIVE_EDITION_ACRONYM = "LdoD-Arquivo";

    private final FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();
    private final FeVirtualRequiresInterface feVirtualRequiresInterface = new FeVirtualRequiresInterface();

    @ModelAttribute("frontendSession")
    public FrontendSession getFrontendSession() {
        return FrontendSession.getFrontendSession();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getFragmentsList(Model model) {
        logger.debug("getFragmentsList");
        model.addAttribute("jpcEdition", this.feTextRequiresInterface.getExpertEditionByAcronym(COELHO_EDITION_ACRONYM));
        model.addAttribute("tscEdition", this.feTextRequiresInterface.getExpertEditionByAcronym(CUNHA_EDITION_ACRONYM));
        model.addAttribute("rzEdition", this.feTextRequiresInterface.getExpertEditionByAcronym(ZENITH_EDITION_ACRONYM));
        model.addAttribute("jpEdition", this.feTextRequiresInterface.getExpertEditionByAcronym(PIZARRO_EDITION_ACRONYM));
        model.addAttribute("fragments", this.feTextRequiresInterface.getFragmentDtosWithScholarInterDtos());

        return "fragment/list";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}")
    public String getFragment(Model model, @PathVariable String xmlId) {
        FragmentDto fragmentDto = this.feTextRequiresInterface.getFragmentByXmlId(xmlId);
//        logger.debug(fragmentDto.getClass().toString());
        if (fragmentDto == null) {
            return "redirect:/error";
        } else {
            logger.debug("JS");
           // model.addAttribute("ldoD", VirtualModule.getInstance());
            model.addAttribute("expertEditions", this.feTextRequiresInterface.getSortedExpertEditionsDto());
            model.addAttribute("user", this.feTextRequiresInterface.getAuthenticatedUser());
            model.addAttribute("ldoDArchiveEdition", this.feTextRequiresInterface.getVirtualEditionByAcronym(ARCHIVE_EDITION_ACRONYM));
            model.addAttribute("fragment", fragmentDto);
            model.addAttribute("fragmentDto", fragmentDto);
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
            logger.debug("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa\n");
            return "redirect:/error";
        }

        String writer;
        ScholarInterDto scholarInterDto = fragmentDto.getScholarInterByUrlId(urlId);
        if (scholarInterDto != null) {
            List<ScholarInterDto> inters = new ArrayList<>();
            writer = this.feTextRequiresInterface.getWriteFromPlainHtmlWriter4OneInter(scholarInterDto.getXmlId(), false);
            inters.add(scholarInterDto);
            model.addAttribute("inters", inters);
        } else {
            List<VirtualEditionInterDto> inters = new ArrayList<>();
            VirtualEditionInterDto virtualEditionInterDto = this.feTextRequiresInterface.getVirtualEditionInterByUrlId(urlId);
            if (virtualEditionInterDto == null) {
                logger.debug("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB\n");
                return "redirect:/error";
            }
            writer = this.feTextRequiresInterface.getWriteFromPlainHtmlWriter4OneInter(virtualEditionInterDto.getLastUsed().getXmlId(), false);
            inters.add(virtualEditionInterDto);
            model.addAttribute("inters", inters);
        }

        model.addAttribute("expertEditions", this.feTextRequiresInterface.getSortedExpertEditionsDto());
        model.addAttribute("user", this.feTextRequiresInterface.getAuthenticatedUser());
        model.addAttribute("ldoDArchiveEdition", this.feTextRequiresInterface.getVirtualEditionByAcronym(ARCHIVE_EDITION_ACRONYM));
        model.addAttribute("fragment", fragmentDto);
        model.addAttribute("writer", writer);
        System.out.println("BCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCBB\n");
        return "fragment/main";
    }

    // added until the controller below is removed
//    @ExceptionHandler({Exception.class})
//    public String handleException(Exception ex) {
//        return "redirect:/error";
//    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/inter/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'fragInter.public')")
    public String getFragmentWithInter(Model model, @PathVariable String externalId) {
        logger.debug("getFragmentWithInter externalId:{}", externalId);

        boolean isScholar = this.feTextRequiresInterface.isDomainObjectScholarInter(externalId);
        if (isScholar) {
            ScholarInterDto scholarInter = this.feTextRequiresInterface.getScholarInterByExternalId(externalId);
            return "redirect:/fragments/fragment/" + scholarInter.getFragmentXmlId() + "/inter/" + scholarInter.getUrlId();
        } else {
            VirtualEditionInterDto virtualEditionInter = this.feVirtualRequiresInterface.getVirtualEditionInterByExternalId(externalId);
            return virtualEditionInter == null ?  "redirect:/error" :  "redirect:/fragments/fragment/" + virtualEditionInter.getFragmentXmlId() + "/inter/" + virtualEditionInter.getUrlId();
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
        //model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("expertEditions", this.feTextRequiresInterface.getSortedExpertEditionsDto());
        model.addAttribute("user", this.feTextRequiresInterface.getAuthenticatedUser());
        model.addAttribute("ldoDArchiveEdition", this.feTextRequiresInterface.getVirtualEditionByAcronym(ARCHIVE_EDITION_ACRONYM));
        model.addAttribute("inters", inters);

        return "fragment/taxonomy";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}/next")
    @PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
    public String getNextFragmentWithInter(Model model, @PathVariable String xmlId, @PathVariable String urlId) {
        //Fragment fragment = TextModule.getInstance().getFragmentByXmlId(xmlId);
        FragmentDto fragment = this.feTextRequiresInterface.getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return "redirect:/error";
        }

       // ExpertEditionInter inter = (ExpertEditionInter) fragment.getScholarInterByUrlId(urlId);
        ScholarInterDto inter = fragment.getScholarInterByUrlId(urlId);
        if (inter != null) {
            inter = inter.getNextNumberInter();

//            return "redirect:/fragments/fragment/" + inter.getFragment().getXmlId() + "/inter/" + inter.getUrlId();
            return "redirect:/fragments/fragment/" + inter.getFragmentXmlId() + "/inter/" + inter.getUrlId();
        }

        //VirtualEditionInter virtualEditionInter = VirtualModule.getInstance().getVirtualEditionInterByUrlId(urlId);
        VirtualEditionInterDto virtualEditionInter = this.feVirtualRequiresInterface.getVirtualEditionInterByUrlId(urlId);
        if (virtualEditionInter != null) {
//            virtualEditionInter = virtualEditionInter.getNextNumberInter();
            virtualEditionInter = virtualEditionInter.getNextInter();

            return "redirect:/fragments/fragment/" + virtualEditionInter.getFragmentXmlId() + "/inter/" + virtualEditionInter.getUrlId();
        }
        return "redirect:/error";

    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}/prev")
    @PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
    public String getPrevFragmentWithInter(Model model, @PathVariable String xmlId, @PathVariable String urlId) {
//        Fragment fragment = TextModule.getInstance().getFragmentByXmlId(xmlId);
        FragmentDto fragment = this.feTextRequiresInterface.getFragmentByXmlId(xmlId);


        if (fragment == null) {
            return "redirect:/error";
        }

//        ExpertEditionInter inter = (ExpertEditionInter) fragment.getScholarInterByUrlId(urlId);
        ScholarInterDto inter = fragment.getScholarInterByUrlId(urlId);

        if (inter != null) {
            inter = inter.getPrevNumberInter();

            return "redirect:/fragments/fragment/" + inter.getFragmentXmlId() + "/inter/" + inter.getUrlId();
        }

//        VirtualEditionInter virtualEditionInter = VirtualModule.getInstance().getVirtualEditionInterByUrlId(urlId);
        VirtualEditionInterDto virtualEditionInter = this.feVirtualRequiresInterface.getVirtualEditionInterByUrlId(urlId);
        if (virtualEditionInter != null) {
            virtualEditionInter = virtualEditionInter.getPrevInter();

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

//        model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("expertEditions", this.feTextRequiresInterface.getSortedExpertEditionsDto());
        model.addAttribute("user", this.feTextRequiresInterface.getAuthenticatedUser());
        model.addAttribute("ldoDArchiveEdition", this.feTextRequiresInterface.getVirtualEditionByAcronym(ARCHIVE_EDITION_ACRONYM));
        model.addAttribute("fragment", fragmentDto);
        if (scholarInters.size() > 0) {
            model.addAttribute("inters", scholarInters);
        } else {
            model.addAttribute("inters", virtualEditionInters);
            scholarInters = virtualEditionInters.stream().map(virtualEditionInter -> virtualEditionInter.getLastUsed()).collect(Collectors.toList());
        }

        if (scholarInters.size() == 1) {
            ScholarInterDto inter = scholarInters.get(0);
//            PlainHtmlWriter4OneInter writer4One = new PlainHtmlWriter4OneInter(inter.getXmlId());
//            writer4One.write(false);
            String writer4One = this.feTextRequiresInterface.getWriteFromPlainHtmlWriter4OneInter(inter.getXmlId(), false);
            model.addAttribute("writer", writer4One);
        } else if (scholarInters.size() > 1) {
//            HtmlWriter2CompInters writer = new HtmlWriter2CompInters(scholarInters);

            Boolean lineByLine = false;
            if (scholarInters.size() > 2) {
                lineByLine = true;
            }

            Map<ScholarInterDto, FeTextRequiresInterface> variations = new HashMap<>();
            for (ScholarInterDto inter : scholarInters) {
                variations.put(inter, new FeTextRequiresInterface());
            }

            System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDD");
            List<String> apps = this.feTextRequiresInterface.putAppTextWithVariations(externalId, scholarInters.stream()
                    .map(scholarInterDto -> scholarInterDto.getXmlId()).collect(Collectors.toList()));
            System.out.println(apps);


//            writer.write(lineByLine, false);
//            String writer = this.feTextRequiresInterface.getWriteFromHtmlWriter2CompIntersLineByLine(scholarInters, lineByLine, false);
            model.addAttribute("showSpaces", false);
            model.addAttribute("lineByLine", lineByLine);
            model.addAttribute("writer", this.feTextRequiresInterface);
            model.addAttribute("variations", variations);
            model.addAttribute("apps", apps);
        }

        return "fragment/main";
    }



    @RequestMapping(method = RequestMethod.GET, value = "/fragment/inter/editorial")
    public String getInterEditorial(@RequestParam(value = "interp[]", required = true) String[] interID,
                                    @RequestParam(value = "diff", required = true) boolean displayDiff, Model model) {
        ScholarInterDto scholarInterDto = this.feTextRequiresInterface.getScholarInterByExternalId(interID[0]);

//        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(scholarInterDto.getXmlId());
//        writer.write(displayDiff);
        String writer = this.feTextRequiresInterface.getWriteFromPlainHtmlWriter4OneInter(scholarInterDto.getXmlId(), displayDiff);

        List<ScholarInterDto> inters = new ArrayList<>();
        inters.add(scholarInterDto);
 //       model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("expertEditions", this.feTextRequiresInterface.getSortedExpertEditionsDto());
        model.addAttribute("inters", inters);
        model.addAttribute("writer", writer);
        model.addAttribute("ldoDArchiveEdition", this.feTextRequiresInterface.getVirtualEditionByAcronym(ARCHIVE_EDITION_ACRONYM));


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

        ScholarInterDto scholarInterDto = this.feTextRequiresInterface.getScholarInterByExternalId(interID[0]);


        List<ScholarInterDto> inters = new ArrayList<>();
        inters.add(scholarInterDto);
        model.addAttribute("inters", inters);
//        model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("expertEditions", this.feTextRequiresInterface.getSortedExpertEditionsDto());
        model.addAttribute("ldoDArchiveEdition", this.feTextRequiresInterface.getVirtualEditionByAcronym(ARCHIVE_EDITION_ACRONYM));

        if (showFacs) {
            SurfaceDto surface = this.feTextRequiresInterface.getSurfaceFromPbTextId(pbTextID, interID[0]);


            String writer = this.feTextRequiresInterface.getWriteFromPlainHtmlWriter4OneInter(scholarInterDto.getXmlId(), displayDiff, displayDel, highlightIns, highlightSubst, showNotes, showFacs, pbTextID);
            model.addAttribute("surface", surface);
            model.addAttribute("prevsurface", this.feTextRequiresInterface.getPrevSurface(pbTextID, interID[0]));
            model.addAttribute("nextsurface", this.feTextRequiresInterface.getNextSurface(pbTextID, interID[0]));
            model.addAttribute("prevpb", this.feTextRequiresInterface.getPrevPbTextExternalId(pbTextID, interID[0]));
            model.addAttribute("nextpb", this.feTextRequiresInterface.getNextPbTextExternalId(pbTextID, interID[0]));
            model.addAttribute("writer", writer);

            return "fragment/facsimile";
        } else {
            String writer = this.feTextRequiresInterface.getWriteFromPlainHtmlWriter4OneInter(scholarInterDto.getXmlId(), displayDiff, displayDel, highlightIns, highlightSubst, showNotes, showFacs, null);
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
                //TODO temporary fix
                //   inters.add(virtualEditionInterDto.getLastUsed());
                inters.add(virtualEditionInterDto.getLastUsed());
            }
        }


        if (inters.size() > 2) {
            lineByLine = true;
        }

        this.feTextRequiresInterface.getWriteFromHtmlWriter2CompIntersLineByLine(inters, lineByLine, showSpaces);

//        model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("expertEditions", this.feTextRequiresInterface.getSortedExpertEditionsDto());
        model.addAttribute("ldoDArchiveEdition", this.feTextRequiresInterface.getVirtualEditionByAcronym(ARCHIVE_EDITION_ACRONYM));
        model.addAttribute("fragment", inters.get(0).getFragmentDto());
        model.addAttribute("lineByLine", lineByLine);
        model.addAttribute("inters", inters);
        model.addAttribute("writer", this.feTextRequiresInterface);
        model.addAttribute("showSpaces", showSpaces);


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

        VirtualEditionInterDto inter = this.feVirtualRequiresInterface.getVirtualEditionInterByExternalId(uri);

        for (AnnotationDto annotation : inter.getAllDepthAnnotationsAccessibleByUser(this.feTextRequiresInterface.getAuthenticatedUser())) {
            AnnotationDTO annotationJson = new AnnotationDTO(annotation);
            annotations.add(annotationJson);
        }

        return new AnnotationSearchJson(annotations);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fragment/annotations")
    public @ResponseBody
    ResponseEntity<AnnotationDTO> createAnnotation(Model model,
                                                   @RequestBody AnnotationDTO annotationJson, HttpServletRequest request) {

        VirtualEditionInterDto inter = this.feVirtualRequiresInterface.getVirtualEditionInterByExternalId(annotationJson.getUri());

        VirtualEditionDto virtualEdition = inter.getVirtualEditionDto();
        String user = this.feTextRequiresInterface.getAuthenticatedUser();

        HumanAnnotationDto annotationDto;
        if (this.feVirtualRequiresInterface.canManipulateAnnotation(virtualEdition.getAcronym(), user)) {
            annotationDto = this.feVirtualRequiresInterface.createHumanAnnotation(inter.getXmlId(), annotationJson.getQuote(), annotationJson.getText(), user,
                    annotationJson.getRanges(), annotationJson.getTags());
            System.out.println("bliblibliblb");
            annotationJson.setId(annotationDto.getExternalId());

            return new ResponseEntity<>(new AnnotationDTO(annotationDto), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/annotations/{id}")
    public @ResponseBody
    ResponseEntity<AnnotationDTO> getAnnotation(Model model, @PathVariable String id) {
        HumanAnnotationDto annotation = this.feVirtualRequiresInterface.getHumanAnnotationfromId(id);
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

        HumanAnnotationDto annotation = this.feVirtualRequiresInterface.getHumanAnnotationfromId(id);
        String user = this.feTextRequiresInterface.getAuthenticatedUser();

        if (annotation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (this.feVirtualRequiresInterface.canUserUpdateHumanAnnotation(id, user)) {
            annotation = this.feVirtualRequiresInterface.updateHumanAnnotation(id, annotationJson.getText(), annotationJson.getTags());
            return new ResponseEntity<>(new AnnotationDTO(annotation), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/fragment/annotations/{id}")
    public @ResponseBody
    ResponseEntity<AnnotationDTO> deleteAnnotation(Model model, @PathVariable String id,
                                                   @RequestBody AnnotationDTO annotationJson) {

        HumanAnnotationDto annotation = this.feVirtualRequiresInterface.getHumanAnnotationfromId(id);
        String user = this.feTextRequiresInterface.getAuthenticatedUser();

        if (annotation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (this.feVirtualRequiresInterface.canUserDeleteHumanAnnotation(id, user)) {
            this.feVirtualRequiresInterface.removeHumanAnnotation(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/annotation/{annotationId}/categories")
    public @ResponseBody
    ResponseEntity<String[]> getAnnotationInter(Model model, @PathVariable String annotationId) {

        HumanAnnotationDto annotation = this.feVirtualRequiresInterface.getHumanAnnotationfromId(annotationId);

      String[] categories = annotation.getTags().stream().map(tagDto -> tagDto.getNameInEdition()).toArray(size -> new String[size]);

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

}
