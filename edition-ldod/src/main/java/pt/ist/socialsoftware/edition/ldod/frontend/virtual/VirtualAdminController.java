package pt.ist.socialsoftware.edition.ldod.frontend.virtual;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDLoadException;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.inout.VirtualEditionFragmentsTEIImport;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.inout.VirtualEditionsTEICorpusImport;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.inout.WriteVirtualEditonsToFile;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.socialaware.AwareAnnotationFactory;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.socialaware.CitationDetecter;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.socialaware.TweetFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class VirtualAdminController {
    private static final Logger logger = LoggerFactory.getLogger(VirtualAdminController.class);

    @Inject
    private SessionRegistry sessionRegistry;

    @Inject
    private PasswordEncoder passwordEncoder;


    @RequestMapping(method = RequestMethod.GET, value = "/export/virtualeditions")
    public void exportVirtualEditions(HttpServletResponse response) throws IOException {
        WriteVirtualEditonsToFile write = new WriteVirtualEditonsToFile();
        String filename = write.export();

        String exportDir = PropertiesManager.getProperties().getProperty("export.dir");
        File directory = new File(exportDir);
        File file = new File(directory, filename);
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setHeader("Content-Type", "application/zip");
        InputStream is = new FileInputStream(file);
        FileCopyUtils.copy(IOUtils.toByteArray(is), response.getOutputStream());
        response.flushBuffer();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/load/virtual-corpus")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadVirtualCorpus(RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile file)
            throws LdoDLoadException {
        if (file == null) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Deve escolher um ficheiro");
            return "redirect:/admin/loadForm";
        }

        VirtualEditionsTEICorpusImport loader = new VirtualEditionsTEICorpusImport();
        try {
            loader.importVirtualEditionsCorpus(file.getInputStream());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Problemas com o ficheiro, tipo ou formato");
            return "redirect:/admin/loadForm";
        }

        redirectAttributes.addFlashAttribute("error", false);
        redirectAttributes.addFlashAttribute("message", "Corpus das edições virtuais carregado");
        return "redirect:/admin/loadForm";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/load/virtual-fragments")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadVirtualFragments(RedirectAttributes redirectAttributes,
                                       @RequestParam("files") MultipartFile[] files) throws LdoDLoadException {
        if (files == null) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Deve escolher um ficheiro");
            return "redirect:/admin/loadForm";
        }

        VirtualEditionFragmentsTEIImport loader = new VirtualEditionFragmentsTEIImport();

        String list = "";
        int total = 0;
        for (MultipartFile file : files) {
            try {
                list = list + "<br/>" + loader.importFragmentFromTEI(file.getInputStream());
                total++;
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", true);
                redirectAttributes.addFlashAttribute("message", "Problemas com o ficheiro, tipo ou formato");
                return "redirect:/admin/loadForm";
            } catch (LdoDException ldodE) {
                redirectAttributes.addFlashAttribute("error", true);
                redirectAttributes.addFlashAttribute("message", ldodE.getMessage());
                return "redirect:/admin/loadForm";
            }
        }

        redirectAttributes.addFlashAttribute("error", false);
        redirectAttributes.addFlashAttribute("message",
                "Fragmentos das edições virtuais carregados: " + total + "<br>" + list);
        return "redirect:/admin/loadForm";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/virtual/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String manageVirtualEditions(Model model) {
        model.addAttribute("editions", VirtualModule.getInstance().getVirtualEditionsSet().stream()
                .sorted((v1, v2) -> v1.getAcronym().compareTo(v2.getAcronym())).collect(Collectors.toList()));

        return "admin/listVirtualEditions";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/virtual/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteVirtualEdition(Model model, @RequestParam("externalId") String externalId) {
        VirtualEdition edition = FenixFramework.getDomainObject(externalId);
        if (edition == null) {
            return "redirect:/error";
        } else {
            edition.remove();
        }
        return "redirect:/admin/virtual/list";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tweets")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String manageTweets(Model model) {
        logger.debug("manageTweets");

        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        model.addAttribute("citations",
                VirtualModule.getInstance().getAllTwitterCitation().stream()
                        .sorted((c1, c2) -> java.time.LocalDateTime.parse(c2.getDate(), formater)
                                .compareTo(java.time.LocalDateTime.parse(c1.getDate(), formater)))
                        .collect(Collectors.toList()));
        model.addAttribute("tweets", VirtualModule.getInstance().getTweetSet());
        model.addAttribute("numberOfCitationsWithInfoRange", VirtualModule.getInstance().getNumberOfCitationsWithInfoRanges());
        return "admin/manageTweets";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tweets/removeTweets")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String removeTweets(Model model) {
        logger.debug("removeTweets");
        VirtualModule.getInstance().removeTweets();
        return "redirect:/admin/tweets";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tweets/generateCitations")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String generateCitations(Model model) throws IOException {
        logger.debug("generateCitations");
        CitationDetecter detecter = new CitationDetecter();
        detecter.detect();

        TweetFactory tweetFactory = new TweetFactory();
        tweetFactory.create();

        AwareAnnotationFactory awareFactory = new AwareAnnotationFactory();
        awareFactory.generate();

        VirtualModule.dailyRegenerateTwitterCitationEdition();

        // Repeat to update edition
        awareFactory.generate();

        return "redirect:/admin/tweets";
    }

}
