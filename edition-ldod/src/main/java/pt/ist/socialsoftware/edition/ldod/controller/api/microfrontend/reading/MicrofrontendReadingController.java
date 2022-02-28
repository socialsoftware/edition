package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.reading;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.CitationDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.ReadingDto;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.recommendation.ReadingRecommendation;

@RestController
@RequestMapping("/api/microfrontend/reading")
public class MicrofrontendReadingController {
	@RequestMapping(method = RequestMethod.GET)
	public List<ExpertEditionDto> startReading() {

		return LdoD.getInstance().getSortedExpertEdition().stream().map(ExpertEditionDto::new)
				.collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.POST, value = "/fragment/{xmlId}/interJson/{urlId}", headers = {
			"Content-type=application/json" })
	public ReadingDto readInterpretationJson(@PathVariable String xmlId, @PathVariable String urlId, @RequestBody ReadingRecommendation jsonRecomendation) {
		Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
		if (fragment == null) {
			return null;
		}

		ExpertEditionInter expertEditionInter = (ExpertEditionInter) fragment.getFragInterByUrlId(urlId);
		if (expertEditionInter == null) {
			return null;
		}

		Set<ExpertEditionInter> recommendations = jsonRecomendation.getNextRecommendations(expertEditionInter.getExternalId());
		ExpertEditionInter prevRecom = jsonRecomendation.getPrevRecommendation();

		PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(expertEditionInter);
		writer.write(false);

		return new ReadingDto(LdoD.getInstance(), expertEditionInter, recommendations, prevRecom, writer, fragment, jsonRecomendation);

	}

	@RequestMapping(method = RequestMethod.POST, value = "/edition/{acronym}/start", headers = {
			"Content-type=application/json" })
	public ReadingDto startReadingEdition(@PathVariable String acronym, @RequestBody ReadingRecommendation jsonRecomendation) {
		ExpertEdition expertEdition = (ExpertEdition) LdoD.getInstance().getEdition(acronym);
		ExpertEditionInter expertEditionInter = expertEdition.getFirstInterpretation();


		jsonRecomendation.clean();
		jsonRecomendation.setTextWeight(1.0);

		return this.readInterpretationJson(expertEditionInter.getFragment().getXmlId(), expertEditionInter.getUrlId(), jsonRecomendation);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/fragment/{xmlId}/inter/{urlId}/next")
	public ReadingDto readNextInterpretation(@PathVariable String xmlId, @PathVariable String urlId, @RequestBody ReadingRecommendation jsonRecomendation) {
		Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
		if (fragment == null) {
			return null;
		}

		ExpertEditionInter expertEditionInter = (ExpertEditionInter) fragment.getFragInterByUrlId(urlId);
		if (expertEditionInter == null) {
			return null;
		}

		FragInter nextExpertEditionInter = expertEditionInter.getEdition().getNextNumberInter(expertEditionInter,
				expertEditionInter.getNumber());

		return this.readInterpretationJson(nextExpertEditionInter.getFragment().getXmlId(), nextExpertEditionInter.getUrlId(), jsonRecomendation);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/fragment/{xmlId}/inter/{urlId}/prev")
	public ReadingDto readPrevInterpretation(@PathVariable String xmlId, @PathVariable String urlId, @RequestBody ReadingRecommendation jsonRecomendation) {
		Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
		if (fragment == null) {
			return null;
		}

		ExpertEditionInter expertEditionInter = (ExpertEditionInter) fragment.getFragInterByUrlId(urlId);
		if (expertEditionInter == null) {
			return null;
		}

		FragInter prevExpertEditionInter = expertEditionInter.getEdition().getPrevNumberInter(expertEditionInter,
				expertEditionInter.getNumber());

		return this.readInterpretationJson(prevExpertEditionInter.getFragment().getXmlId(), prevExpertEditionInter.getUrlId(), jsonRecomendation);
	}


	@RequestMapping(method = RequestMethod.POST, value = "/inter/prev/recom")
	public ReadingDto readPreviousRecommendedFragment(@RequestBody ReadingRecommendation jsonRecomendation) {

		String expertEditionInterId = jsonRecomendation.prevRecommendation();
		ExpertEditionInter expertEditionInter = FenixFramework.getDomainObject(expertEditionInterId);

		return this.readInterpretationJson(expertEditionInter.getFragment().getXmlId(), expertEditionInter.getUrlId(), jsonRecomendation);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/inter/prev/recom/reset")
	public ReadingRecommendation resetPreviousRecommendedFragments(@RequestBody ReadingRecommendation jsonRecomendation) {

		jsonRecomendation.resetPrevRecommendations();

		return jsonRecomendation;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/citations")
	public List<CitationDto> listCitations() {

		return LdoD.getInstance().getCitationsWithInfoRanges().stream().map(CitationDto::new).collect(Collectors.toList());
	}

}