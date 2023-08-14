/**
 * File:  StudentClubResource.java Course materials (23S) CST 8277
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.soteria.WrappingCallerPrincipal;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.USER_ROLE;

@Path("membershipcard")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MembershipCardResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;

    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getMembershipCards() {
        LOG.debug("Retrieving all membership cards...");
        List<MembershipCard> results = service.getAll(MembershipCard.class, MembershipCard.ALL_CARDS_QUERY_NAME);
        LOG.debug("Membership cards found = {}", results);
        Response response = Response.ok(results).build();
        return response;
    }

    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path("/{membershipCardId}")
    public Response getMembershipCardById(@PathParam("membershipCardId") int membershipCardId) {
        LOG.debug("Retrieving membership card with id = {}", membershipCardId);
        MembershipCard membershipCard;
        Response response;

        if (sc.isCallerInRole(ADMIN_ROLE)) {

            try {
                membershipCard = service.getById(MembershipCard.class, MembershipCard.ID_CARD_QUERY_NAME, membershipCardId);
                response = Response.ok(membershipCard).build();
            } catch (Exception e) {
                e.printStackTrace();
                HttpErrorResponse err = new HttpErrorResponse(
                        Response.Status.BAD_REQUEST.getStatusCode(),
                        String.format("No membership card found with id %d", membershipCardId));
                return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
            }

        } else if (sc.isCallerInRole(USER_ROLE)) {

            try {
                membershipCard = service.getById(MembershipCard.class, MembershipCard.ID_CARD_QUERY_NAME, membershipCardId);
            } catch (Exception e) {
                e.printStackTrace();
                HttpErrorResponse err = new HttpErrorResponse(
                        Response.Status.BAD_REQUEST.getStatusCode(),
                        String.format("No membership card found with id %d", membershipCardId));
                return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
            }

            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal) sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser) wCallerPrincipal.getWrapped();
            Student student = sUser.getStudent();

            if (!student.getMembershipCards().stream().map(PojoBase::getId).toList().contains(membershipCardId)) {
                throw new ForbiddenException("User trying to access resource it does not own");
            } else {
                response = Response.ok(membershipCard).build();
            }

        } else {
            response = Response.status(Response.Status.BAD_REQUEST).build();
        }

        return response;
    }

    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{membershipCardId}")
    public Response deleteMembershipCard(@PathParam("membershipCardId") int membershipCardId) {
        LOG.debug("Deleting membership card with id = {}", membershipCardId);
        service.deleteMembershipCardById(membershipCardId);
        return Response.ok().build();
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{studentId}")
    public Response addMembershipCard(@PathParam("studentId") int studentId, MembershipCard newMembershipCard) {
        LOG.debug("Adding a new membership card = {}", newMembershipCard);
        MembershipCard membershipCard = service.persistMembershipCard(newMembershipCard, studentId);
        return Response.ok(membershipCard).build();
    }
}
