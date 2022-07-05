package pt.ist.socialsoftware.edition.ldod.controller.signup;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.ControllersTestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.controller.SignupController;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;
import pt.ist.socialsoftware.edition.ldod.forms.SignupForm;
import pt.ist.socialsoftware.edition.ldod.utils.Emailer;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class SignupTest extends ControllersTestWithFragmentsLoading {

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    Emailer emailer;

    @InjectMocks
    SignupController signupController;

    @Override
    protected Object getController() {
        return this.signupController;
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
    public void signupFormTest() throws Exception {
        this.mockMvc.perform(get("/signup")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attribute("signupForm", notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void signupTest() throws Exception {
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn(anyString());

        this.mockMvc.perform(post("/signup")
                .param("username", "temp")
                .param("password", "123456")
                .param("firstName","Temp")
                .param("lastName","Temp")
                .param("email", "temp@temp.com")
                .param("conduct","true")
                .param("socialMediaService","")
                .sessionAttr("signupForm", new SignupForm()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signin"))
                .andExpect(model().attribute("message",notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void authorizeRegistrationTest() throws Exception {
        RegistrationToken token = new RegistrationToken("token", LdoD.getInstance().getUser("ars"));

        this.mockMvc.perform(get("/signup/registrationAuthorization")
                .param("token", token.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signin"))
                .andExpect(model().attribute("message",notNullValue()));
    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void confirmRegistrationTest() throws Exception {
        RegistrationToken token = new RegistrationToken("token", LdoD.getInstance().getUser("ars"));
        token.setAuthorized(true);

        this.mockMvc.perform(get("/signup/registrationConfirm")
                .param("token", token.getToken()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signin"));
    }

}
