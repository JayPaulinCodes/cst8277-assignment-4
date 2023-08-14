/**
 * File:  CourseResource.java Course materials (23S) CST 8277
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
import acmecollege.entity.Course;
import acmecollege.entity.Professor;
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
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;

@Path(COURSE_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;


    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getCourses() {
        LOG.debug("retrieving all courses ...");
        List<Course> courses = service.getAll(Course.class, Course.ALL_COURSES_QUERY);
        Response response = Response.ok(courses).build();
        return response;
    }

    @GET
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getCourseById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("try to retrieve specific course " + id);
        Response response = null;
        Course course = null;

        course = service.getById(Course.class, Course.GET_COURSE_BY_ID_QUERY, id);
        response = Response.status(course == null ? Response.Status.NOT_FOUND : Response.Status.OK).entity(course).build();

        return response;
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addCourse(Course newCourse) {
        Response response = null;
        Course newCourseWithIdTimestamps = service.persistEntity(Course.class, newCourse);
        response = Response.ok(newCourseWithIdTimestamps).build();
        return response;
    }

    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response deleteCourse(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        Response response = null;
        service.deleteCourseById(id);
        response = Response.ok().build();
        return response;
    }
}
