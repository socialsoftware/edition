package pt.ist.socialsoftware.edition.ldod.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.dto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.ldod.dto.VirtualEditionInterListDto;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;

@RestController
@RequestMapping("/api/services")
public class APIVirtualEditionController {
	private static Logger logger = LoggerFactory.getLogger(APIVirtualEditionController.class);

	@GetMapping("/edition/{acronym}/index")
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
	public @ResponseBody ResponseEntity<VirtualEditionInterListDto> getVirtualEditionIndex(
			@PathVariable(value = "acronym") String acronym) {
		logger.debug("getVirtualEditionIndex acronym:{}", acronym);
		VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(acronym);

		if (virtualEdition == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			VirtualEditionInterListDto result = new VirtualEditionInterListDto(virtualEdition, true);
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@GetMapping("/edition/{acronym}/inter/{urlId}")
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
	public @ResponseBody ResponseEntity<VirtualEditionInterDto> getVirtualEditionInterText(
			@PathVariable(value = "acronym") String acronym, @PathVariable(value = "urlId") String urlId) {
		logger.debug("getVirtualEditionInterText acronym:{} urlId:{}", acronym, urlId);
		VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(acronym);
		if (virtualEdition == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		VirtualEditionInter inter = (VirtualEditionInter) virtualEdition.getFragInterByUrlId(urlId);
		if (inter == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		VirtualEditionInterDto result = new VirtualEditionInterDto(inter);
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	@GetMapping("/{username}/restricted/virtualeditions")
	@PreAuthorize("hasPermission(#username, 'user.logged')")
	public @ResponseBody ResponseEntity<List<VirtualEditionInterListDto>> getVirtualEditions4User(
			@PathVariable(value = "username") String username) {
		LdoDUser user = LdoD.getInstance().getUser(username);
		if (user != null) {
			List<VirtualEdition> virtualEditionList = LdoD.getInstance().getVirtualEditions4User(user,
					LdoDSession.getLdoDSession());
			List<VirtualEditionInterListDto> result = virtualEditionList.stream()
					.filter(virtualEdition -> virtualEdition.getParticipantSet().contains(user))
					.map(ve -> new VirtualEditionInterListDto(ve, true)).collect(Collectors.toList());

			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/{username}/public/virtualeditions")
	public @ResponseBody ResponseEntity<List<VirtualEditionInterListDto>> getPublicVirtualEditions4User(
			@PathVariable(value = "username") String username) {
		LdoDUser user = LdoD.getInstance().getUser(username);
		if (user != null) {
			List<VirtualEditionInterListDto> result = LdoD.getInstance().getVirtualEditionsSet().stream().filter(
					virtualEdition -> virtualEdition.getParticipantList().contains(user) && virtualEdition.getPub())
					.map(ve -> new VirtualEditionInterListDto(ve, true)).collect(Collectors.toList());

			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
