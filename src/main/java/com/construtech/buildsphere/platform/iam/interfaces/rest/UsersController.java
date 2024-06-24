package com.construtech.buildsphere.platform.iam.interfaces.rest;

import com.construtech.buildsphere.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.construtech.buildsphere.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.construtech.buildsphere.platform.iam.domain.services.UserQueryService;
import com.construtech.buildsphere.platform.iam.interfaces.rest.resources.UserResource;
import com.construtech.buildsphere.platform.iam.interfaces.rest.transform.UserTransform;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User Management Endpoints")
public class UsersController {
    private final UserQueryService userQueryService;

    public UsersController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping
    public ResponseEntity<List<UserResource>> getAllUsers() {
        var getAllUsersQuery = new GetAllUsersQuery();
        var users = userQueryService.handle(getAllUsersQuery);
        var userResources = users.stream().map(UserTransform::toResourceFromEntity).toList();
        return ResponseEntity.ok(userResources);
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        var userResource = UserTransform.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    @PutMapping(value = "/{userId}")
    public ResponseEntity<UserResource> updateUser(@PathVariable Long userId, @RequestBody UserResource userResource) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        var updatedUser = user.get().update(userResource.username());
        var userResourceUpdated = UserTransform.toResourceFromEntity(updatedUser);
        return ResponseEntity.ok(userResourceUpdated);
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        user.get().delete();
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{userId}/roles")
    public ResponseEntity<UserResource> addRoleToUser(@PathVariable Long userId, @RequestBody String roleName) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        var updatedUser = user.get().addRole(roleName);
        var userResourceUpdated = UserTransform.toResourceFromEntity(updatedUser);
        return ResponseEntity.ok(userResourceUpdated);
    }

}