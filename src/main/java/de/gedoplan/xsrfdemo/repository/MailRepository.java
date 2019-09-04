package de.gedoplan.xsrfdemo.repository;

import java.util.HashMap;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Dominik Mathmann
 */
@ApplicationScoped
public class MailRepository {

    private HashMap<String, String> store = new HashMap<>();

    public String getMailAdress(String user) {
        if (!this.store.containsKey(user)) {
            this.store.put(user, "dominik.matmann@gedoplan.de");
        }

        return this.store.get(user);

    }

    public void setMailAdress(String username, String value) {
        this.store.put(username, value);
    }
}
