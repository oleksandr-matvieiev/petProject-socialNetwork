package org.petproject.socialnetwork;

import org.petproject.socialnetwork.Exceptions.RoleNotFound;
import org.petproject.socialnetwork.Model.Role;
import org.petproject.socialnetwork.Model.RoleName;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.RoleRepository;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.superadmim.username:superadmin}")
    private String superAdminUsername;
    @Value("${app.superadmin.email:superadmin@example.com}")
    private String superAdminEmail;
    @Value("${app.superadmin.password:superpassword}")
    private String superAdminPassword;


    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName(RoleName.USER).isEmpty()) {
            Role userRole = new Role();
            userRole.setName(RoleName.USER);
            roleRepository.save(userRole);
        }

        if (roleRepository.findByName(RoleName.ADMIN).isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName(RoleName.ADMIN);
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName(RoleName.SUPER_ADMIN).isEmpty()) {
            Role superAdminRole = new Role();
            superAdminRole.setName(RoleName.SUPER_ADMIN);
            roleRepository.save(superAdminRole);
        }
        if (!userRepository.existsByUsername(superAdminUsername)) {
            User superAdmin = new User();
            superAdmin.setUsername(superAdminUsername);
            superAdmin.setEmail(superAdminEmail);
            superAdmin.setPassword(passwordEncoder.encode(superAdminPassword));

            Role superAdminRole = roleRepository.findByName(RoleName.SUPER_ADMIN)
                    .orElseThrow(() -> new RoleNotFound("Super Admin role not found"));

            superAdmin.setRoles(Set.of(superAdminRole));
            userRepository.save(superAdmin);

            System.out.println("Super Admin created: " + superAdminUsername);
        }
    }
}
