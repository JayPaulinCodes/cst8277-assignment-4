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

public class MembershipCardSerializer extends StdSerializer<MembershipCard> implements Serializable {
    private static final long serialVersionUID = 1L;

    public MembershipCardSerializer() {
        this(null);
    }

    public MembershipCardSerializer(Class<MembershipCard> t) {
        super(t);
    }

    /**
     * This is to prevent back and forth serialization between many-to-many relations.<br>
     * This is done by setting the relation to null.
     */
    @Override
    public void serialize(MembershipCard originalEntity, JsonGenerator generator, SerializerProvider provider)
        throws IOException {
        MembershipCard hallowEntity = new MembershipCard();
        hallowEntity.setId(originalEntity.getId());
        hallowEntity.setVersion(originalEntity.getVersion());
        hallowEntity.setCreated(originalEntity.getCreated());
        hallowEntity.setUpdated(originalEntity.getUpdated());
        hallowEntity.setOwner(originalEntity.getOwner());
        hallowEntity.setSigned(originalEntity.getSigned() == 1);

        generator.writeObject(hallowEntity);
    }
}