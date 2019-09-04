package de.gedoplan.xsrfdemo.filter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import de.gedoplan.xsrfdemo.service.XSRFTokenService;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Dominik Mathmann
 */
//@WebFilter(urlPatterns = {"/rest/*"})
public class XSRFFilter implements Filter {

    @Inject
    private XSRFTokenService service;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getMethod().equalsIgnoreCase("POST") && !service.isValid(req)) {
            res.sendError(res.SC_FORBIDDEN, "XSRF Protection");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
