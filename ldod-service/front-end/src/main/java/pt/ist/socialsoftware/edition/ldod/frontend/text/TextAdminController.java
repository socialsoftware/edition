package pt.ist.socialsoftware.edition.ldod.frontend.text;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.socialsoftware.edition.ldod.frontend.text.textDto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.LdoDException;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.LdoDLoadException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class TextAdminController {
    private static final Logger logger = LoggerFactory.getLogger(TextAdminController.class);

    FeTextRequiresInterface frontendTextRequiresInterface = new FeTextRequiresInterface();

    @Inject
    private SessionRegistry sessionRegistry;

    @Inject
    private PasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.GET, value = "/loadForm")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadForm(Model model) {
        Boolean error = (Boolean) model.asMap().get("error");
        model.addAttribute("error", error);
        String message = (String) model.asMap().get("message");
        model.addAttribute("message", message);
        return "admin/loadForm";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/load/corpus")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadTEICorpus(RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile file)
            throws LdoDLoadException {

        if (file == null) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Deve escolher um ficheiro");
            return "redirect:/admin/loadForm";
        }

        try {

           frontendTextRequiresInterface.getLoaderTEICorpus(file.getInputStream());
           frontendTextRequiresInterface.getLoaderTEICorpusVirtual(file.getInputStream());

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Problemas com o ficheiro, tipo ou formato");
            return "redirect:/admin/loadForm";
        } catch (LdoDException ldodE) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", ldodE.getMessage());
            return "redirect:/admin/loadForm";
        }

        redirectAttributes.addFlashAttribute("error", false);
        redirectAttributes.addFlashAttribute("message", "Corpus carregado");
        return "redirect:/admin/loadForm";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/load/fragmentsAtOnce")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadTEIFragmentsAtOnce(RedirectAttributes redirectAttributes,
                                         @RequestParam("file") MultipartFile file) throws LdoDLoadException {
        String message = null;

        if (file == null) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Deve escolher um ficheiro");
            return "redirect:/admin/loadForm";
        }
        try {
            message = frontendTextRequiresInterface.getLoadTEIFragmentsAtOnce(file.getInputStream());
        //    frontendTextRequiresInterface.getFragmentCorpusGenerator();
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Problemas com o ficheiro, tipo ou formato");
            return "redirect:/admin/loadForm";
        } catch (LdoDException ldodE) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", ldodE.getMessage());
            return "redirect:/admin/loadForm";
        }

        if (message == null) {
            redirectAttributes.addFlashAttribute("error", false);
            redirectAttributes.addFlashAttribute("message", "Fragmento carregado");
            return "redirect:/admin/loadForm";
        } else {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/loadForm";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/load/fragmentsStepByStep")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadTEIFragmentsStepByStep(RedirectAttributes redirectAttributes,
                                             @RequestParam("files") MultipartFile[] files) throws LdoDLoadException {

        if (files == null) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Deve escolher um ficheiro");
            return "redirect:/admin/loadForm";
        }

        String list = "";
        int total = 0;
        for (MultipartFile file : files) {
            try {
                list = list + this.frontendTextRequiresInterface.getLoadTEIFragmentsStepByStep(file.getInputStream());
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
//        frontendTextRequiresInterface.getFragmentCorpusGenerator();

        redirectAttributes.addFlashAttribute("error", false);
        redirectAttributes.addFlashAttribute("message", "Fragmentos carregados: " + total + "<br>" + list);
        return "redirect:/admin/loadForm";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteFragmentsList(Model model) {
        //model.addAttribute("fragments", TextModule.getInstance().getFragmentsSet());
        model.addAttribute("fragments", this.frontendTextRequiresInterface.getFragmentDtoSet());
        return "admin/deleteFragment";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fragment/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteFragment(Model model, @RequestParam("externalId") String externalId) {
        //Fragment fragment = FenixFramework.getDomainObject(externalId);
        FragmentDto fragment = this.frontendTextRequiresInterface.getFragmentByExternalId(externalId);
        if (fragment == null) {
            return "redirect:/error";
//        } else if (TextModule.getInstance().getFragmentsSet().size() >= 1) {
        } else if (this.frontendTextRequiresInterface.getFragmentDtoSet().size() >= 1) {
            this.frontendTextRequiresInterface.removeFragmentByExternalId(externalId);
        }
        return "redirect:/admin/fragment/list";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fragment/deleteAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteAllFragments(Model model) {
       for (FragmentDto fragmentDto: this.frontendTextRequiresInterface.getFragmentDtoSet()) {
           this.frontendTextRequiresInterface.removeFragmentByExternalId(fragmentDto.getExternalId());
       }
        return "redirect:/admin/fragment/list";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exportForm")
    public String exportForm(Model model) {
        return "admin/exportForm";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/exportSearch")
    public String exportSearch(Model model, @RequestParam("query") String query) {
        List<String> frags = new ArrayList<>();
        int n = 0;

//        if (query.compareTo("") != 0) {
//            for (Fragment frag : TextModule.getInstance().getFragmentsSet()) {
//                if (frag.getTitle().contains(query)) {
//                    frags.add("<a href=\"/fragments/fragment/" + frag.getExternalId() + "\">"
//                            + frag.getTitle().replace(query, "<b><u>" + query + "</u></b>") + "</a>");
//                    n++;
//                }
//            }
//        }

        if (query.compareTo("") != 0) {
            for (FragmentDto frag : this.frontendTextRequiresInterface.getFragmentDtoSet() ) {
                if (frag.getTitle().contains(query)) {
                    frags.add("<a href=\"/fragments/fragment/" + frag.getExternalId() + "\">"
                            + frag.getTitle().replace(query, "<b><u>" + query + "</u></b>") + "</a>");
                    n++;
                }
            }
        }


        model.addAttribute("query", query);
        model.addAttribute("nResults", n);
        model.addAttribute("frags", frags);

        return "admin/exportForm";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/exportSearchResult")
    public void exportSearchResult(HttpServletResponse response, Model model, @RequestParam("query") String query) {

//        Map<Fragment, Set<ScholarInter>> searchResult = new HashMap<>();
//
//        for (Fragment frag : TextModule.getInstance().getFragmentsSet()) {
//            if (frag.getTitle().contains(query)) {
//                Set<ScholarInter> inters = new HashSet<>();
//                for (ScholarInter inter : frag.getScholarInterSet()) {
//                    inters.add(inter);
//                }
//                searchResult.put(frag, inters);
//            }
//        }
//
//        ExpertEditionTEIExport teiGenerator = new ExpertEditionTEIExport();
//        teiGenerator.generate(searchResult);
        String XMLresult = this.frontendTextRequiresInterface.exportWithQueryExpertEditionTEI(query);

        try {
            // get your file as InputStream
            InputStream is = IOUtils.toInputStream(XMLresult, "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=tei.xml");
            response.setContentType("application/tei+xml");
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            System.out.println("Error writing file to output stream. Filename was '{}'");
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exportAll")
    public void exportAll(HttpServletResponse response) {

//        Map<Fragment, Set<ScholarInter>> searchResult = new HashMap<>();
//        for (Fragment frag : TextModule.getInstance().getFragmentsSet()) {
//            Set<ScholarInter> inters = new HashSet<>();
//
//            for (ScholarInter inter : frag.getScholarInterSet()) {
//                inters.add(inter);
//            }
//            searchResult.put(frag, inters);
//        }
//
//        ExpertEditionTEIExport teiGenerator = new ExpertEditionTEIExport();
//        teiGenerator.generate(searchResult);

        String XMLresult = this.frontendTextRequiresInterface.exportAllExpertEditionTEI();


        try {
            // get your file as InputStream
            InputStream is = IOUtils.toInputStream(XMLresult, "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=tei.xml");
            response.setContentType("application/tei+xml");
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            System.out.println("Error writing file to output stream. Filename was '{}'");
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exportRandom")
    public void exportRandom(HttpServletResponse response) {

//        Map<Fragment, Set<ScholarInter>> searchResult = new HashMap<>();
//        List<Fragment> fragments = new ArrayList<>(TextModule.getInstance().getFragmentsSet());
//
//        List<String> fragsRandom = new ArrayList<>();
//
//        int size = fragments.size();
//
//        int fragPos = 0;
//        Fragment frag = null;
//
//        for (int i = 0; i < 3; i++) {
//            fragPos = (int) (Math.random() * size);
//            frag = fragments.get(fragPos);
//
//            fragsRandom.add("<a href=\"/fragments/fragment/" + frag.getExternalId() + "\">" + frag.getTitle() + "</a>");
//
//            Set<ScholarInter> inters = new HashSet<>();
//            for (ScholarInter inter : frag.getScholarInterSet()) {
//                //TODO: fragments have source inters. Should they be ingnored or should the code be rewritten for shcolar inters?
//                inters.add(inter);
//            }
//            searchResult.put(frag, inters);
//        }
//
//        ExpertEditionTEIExport teiGenerator = new ExpertEditionTEIExport();
//        teiGenerator.generate(searchResult);

        String XMLresult = this.frontendTextRequiresInterface.exportRandomExpertEditionTEI();

        try {
            // get your file as InputStream
            InputStream is = IOUtils.toInputStream(XMLresult, "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=tei.xml");
            response.setContentType("application/tei+xml");
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            System.out.println("Error writing file to output stream. Filename was '{}'");
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

}
