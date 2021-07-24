package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.FragmentBodyDto;
import pt.ist.socialsoftware.edition.ldod.domain.AppText;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.PbText;
import pt.ist.socialsoftware.edition.ldod.domain.SourceInter;
import pt.ist.socialsoftware.edition.ldod.domain.Surface;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.generators.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.ldod.generators.HtmlWriter4Variations;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;

@RestController
@RequestMapping("/api/microfrontend/fragment")
public class MicrofrontendFragmentController {
	@RequestMapping(method = RequestMethod.GET, value = "/{xmlId}")
	public FragmentBodyDto getFragment(@PathVariable String xmlId) {
		Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);

		if (fragment == null) {
			return null;
		} else {
			return new FragmentBodyDto(LdoD.getInstance(), LdoDUser.getAuthenticatedUser(), fragment, new ArrayList<FragInter>());
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{xmlId}/inter/{urlId}")
	@PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
	public FragmentBodyDto getFragmentWithInterForUrlId(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String xmlId, @PathVariable String urlId, @RequestBody ArrayList<String> selectedVE) {

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

		return new FragmentBodyDto(LdoD.getInstance(), user, fragment, inters, writer, hasAccess, selectedVE);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{xmlId}/inter/{urlId}/noUser")
	@PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
	public FragmentBodyDto getFragmentWithInterForUrlIdNoUser(@PathVariable String xmlId, @PathVariable String urlId, @RequestBody ArrayList<String> selectedVE) {

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

		return new FragmentBodyDto(LdoD.getInstance(), null, fragment, inters, writer, hasAccess, selectedVE);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{xmlId}/inter/{urlId}/nextFrag")
	@PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
	public FragmentBodyDto getNextFragmentWithInter(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String xmlId, @PathVariable String urlId, @RequestBody ArrayList<String> selectedVE) {
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
		
		if(currentUser == null) {
			System.out.println("null");
			return this.getFragmentWithInterForUrlIdNoUser(inter.getFragment().getXmlId(), inter.getUrlId(), selectedVE);
		}

		return this.getFragmentWithInterForUrlId(currentUser, inter.getFragment().getXmlId(), inter.getUrlId(), selectedVE);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{xmlId}/inter/{urlId}/prevFrag")
	@PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
	public FragmentBodyDto getPrevFragmentWithInter(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String xmlId, @PathVariable String urlId, @RequestBody ArrayList<String> selectedVE) {
		
		System.out.println(currentUser);
		
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
		
		if(currentUser == null) {
			System.out.println("null");
			return this.getFragmentWithInterForUrlIdNoUser(inter.getFragment().getXmlId(), inter.getUrlId(), selectedVE);
		}

		return this.getFragmentWithInterForUrlId(currentUser, inter.getFragment().getXmlId(), inter.getUrlId(), selectedVE);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/inter")
	public FragmentBodyDto getInter(@RequestParam(value = "fragment", required = true) String externalId,
			@RequestParam(value = "inters[]", required = false) String[] intersID) {

		Fragment fragment = FenixFramework.getDomainObject(externalId);

		List<FragInter> inters = new ArrayList<>();
		System.out.println(intersID);
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

		
		return new FragmentBodyDto(LdoD.getInstance(), LdoDUser.getAuthenticatedUser(), fragment, inters, writer, writer4One, variations, apps);

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/inter/editorial")
	public FragmentBodyDto getInterEditorial(@RequestParam(value = "interp[]", required = true) String[] interID,
			@RequestParam(value = "diff", required = true) boolean displayDiff) {
		FragInter inter = FenixFramework.getDomainObject(interID[0]);

		PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);
		writer.write(displayDiff);

		List<FragInter> inters = new ArrayList<>();
		inters.add(inter);

		return new FragmentBodyDto(inters, writer);

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/inter/authorial")
	public FragmentBodyDto getInterAuthorial(@RequestParam(value = "interp[]", required = true) String[] interID,
			@RequestParam(value = "diff", required = true) boolean displayDiff,
			@RequestParam(value = "del", required = true) boolean displayDel,
			@RequestParam(value = "ins", required = true) boolean highlightIns,
			@RequestParam(value = "subst", required = true) boolean highlightSubst,
			@RequestParam(value = "notes", required = true) boolean showNotes,
			@RequestParam(value = "facs", required = true) boolean showFacs,
			@RequestParam(value = "pb", required = false) String pbTextID) {
		SourceInter inter = FenixFramework.getDomainObject(interID[0]);
		PbText pbText = null;
		System.out.println(pbTextID);
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
	public FragmentBodyDto getInterCompare(@RequestParam(value = "inters[]", required = true) String[] intersID,
			@RequestParam(value = "line") boolean lineByLine,
			@RequestParam(value = "spaces", required = true) boolean showSpaces) {
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
	public String addInter(@PathVariable String veId, @PathVariable String interId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(veId);
		FragInter inter = FenixFramework.getDomainObject(interId);
		if (virtualEdition == null || inter == null) {
			return "error 1";
		}

		VirtualEditionInter addInter = virtualEdition.createVirtualEditionInter(inter,
				virtualEdition.getMaxFragNumber() + 1);
		

		
		if (addInter == null) {
			return "error";
		} else {
			return "success";
		}
	}
}
