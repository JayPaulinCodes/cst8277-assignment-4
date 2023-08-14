/**
 * File:  SecurityRoleSerializer.java
 * Course materials (23S) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author Mike Norman
 *
 */
package acmecollege.rest.serializer;

import acmecollege.entity.ClubMembership;
import acmecollege.entity.Course;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.io.Serializable;

public class CourseSerializer extends StdSerializer<Course> implements Serializable {
    private static final long serialVersionUID = 1L;

    public CourseSerializer() {
        this(null);
    }

    public CourseSerializer(Class<Course> t) {
        super(t);
    }

    /**
     * This is to prevent back and forth serialization between many-to-many relations.<br>
     * This is done by setting the relation to null.
     */
    @Override
    public void serialize(Course originalEntity, JsonGenerator generator, SerializerProvider provider)
        throws IOException {
        Course hallowEntity = new Course();
        hallowEntity.setId(originalEntity.getId());
        hallowEntity.setVersion(originalEntity.getVersion());
        hallowEntity.setCreated(originalEntity.getCreated());
        hallowEntity.setUpdated(originalEntity.getUpdated());
        hallowEntity.setCourseCode(originalEntity.getCourseCode());
        hallowEntity.setOnline(originalEntity.getOnline());
        hallowEntity.setYear(originalEntity.getYear());
        hallowEntity.setSemester(originalEntity.getSemester());
        hallowEntity.setCourseTitle(originalEntity.getCourseTitle());
        hallowEntity.setCreditUnits(originalEntity.getCreditUnits());

        generator.writeObject(hallowEntity);
    }
}