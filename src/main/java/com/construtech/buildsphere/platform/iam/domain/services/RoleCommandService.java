package com.construtech.buildsphere.platform.iam.domain.services;

import com.construtech.buildsphere.platform.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
    void handle(SeedRolesCommand command);
}