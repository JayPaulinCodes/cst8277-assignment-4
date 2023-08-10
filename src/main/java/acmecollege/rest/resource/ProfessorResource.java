/**
 * File:  ProfessorResource.java Course materials (23S) CST 8277
 *
 * @author Jacob Paulin
 *
 * Updated by:  Group 40
 *   41024610, Jacob, Paulin
 *   041053188, Taeung, Park
 *   041065803, Doyoung, Kim
 *   041053986, Dawon, Jun
 */
package acmecollege.rest.resource;

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.Professor;
import acmecollege.entity.SecurityUser;
import acmecollege.entity.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.soteria.WrappingCallerPrincipal;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import static acmecollege.utility.MyConstants.*;
import static acmecollege.utility.MyConstants.USER_ROLE;

@Path(PROFESSOR_SUBRESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProfessorResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;


    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getProfessors() {
        LOG.debug("retrieving all professors ...");
        List<Professor> professors = service.getAll(Professor.class, Professor.GET_ALL_PROFESSORS_QUERY_NAME);
        Response response = Response.ok(professors).build();
        return response;
    }

    @GET
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getProfessorById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("try to retrieve specific professor " + id);
        Response response = null;
        Professor professor = null;

        professor = service.getById(Professor.class, Professor.GET_PROFESSOR_BY_ID_QUERY_NAME, id);
        response = Response.status(professor == null ? Response.Status.NOT_FOUND : Response.Status.OK).entity(professor).build();

        return response;
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addProfessor(Professor newProfessor) {
        Response response = null;
        Professor newProfessorWithIdTimestamps = service.persistEntity(Professor.class, newProfessor);
        response = Response.ok(newProfessorWithIdTimestamps).build();
        return response;
    }

    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response deleteProfessor(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        Response response = null;
        service.deleteProfessorById(id);
        response = Response.status(Response.Status.OK).build();
        return response;
    }
}
