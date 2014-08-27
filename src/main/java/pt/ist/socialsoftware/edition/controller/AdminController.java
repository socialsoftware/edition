package pt.ist.socialsoftware.edition.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.security.access.prepost.PreAuthorize;
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
	@PreAuthorize("hasRole('ADMIN')")
	public String corpusForm(Model model) {
		return "admin/loadCorpus";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/load/corpus")
	@PreAuthorize("hasRole('ADMIN')")
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
	@PreAuthorize("hasRole('ADMIN')")
	public String fragmentFormAtOnce(Model model) {
		return "admin/loadFragmentsAtOnce";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/load/fragmentFormStepByStep")
	@PreAuthorize("hasRole('ADMIN')")
	public String fragmentFormStepByStep(Model model) {
		return "admin/loadFragmentsStepByStep";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/load/fragmentsAtOnce")
	@PreAuthorize("hasRole('ADMIN')")
	public String loadTEIFragmentsAtOnce(Model model,
			@RequestParam("file") MultipartFile file) throws LdoDLoadException {
		String message = null;

		if (file == null) {
			throw new LdoDLoadException("Deve escolher um ficheiro");
		}

		LoadTEIFragments loader = new LoadTEIFragments();
		try {
			message = loader.loadFragmentsAtOnce(file.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (message == null) {
			return writeMessage(model, "Fragmentos carregados",
					"/search/fragments");
		} else {
			model.addAttribute("message", message);
			return "utils/ldoDExceptionPage";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/load/fragmentsStepByStep")
	@PreAuthorize("hasRole('ADMIN')")
	public String loadTEIFragmentsStepByStep(Model model,
			@RequestParam("files") MultipartFile[] files)
			throws LdoDLoadException {

		if (files == null) {
			throw new LdoDLoadException("Deve escolher um ficheiro");
		}

		LoadTEIFragments loader = new LoadTEIFragments();

		String list = "";
		int total = 0;
		for (MultipartFile file : files) {
			try {
				list = list
						+ loader.loadFragmentsStepByStep(file.getInputStream());
				total++;
			} catch (IOException e) {
				throw new LdoDLoadException(
						"Problemas com o ficheiro, tipo ou formato");
			}
		}

		return writeMessage(model, "Fragmentos carregados: " + total + "<br>"
				+ list, "/search/fragments");

	}

	private String writeMessage(Model model, String message, String back) {
		model.addAttribute("message", message);
		model.addAttribute("page", back);
		return "utils/okMessage";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragment/list")
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteFragmentsList(Model model) {
		model.addAttribute("fragments", LdoD.getInstance().getFragmentsSet());
		return "admin/deleteFragment";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/fragment/delete")
	@PreAuthorize("hasRole('ADMIN')")
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

	@RequestMapping(method = RequestMethod.POST, value = "/fragment/deleteAll")
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteAllFragments(Model model) {
		for (Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
			fragment.remove();
		}

		model.addAttribute("fragments", LdoD.getInstance().getFragmentsSet());
		return "admin/deleteFragment";

	}

	@RequestMapping(method = RequestMethod.GET, value = "/user/create")
	@PreAuthorize("hasRole('ADMIN')")
	public String createUsers(Model model) throws FileNotFoundException,
			IOException {
		LdoD.getInstance().createUsers();
		return writeMessage(model, "Utilizadores criados", "/");

	}
}
