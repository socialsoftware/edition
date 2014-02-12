package pt.ist.socialsoftware.edition.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.loaders.LoadTEICorpus;
import pt.ist.socialsoftware.edition.loaders.LoadTEIFragments;
import pt.ist.socialsoftware.edition.shared.exception.LdoDLoadException;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@RequestMapping(method = RequestMethod.GET, value = "/load/corpusForm")
	public String corpusForm(Model model) {
		return "admin/loadCorpus";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/load/corpus")
	public String loadTEICorpus(Model model,
			@RequestParam("file") MultipartFile file) throws LdoDLoadException {

		if (file == null) {
			throw new LdoDLoadException("Deve escolher um ficheiro");
		}

		LoadTEICorpus loader = new LoadTEICorpus();
		try {
			loader.loadTEICorpus(file.getInputStream());
		} catch (IOException e) {
			throw new LdoDLoadException(
					"Problemas com o ficheiro, tipo ou formato");
		}

		return writeMessage(model, "Corpus carregado", "/");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/load/fragmentFormAtOnce")
	public String fragmentFormAtOnce(Model model) {
		return "admin/loadFragmentsAtOnce";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/load/fragmentFormStepByStep")
	public String fragmentFormStepByStep(Model model) {
		return "admin/loadFragmentsStepByStep";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/load/fragmentAtOnce")
	public String loadTEIFragmentsAtOnce(Model model,
			@RequestParam("file") MultipartFile file) throws LdoDLoadException {

		if (file == null) {
			throw new LdoDLoadException("Deve escolher um ficheiro");
		}

		LoadTEIFragments loader = new LoadTEIFragments();
		try {
			loader.loadFragmentsAtOnce(file.getInputStream());
		} catch (IOException e) {
			throw new LdoDLoadException(
					"Problemas com o ficheiro, tipo ou formato");
		}

		return writeMessage(model, "Fragmentos carregados", "/search/fragments");

	}

	@RequestMapping(method = RequestMethod.POST, value = "/load/fragmentsStepByStep")
	public String loadTEIFragmentsStepByStep(Model model,
			@RequestParam("file") MultipartFile file) throws LdoDLoadException {

		if (file == null) {
			throw new LdoDLoadException("Deve escolher um ficheiro");
		}

		LoadTEIFragments loader = new LoadTEIFragments();
		try {
			loader.loadFragmentsStepByStep(file.getInputStream());
		} catch (IOException e) {
			throw new LdoDLoadException(
					"Problemas com o ficheiro, tipo ou formato");
		}

		return writeMessage(model, "Fragmentos carregados", "/search/fragments");

	}

	private String writeMessage(Model model, String message, String back) {
		model.addAttribute("message", message);
		model.addAttribute("page", back);
		return "utils/okMessage";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragment/list")
	public String deleteFragmentsList(Model model) {
		model.addAttribute("fragments", LdoD.getInstance().getFragmentsSet());
		return "admin/deleteFragment";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/fragment/delete")
	public String deleteFragment(Model model,
			@RequestParam("externalId") String externalId) {
		Fragment fragment = FenixFramework.getDomainObject(externalId);

		if (fragment == null) {
			return "utils/pageNotFound";
		} else if (LdoD.getInstance().getFragmentsSet().size() >= 1) {
			fragment.remove();
		}
		model.addAttribute("fragments", LdoD.getInstance().getFragmentsSet());
		return "admin/deleteFragment";

	}

}
