package pt.ist.socialsoftware.edition.ldod.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebAppConfiguration
@SpringBootTest
public class VirtualEditionControllerTest extends ControllerTest{


    private VirtualEditionController virtualEditionController = new VirtualEditionController();
    private AdminController adminController = new AdminController();

    @MockBean
    FilterChainProxy filterChainProxy;

    @BeforeEach
    public void configuration() {



        mvc = MockMvcBuilders
                .standaloneSetup(virtualEditionController, adminController)
//                .alwaysDo(print())
//                .webAppContextSetup(context)
//                .apply(springSecurity())
                .build();

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test1() throws Exception {
//
        MvcResult result = mvc
                .perform(get("/virtualeditions")
                .with(user("user")
                .password("user")))
                .andReturn();
    }

}
