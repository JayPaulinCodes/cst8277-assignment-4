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
import acmecollege.entity.MembershipCard;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ClubMembershipSerializer extends StdSerializer<ClubMembership> implements Serializable {
    private static final long serialVersionUID = 1L;

    public ClubMembershipSerializer() {
        this(null);
    }

    public ClubMembershipSerializer(Class<ClubMembership> t) {
        super(t);
    }

    /**
     * This is to prevent back and forth serialization between many-to-many relations.<br>
     * This is done by setting the relation to null.
     */
    @Override
    public void serialize(ClubMembership originalEntity, JsonGenerator generator, SerializerProvider provider)
        throws IOException {
        ClubMembership hallowEntity = new ClubMembership();
        hallowEntity.setId(originalEntity.getId());
        hallowEntity.setVersion(originalEntity.getVersion());
        hallowEntity.setCreated(originalEntity.getCreated());
        hallowEntity.setUpdated(originalEntity.getUpdated());
        hallowEntity.setDurationAndStatus(originalEntity.getDurationAndStatus());

        generator.writeObject(hallowEntity);
    }
}