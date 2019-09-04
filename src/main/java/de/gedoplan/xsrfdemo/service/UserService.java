package de.gedoplan.xsrfdemo.service;

import de.gedoplan.xsrfdemo.security.JWTService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;

/**
 *
 * @author Dominik Mathmann
 */
@ApplicationScoped
public class UserService {

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Inject
    private JWTService jwtService;

    public String login(String username, String password) {
        final CredentialValidationResult validate = this.identityStoreHandler.validate(new UsernamePasswordCredential(username, password));
        if (validate.getStatus() == CredentialValidationResult.Status.VALID) {
            return jwtService.generateToken(validate.getCallerPrincipal().getName(), validate.getCallerGroups());
        }
        
        return null;
    }
}
