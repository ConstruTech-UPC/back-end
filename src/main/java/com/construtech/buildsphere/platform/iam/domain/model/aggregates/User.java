package com.construtech.buildsphere.platform.iam.domain.model.aggregates;
import com.construtech.buildsphere.platform.iam.domain.model.entities.Role;
import com.construtech.buildsphere.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
public class User extends AuditableAbstractAggregateRoot<User> {
    @Getter
    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String username;

    @Getter
    @NotBlank
    @Size(max = 120)
    private String password;

    @Getter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() { this.roles = new HashSet<>(); }

    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, List<Role> roles) {
        this(username, password);
        addRoles(roles);
    }

    public User addRoles(List<Role> roles) {
        var validatedRoleSet = Role.validateRoleSet(roles);
        this.roles.addAll(validatedRoleSet);
        return this;
    }

    public User update(String username) {
        this.username = username;
        return this;
    }

    public void delete() {
        this.username = null;
        this.password = null;
    }

    public User addRole(String roleName) {
        var role = Role.toRoleFromName(roleName);
        this.roles.add(role);
        return this;
    }
}