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
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import acmecollege.entity.*;
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

    public WebTarget newTarget() {
        Client client = ClientBuilder.newClient(
                new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        return client.target(uri);
    }

    public <T> int getSize(Class<T> entity, String path) {
        try (Response response = newTarget()
                .register(adminAuth)
                .path(path)
                .request()
                .get()) {
            List<T> results = response.readEntity(new GenericType<List<T>>(){});
            return results.size();
        }
    }

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
    public void setUp() throws InterruptedException {
        Client client = ClientBuilder.newClient(
                new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
//        Thread.sleep(250);
    }

//    @Test
    public void test_01_all_students_with_adminrole() throws JsonMappingException, JsonProcessingException {
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



    /////////////////////////////////////////////
    // ----------[ Student | Start ]---------- //
    /////////////////////////////////////////////
    @Test
    public void test00_get_all_students_admin() {
        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");

        List<Student> students = response.readEntity(new GenericType<List<Student>>(){});
        assertThat(students, is(not(empty())));
        assertThat(students, hasSize(1));
    }

    @Test
    public void test01_get_all_students_user() {
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
    public void test02_get_student_by_id_admin() {
        Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME+"/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        Student student = response.readEntity(new GenericType<Student>(){});
        assertThat(student, is(not(student.getFirstName().isEmpty())));
        assertThat(student, is(not(student.getLastName().isEmpty())));
    }

    @Test
    public void test03_get_other_student_by_id_user() {
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
    public void test04_get_own_student_by_id_user() {
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
    public void test05_post_new_student_admin() {
        Student dummyStudent = new Student();
        dummyStudent.setFirstName("Michael");
        dummyStudent.setLastName("Smith");
        int sizeBefore = getSize(Student.class, STUDENT_RESOURCE_NAME);

        try (Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .post(Entity.json(dummyStudent))) {
            assertThat(response.getStatus(), is(200));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(Student.class, STUDENT_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore + 1));
    }

    @Test
    public void test06_post_duplicate_student_admin() {
        Student dummyStudent = new Student();
        dummyStudent.setFirstName("Michael");
        dummyStudent.setLastName("Smith");
        int sizeBefore = getSize(Student.class, STUDENT_RESOURCE_NAME);

        try (Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .post(Entity.json(dummyStudent))) {
            assertThat(response.getStatus(), is(200));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(Student.class, STUDENT_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore + 1));
    }

    @Test
    public void test07_post_new_student_user() {
        Student dummyStudent = new Student();
        dummyStudent.setFirstName("Michael");
        dummyStudent.setLastName("Smith");
        int sizeBefore = getSize(Student.class, STUDENT_RESOURCE_NAME);

        try (Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .post(Entity.json(dummyStudent))) {
            assertThat(response.getStatus(), is(403));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(Student.class, STUDENT_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore));
    }

    @Test
    public void test08_delete_student_by_id_admin() {
        int sizeBefore = getSize(Student.class, STUDENT_RESOURCE_NAME);

        try (Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME + "/2")
                .request()
                .delete()) {
            assertThat(response.getStatus(), is(200));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(Student.class, STUDENT_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore - 1));
    }

    @Test
    public void test09_delete_student_by_id_user() {
        int sizeBefore = getSize(Student.class, STUDENT_RESOURCE_NAME);

        try (Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME + "/2")
                .request()
                .delete()) {
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
            assertThat(response.getStatus(), is(403));
        }

        int sizeAfter = getSize(Student.class, STUDENT_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore));
    }
    ///////////////////////////////////////////
    // ----------[ Student | End ]---------- //
    ///////////////////////////////////////////



    ///////////////////////////////////////////////
    // ----------[ Professor | Start ]---------- //
    ///////////////////////////////////////////////
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
        List<Professor> results = response.readEntity(new GenericType<List<Professor>>(){});
        assertThat(results, is(not(empty())));
        assertThat(results, hasSize(1));
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
        int sizeBefore = getSize(Professor.class, PROFESSOR_SUBRESOURCE_NAME);

        try (Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(PROFESSOR_SUBRESOURCE_NAME)
                .request()
                .post(Entity.json(dummyProfessor))) {
            assertThat(response.getStatus(), is(200));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(Professor.class, PROFESSOR_SUBRESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore + 1));
    }

    @Test
    public void test15_post_new_professor_user() {
        Professor dummyProfessor = new Professor();
        dummyProfessor.setFirstName("Charles");
        dummyProfessor.setLastName("Xavier");
        dummyProfessor.setDepartment("Physics");
        int sizeBefore = getSize(Professor.class, PROFESSOR_SUBRESOURCE_NAME);

        try (Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(PROFESSOR_SUBRESOURCE_NAME)
                .request()
                .post(Entity.json(dummyProfessor))) {
            assertThat(response.getStatus(), is(403));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(Professor.class, PROFESSOR_SUBRESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore));
    }

    @Test
    public void test16_delete_professor_by_id_admin() {
        int sizeBefore = getSize(Professor.class, PROFESSOR_SUBRESOURCE_NAME);

        try (Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(PROFESSOR_SUBRESOURCE_NAME + "/2")
                .request()
                .delete()) {
            assertThat(response.getStatus(), is(200));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(Professor.class, PROFESSOR_SUBRESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore - 1));
    }

    @Test
    public void test17_delete_professor_by_id_user() {
        int sizeBefore = getSize(Professor.class, PROFESSOR_SUBRESOURCE_NAME);

        try (Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(PROFESSOR_SUBRESOURCE_NAME + "/2")
                .request()
                .delete()) {
            assertThat(response.getStatus(), is(403));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(Professor.class, PROFESSOR_SUBRESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore));
    }
    /////////////////////////////////////////////
    // ----------[ Professor | End ]---------- //
    /////////////////////////////////////////////



    ////////////////////////////////////////////
    // ----------[ Course | Start ]---------- //
    ////////////////////////////////////////////
    @Test
    public void test18_get_all_courses_admin() {
        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        List<Course> results = response.readEntity(new GenericType<List<Course>>(){});
        assertThat(results, is(not(empty())));
        assertThat(results, hasSize(2));
    }

    @Test
    public void test19_get_all_courses_user() {
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
    public void test20_get_course_by_id_admin() {
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
    public void test21_get_course_by_id_user() {
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
    public void test22_post_new_course_admin() {
        int sizeBefore = getSize(Course.class, COURSE_RESOURCE_NAME);

        Course dummyCourse = new Course();
        dummyCourse.setCourseCode("CST8101");
        dummyCourse.setCourseTitle("Computer Essentials");
        dummyCourse.setYear(2022);
        dummyCourse.setSemester("WINTER");
        dummyCourse.setCreditUnits(3);
        dummyCourse.setOnline((byte) 0);

        try (Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME)
                .request()
                .post(Entity.json(dummyCourse))) {
            assertThat(response.getStatus(), is(200));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(Course.class, COURSE_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore + 1));
    }

    @Test
    public void test23_post_new_course_user() {
        int sizeBefore = getSize(Course.class, COURSE_RESOURCE_NAME);

        Course dummyCourse = new Course();
        dummyCourse.setCourseCode("CST8101");
        dummyCourse.setCourseTitle("Computer Essentials");
        dummyCourse.setYear(2022);
        dummyCourse.setSemester("WINTER");
        dummyCourse.setCreditUnits(3);
        dummyCourse.setOnline((byte) 0);

        try (Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME)
                .request()
                .post(Entity.json(dummyCourse))) {
            assertThat(response.getStatus(), is(403));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(Course.class, COURSE_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore));
    }

    @Test
    public void test24_delete_course_by_id_admin() {
        int sizeBefore = getSize(Course.class, COURSE_RESOURCE_NAME);

        try (Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME + "/3")
                .request()
                .delete()) {
            assertThat(response.getStatus(), is(200));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(Course.class, COURSE_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore - 1));
    }

    @Test
    public void test25_delete_course_by_id_user() {
        int sizeBefore = getSize(Course.class, COURSE_RESOURCE_NAME);

        try (Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME + "/3")
                .request()
                .delete()) {
            assertThat(response.getStatus(), is(403));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(Course.class, COURSE_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore));
    }
    //////////////////////////////////////////
    // ----------[ Course | End ]---------- //
    //////////////////////////////////////////



    /////////////////////////////////////////////////
    // ----------[ StudentClub | Start ]---------- //
    /////////////////////////////////////////////////
    @Test
    public void test26_get_all_student_clubs_admin() {
        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        List<StudentClub> results = response.readEntity(new GenericType<List<StudentClub>>(){});
        assertThat(results, is(not(empty())));
        assertThat(results, hasSize(2));
    }

    @Test
    public void test27_get_all_student_clubs_user() {
        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        List<StudentClub> results = response.readEntity(new GenericType<List<StudentClub>>(){});
        assertThat(results, is(not(empty())));
        assertThat(results, hasSize(2));
    }

    @Test
    public void test28_get_student_club_by_id_admin() {
        Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME+"/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        StudentClub result = response.readEntity(new GenericType<StudentClub>(){});
        assertThat(result, is(not(result.getName().isEmpty())));
    }

    @Test
    public void test29_get_student_club_by_id_user() {
        Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME+"/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        StudentClub result = response.readEntity(new GenericType<StudentClub>(){});
        assertThat(result, is(not(result.getName().isEmpty())));
    }

    @Test
    public void test30_post_new_academic_student_club_admin() {
        int sizeBefore = getSize(StudentClub.class, STUDENT_CLUB_RESOURCE_NAME);

        StudentClub studentClub = new AcademicStudentClub();
        studentClub.setName("New Student Club");

        try (Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME)
                .request()
                .post(Entity.json(studentClub))) {
            assertThat(response.getStatus(), is(200));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(StudentClub.class, STUDENT_CLUB_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore + 1));
    }

    @Test
    public void test31_post_duplicate_academic_student_club_admin() {
        int sizeBefore = getSize(StudentClub.class, STUDENT_CLUB_RESOURCE_NAME);

        StudentClub studentClub = new AcademicStudentClub();
        studentClub.setName("New Student Club");

        try (Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME)
                .request()
                .post(Entity.json(studentClub))) {
            assertThat(response.getStatus(), is(409));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(StudentClub.class, STUDENT_CLUB_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore));
    }

    @Test
    public void test32_post_new_non_academic_student_club_admin() {
        int sizeBefore = getSize(StudentClub.class, STUDENT_CLUB_RESOURCE_NAME);

        StudentClub studentClub = new NonAcademicStudentClub();
        studentClub.setName("Better Student Club");

        try (Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME)
                .request()
                .post(Entity.json(studentClub))) {
            assertThat(response.getStatus(), is(200));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(StudentClub.class, STUDENT_CLUB_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore + 1));
    }

    @Test
    public void test33_post_duplicate_non_academic_student_club_admin() {
        int sizeBefore = getSize(StudentClub.class, STUDENT_CLUB_RESOURCE_NAME);

        StudentClub studentClub = new NonAcademicStudentClub();
        studentClub.setName("Better Student Club");

        try (Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME)
                .request()
                .post(Entity.json(studentClub))) {
            assertThat(response.getStatus(), is(409));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(StudentClub.class, STUDENT_CLUB_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore));
    }

    @Test
    public void test34_post_new_academic_student_club_user() {
        int sizeBefore = getSize(StudentClub.class, STUDENT_CLUB_RESOURCE_NAME);

        StudentClub studentClub = new AcademicStudentClub();
        studentClub.setName("New Student Club");

        try (Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME)
                .request()
                .post(Entity.json(studentClub))) {
            assertThat(response.getStatus(), is(403));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(StudentClub.class, STUDENT_CLUB_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore));
    }

    @Test
    public void test35_post_new_non_academic_student_club_user() {
        int sizeBefore = getSize(StudentClub.class, STUDENT_CLUB_RESOURCE_NAME);

        StudentClub studentClub = new NonAcademicStudentClub();
        studentClub.setName("Better Student Club");

        try (Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME)
                .request()
                .post(Entity.json(studentClub))) {
            assertThat(response.getStatus(), is(403));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(StudentClub.class, STUDENT_CLUB_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore));
    }

    @Test
    public void test36_delete_student_club_by_id_admin() {
        int sizeBefore = getSize(StudentClub.class, STUDENT_CLUB_RESOURCE_NAME);

        try (Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME + "/4")
                .request()
                .delete()) {
            assertThat(response.getStatus(), is(200));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(StudentClub.class, STUDENT_CLUB_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore - 1));
    }

    @Test
    public void test37_delete_student_club_by_id_user() {
        int sizeBefore = getSize(StudentClub.class, STUDENT_CLUB_RESOURCE_NAME);

        try (Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME + "/4")
                .request()
                .delete()) {
            assertThat(response.getStatus(), is(403));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(StudentClub.class, STUDENT_CLUB_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore));
    }
    ///////////////////////////////////////////////
    // ----------[ StudentClub | End ]---------- //
    ///////////////////////////////////////////////



    ////////////////////////////////////////////////////
    // ----------[ MembershipCard | Start ]---------- //
    ////////////////////////////////////////////////////
    @Test
    public void test38_get_all_membership_cards_admin() {
        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path("membershipcard")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        List<MembershipCard> results = response.readEntity(new GenericType<List<MembershipCard>>(){});
        assertThat(results, is(not(empty())));
        assertThat(results, hasSize(2));
    }

    @Test
    public void test39_get_all_membership_cards_user() {
        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path("membershipcard")
                .request()
                .get();
        assertThat(response.getStatus(), is(403));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test40_get_membership_card_by_id_admin() {
        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path("membershipcard/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test41_get_own_membership_card_by_id_user() {
        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path("membershipcard/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
    }

    @Test
    public void test42_get_other_membership_card_by_id_user() {
        // Create dummy card for test sake
        MembershipCard dummyCard = new MembershipCard();
        dummyCard.setSigned(true);
        newTarget().register(adminAuth).path("membershipcard/3").request().post(Entity.json(dummyCard));

        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path("membershipcard/3")
                .request()
                .get();
        assertThat(response.getStatus(), is(403));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");

    }

    @Test
    public void test43_post_new_membership_card_admin() {
        int sizeBefore = getSize(MembershipCard.class, "membershipcard");

        MembershipCard dummyCard = new MembershipCard();
        dummyCard.setSigned(true);

        try (Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path("membershipcard/1")
                .request()
                .post(Entity.json(dummyCard))) {
            assertThat(response.getStatus(), is(200));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(MembershipCard.class, "membershipcard");
        assertThat(sizeAfter, is(sizeBefore + 1));
    }

    @Test
    public void test44_post_new_membership_card_user() {
        int sizeBefore = getSize(MembershipCard.class, "membershipcard");

        MembershipCard dummyCard = new MembershipCard();
        dummyCard.setSigned(true);

        try (Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path("membershipcard/1")
                .request()
                .post(Entity.json(dummyCard))) {
            assertThat(response.getStatus(), is(403));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(MembershipCard.class, "membershipcard");
        assertThat(sizeAfter, is(sizeBefore));
    }

    @Test
    public void test45_delete_membership_card_by_id_admin() {
        int sizeBefore = getSize(MembershipCard.class, "membershipcard");

        try (Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path("membershipcard/4")
                .request()
                .delete()) {
            assertThat(response.getStatus(), is(200));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(MembershipCard.class, "membershipcard");
        assertThat(sizeAfter, is(sizeBefore - 1));
    }

    @Test
    public void test46_delete_membership_card_by_id_user() {
        int sizeBefore = getSize(MembershipCard.class, "membershipcard");

        try (Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path("membershipcard/5")
                .request()
                .delete()) {
            assertThat(response.getStatus(), is(403));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(MembershipCard.class, "membershipcard");
        assertThat(sizeAfter, is(sizeBefore));
    }
    //////////////////////////////////////////////////
    // ----------[ MembershipCard | End ]---------- //
    //////////////////////////////////////////////////



    ////////////////////////////////////////////////////
    // ----------[ ClubMembership | Start ]---------- //
    ////////////////////////////////////////////////////
    @Test
    public void test47_get_all_club_memberships_admin() {
        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path(CLUB_MEMBERSHIP_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        List<ClubMembership> results = response.readEntity(new GenericType<List<ClubMembership>>(){});
        assertThat(results, is(not(empty())));
        assertThat(results, hasSize(2));
    }

    @Test
    public void test48_get_all_club_memberships_user() {
        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(CLUB_MEMBERSHIP_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        List<ClubMembership> results = response.readEntity(new GenericType<List<ClubMembership>>(){});
        assertThat(results, is(not(empty())));
        assertThat(results, hasSize(2));
    }

    @Test
    public void test49_get_club_memberships_by_id_admin() {
        Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(CLUB_MEMBERSHIP_RESOURCE_NAME+"/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        ClubMembership result = response.readEntity(new GenericType<ClubMembership>(){});
        assertThat(result, is(not(result.getId() > 0)));
    }

    @Test
    public void test50_get_club_memberships_by_id_user() {
        Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(CLUB_MEMBERSHIP_RESOURCE_NAME+"/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        ClubMembership result = response.readEntity(new GenericType<ClubMembership>(){});
        assertThat(result, is(not(result.getId() > 0)));
    }

    @Test
    public void test51_post_new_club_membership_admin() {
        int sizeBefore = getSize(ClubMembership.class, CLUB_MEMBERSHIP_RESOURCE_NAME);

        DurationAndStatus dur = new DurationAndStatus();
        dur.setActive((byte) 0);
        dur.setStartDate(LocalDateTime.parse("2023-08-01T10:00:00"));
        dur.setEndDate(LocalDateTime.parse("2023-10-01T10:00:00"));

        ClubMembership newData = new ClubMembership();
        newData.setDurationAndStatus(dur);

        try (Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(CLUB_MEMBERSHIP_RESOURCE_NAME+"/3/1")
                .request()
                .post(Entity.json(newData))) {
            assertThat(response.getStatus(), is(200));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(ClubMembership.class, CLUB_MEMBERSHIP_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore + 1));
    }

    @Test
    public void test52_post_new_club_membership_user() {
        int sizeBefore = getSize(ClubMembership.class, CLUB_MEMBERSHIP_RESOURCE_NAME);

        DurationAndStatus dur = new DurationAndStatus();
        dur.setActive((byte) 0);
        dur.setStartDate(LocalDateTime.parse("2023-08-01T10:00:00"));
        dur.setEndDate(LocalDateTime.parse("2023-10-01T10:00:00"));

        ClubMembership newData = new ClubMembership();
        newData.setDurationAndStatus(dur);

        try (Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(CLUB_MEMBERSHIP_RESOURCE_NAME+"/3/1")
                .request()
                .post(Entity.json(newData))) {
            assertThat(response.getStatus(), is(403));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(ClubMembership.class, CLUB_MEMBERSHIP_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore));
    }

    @Test
    public void test53_delete_club_membership_by_id_admin() {
        int sizeBefore = getSize(ClubMembership.class, CLUB_MEMBERSHIP_RESOURCE_NAME);

        try (Response response = webTarget
//                .register(userAuth)
                .register(adminAuth)
                .path(CLUB_MEMBERSHIP_RESOURCE_NAME+"/3")
                .request()
                .delete()) {
            assertThat(response.getStatus(), is(200));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(ClubMembership.class, CLUB_MEMBERSHIP_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore - 1));
    }

    @Test
    public void test54_delete_club_membership_by_id_user() {
        int sizeBefore = getSize(ClubMembership.class, CLUB_MEMBERSHIP_RESOURCE_NAME);

        try (Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(CLUB_MEMBERSHIP_RESOURCE_NAME+"/3")
                .request()
                .delete()) {
            assertThat(response.getStatus(), is(403));
            System.out.println("Status: " + response.getStatus() + " (" + response.getStatusInfo() + ")");
        }

        int sizeAfter = getSize(ClubMembership.class, CLUB_MEMBERSHIP_RESOURCE_NAME);
        assertThat(sizeAfter, is(sizeBefore));
    }
    //////////////////////////////////////////////////
    // ----------[ ClubMembership | End ]---------- //
    //////////////////////////////////////////////////

}