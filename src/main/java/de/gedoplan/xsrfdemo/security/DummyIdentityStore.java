package de.gedoplan.xsrfdemo.security;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

/**
 * Auth-Quelle.
 *
 * @author Dominik Mathmann
 */
@RequestScoped
public class DummyIdentityStore implements IdentityStore {

    private final String VALID_PASSWORD = "admin";
    private final String VALID_USER = "admin";

    @Override
    public Set<ValidationType> validationTypes() {
        return EnumSet.of(ValidationType.PROVIDE_GROUPS, ValidationType.VALIDATE);
    }

    @Override
    public CredentialValidationResult validate(Credential credential) {
        CredentialValidationResult result;

        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePassword = (UsernamePasswordCredential) credential;

            if (usernamePassword.getPassword().compareTo(VALID_PASSWORD) && usernamePassword.getCaller().equals(VALID_USER)) {
                final String username = usernamePassword.getCaller();
                result = new CredentialValidationResult(usernamePassword.getCaller(), getGroups(username));
            } else {
                result = CredentialValidationResult.INVALID_RESULT;
            }
        } else {
            result = CredentialValidationResult.INVALID_RESULT;
        }
        return result;
    }
    
    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
       return getGroups(validationResult.getCallerPrincipal().getName());
    }
    
    private Set<String> getGroups(String user){
        // read groups for user ... or just return this:
        return Collections.singleton("users");        
    }

}
