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

import acmecollege.entity.Course;
import acmecollege.entity.Professor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.io.Serializable;

public class ProfessorSerializer extends StdSerializer<Professor> implements Serializable {
    private static final long serialVersionUID = 1L;

    public ProfessorSerializer() {
        this(null);
    }

    public ProfessorSerializer(Class<Professor> t) {
        super(t);
    }

    /**
     * This is to prevent back and forth serialization between many-to-many relations.<br>
     * This is done by setting the relation to null.
     */
    @Override
    public void serialize(Professor originalEntity, JsonGenerator generator, SerializerProvider provider)
        throws IOException {
        Professor hallowEntity = new Professor();
        hallowEntity.setId(originalEntity.getId());
        hallowEntity.setVersion(originalEntity.getVersion());
        hallowEntity.setCreated(originalEntity.getCreated());
        hallowEntity.setUpdated(originalEntity.getUpdated());
        hallowEntity.setFirstName(originalEntity.getFirstName());
        hallowEntity.setLastName(originalEntity.getLastName());
        hallowEntity.setDepartment(originalEntity.getDepartment());
        hallowEntity.setHighestEducationalDegree(originalEntity.getHighestEducationalDegree());
        hallowEntity.setSpecialization(originalEntity.getSpecialization());

        generator.writeObject(hallowEntity);
    }
}