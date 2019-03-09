package com.revature.pokemonv2.filter;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Authentication filter for the Java Web Token, if an end point that requires
 * an authorization token, this filter will check if there is a token, if not
 * throws an unauthorized error.
 */
public class AuthenticationFilter {

	private ObjectMapper mapper;

	/**
	 * Public constructor for the authentication filter
	 */
	public AuthenticationFilter() {
	}

	/**
	 * Destroy method
	 */
	public void destroy() {
	}

	/**
	 * Checks if the header of the request is "Authorization", if null will respond
	 * with an unauthorized status.
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		//Check to see if there is a header "Authorization"
		final String token = httpRequest.getHeader("Authorization");
		
		//There is no token, the user is not logged in
		if (token == null) {
			response.resetBuffer();
			response.setContentType("application/json");
			response.getOutputStream().write(mapper
					.writeValueAsBytes(Collections.singletonMap("message", "You must log in to view this resource")));
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		chain.doFilter(httpRequest, httpResponse);
	}

	/**
	 * Initializes the mapper object on startup.
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		mapper = new ObjectMapper();
	}

}
