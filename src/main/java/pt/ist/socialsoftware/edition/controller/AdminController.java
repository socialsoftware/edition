package pt.ist.socialsoftware.edition.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.Edition.EditionType;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.loaders.LoadTEICorpus;
import pt.ist.socialsoftware.edition.loaders.LoadTEIFragments;
import pt.ist.socialsoftware.edition.shared.exception.LdoDLoadException;
import pt.ist.socialsoftware.edition.visitors.TEIGenerator;

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

        return writeMessage(model,
                "Fragmentos carregados: " + total + "<br>" + list,
                "/search/fragments");

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

    @RequestMapping(method = RequestMethod.GET, value = "/exportForm")
    public String exportForm(Model model) {
        return "admin/exportForm";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/exportSearch")
    public String exportSearch(Model model,
            @RequestParam("query") String query) {

        LdoD ldoD = LdoD.getInstance();

        List<String> frags = new ArrayList<String>();
        int n = 0;

        if (query.compareTo("") != 0) {
            for (Fragment frag : ldoD.getFragmentsSet()) {
                if (frag.getTitle().contains(query)) {
                    frags.add(
                            "<a href=\"/fragments/fragment/"
                                    + frag.getExternalId() + "\">"
                                    + frag.getTitle().replace(query,
                                            "<b><u>" + query + "</u></b>")
                            + "</a>");
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
    public void exportSearchResult(HttpServletResponse response, Model model,
            @RequestParam("query") String query) {

        LdoD ldoD = LdoD.getInstance();

        Map<Fragment, Set<FragInter>> searchResult = new HashMap<Fragment, Set<FragInter>>();

        for (Fragment frag : ldoD.getFragmentsSet()) {
            if (frag.getTitle().contains(query)) {
                Set<FragInter> inters = new HashSet<FragInter>();
                for (FragInter inter : frag.getFragmentInterSet()) {
                    if (inter.getSourceType() != EditionType.VIRTUAL) {
                        inters.add(inter);
                    }
                }
                searchResult.put(frag, inters);
            }
        }

        TEIGenerator teiGenerator = new TEIGenerator();
        teiGenerator.generate(searchResult);

        try {
            // get your file as InputStream
            InputStream is = IOUtils.toInputStream(teiGenerator.getXMLResult(),
                    "UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment; filename=tei.xml");
            response.setContentType("application/tei+xml");
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            System.out.println(
                    "Error writing file to output stream. Filename was '{}'");
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exportAll")
    public void exportAll(HttpServletResponse response) {

        LdoD ldoD = LdoD.getInstance();

        Map<Fragment, Set<FragInter>> searchResult = new HashMap<Fragment, Set<FragInter>>();

        for (Fragment frag : ldoD.getFragmentsSet()) {
            Set<FragInter> inters = new HashSet<FragInter>();

            for (FragInter inter : frag.getFragmentInterSet()) {
                if (inter.getSourceType() != EditionType.VIRTUAL) {

                    inters.add(inter);
                }
            }
            searchResult.put(frag, inters);
        }

        TEIGenerator teiGenerator = new TEIGenerator();
        teiGenerator.generate(searchResult);

        try {
            // get your file as InputStream
            InputStream is = IOUtils.toInputStream(teiGenerator.getXMLResult(),
                    "UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment; filename=tei.xml");
            response.setContentType("application/tei+xml");
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            System.out.println(
                    "Error writing file to output stream. Filename was '{}'");
            throw new RuntimeException("IOError writing file to output stream");
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/exportRandom")
    public void exportRandom(HttpServletResponse response) {

        LdoD ldoD = LdoD.getInstance();

        Map<Fragment, Set<FragInter>> searchResult = new HashMap<Fragment, Set<FragInter>>();

        List<Fragment> fragments = new ArrayList<Fragment>(
                LdoD.getInstance().getFragmentsSet());

        List<String> fragsRandom = new ArrayList<String>();

        int size = fragments.size();

        int fragPos = 0;
        Fragment frag = null;

        for (int i = 0; i < 3; i++) {
            fragPos = (int) (Math.random() * size);
            frag = fragments.get(fragPos);

            fragsRandom.add("<a href=\"/fragments/fragment/"
                    + frag.getExternalId() + "\">" + frag.getTitle() + "</a>");

            Set<FragInter> inters = new HashSet<FragInter>();
            for (FragInter inter : frag.getFragmentInterSet()) {
                if (inter.getSourceType() != EditionType.VIRTUAL) {

                    inters.add(inter);
                }
            }
            searchResult.put(frag, inters);
        }

        TEIGenerator teiGenerator = new TEIGenerator();
        teiGenerator.generate(searchResult);

        try {
            // get your file as InputStream
            InputStream is = IOUtils.toInputStream(teiGenerator.getXMLResult(),
                    "UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment; filename=tei.xml");
            response.setContentType("application/tei+xml");
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            System.out.println(
                    "Error writing file to output stream. Filename was '{}'");
            throw new RuntimeException("IOError writing file to output stream");
        }

    }
}
