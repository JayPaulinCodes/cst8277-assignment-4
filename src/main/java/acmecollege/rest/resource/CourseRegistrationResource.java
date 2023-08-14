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
import org.apache.logging.log4j.Level;
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

import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.COURSE_REGISTRATION_RESOURCE_NAME;

@Path(COURSE_REGISTRATION_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseRegistrationResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;

    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getCourseRegistrations() {
        LOG.debug("retrieving all course registrations ...");
        List<CourseRegistration> results = service.getAll(CourseRegistration.class, CourseRegistration.FIND_ALL);
        Response response = Response.ok(results).build();
        return response;
    }

    @GET
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{studentId}/{courseId}")
    public Response getCourseRegistrationById(@PathParam("studentId") int studentId, @PathParam("courseId") int courseId) {
        LOG.debug("Retrieving course registration with ids = {}, {}", studentId, courseId);
        CourseRegistration result = service.getCourseRegistrationById(studentId, courseId);
        Response response = Response.ok(result).build();
        return response;
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{studentId}/{courseId}")
    public Response addCourseRegistration(@PathParam("studentId") int studentId, @PathParam("courseId") int courseId, CourseRegistration courseRegistration) {
        LOG.debug("Adding course registration with ids = {}, {} and body = {}", studentId, courseId, courseRegistration);
        LOG.log(Level.INFO, "HERE");

        Student student;
        Course course;

        try {
            student = service.getById(
                    Student.class,
                    Student.QUERY_STUDENT_BY_ID,
                    studentId);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getStackTrace());
            HttpErrorResponse err = new HttpErrorResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    String.format("No student found with id %d", studentId));
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }

        try {
            course = service.getById(
                    Course.class,
                    Course.GET_COURSE_BY_ID_QUERY,
                    courseId);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getStackTrace());
            HttpErrorResponse err = new HttpErrorResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    String.format("No course found with id %d", courseId));
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }

        service.persistCourseRegistration(courseRegistration, studentId, courseId);

        return Response.ok(courseRegistration).build();
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{studentId}/{courseId}/{professorId}")
    public Response addCourseRegistrationWithProfessor(@PathParam("studentId") int studentId, @PathParam("courseId") int courseId,
                                          @PathParam("professorId") int professorId, CourseRegistration courseRegistration) {
        LOG.debug("Adding course registration with ids = {}, {}, {} and body = {}", studentId, courseId, professorId, courseRegistration);

        Student student;
        Course course;
        Professor professor;

        try {
            student = service.getById(
                    Student.class,
                    Student.QUERY_STUDENT_BY_ID,
                    studentId);
        } catch (Exception e) {
            e.printStackTrace();
            HttpErrorResponse err = new HttpErrorResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    String.format("No student found with id %d", studentId));
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }

        try {
            course = service.getById(
                    Course.class,
                    Course.GET_COURSE_BY_ID_QUERY,
                    courseId);
        } catch (Exception e) {
            e.printStackTrace();
            HttpErrorResponse err = new HttpErrorResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    String.format("No course found with id %d", courseId));
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }

        try {
            professor = service.getById(
                    Professor.class,
                    Professor.GET_PROFESSOR_BY_ID_QUERY_NAME,
                    professorId);
        } catch (Exception e) {
            e.printStackTrace();
            HttpErrorResponse err = new HttpErrorResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    String.format("No professor found with id %d", professorId));
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }

        service.persistCourseRegistration(courseRegistration, studentId, courseId, professorId);

        return Response.ok(courseRegistration).build();
    }

    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{studentId}/{courseId}")
    public Response deleteCourseRegistration(@PathParam("studentId") int studentId, @PathParam("courseId") int courseId) {
        LOG.debug("Deleting course registration with ids = {}, {}", studentId, courseId);
        service.deleteCourseRegistration(studentId, courseId);
        Response response = Response.ok().build();
        return response;
    }
}
