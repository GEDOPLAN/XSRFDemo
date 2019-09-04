package de.gedoplan.xsrfdemo.rs;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author Dominik Mathmann
 */
@ApplicationPath("rest")
public class ApplicationConfig extends Application{

    public ApplicationConfig() {
        System.out.println("de.gedoplan.xsrfdemo.rs.ApplicationConfig.<init>()");
    }

    
    
    
}
