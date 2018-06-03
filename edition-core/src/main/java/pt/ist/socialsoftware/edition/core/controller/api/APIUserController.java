package pt.ist.socialsoftware.edition.core.controller.api;

import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.socialsoftware.edition.core.domain.FragInter;
import pt.ist.socialsoftware.edition.core.domain.LdoD;
import pt.ist.socialsoftware.edition.core.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.core.dto.EditionFragmentsDTO;
import pt.ist.socialsoftware.edition.core.dto.FragmentDTO;
import pt.ist.socialsoftware.edition.core.dto.FragmentMetaInfoDTO;
import pt.ist.socialsoftware.edition.core.dto.LdoDUserDTO;
import pt.ist.socialsoftware.edition.core.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.core.utils.PropertiesManager;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static pt.ist.socialsoftware.edition.core.security.SecurityConstants.HEADER_STRING;
import static pt.ist.socialsoftware.edition.core.security.SecurityConstants.SECRET;
import static pt.ist.socialsoftware.edition.core.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api")
public class APIUserController {
    private static Logger logger = LoggerFactory.getLogger(APIUserController.class);

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        logger.debug("getAuthentication");
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            String user = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody().getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }

    @GetMapping(value = "/user")
    public LdoDUserDTO getCurrentUser(HttpServletRequest request) {
        logger.debug("getCurrentUser");
        //LdoDUserDTO user = new LdoDUserDTO(LdoDUser.getAuthenticatedUser().getUsername(), LdoDUser.getAuthenticatedUser().getPassword());
        //LdoDUserDTO user = new LdoDUserDTO("gm","1");
        UsernamePasswordAuthenticationToken u = getAuthentication(request);
        return new LdoDUserDTO(u.getPrincipal().toString(), "");
    }

    @GetMapping(value = "/users/{username}")
    public LdoDUserDTO getUserProfile(@PathVariable(value = "username") String username) {
        logger.debug("getUserProfile");
        //LdoDUser u = LdoDUser.getAuthenticatedUser();
        //	LdoDUserDTO user = new LdoDUserDTO(u.getUsername(), u.getPassword());
        LdoDUserDTO user = new LdoDUserDTO(username,"pass");
        return user;
    }

    @GetMapping(value = "/edition/{acronym}")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public EditionFragmentsDTO getEdition(@PathVariable(value = "acronym") String acronym) {
        logger.debug("getEdition");
        VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(acronym);
        EditionFragmentsDTO editionFragmentsDTO = new EditionFragmentsDTO();

        String intersFilesPath = PropertiesManager.getProperties().getProperty("inters.dir");
        List<FragmentDTO> fragments = new ArrayList<>();

        for (FragInter inter : virtualEdition.getIntersSet()) {
            FragInter lastInter = inter.getLastUsed();
            String text;
            try {
                text = new String(
                        Files.readAllBytes(Paths.get(intersFilesPath + lastInter.getExternalId() + ".txt")));
            } catch (IOException e) {
                throw new LdoDException("VirtualEditionController::getTranscriptions IOException");
            }

            FragmentDTO fragment = new FragmentDTO();
            fragment.setMeta(new FragmentMetaInfoDTO(lastInter));
            fragment.setText(text);

            fragments.add(fragment);

        }
        editionFragmentsDTO.setFragments(fragments);
        return editionFragmentsDTO;
    }
}
