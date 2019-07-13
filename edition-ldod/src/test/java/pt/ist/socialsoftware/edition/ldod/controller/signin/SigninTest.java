package pt.ist.socialsoftware.edition.ldod.controller.signin;

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
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.frontend.user.SigninController;
import pt.ist.socialsoftware.edition.ldod.utils.controller.LdoDExceptionHandler;

import java.io.FileNotFoundException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
