package pt.ist.socialsoftware.edition.ldod.controller.about;

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
import pt.ist.socialsoftware.edition.ldod.controller.AboutController;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class AboutTest extends ControllersTestWithFragmentsLoading {

	@InjectMocks
	private AboutController aboutController;

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
	protected Object getController() {
		return this.aboutController;
	}

	@Test
	public void showArchive() throws Exception {
		this.mockMvc.perform(get("/about/archive")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("about/archive-main"));
	}

	@Test
	public void showVideos() throws Exception {
		this.mockMvc.perform(get("/about/videos")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("about/videos-main"));
	}

	@Test
	public void showFAQ() throws Exception {
		this.mockMvc.perform(get("/about/faq")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("about/faq-main"));
	}

	@Test
	public void showEncoding() throws Exception {
		this.mockMvc.perform(get("/about/encoding")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("about/encoding-main"));
	}

	@Test
	public void showArticles() throws Exception {
		this.mockMvc.perform(get("/about/articles")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("about/articles-main"));
	}

	@Test
	public void showConduct() throws Exception {
		this.mockMvc.perform(get("/about/conduct")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("about/conduct-main"));
	}

	@Test
	public void showPrivacy() throws Exception {
		this.mockMvc.perform(get("/about/privacy")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("about/privacy-main"));
	}

	@Test
	public void showTeam() throws Exception {
		this.mockMvc.perform(get("/about/team")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("about/team-main"));
	}

	@Test
	public void showAcknowledgements() throws Exception {
		this.mockMvc.perform(get("/about/acknowledgements")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("about/acknowledgements-main"));
	}

	@Test
	public void showContact() throws Exception {
		this.mockMvc.perform(get("/about/contact")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("about/contact-main"));
	}

	@Test
	public void showCopyright() throws Exception {
		this.mockMvc.perform(get("/about/copyright")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("about/copyright-main"));
	}

}
