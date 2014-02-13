package pt.ist.socialsoftware.edition.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class TransactionFilter implements Filter {
	private final static Logger logger = LoggerFactory
			.getLogger(TransactionFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		atomicDoFilter(request, response, chain);

		// try {
		// Transaction.begin(false);
		// chain.doFilter(request, response);
		// } catch (Exception e) {
		// if (logger.isDebugEnabled()) {
		// logger.error("Exception: {}", e.getMessage(), e);
		// }
		// Transaction.abort();
		// }
		//
		// if (Transaction.isInTransaction()) {
		// try {
		// Transaction.commit();
		// } catch (Exception e) {
		// if (logger.isDebugEnabled()) {
		// logger.error("Exception: {}", e.getMessage(), e);
		// }
		//
		// Transaction.abort();
		// }
		// }
	}

	@Atomic(mode = TxMode.SPECULATIVE_READ)
	private void atomicDoFilter(ServletRequest request,
			ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}