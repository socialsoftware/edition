package pt.ist.socialsoftware.edition.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import pt.ist.socialsoftware.edition.loaders.LoadTEICorpus;
import pt.ist.socialsoftware.edition.loaders.LoadTEIFragments;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

@Controller
@RequestMapping("/load")
public class LoadController {

	@RequestMapping(method = RequestMethod.GET, value = "/corpusForm")
	public String corpusForm(Model model) {
		return "loadCorpus";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/corpus")
	public String loadTEICorpus(Model model,
			@RequestParam("file") MultipartFile file) throws LdoDException {

		if (file == null) {
			throw new LdoDException("Deve escolher um ficheiro");
		}

		LoadTEICorpus loader = new LoadTEICorpus();
		try {
			loader.loadTEICorpus(file.getInputStream());
		} catch (IOException e) {
			throw new LdoDException("Problemas com o ficheiro, tipo ou formato");
		}

		return writeMessage(model, "Corpus carregado", "/");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragmentForm")
	public String fragmentForm(Model model) {
		return "loadFragments";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/fragments")
	public String loadTEIFragments(Model model,
			@RequestParam("file") MultipartFile file) throws LdoDException {

		if (file == null) {
			throw new LdoDException("Deve escolher um ficheiro");
		}

		LoadTEIFragments loader = new LoadTEIFragments();
		try {
			loader.loadFragments(file.getInputStream());
		} catch (IOException e) {
			throw new LdoDException("Problemas com o ficheiro, tipo ou formato");
		}

		return writeMessage(model, "Fragmentos carregados", "/search/fragments");

	}

	private String writeMessage(Model model, String message, String back) {
		model.addAttribute("message", message);
		model.addAttribute("page", back);
		return "okMessage";
	}

}
