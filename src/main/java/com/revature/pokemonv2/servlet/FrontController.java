package com.revature.pokemonv2.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.pokemonv2.dispatcher.MasterDispatcher;

public class FrontController extends HttpServlet{
	private static final long serialVersionUID = 4826138980180601133L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		
		if (uri.contains("/servlet/"))
			MasterDispatcher.process(request, response);
		else if (uri.equals("/PokemonCollector/") || uri.equals("/PokemonCollector"))
			request.getRequestDispatcher("/ng/index.html").forward(request, response);
		else
			super.doGet(request, response);
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		
		if (uri.contains("/servlet/"))
			MasterDispatcher.process(request, response);
		else
			super.doPost(request, response);
	}
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		
		if (uri.contains("/servlet/"))
			MasterDispatcher.process(request, response);
		else
			super.doPut(request, response);
	}
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		
		if (uri.contains("/servlet/"))
			MasterDispatcher.process(request, response);
		else
			super.doDelete(request, response);
	}
}

