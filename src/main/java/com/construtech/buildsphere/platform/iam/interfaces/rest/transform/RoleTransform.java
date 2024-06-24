package com.construtech.buildsphere.platform.iam.interfaces.rest.transform;
import com.construtech.buildsphere.platform.iam.domain.model.entities.Role;
import com.construtech.buildsphere.platform.iam.interfaces.rest.resources.RoleResource;
public class RoleTransform {
    public static RoleResource toResourceFromEntity(Role entity) {
        return new RoleResource(entity.getId(), entity.getStringName());
    }
}
