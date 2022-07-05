package pt.ist.socialsoftware.edition.ldod.controller.home;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pt.ist.socialsoftware.edition.ldod.ControllersTestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.config.Application;
import pt.ist.socialsoftware.edition.ldod.controller.HomeController;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class HomeTest extends ControllersTestWithFragmentsLoading {

	@InjectMocks
	HomeController homeController;

	@Override
	protected String[] fragmentsToLoad4Test() {
		return new String[0];
	}

	@Override
	protected Object getController() {
		return this.homeController;
	}

	@Override
	protected void populate4Test() {
	}

	@Override
	protected void unpopulate4Test() {
	}

	@Test
	public void getHomeTest() throws Exception {
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("home"));
	}
}
