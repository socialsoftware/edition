package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.fragment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.FragInterRequestBodyDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.FragmentBodyDto;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.generators.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.ldod.generators.HtmlWriter4Variations;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;
import pt.ist.socialsoftware.edition.ldod.utils.AnnotationDTO;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/microfrontend/fragment")
public class MicrofrontendFragmentController {

    //////////////////////////// VIRTUAL //////////////////////////////////

    @RequestMapping(method = RequestMethod.POST, value = "/virtual/{xmlId}")
    public FragmentBodyDto getVirtualFragment(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String xmlId, @RequestBody ArrayList<String> selectedVE) {
        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
        if (fragment == null) return null;
        return currentUser != null
                ? new FragmentBodyDto(LdoD.getInstance(), currentUser.getUser(), fragment, new ArrayList<>(), selectedVE, "virtual")
                : new FragmentBodyDto(LdoD.getInstance(), null, fragment, new ArrayList<>(), selectedVE, "virtual");
    }



    @RequestMapping(method = RequestMethod.POST, value = "/virtual/{xmlId}/NoUser")
    public FragmentBodyDto getVirtualFragment(@PathVariable String xmlId, @RequestBody ArrayList<String> selectedVE) {
        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            return null;
        } else {
            return new FragmentBodyDto(LdoD.getInstance(), null, fragment, new ArrayList<FragInter>(), selectedVE, "virtual");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/virtual/{xmlId}/inter/{urlId}")
    @PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
    public FragmentBodyDto getVirtualFragmentWithInterForUrlId(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String xmlId, @PathVariable String urlId, @RequestBody ArrayList<String> selectedVE) {

        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            return null;
        }

        FragInter inter = fragment.getFragInterByUrlId(urlId);

        if (inter == null) {
            return null;
        }

        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getLastUsed());
        writer.write(false);

        boolean hasAccess = true;
        // if it is a virtual interpretation check access and set session
        LdoDUser user = currentUser.getUser();

        if (inter.getSourceType() == Edition.EditionType.VIRTUAL) {
            VirtualEdition virtualEdition = (VirtualEdition) inter.getEdition();


            if (!virtualEdition.checkAccess()) {
                hasAccess = false;
            }
        }

        List<FragInter> inters = new ArrayList<>();
        inters.add(inter);

        return new FragmentBodyDto(LdoD.getInstance(), user, fragment, inters, writer, hasAccess, selectedVE, "virtual");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/virtual/{xmlId}/inter/{urlId}/noUser")
    @PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
    public FragmentBodyDto getVirtualFragmentWithInterForUrlIdNoUser(@PathVariable String xmlId, @PathVariable String urlId, @RequestBody ArrayList<String> selectedVE) {

        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            return null;
        }

        FragInter inter = fragment.getFragInterByUrlId(urlId);

        if (inter == null) {
            return null;
        }

        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getLastUsed());
        writer.write(false);

        boolean hasAccess = true;
        // if it is a virtual interpretation check access and set session

        if (inter.getSourceType() == Edition.EditionType.VIRTUAL) {
            VirtualEdition virtualEdition = (VirtualEdition) inter.getEdition();


            if (!virtualEdition.checkAccess()) {
                hasAccess = false;
            }
        }

        List<FragInter> inters = new ArrayList<>();
        inters.add(inter);

        return new FragmentBodyDto(LdoD.getInstance(), null, fragment, inters, writer, hasAccess, selectedVE, "virtual");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/virtual/{xmlId}/inter/{urlId}/nextFrag")
    @PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
    public FragmentBodyDto getNextVirtualFragmentWithInter(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String xmlId, @PathVariable String urlId, @RequestBody ArrayList<String> selectedVE) {
        Fragment fragment = FenixFramework.getDomainRoot().getLdoD().getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return null;
        }

        FragInter inter = fragment.getFragInterByUrlId(urlId);
        if (inter == null) {
            return null;
        }

        Edition edition = inter.getEdition();
        inter = edition.getNextNumberInter(inter, inter.getNumber());

        if (currentUser == null) {
            return this.getVirtualFragmentWithInterForUrlIdNoUser(inter.getFragment().getXmlId(), inter.getUrlId(), selectedVE);
        }

        return this.getVirtualFragmentWithInterForUrlId(currentUser, inter.getFragment().getXmlId(), inter.getUrlId(), selectedVE);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/virtual/{xmlId}/inter/{urlId}/prevFrag")
    @PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
    public FragmentBodyDto getPrevVirtualFragmentWithInter(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String xmlId, @PathVariable String urlId, @RequestBody ArrayList<String> selectedVE) {


        Fragment fragment = FenixFramework.getDomainRoot().getLdoD().getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return null;
        }

        FragInter inter = fragment.getFragInterByUrlId(urlId);
        if (inter == null) {
            return null;
        }

        Edition edition = inter.getEdition();
        inter = edition.getPrevNumberInter(inter, inter.getNumber());

        if (currentUser == null) {
            return this.getVirtualFragmentWithInterForUrlIdNoUser(inter.getFragment().getXmlId(), inter.getUrlId(), selectedVE);
        }

        return this.getVirtualFragmentWithInterForUrlId(currentUser, inter.getFragment().getXmlId(), inter.getUrlId(), selectedVE);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/virtual/inter")
    public FragmentBodyDto getVirtualInter(@AuthenticationPrincipal LdoDUserDetails currentUser, @RequestParam(value = "fragment", required = true) String externalId, @RequestParam(value = "inters[]", required = false) String[] intersID, @RequestParam(value = "selectedVE", required = true) ArrayList<String> selectedVE) {

        Fragment fragment = FenixFramework.getDomainObject(externalId);

        List<FragInter> inters = new ArrayList<>();
        if (intersID != null) {
            for (String interID : intersID) {
                FragInter inter = (FragInter) FenixFramework.getDomainObject(interID);
                if (inter != null) {
                    inters.add(inter);
                }
            }
        }

        HtmlWriter2CompInters writer = null;
        PlainHtmlWriter4OneInter writer4One = null;
        Boolean lineByLine = false;
        Map<FragInter, HtmlWriter4Variations> variations = new HashMap<>();
        List<AppText> apps = new ArrayList<>();

        if (inters.size() == 1) {
            FragInter inter = inters.get(0);
            writer4One = new PlainHtmlWriter4OneInter(inter);
            writer4One.write(false);
        } else if (inters.size() > 1) {
            writer = new HtmlWriter2CompInters(inters);
            if (inters.size() > 2) {
                lineByLine = true;
            }


            for (FragInter inter : inters) {
                variations.put(inter, new HtmlWriter4Variations(inter));
            }

            inters.get(0).getFragment().getTextPortion().putAppTextWithVariations(apps, inters);
            Collections.reverse(apps);

            writer.write(lineByLine, false);
        }

        LdoDUser user = currentUser.getUser();

        return new FragmentBodyDto(LdoD.getInstance(), user, fragment, inters, writer, writer4One, variations, apps, selectedVE);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/virtual/inter/noUser")
    public FragmentBodyDto getVirtualInterNoUser(@RequestParam(value = "fragment", required = true) String externalId, @RequestParam(value = "inters[]", required = false) String[] intersID, @RequestParam(value = "selectedVE", required = true) ArrayList<String> selectedVE) {

        Fragment fragment = FenixFramework.getDomainObject(externalId);

        List<FragInter> inters = new ArrayList<>();
        if (intersID != null) {
            for (String interID : intersID) {
                FragInter inter = (FragInter) FenixFramework.getDomainObject(interID);
                if (inter != null) {
                    inters.add(inter);
                }
            }
        }

        HtmlWriter2CompInters writer = null;
        PlainHtmlWriter4OneInter writer4One = null;
        Boolean lineByLine = false;
        Map<FragInter, HtmlWriter4Variations> variations = new HashMap<>();
        List<AppText> apps = new ArrayList<>();

        if (inters.size() == 1) {
            FragInter inter = inters.get(0);
            writer4One = new PlainHtmlWriter4OneInter(inter);
            writer4One.write(false);
        } else if (inters.size() > 1) {
            writer = new HtmlWriter2CompInters(inters);
            if (inters.size() > 2) {
                lineByLine = true;
            }


            for (FragInter inter : inters) {
                variations.put(inter, new HtmlWriter4Variations(inter));
            }

            inters.get(0).getFragment().getTextPortion().putAppTextWithVariations(apps, inters);
            Collections.reverse(apps);

            writer.write(lineByLine, false);
        }


        return new FragmentBodyDto(LdoD.getInstance(), null, fragment, inters, writer, writer4One, variations, apps, selectedVE);

    }

    /////////////////////////////// EXPERT //////////////////////////////


    @RequestMapping(method = RequestMethod.POST, value = "/{xmlId}")
    public FragmentBodyDto getFragment(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String xmlId) {
        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            return null;
        } else {
            if (currentUser != null)
                return new FragmentBodyDto(LdoD.getInstance(), currentUser.getUser(), fragment, new ArrayList<FragInter>());
            else return new FragmentBodyDto(LdoD.getInstance(), null, fragment, new ArrayList<FragInter>());
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{xmlId}/NoUser")
    public FragmentBodyDto getFragment(@PathVariable String xmlId) {
        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            return null;
        } else {
            return new FragmentBodyDto(LdoD.getInstance(), null, fragment, new ArrayList<FragInter>());
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{xmlId}/inter/{urlId}")
    public FragmentBodyDto getFragmentWithInterForUrlId(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String xmlId, @PathVariable String urlId) {

        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            return null;
        }

        FragInter inter = fragment.getFragInterByUrlId(urlId);

        if (inter == null) {
            return null;
        }

        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getLastUsed());
        writer.write(false);

        boolean hasAccess = true;
        // if it is a virtual interpretation check access and set session
        LdoDUser user = currentUser.getUser();

        if (inter.getSourceType() == Edition.EditionType.VIRTUAL) {
            VirtualEdition virtualEdition = (VirtualEdition) inter.getEdition();


            if (!virtualEdition.checkAccess()) {
                hasAccess = false;
            }
        }

        List<FragInter> inters = new ArrayList<>();
        inters.add(inter);

        return new FragmentBodyDto(LdoD.getInstance(), user, fragment, inters, writer, hasAccess);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{xmlId}/inter/{urlId}/noUser")
    public FragmentBodyDto getFragmentWithInterForUrlIdNoUser(@PathVariable String xmlId, @PathVariable String urlId) {

        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            return null;
        }

        FragInter inter = fragment.getFragInterByUrlId(urlId);

        if (inter == null) {
            return null;
        }

        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getLastUsed());
        writer.write(false);

        boolean hasAccess = true;
        // if it is a virtual interpretation check access and set session

        if (inter.getSourceType() == Edition.EditionType.VIRTUAL) {
            VirtualEdition virtualEdition = (VirtualEdition) inter.getEdition();


            if (!virtualEdition.checkAccess()) {
                hasAccess = false;
            }
        }

        List<FragInter> inters = new ArrayList<>();
        inters.add(inter);

        return new FragmentBodyDto(LdoD.getInstance(), null, fragment, inters, writer, hasAccess);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{xmlId}/inter/{urlId}/nextFrag")
    public FragmentBodyDto getNextFragmentWithInter(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String xmlId, @PathVariable String urlId) {
        Fragment fragment = FenixFramework.getDomainRoot().getLdoD().getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return null;
        }

        FragInter inter = fragment.getFragInterByUrlId(urlId);
        if (inter == null) {
            return null;
        }

        Edition edition = inter.getEdition();
        inter = edition.getNextNumberInter(inter, inter.getNumber());

        if (currentUser == null) {
            return this.getFragmentWithInterForUrlIdNoUser(inter.getFragment().getXmlId(), inter.getUrlId());
        }

        return this.getFragmentWithInterForUrlId(currentUser, inter.getFragment().getXmlId(), inter.getUrlId());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{xmlId}/inter/{urlId}/prevFrag")
    public FragmentBodyDto getPrevFragmentWithInter(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String xmlId, @PathVariable String urlId) {


        Fragment fragment = FenixFramework.getDomainRoot().getLdoD().getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return null;
        }

        FragInter inter = fragment.getFragInterByUrlId(urlId);
        if (inter == null) {
            return null;
        }

        Edition edition = inter.getEdition();
        inter = edition.getPrevNumberInter(inter, inter.getNumber());

        if (currentUser == null) {
            return this.getFragmentWithInterForUrlIdNoUser(inter.getFragment().getXmlId(), inter.getUrlId());
        }

        return this.getFragmentWithInterForUrlId(currentUser, inter.getFragment().getXmlId(), inter.getUrlId());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/inter")
    public FragmentBodyDto getInter(@RequestParam(value = "fragment", required = true) String externalId, @RequestParam(value = "inters[]", required = false) String[] intersID) {

        Fragment fragment = FenixFramework.getDomainObject(externalId);

        List<FragInter> inters = new ArrayList<>();
        if (intersID != null) {
            for (String interID : intersID) {
                FragInter inter = FenixFramework.getDomainObject(interID);
                if (inter != null) {
                    inters.add(inter);
                }
            }
        }


        HtmlWriter2CompInters writer = null;
        PlainHtmlWriter4OneInter writer4One = null;
        Boolean lineByLine = false;
        Map<FragInter, HtmlWriter4Variations> variations = new HashMap<>();
        List<AppText> apps = new ArrayList<>();

        if (inters.size() == 1) {
            FragInter inter = inters.get(0);
            writer4One = new PlainHtmlWriter4OneInter(inter);
            writer4One.write(false);
        } else if (inters.size() > 1) {
            writer = new HtmlWriter2CompInters(inters);
            if (inters.size() > 2) {
                lineByLine = true;
            }


            for (FragInter inter : inters) {
                variations.put(inter, new HtmlWriter4Variations(inter));
            }

            inters.get(0).getFragment().getTextPortion().putAppTextWithVariations(apps, inters);
            Collections.reverse(apps);

            writer.write(lineByLine, false);
        }


        return new FragmentBodyDto(LdoD.getInstance(), LdoDUser.getAuthenticatedUser(), fragment, inters, writer, writer4One, variations, apps);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/inter/editorial")
    public FragmentBodyDto getInterEditorial(@RequestParam(value = "interp[]", required = true) String[] interID, @RequestParam(value = "diff", required = true) boolean displayDiff) {
        FragInter inter = FenixFramework.getDomainObject(interID[0]);

        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);
        writer.write(displayDiff);

        List<FragInter> inters = new ArrayList<>();
        inters.add(inter);

        return new FragmentBodyDto(inters, writer);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/inter/authorial")
    public FragmentBodyDto getInterAuthorial(@RequestParam(value = "interp[]", required = true) String[] interID, @RequestParam(value = "diff", required = true) boolean displayDiff, @RequestParam(value = "del", required = true) boolean displayDel, @RequestParam(value = "ins", required = true) boolean highlightIns, @RequestParam(value = "subst", required = true) boolean highlightSubst, @RequestParam(value = "notes", required = true) boolean showNotes, @RequestParam(value = "facs", required = true) boolean showFacs, @RequestParam(value = "pb", required = false) String pbTextID) {
        SourceInter inter = FenixFramework.getDomainObject(interID[0]);
        PbText pbText = null;
        if (pbTextID != null && !pbTextID.equals("")) {
            pbText = FenixFramework.getDomainObject(pbTextID);
        }

        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);

        List<FragInter> inters = new ArrayList<>();
        inters.add(inter);

        if (showFacs) {
            Surface surface = null;
            if (pbText == null) {
                surface = inter.getSource().getFacsimile().getFirstSurface();
            } else {
                surface = pbText.getSurface();
            }

            writer.write(displayDiff, displayDel, highlightIns, highlightSubst, showNotes, showFacs, pbText);
            return new FragmentBodyDto(inters, surface, pbText, writer, inter);

        } else {
            writer.write(displayDiff, displayDel, highlightIns, highlightSubst, showNotes, showFacs, null);
            return new FragmentBodyDto(inters, writer);
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/inter/compare")
    public FragmentBodyDto getInterCompare(@RequestParam(value = "inters[]", required = true) String[] intersID, @RequestParam(value = "line") boolean lineByLine, @RequestParam(value = "spaces", required = true) boolean showSpaces) {
        List<FragInter> inters = new ArrayList<>();
        for (String interID : intersID) {
            inters.add((FragInter) FenixFramework.getDomainObject(interID));
        }

        HtmlWriter2CompInters writer = new HtmlWriter2CompInters(inters);

        if (inters.size() > 2) {
            lineByLine = true;
        }
        writer.write(lineByLine, showSpaces);


        return new FragmentBodyDto(inters, writer);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/addinter/{veId}/{interId}")
    public ResponseEntity<Object> addInter(@PathVariable String veId, @PathVariable String interId) {
        VirtualEdition virtualEdition = FenixFramework.getDomainObject(veId);
        FragInter inter = FenixFramework.getDomainObject(interId);
        if (virtualEdition == null || inter == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        VirtualEditionInter addInter = virtualEdition.createVirtualEditionInter(inter, virtualEdition.getMaxFragNumber() + 1);

        if (addInter == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/virtual/no-auth/{xmlId}/inter/{urlId}")
    @PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
    public FragmentBodyDto getNoAuthVirtualFragmentInter(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String xmlId, @PathVariable String urlId, @RequestBody FragInterRequestBodyDto body) {
        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
        if (fragment == null) return null;
        FragInter inter = fragment.getFragInterByUrlId(urlId);
        if (inter == null) return null;

        return body.getInters().isEmpty()
            ? getFragmentBodyDto(body, inter, currentUser, fragment)
            : body.getInters().size() == 1
                ? getFragmentBodyDto(body, FenixFramework.getDomainObject(body.getInters().get(0)), currentUser, fragment)
                : getFragmentBodyDto4Compare(
                    body,
                    inter,
                    body.getInters()
                            .stream()
                            .map(id -> (FragInter) FenixFramework.getDomainObject(id))
                            .collect(Collectors.toList()),
                    currentUser,
                    fragment);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/virtual/no-auth/{xmlId}/inter/{urlId}/next")
    @PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
    public FragmentBodyDto getNextVirtualFragmentInter(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String xmlId, @PathVariable String urlId, @RequestBody FragInterRequestBodyDto body) {
        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
        if (fragment == null) return null;
        FragInter inter = fragment.getFragInterByUrlId(urlId);
        if (inter == null) return null;
        FragInter nextInter = inter.getEdition().getNextNumberInter(inter, inter.getNumber());
        Fragment nextFrag = nextInter.getFragment();

        return getFragmentBodyDto(body, nextInter, currentUser, nextFrag);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/virtual/no-auth/{xmlId}/inter/{urlId}/prev")
    @PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
    public FragmentBodyDto getPrevVirtualFragmentInter(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String xmlId, @PathVariable String urlId, @RequestBody FragInterRequestBodyDto body) {
        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
        if (fragment == null) return null;
        FragInter inter = fragment.getFragInterByUrlId(urlId);
        if (inter == null) return null;
        FragInter prevInter = inter.getEdition().getPrevNumberInter(inter, inter.getNumber());
        Fragment prevFrag = prevInter.getFragment();

        return getFragmentBodyDto(body, prevInter, currentUser, prevFrag);
    }

    private FragmentBodyDto getFragmentBodyDto4Compare(FragInterRequestBodyDto body, FragInter inter, List<FragInter> inters, LdoDUserDetails currentUser, Fragment fragment) {

        HtmlWriter2CompInters writer = new HtmlWriter2CompInters(inters);
        boolean lineByLine = inters.size() > 2 || body.isLine();
        writer.write(lineByLine, body.isAlign());
        Map<FragInter, HtmlWriter4Variations> variations = inters.stream().collect(Collectors.toMap(i -> i, HtmlWriter4Variations::new));
        List<AppText> apps = new ArrayList<>();
        fragment.getTextPortion().putAppTextWithVariations(apps, inters);
        Collections.reverse(apps);
        return currentUser != null
                ? new FragmentBodyDto(LdoD.getInstance(), currentUser.getUser(), fragment, inter, inters, writer, variations, apps, body.getSelectedVE(), "virtual")
                : new FragmentBodyDto(LdoD.getInstance(), null, fragment, inter, inters, writer, variations, apps, body.getSelectedVE(), "virtual");

    }

    private FragmentBodyDto getFragmentBodyDto(FragInterRequestBodyDto body, FragInter inter, LdoDUserDetails currentUser, Fragment fragment) {
        PbText pbText = body.getPbText() != null ? FenixFramework.getDomainObject(body.getPbText()) : null;

        Surface surface = body.isFac() ? pbText == null ? ((SourceInter) inter).getSource().getFacsimile().getFirstSurface() : pbText.getSurface() : null;

        List<AnnotationDTO> annotationDTOList = inter.getAllDepthHumanAnnotations().stream().map(AnnotationDTO::new).collect(Collectors.toList());

        // if it is a virtual interpretation check access and set session
        boolean hasAccess = inter.getSourceType() != Edition.EditionType.VIRTUAL || ((VirtualEdition) inter.getEdition()).checkAccess();

        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getLastUsed());
        writer.write(body.isDiff(), body.isDel(), body.isIns(), body.isSub(), body.isNote(), body.isFac(), pbText);

        return currentUser != null
                ? new FragmentBodyDto(LdoD.getInstance(), currentUser.getUser(), fragment, inter, surface, pbText, writer, hasAccess, body.getSelectedVE(), annotationDTOList, "virtual")
                : new FragmentBodyDto(LdoD.getInstance(), null, fragment, inter, surface, pbText, writer, hasAccess, body.getSelectedVE(), annotationDTOList, "virtual");
    }
}