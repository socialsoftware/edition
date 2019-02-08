package pt.ist.socialsoftware.edition.ldod.controller.signin;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import pt.ist.socialsoftware.edition.ldod.ControllersTestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.controller.SigninController;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;

import java.io.FileNotFoundException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class SigninTest extends ControllersTestWithFragmentsLoading {

    @InjectMocks
    SigninController signinController;

    @Override
    protected Object getController() {
        return this.signinController;
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


    @Override
    @BeforeEach
    public void setUp() throws FileNotFoundException {
        super.setUp();

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view/");
        viewResolver.setSuffix(".jsp");

        this.mockMvc = MockMvcBuilders.standaloneSetup(getController()).setViewResolvers(viewResolver)
                .setControllerAdvice(new LdoDExceptionHandler())
                .addFilters(new TransactionFilter()).build();
    }

    @Test
    public void signInPageTest() throws Exception {

        this.mockMvc.perform(get("/signin")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));
    }
}
