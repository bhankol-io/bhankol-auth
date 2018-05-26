package com.bhankol.application.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by pravingosavi on 21/05/18.
 */

@Component
public class RESTCorsFilter extends OncePerRequestFilter {

	private final Logger LOG = LoggerFactory.getLogger(RESTCorsFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
		LOG.info("Adding CORS Headers ........................");
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
		res.setHeader("Access-Control-Max-Age", "3600");
		res.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Content-Type, Authorization, Access-Control-Allow-Methods, X-ConnectionId, access_token, x-requested-with");
		chain.doFilter(req, res);
	}


	@Override
	public void destroy() {
	}


}
