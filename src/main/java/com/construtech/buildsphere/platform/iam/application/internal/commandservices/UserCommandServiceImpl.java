package com.construtech.buildsphere.platform.iam.application.internal.commandservices;

import com.construtech.buildsphere.platform.iam.domain.model.aggregates.User;
import com.construtech.buildsphere.platform.iam.domain.model.commands.SignInCommand;
import com.construtech.buildsphere.platform.iam.domain.model.commands.SignUpCommand;
import com.construtech.buildsphere.platform.iam.domain.model.valueobjects.Roles;
import com.construtech.buildsphere.platform.iam.domain.services.UserCommandService;
import com.construtech.buildsphere.platform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.construtech.buildsphere.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserCommandServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByUsername(command.username()))
            throw new RuntimeException("Username already exists");

        var roles = command.roles();
        if (roles.isEmpty()) {
            var role = roleRepository.findByName(Roles.ROLE_USER);
            if (role.isPresent()) roles.add(role.get());
        } else roles = roles.stream().map(role -> roleRepository.findByName(role.getName())
                .orElseThrow(() -> new RuntimeException("Role not found"))).toList();
        var user = new User(command.username(), command.password(), roles);
        userRepository.save(user);
        return userRepository.findByUsername(command.username());
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByUsername(command.username())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!command.password().equals(user.getPassword()))
            throw new RuntimeException("Invalid password");
        return Optional.of(new ImmutablePair<>(user, "token"));
    }
}