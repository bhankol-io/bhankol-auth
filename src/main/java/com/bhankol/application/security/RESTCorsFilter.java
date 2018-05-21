package com.bhankol.application.security;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RESTCorsFilter implements Filter {
	private String clientURL="http://localhost:5010";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
	    
	    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
		    response.setHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
		    response.setHeader("Access-Control-Max-Age", "3600");
		    response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Content-Type, Authorization, Access-Control-Allow-Methods, X-ConnectionId, access_token, x-requested-with");
	    } else {
	    	chain.doFilter(req, res);
	    }
	}

	@Override
	public void destroy() {
	}

	public String getClientURL() {
		return clientURL;
	}

	public void setClientURL(String clientURL) {
		this.clientURL = clientURL;
	}
}
