package pvytykac.net.scrape.server;

import java.io.IOException;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class UserAgentFilter implements Filter {

	private final Set<String> supportedVersions;

	public UserAgentFilter(Set<String> supportedVersions) {
		this.supportedVersions = supportedVersions;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String agent = ((HttpServletRequest) request).getHeader("User-Agent");

		if (agent != null && supportedVersions.stream().anyMatch(version -> ("std-client " + version).equals(agent))) {
			chain.doFilter(request, response);
		} else {
			String error = new JSONObject()
					.put("error", "unsupported client version '" + agent + "'")
					.toString();

			((HttpServletResponse) response).setStatus(400);
			response.getWriter().write(error);
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {}
}
