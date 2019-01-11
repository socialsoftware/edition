package pt.ist.socialsoftware.edition.ldod;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;

public abstract class ControllersTestWithFragmentsLoading extends TestWithFragmentsLoading {
	protected MockMvc mockMvc;

	@Override
	@BeforeEach
	public void setUp() throws FileNotFoundException {
		super.setUp();

		generateMockMvc();
	}

	private void generateMockMvc() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(getController()).setControllerAdvice(new LdoDExceptionHandler())
				.addFilters(new TransactionFilter()).build();
	}

	protected abstract Object getController();

}
