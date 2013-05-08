package pt.ist.socialsoftware.edition.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import jvstm.Transaction;

public class TransactionFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			Transaction.begin(false);
			chain.doFilter(request, response);
		} catch (Exception e) {
			Transaction.abort();
			throw (ServletException) e;
		}

		if (Transaction.isInTransaction()) {
			try {
				Transaction.commit();
			} catch (Exception e) {
				Transaction.abort();
				throw (ServletException) e;
			}
		}
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