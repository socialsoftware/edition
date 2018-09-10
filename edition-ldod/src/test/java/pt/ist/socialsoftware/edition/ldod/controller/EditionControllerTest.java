package pt.ist.socialsoftware.edition.ldod.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class EditionControllerTest extends ControllerTest {

    private EditionController editionController = new EditionController();

    @BeforeEach
    void configuration() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders
                .standaloneSetup(editionController)
//                .alwaysDo(print())
                .build();
    }

    @Test
    void achronymList() throws Exception {
        Map model = mvc.perform(get("/edition/acronym/JPC")).andReturn().getModelAndView().getModel();

        assertNotNull(model.get("edition"), "Edition not found");
        assertNull(model.get("heteronym"), "Heteronym found");
    }

    @Test
    void userContribution() throws Exception {
        Map model = mvc.perform(get("/edition/user/ars")).andReturn().getModelAndView().getModel();

        assertNotNull(model.get("user"), "User not found");
    }

//    @Test
//    void taxonomy() throws Exception {
//        Map model = mvc.perform(get("/edition/acronym/LdoD-JPC-anot/taxonomy")).andReturn().getModelAndView().getModel();
//
//        assertNotNull(model.get("taxonomy"), "Taxonomy not found");
//    }
//
//    @Test
//    void category() throws Exception {
//        Map model = mvc.perform(get("/edition/acronym/LdoD-JPC-anot/category/HAECKEL")).andReturn().getModelAndView().getModel();
//
//        assertNotNull(model.get("category"), "Category not found");
//    }
}