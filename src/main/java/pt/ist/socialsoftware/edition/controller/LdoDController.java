package pt.ist.socialsoftware.edition.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.loaders.LoadTEICorpus;
import pt.ist.socialsoftware.edition.loaders.LoadTEIFragments;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

@Controller
public class LdoDController {

	@RequestMapping(method = RequestMethod.GET, value = "/load/corpusForm")
	public String corpusForm(Model model) {
		return "loadCorpus";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/load/corpus")
	public String loadTEICorpus(Model model,
			@RequestParam("file") MultipartFile file) {

		if (file == null) {
			return writeMessage(model, "ERRO", "Escolha um ficheiro",
					"/load/corpusForm");
		}

		LoadTEICorpus loader = new LoadTEICorpus();
		try {
			loader.loadTEICorpus(file.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return writeMessage(model, "ERRO", "Problemas com o ficheiro",
					"/load/corpusForm");
		} catch (LdoDException eldod) {
			return writeMessage(model, "ERRO", eldod.getMessage(),
					"/load/corpusForm");
		}
		return writeMessage(model, "SUCESSO", "Corpus carregado", "/");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/load/fragmentForm")
	public String fragmentForm(Model model) {
		return "loadFragments";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/load/fragments")
	public String loadTEIFragments(Model model,
			@RequestParam("file") MultipartFile file) {

		if (file == null) {
			return writeMessage(model, "ERRO", "Escolha um ficheiro",
					"/load/fragmentForm");
		}

		LoadTEIFragments loader = new LoadTEIFragments();
		try {
			loader.loadFragments(file.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return writeMessage(model, "ERRO", "Problemas com o ficheiro",
					"/load/fragmentForm");
		} catch (LdoDException eldod) {
			return writeMessage(model, "ERRO", eldod.getMessage(),
					"/load/fragmentForm");
		}

		return writeMessage(model, "SUCESSO", "Fragmentos carregados",
				"/search/fragments");

	}

	@RequestMapping(method = RequestMethod.GET, value = "/search/fragments")
	public String listFragments(Model model) {
		model.addAttribute("fragments", LdoD.getInstance().getFragmentsSet());
		return "listFragments";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragments/{id}")
	public String getFragment(Model model, @PathVariable String id) {
		Fragment fragment = AbstractDomainObject.fromExternalId(id);

		if (fragment == null) {
			return "fragmentNotFound";
		} else {
			model.addAttribute("fragment", fragment);
			model.addAttribute("transcription",
					"Selecione um testemunho para obter a transcrição");

			return "fragment";
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragments/{id1}/{id2}")
	public String getFragmentInter(Model model, @PathVariable String id1,
			@PathVariable String id2) {
		Fragment fragment = AbstractDomainObject.fromExternalId(id1);
		FragInter fragInter = AbstractDomainObject.fromExternalId(id2);

		if (fragment == null) {
			return "fragmentNotFound";
		} else if (fragInter == null) {
			return "fragmentNotFound";
		} else {
			model.addAttribute("fragment", fragment);
			model.addAttribute("transcription", fragInter.getTranscription());
			return "fragment";
		}

	}

	private String writeMessage(Model model, String type, String message,
			String back) {
		model.addAttribute("type", type);
		model.addAttribute("message", message);
		model.addAttribute("page", back);
		return "message";
	}

}
