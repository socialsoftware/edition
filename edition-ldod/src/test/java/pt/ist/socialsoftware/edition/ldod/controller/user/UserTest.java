package pt.ist.socialsoftware.edition.ldod.controller.user;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.ControllersTestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.config.WebSecurityConfig;
import pt.ist.socialsoftware.edition.ldod.controller.UserController;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.Role;
import pt.ist.socialsoftware.edition.ldod.forms.ChangePasswordForm;
import pt.ist.socialsoftware.edition.ldod.utils.Bootstrap;
import pt.ist.socialsoftware.edition.ldod.validator.ChangePasswordValidator;

import java.io.FileNotFoundException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class UserTest extends ControllersTestWithFragmentsLoading {

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserController userController;

    @Override
    protected Object getController() {
        return this.userController;
    }

    @Override
    @Atomic(mode = Atomic.TxMode.WRITE)
    protected void populate4Test() {

    }

    @Override
    @Atomic(mode = Atomic.TxMode.WRITE)
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
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(true);
    }

    @BeforeAll
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void setUpAll() {

        // Temp user for use in changePasswordTest
        // Must be created in static beforeAll context due to requirements of the WithUserDetails annotation

        if (LdoD.getInstance() == null)
            Bootstrap.initializeSystem();

        Role user = Role.getRole(Role.RoleType.ROLE_USER);
        Role admin = Role.getRole(Role.RoleType.ROLE_ADMIN);

        LdoDUser temp = new LdoDUser(LdoD.getInstance(), "temp", "$2a$11$FqP6hxx1OzHeP/MHm8Ccier/ZQjEY5opPTih37DR6nQE1XFc0lzqW",
                "Temp", "Temp", "temp@temp.temp");

        temp.setEnabled(true);
        temp.addRoles(user);
        temp.addRoles(admin);

    }

    @AfterAll
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void tearDownAll() {
        LdoD.getInstance().getUser("temp").remove();
    }

    @Test
    public void getPasswordFormTest() throws Exception {

        this.mockMvc.perform(get("/user/changePassword")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/changePassword"))
                .andExpect(model().attribute("changePasswordForm",notNullValue()));

    }

    @Test
    @Atomic(mode = Atomic.TxMode.WRITE)
    @WithUserDetails(value = "temp")
    public void changePasswordTest() throws Exception {

        this.mockMvc.perform(post("/user/changePassword")
                .param("username","temp")
                .param("currentPassword", "temp")
                .param("newPassword", "123456")
                .param("retypedPassword", "123456")
                .sessionAttr("userForm",new ChangePasswordForm()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

    }
}
