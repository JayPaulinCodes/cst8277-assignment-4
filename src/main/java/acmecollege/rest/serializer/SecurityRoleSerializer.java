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

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import acmecollege.entity.SecurityRole;

public class SecurityRoleSerializer extends StdSerializer<Set<SecurityRole>> implements Serializable {
    private static final long serialVersionUID = 1L;

    public SecurityRoleSerializer() {
        this(null);
    }

    public SecurityRoleSerializer(Class<Set<SecurityRole>> t) {
        super(t);
    }

    /**
     * This is to prevent back and forth serialization between many-to-many relations.<br>
     * This is done by setting the relation to null.
     */
    @Override
    public void serialize(Set<SecurityRole> originalRoles, JsonGenerator generator, SerializerProvider provider)
        throws IOException {
        
        Set<SecurityRole> hollowRoles = new HashSet<>();
        for (SecurityRole originalRole : originalRoles) {
            // Create a 'hollow' copy of the original Security Roles entity
            SecurityRole hollowP = new SecurityRole();
            hollowP.setId(originalRole.getId());
            hollowP.setRoleName(originalRole.getRoleName());
            hollowP.setUsers(null);
            hollowRoles.add(hollowP);
        }
        generator.writeObject(hollowRoles);
    }
}