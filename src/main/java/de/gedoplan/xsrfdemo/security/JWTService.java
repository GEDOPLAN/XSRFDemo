package de.gedoplan.xsrfdemo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Dominik Mathmann
 */
@ApplicationScoped
public class JWTService {

    private final Algorithm ALG = Algorithm.HMAC256("mysuper!secret");

    public static final String GROUP_CLAIM = "GROUPS";

    public String generateToken(String username, Set<String> groups) {
        try {
            String token = JWT.create()
                    .withIssuer(username)
                    .withArrayClaim(GROUP_CLAIM, groups.toArray(new String[0]))
                    .sign(ALG);

            return token;
        } catch (JWTCreationException exception) {
            exception.printStackTrace();
            throw new RuntimeException("Error while creating Token: " + exception.getMessage());
        }
    }

    public DecodedJWT validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALG)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt;
        } catch (JWTVerificationException exception) {
            exception.printStackTrace();
            return null;
        }

    }
}
