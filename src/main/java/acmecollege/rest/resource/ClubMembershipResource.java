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
import acmecollege.entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
    @Path("/{membershipCardId}/{studentClubId}")
    public Response addClubMembership(@PathParam("membershipCardId") int membershipCardId,
                                      @PathParam("studentClubId") int studentClubId, ClubMembership newClubMembership) {
        LOG.debug("Adding a new club membership = {}", newClubMembership);
        MembershipCard membershipCard;
        StudentClub studentClub;

        try {
            membershipCard = service.getById(
                    MembershipCard.class,
                    MembershipCard.ID_CARD_QUERY_NAME,
                    membershipCardId);
        } catch (Exception e) {
            e.printStackTrace();
            HttpErrorResponse err = new HttpErrorResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    String.format("No membership card found with id %d", membershipCardId));
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }

        try {
            studentClub = service.getById(
                    StudentClub.class,
                    StudentClub.SPECIFIC_STUDENT_CLUB_QUERY_NAME,
                    studentClubId);
        } catch (Exception e) {
            e.printStackTrace();
            HttpErrorResponse err = new HttpErrorResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    String.format("No student club found with id %d", studentClubId));
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }

        service.persistClubMembership(newClubMembership, studentClubId, membershipCardId);

        return Response.ok(newClubMembership).build();
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
