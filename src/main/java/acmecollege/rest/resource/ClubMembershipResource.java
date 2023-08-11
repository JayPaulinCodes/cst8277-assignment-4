/**
 * File:  StudentResource.java Course materials (23S) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 *
 * Updated by:  Group 40
 *   41024610, Jacob, Paulin
 *   041053188, Taeung, Park
 *   041065803, Doyoung, Kim
 *   041053986, Dawon, Jun
 */
package acmecollege.rest.resource;

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.ClubMembership;
import acmecollege.entity.Course;
import acmecollege.entity.Student;
import acmecollege.entity.StudentClub;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import static acmecollege.utility.MyConstants.*;

@Path(CLUB_MEMBERSHIP_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClubMembershipResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;

    @GET
    public Response getClubMemberships() {
        LOG.debug("retrieving all club memberships ...");
        List<ClubMembership> clubMemberships = service.getAll(ClubMembership.class, ClubMembership.FIND_ALL);
        Response response = Response.ok(clubMemberships).build();
        return response;
    }

    @GET
    @Path("/{clubMembershipId}")
    public Response getStudentClubById(@PathParam("clubMembershipId") int clubMembershipId) {
        LOG.debug("Retrieving club membership with id = {}", clubMembershipId);
        ClubMembership clubMembership = service.getClubMembershipById(clubMembershipId);
        Response response = Response.ok(clubMembership).build();
        return response;
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addClubMembership(ClubMembership newClubMembership) {
        LOG.debug("Adding a new club membership = {}", newClubMembership);
        Response response = null;
        ClubMembership newClubMembershipWithIdTimestamps = service.persistClubMembership(newClubMembership);
        response = Response.ok(newClubMembershipWithIdTimestamps).build();
        return response;
    }

    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{clubMembershipId}")
    public Response deleteStudentClub(@PathParam("clubMembershipId") int clubMembershipId) {
        LOG.debug("Deleting club membership with id = {}", clubMembershipId);
        service.deleteClubMembershipById(clubMembershipId);
        Response response = Response.ok().build();
        return response;
    }
}
