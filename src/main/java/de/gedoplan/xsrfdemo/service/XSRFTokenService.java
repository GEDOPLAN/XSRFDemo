package de.gedoplan.xsrfdemo.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dominik Mathmann
 */
@ApplicationScoped
public class XSRFTokenService {

    public static final String KEY_CSRF_TOKEN = "X-XSRF-TOKEN’";

    @Inject
    private Principal principal;

    public boolean isValid(HttpServletRequest req) {
        return getToken().equals(req.getHeader(KEY_CSRF_TOKEN));
    }

    public String getToken() {
        return md5(principal.getName() + "#Secret!Salt#");
    }

    private String md5(String input) {

        String md5 = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes(), 0, input.length());
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }
}
