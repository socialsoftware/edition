package pt.ist.socialsoftware.edition.ldod.controller.api;

import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;

import java.io.FileNotFoundException;
import java.util.Collections;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ClassificationGameControllerTest {
    @InjectMocks
    ClassificationGameController classificationGameController;

    protected MockMvc mockMvc;

    @BeforeAll
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void setUpAll() throws FileNotFoundException {
        TestLoadUtils.setUpDatabaseWithCorpus();

        String[] fragments = { "001.xml", "002.xml", "003.xml" };
        TestLoadUtils.loadFragments(fragments);

        TestLoadUtils.loadVirtualEditionsCorpus();
        String[] virtualEditionFragments = {"virtual-Fr001.xml", "virtual-Fr002.xml", "virtual-Fr003.xml"};
        TestLoadUtils.loadVirtualEditionFragments(virtualEditionFragments);
    }

    @AfterAll
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void tearDownAll() {
        TestLoadUtils.cleanDatabaseButCorpus();
    }

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.classificationGameController)
                .setControllerAdvice(new LdoDExceptionHandler()).addFilters(new TransactionFilter()).build();
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getActiveGamesTest() throws Exception {
        VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);
        LdoDUser user = LdoD.getInstance().getUser("ars");
        ClassificationGame classificationGame = new ClassificationGame(virtualEdition, "Description", DateTime.now(),
                virtualEdition.getAllDepthVirtualEditionInters().get(0), user);
        classificationGame.addParticipant("ars");

        this.mockMvc.perform(get("/api/services/ldod-game/{username}/active", "ars"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void endTest() throws Exception {
        VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);
        LdoDUser user = LdoD.getInstance().getUser("ars");
        ClassificationGame classificationGame = new ClassificationGame(virtualEdition, "Description", DateTime.now(),
                virtualEdition.getAllDepthVirtualEditionInters().get(0), user);
        classificationGame.addParticipant("ars");
        ClassificationGameParticipant participant = classificationGame.getParticipant("ars");

        ClassificationGameRound round = new ClassificationGameRound();
        round.setNumber(1);
        round.setRound(1);
        round.setTag("XXXX");
        round.setVote(1);
        round.setClassificationGameParticipant(participant);
        round.setTime(DateTime.now());
        participant.setScore(participant.getScore() + ClassificationGame.SUBMIT_TAG);

        this.mockMvc.perform(get("/api/services/ldod-game/end/{gameId}", classificationGame.getExternalId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails("ars")
    public void getLeaderboardTest() throws Exception {
        VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(Edition.ARCHIVE_EDITION_ACRONYM);
        LdoDUser user = LdoD.getInstance().getUser("ars");
        ClassificationGame classificationGame = new ClassificationGame(virtualEdition, "Description", DateTime.now(),
                virtualEdition.getAllDepthVirtualEditionInters().get(0), user);
        classificationGame.addParticipant("ars");
        ClassificationGameParticipant participant = classificationGame.getParticipant("ars");

        ClassificationGameRound round = new ClassificationGameRound();
        round.setNumber(1);
        round.setRound(1);
        round.setTag("XXXX");
        round.setVote(1);
        round.setClassificationGameParticipant(participant);
        round.setTime(DateTime.now());
        participant.setScore(participant.getScore() + ClassificationGame.SUBMIT_TAG);

        this.mockMvc.perform(get("/api/services/ldod-game/leaderboard"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
    }

}
