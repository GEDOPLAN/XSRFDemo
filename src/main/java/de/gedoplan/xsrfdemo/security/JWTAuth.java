package de.gedoplan.xsrfdemo.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Dominik Mathmann
 */
@RequestScoped
public class JWTAuth implements HttpAuthenticationMechanism {

    public static final String GED_AUTH_COOKIE = "GED-AUTH";

    @Inject
    private JWTService jwtService;

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {
        AuthenticationStatus result;

        if (httpMessageContext.isProtected()) {
            String auth = getTokenFromCookie(request);
            if (auth == null) {
                auth = getTokenFromHeader(request);
            }

            if (auth == null) {
                result = httpMessageContext.responseUnauthorized();
            } else {
                final DecodedJWT validateToken = jwtService.validateToken(auth);
                if (validateToken == null) {
                    result = httpMessageContext.responseUnauthorized();
                } else {
                    Set<String> groups = new HashSet<>();
                    groups.addAll(validateToken.getClaim(JWTService.GROUP_CLAIM).asList(String.class));
                    result = httpMessageContext.notifyContainerAboutLogin(validateToken.getIssuer(), groups);
                }
            }
        } else {
            result = httpMessageContext.doNothing();
        }

        return result;

    }

    @Override
    public AuthenticationStatus secureResponse(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {
        return HttpAuthenticationMechanism.super.secureResponse(request, response, httpMessageContext); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cleanSubject(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) {
        HttpAuthenticationMechanism.super.cleanSubject(request, response, httpMessageContext); //To change body of generated methods, choose Tools | Templates.
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        String result = null;

        if (request.getCookies() != null) {
            final Optional<Cookie> findFirst = Stream.of(request.getCookies()).filter(c -> c.getName().equals(GED_AUTH_COOKIE)).findFirst();
            if (findFirst.isPresent()) {
                result = findFirst.get().getValue();
            }
        }

        return result;
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String result = null;

        String auth = request.getHeader("Authorization");
        if (auth != null) {
            if (auth.startsWith("Bearer")) {
                auth = auth.replace("Bearer ", "");
            }
            result = auth;
        }

        return result;
    }
}
