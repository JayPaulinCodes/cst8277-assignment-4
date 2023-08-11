/**
 * File:  TestACMECollegeSystem.java
 * Course materials (23S) CST 8277
 * Teddy Yap
 * (Original Author) Mike Norman
 *
 * @date 2020 10
 *
 * (Modified) @author Student Name
 */
package acmecollege;

import static acmecollege.utility.MyConstants.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import acmecollege.entity.Course;
import acmecollege.entity.Professor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import acmecollege.entity.Student;

@SuppressWarnings("unused")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestACMECollegeSystem {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);

    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;

    // Test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;

    @BeforeAll
    public static void oneTimeSetUp() throws Exception {
        logger.debug("oneTimeSetUp");
        uri = UriBuilder
            .fromUri(APPLICATION_CONTEXT_ROOT + APPLICATION_API_VERSION)
            .scheme(HTTP_SCHEMA)
            .host(HOST)
            .port(PORT)
            .build();
        adminAuth = HttpAuthenticationFeature.basic(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_PASSWORD);
        userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER, DEFAULT_USER_PASSWORD);
    }

    protected WebTarget webTarget;
    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
    }

    @Test
    public void test00_all_students_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<Student> students = response.readEntity(new GenericType<List<Student>>(){});
        assertThat(students, is(not(empty())));
        assertThat(students, hasSize(1));
    }



    @Test
    public void test01_get_all_students_admin() {
        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test02_get_all_students_user() {
        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(403));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test03_get_student_by_id_admin() {
        Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME+"/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test04_get_student_by_id_user() {
        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME+"/2")
                .request()
                .get();
        assertThat(response.getStatus(), is(403));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test05_get_own_student_by_id_user() {
        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME+"/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test06_post_new_student_admin() {
        Student dummyStudent = new Student();
        dummyStudent.setFirstName("Michael");
        dummyStudent.setLastName("Smith");

        Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .post(Entity.json(dummyStudent));
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test07_post_new_student_user() {
        Student dummyStudent = new Student();
        dummyStudent.setFirstName("Michael");
        dummyStudent.setLastName("Smith");

        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .post(Entity.json(dummyStudent));
        assertThat(response.getStatus(), is(403));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test08_delete_student_by_id_admin() {
        Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME+"/2")
                .request()
                .delete();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test09_delete_student_by_id_user() {
        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME+"/2")
                .request()
                .delete();
        assertThat(response.getStatus(), is(403));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test10_get_all_professors_admin() {
        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path(PROFESSOR_SUBRESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test11_get_all_professors_user() {
        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(PROFESSOR_SUBRESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(403));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test12_get_professor_by_id_admin() {
        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path(PROFESSOR_SUBRESOURCE_NAME+"/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test13_get_professor_by_id_user() {
        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(PROFESSOR_SUBRESOURCE_NAME+"/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(403));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test14_post_new_professor_admin() {
        Professor dummyProfessor = new Professor();
        dummyProfessor.setFirstName("Charles");
        dummyProfessor.setLastName("Xavier");
        dummyProfessor.setDepartment("Physics");

        Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(PROFESSOR_SUBRESOURCE_NAME)
                .request()
                .post(Entity.json(dummyProfessor));
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test15_post_new_professor_user() {
        Professor dummyProfessor = new Professor();
        dummyProfessor.setFirstName("Charles");
        dummyProfessor.setLastName("Xavier");
        dummyProfessor.setDepartment("Physics");

        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(PROFESSOR_SUBRESOURCE_NAME)
                .request()
                .post(Entity.json(dummyProfessor));
        assertThat(response.getStatus(), is(403));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test15_delete_professor_by_id_admin() {
        Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(PROFESSOR_SUBRESOURCE_NAME+"/2")
                .request()
                .delete();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test16_delete_professor_by_id_user() {
        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(PROFESSOR_SUBRESOURCE_NAME+"/2")
                .request()
                .delete();
        assertThat(response.getStatus(), is(403));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test17_get_all_courses_admin() {
        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test18_get_all_courses_user() {
        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(403));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test19_get_course_by_id_admin() {
        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME+"/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test20_get_course_by_id_user() {
        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME+"/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(403));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test21_post_new_course_admin() {
        Course dummyCourse = new Course();
        dummyCourse.setCourseCode("CST8101");
        dummyCourse.setCourseTitle("Computer Essentials");
        dummyCourse.setYear(2022);
        dummyCourse.setSemester("WINTER");
        dummyCourse.setCreditUnits(3);
        dummyCourse.setOnline((byte) 0);

        Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME)
                .request()
                .post(Entity.json(dummyCourse));
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test22_post_new_course_user() {
        Course dummyCourse = new Course();
        dummyCourse.setCourseCode("CST8101");
        dummyCourse.setCourseTitle("Computer Essentials");
        dummyCourse.setYear(2022);
        dummyCourse.setSemester("WINTER");
        dummyCourse.setCreditUnits(3);
        dummyCourse.setOnline((byte) 0);

        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME)
                .request()
                .post(Entity.json(dummyCourse));
        assertThat(response.getStatus(), is(403));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test23_delete_course_by_id_admin() {
        Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME+"/3")
                .request()
                .delete();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test24_delete_course_by_id_user() {
        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME+"/3")
                .request()
                .delete();
        assertThat(response.getStatus(), is(403));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

}