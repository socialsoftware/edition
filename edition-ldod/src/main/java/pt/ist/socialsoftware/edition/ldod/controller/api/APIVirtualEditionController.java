package pt.ist.socialsoftware.edition.ldod.controller.api;

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
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.dto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.ldod.dto.VirtualEditionInterListDto;

@RestController
@RequestMapping("/api/services")
public class APIVirtualEditionController {
	private static Logger logger = LoggerFactory.getLogger(APIVirtualEditionController.class);

	@GetMapping("/edition/{acronym}/index")
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
	public @ResponseBody ResponseEntity<VirtualEditionInterListDto> getVirtualEditionIndex(
			@PathVariable(value = "acronym") String acronym) {
		logger.debug("getVirtualEdition acronym:{}", acronym);
		VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(acronym);

		if (virtualEdition == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			VirtualEditionInterListDto result = new VirtualEditionInterListDto(virtualEdition);
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@GetMapping("/edition/{acronym}/inter/{urlId}")
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
	public @ResponseBody ResponseEntity<VirtualEditionInterDto> getVirtualEditionInterText(
			@PathVariable(value = "acronym") String acronym, @PathVariable(value = "urlId") String urlId) {
		logger.debug("getVirtualEdition acronym:{}", acronym);
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

}
