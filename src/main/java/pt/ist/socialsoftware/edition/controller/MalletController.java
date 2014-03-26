package pt.ist.socialsoftware.edition.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.mallet.TopicModeler;

@Controller
@RequestMapping("/mallet")
public class MalletController {

	@RequestMapping(method = RequestMethod.GET, value = "/topics")
	public String topicModelling(Model modelSpring) throws IOException {
		LdoD ldoD = LdoD.getInstance();
		Edition edition = ldoD.getEdition("RZ");

		TopicModeler modeler = new TopicModeler();

		String results = modeler.generate(edition);

		modelSpring.addAttribute("results", results);
		return "mallet/results";
	}
}
