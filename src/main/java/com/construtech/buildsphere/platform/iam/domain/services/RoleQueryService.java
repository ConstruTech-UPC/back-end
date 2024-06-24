package com.construtech.buildsphere.platform.iam.domain.services;

import com.construtech.buildsphere.platform.iam.domain.model.entities.Role;
import com.construtech.buildsphere.platform.iam.domain.model.queries.GetAllRolesQuery;
import com.construtech.buildsphere.platform.iam.domain.model.queries.GetRoleByIdQuery;

import java.util.List;
import java.util.Optional;

public interface RoleQueryService {
    List<Role> handle(GetAllRolesQuery query);
    Optional<Role> handle(GetRoleByIdQuery query);


}