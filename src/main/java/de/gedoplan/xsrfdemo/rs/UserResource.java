package de.gedoplan.xsrfdemo.rs;

import de.gedoplan.xsrfdemo.repository.MailRepository;
import de.gedoplan.xsrfdemo.security.JWTAuth;
import de.gedoplan.xsrfdemo.service.UserService;
import java.security.Principal;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 *
 * @author Dominik Mathmann
 */
@Path("/user")
@Stateless
public class UserResource {

    @Inject
    private MailRepository repo;

    @Inject
    private UserService userService;
    @Inject
    private Principal principal;

    @POST
    @Path("login")
    @PermitAll
    public Response login(@FormParam("us") String user, @FormParam("pw") String password) {
        String token = userService.login(user, password);

        if (token == null) {
            return Response.status(Response.Status.FORBIDDEN.getStatusCode(), "Invalid Login").build();
        } else {
            // Auth over Cookie = we will set a new cookie
            NewCookie cookie = new NewCookie(JWTAuth.GED_AUTH_COOKIE, token);
            return Response.ok().cookie(cookie).build();
            
            // Auth over Header-Bearer = we will send the token in the response, client should add it to every request
            //return Response.ok(token).build();
        }
    }

    @POST
    @RolesAllowed("users")
    public Response saveMail(@FormParam("mail") String newMail) {
        this.repo.setMailAdress(principal.getName(), newMail);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("users")
    public String getMail() {
        return this.repo.getMailAdress(principal.getName());
    }
}
