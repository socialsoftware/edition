package pt.ist.socialsoftware.edition.ldod;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import pt.ist.socialsoftware.edition.ldod.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.ldod.filters.TransactionFilter;

import java.io.FileNotFoundException;

public abstract class ControllersTestWithFragmentsLoading extends TestWithFragmentsLoading {
	protected MockMvc mockMvc;

	@Override
	@BeforeEach
	public void setUp() throws FileNotFoundException {
		TestLoadUtils.loadFragments(fragmentsToLoad4Test());

		populate4Test();

		generateMockMvc();
	}

	private void generateMockMvc() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(getController()).setControllerAdvice(new LdoDExceptionHandler())
				.addFilters(new TransactionFilter()).build();
	}

	protected abstract Object getController();

}
