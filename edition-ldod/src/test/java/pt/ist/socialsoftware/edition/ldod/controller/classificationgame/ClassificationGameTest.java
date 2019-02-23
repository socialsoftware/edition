package pt.ist.socialsoftware.edition.ldod.controller.classificationgame;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.ist.socialsoftware.edition.ldod.ControllersTestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.ClassificationGameHomeController;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ClassificationGameTest extends ControllersTestWithFragmentsLoading {

    @InjectMocks
    ClassificationGameHomeController classificationGameHomeController;


    @Override
    protected Object getController() {
        return this.classificationGameHomeController;
    }

    @Override
    protected void populate4Test() {

    }

    @Override
    protected void unpopulate4Test() {

    }

    @Override
    protected String[] fragmentsToLoad4Test() {
        return new String[0];
    }

    @Test
    public void gamePageTest() throws Exception {
        this.mockMvc.perform(get("/classificationGames")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("virtual/classificationGames"));
    }

    @Test
    public void ganmeClientTest() throws Exception {
        this.mockMvc.perform(get("/classification-game")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("classificationGame"));
    }
}
