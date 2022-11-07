package pt.ist.socialsoftware.edition.ldod.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.dto.Fragment2CriticalDto;
import pt.ist.socialsoftware.edition.ldod.dto.VirtualEdition2CriticalDto;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/critical")
public class CriticalController {
    private static Logger logger = LoggerFactory.getLogger(CriticalController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public @ResponseBody ResponseEntity<VirtualEdition2CriticalDto> getEdition(@PathVariable String acronym) {
        logger.debug("getEdition acronym:{}", acronym);

        VirtualEdition2CriticalDto virtualEdition2CriticalDto = new VirtualEdition2CriticalDto();

        VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(acronym);
        if (virtualEdition != null) {
            List<Fragment2CriticalDto> fragments = new ArrayList<>();

            virtualEdition.getAllDepthVirtualEditionInters().stream().sorted(Comparator.comparing(FragInter::getTitle))
                    .forEach(inter -> {
                        Fragment2CriticalDto fragment = new Fragment2CriticalDto(inter);

                        fragments.add(fragment);
                    });

            virtualEdition2CriticalDto.setFragments(fragments);

            return new ResponseEntity<>(virtualEdition2CriticalDto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}/fragments/{urlId}")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public @ResponseBody ResponseEntity<Fragment2CriticalDto> getEditionFragment(@PathVariable String acronym, @PathVariable String urlId) {
        logger.debug("getEditionFragment acronym:{}, urlId:{}", acronym, urlId);

        VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(acronym);
        if (virtualEdition != null) {
            Optional<VirtualEditionInter> inter = virtualEdition.getAllDepthVirtualEditionInters().stream()
                    .filter(virtualEditionInter -> virtualEditionInter.getUrlId().equals(urlId))
                    .findAny();

            if (inter.isPresent()) {
                PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.get().getLastUsed());
                writer.write(false);
                Fragment2CriticalDto fragment2CriticalDto = new Fragment2CriticalDto(inter.get(), writer.getTranscription());
                return new ResponseEntity<>(fragment2CriticalDto, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
