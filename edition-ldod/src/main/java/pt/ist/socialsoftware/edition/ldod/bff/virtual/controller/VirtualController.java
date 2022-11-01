package pt.ist.socialsoftware.edition.ldod.bff.virtual.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.MainResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.ClassGameDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.VirtualEditionLinearBodyDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.VirtualEditionsDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.service.VirtualService;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/virtual")
public class VirtualController {

    @Autowired
    private VirtualService virtualService;

    private static Logger logger = LoggerFactory.getLogger(VirtualController.class);

    @GetMapping("/virtual-editions")
    public ResponseEntity<VirtualEditionsDto> getVirtualEditions() {
        return ResponseEntity.status(HttpStatus.OK).body(virtualService.getVirtualEditionsList());
    }

    @GetMapping("/virtual-editions/game/{gameExternalId}")
    public ResponseEntity<?> getVeClassGame(@PathVariable String gameExternalId) {
        return ResponseEntity.status(HttpStatus.OK).body(virtualService.getVeClassGameDto(gameExternalId));
    }

    @GetMapping("/restricted/virtual-edition/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public ResponseEntity<?> getVirtualEdition(@PathVariable String externalId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.getVirtualEdition(externalId));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }


    @PostMapping("/restricted/create")
    public ResponseEntity<?> createVirtualEdition(@RequestBody VirtualEditionDto veDto) {

        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.createVirtualEdition(veDto));
        } catch (LdoDException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MainResponseDto.AuthResponseDtoBuilder(false)
                            .message(ex.getMessage())
                            .build());
        }
    }

    @GetMapping("/restricted/delete/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public ResponseEntity<?> deleteVirtualEdition(@PathVariable String externalId) {
        try {
            virtualService.deleteVirtualEdition(externalId);
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.getVirtualEditionsList());
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @GetMapping("/restricted/update-selected-ve/{selected}/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.public')")
    public ResponseEntity<?> updateSelectedVE(@PathVariable String externalId, @PathVariable boolean selected) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.toggleSelectedVE(externalId, selected));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @GetMapping("/restricted/participants/submit/{externalId}")
    public ResponseEntity<?> submitParticipation(@PathVariable String externalId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.submitParticipant(externalId));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @GetMapping("/restricted/participants/cancel/{externalId}")
    public ResponseEntity<?> cancelParticipationSubmission(@PathVariable String externalId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.cancelParticipation(externalId));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @GetMapping("/restricted/{externalId}/participants/{username}/approve")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
    public ResponseEntity<?> approveParticipant(@PathVariable String externalId, @PathVariable String username) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.approveParticipation(externalId, username));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @GetMapping("/restricted/{externalId}/participants/{username}/add")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
    public ResponseEntity<?> addParticipant(@PathVariable String externalId, @PathVariable String username) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.addParticipant(externalId, username));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }

    }

    @GetMapping("/restricted/{externalId}/participants/{username}/remove")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public ResponseEntity<?> removeParticipant(@PathVariable String externalId, @PathVariable String username) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.removeParticipant(externalId, username));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @GetMapping("/restricted/{externalId}/participants/{username}/role")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
    public ResponseEntity<?> switchRole(@PathVariable String externalId, @PathVariable String username) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.switchRole(externalId, username));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @GetMapping("/restricted/{externalId}/games")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public ResponseEntity<?> classificationGame(@PathVariable String externalId) {
        return ResponseEntity.status(HttpStatus.OK).body(virtualService.getClassificationGames(externalId));
    }

    @PostMapping("/restricted/{externalId}/games/create")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
    public ResponseEntity<?> createClassificationGame(@PathVariable String externalId, @RequestBody ClassGameDto gameDto) {
        return ResponseEntity.status(HttpStatus.OK).body(virtualService.createClassGame(externalId, gameDto));
    }

    @GetMapping("/restricted/{externalId}/games/{gameExternalId}/remove")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
    public ResponseEntity<?> removeClassificationGame(@PathVariable String externalId,
                                                      @PathVariable String gameExternalId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.removeClassGame(externalId, gameExternalId));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @PostMapping("/restricted/edit/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
    public ResponseEntity<?> editVirtualEdition(@PathVariable String externalId, @RequestBody VirtualEditionDto veDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.editVirtualEdition(externalId, veDto));
        } catch (LdoDException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MainResponseDto.AuthResponseDtoBuilder(false)
                            .message(ex.getMessage())
                            .build());
        }
    }

    @GetMapping("/restricted/assisted/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public ResponseEntity<?> presentEditionWithRecommendation(@PathVariable String externalId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.getEditionWithRecommendation(externalId));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @PostMapping("/restricted/assisted/{externalId}/linear")
    public ResponseEntity<?> setLinearVE(@PathVariable String externalId, @RequestBody VirtualEditionLinearBodyDto body) {

        logger.debug("setLinearVirtualEdition properties:{}", body.getProperties().stream()
                .map(p -> p.getClass().getName().substring(p.getClass().getName().lastIndexOf(".") + 1) + " "
                        + p.getWeight())
                .collect(Collectors.joining(";")));

        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.setLinearVE(externalId, body));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @PostMapping("/restricted/assisted/{externalId}/linear/save")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public ResponseEntity<?> saveLinearVirtualEdition(@PathVariable String externalId, @RequestBody List<String> inters) {
        return ResponseEntity.status(HttpStatus.OK).body(virtualService.saveLinearVE(externalId, inters));

    }

    @GetMapping("/restricted/manual/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public ResponseEntity<?> presentEditionForManualSort(@PathVariable String externalId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.getEditionForManualSort(externalId));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }
    }

    @PostMapping("/restricted/manual/{externalId}/mutate")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public ResponseEntity<?> mutateVirtualEdition(
            @PathVariable String externalId, @RequestBody List<String> inters) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(virtualService.mutateVirtualEditionInters(externalId, inters));
        } catch (LdoDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MainResponseDto(false, e.getMessage()));
        }

    }


}