/**************************************************************************************************
 * File:  MyConstants.java
 * Course materials (23S) CST 8277
 * @author Teddy Yap
 * @author Mike Norman
 *
 */
package acmecollege.utility;

/**
 * <p>
 * This class holds various constants used by this app's artifacts
 * <p>
 * The key idea here is that often an annotation contains String-based parameters that <b><u>must be an exact match</u></b> <br/>
 * to a string used elsewhere.  Use of this type of 'contants' Interface class prevents errors such as:
<blockquote><pre>
{@literal @}GET
{@literal @}Path("{<b><u>emailID</u></b>}/project")  // accidently capitalized <b><u>ID</u></b>, instead of camel-case <b><u>Id</u></b>
public List<Project> getProjects({@literal @}PathParam("<b><u>emailId</u></b>") String emailId) ...  // path parameter does not match annotation
</pre></blockquote>
 *
 * @author Shariar (Shawn) Emami
 * @author mwnorman (original)
 */
public interface MyConstants {

    // Constants on Interfaces are 'public static final' by default,
    // but I leave 'em in case I move a constant to a class

    //REST constants
    public static final String APPLICATION_API_VERSION = "/api/v1";
    public static final String SLASH = "/";
    public static final String REST_APPLICATION_PATH = SLASH + "api" + SLASH + "v1";
    public static final String APPLICATION_CONTEXT_ROOT = SLASH + "rest-acmecollege";
    public static final String RESOURCE_PATH_ID_ELEMENT =  "id";
    public static final String RESOURCE_PATH_ID_PATH =  "/{" + RESOURCE_PATH_ID_ELEMENT + "}";
    public static final String CREDENTIAL_RESOURCE_NAME = "credential";
    public static final String STUDENT_RESOURCE_NAME =  "student";
    public static final String COURSE_RESOURCE_NAME = "course";
    public static final String MEMBERSHIP_CARD_RESOURCE_NAME = "membershipcard";
    public static final String COURSE_REGISTRATION_RESOURCE_NAME = "courseregistration";
    public static final String STUDENT_CLUB_RESOURCE_NAME =  "studentclub";
    public static final String CLUB_MEMBERSHIP_RESOURCE_NAME = "clubmembership";
    public static final String PROFESSOR_SUBRESOURCE_NAME =  "professor";
    public static final String COURSE_PROFESSOR_RESOURCE_PATH =
        RESOURCE_PATH_ID_PATH + SLASH + PROFESSOR_SUBRESOURCE_NAME;
    public static final String STUDENT_COURSE_PROFESSOR_RESOURCE_PATH = "/{studentId}/course/{courseId}/professor";

    //Security constants
    public static final String USER_ROLE = "USER_ROLE";
    public static final String ADMIN_ROLE = "ADMIN_ROLE";
    public static final String ACCESS_REQUIRES_AUTHENTICATION =
        "Access requires authentication";
    public static final String ACCESS_TO_THE_SPECIFIED_RESOURCE_HAS_BEEN_FORBIDDEN =
        "Access to the specified resource has been forbidden";
    //Eclipse MicroProfile Config - externalise configuration:  default in META-INF/microprofile-config.properties
    public static final String DEFAULT_ADMIN_USER_PROPNAME = "default-admin-user";
    public static final String DEFAULT_ADMIN_USER = "admin";
    public static final String DEFAULT_ADMIN_USER_PASSWORD_PROPNAME = "default-admin-user-password";
    public static final String DEFAULT_ADMIN_USER_PASSWORD = "admin";
    public static final String DEFAULT_USER = "cst8277";
    public static final String DEFAULT_USER_PASSWORD = "8277";
    public static final String DEFAULT_USER_PREFIX = "user";

    // The nickname of this hash algorithm is 'PBandJ' (Peanut-Butter-And-Jam, like the sandwich!)
    // I would like to use the constants from org.glassfish.soteria.identitystores.hash.Pbkdf2PasswordHashImpl
    // but they are not visible, so type in them all over again :-( Hope there are no typos!
    public static final String PROPERTY_ALGORITHM  = "Pbkdf2PasswordHash.Algorithm";
    public static final String DEFAULT_PROPERTY_ALGORITHM  = "PBKDF2WithHmacSHA256";
    public static final String PROPERTY_ITERATIONS = "Pbkdf2PasswordHash.Iterations";
    public static final String DEFAULT_PROPERTY_ITERATIONS = "2048";
    public static final String PROPERTY_SALT_SIZE = "Pbkdf2PasswordHash.SaltSizeBytes";
    public static final String DEFAULT_SALT_SIZE = "32";
    public static final String PROPERTY_KEY_SIZE = "Pbkdf2PasswordHash.KeySizeBytes";
    public static final String DEFAULT_KEY_SIZE = "32";

    //JPA constants
    public static final String PU_NAME = "acmecollege-PU";
    public static final String PARAM1 = "param1";

}
