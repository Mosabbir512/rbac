package com.example.mosabbir.config;

import com.example.mosabbir.entity.Permission;
import com.example.mosabbir.entity.Role;
import com.example.mosabbir.entity.User;
import com.example.mosabbir.repository.PermissionRepository;
import com.example.mosabbir.repository.RoleRepository;
import com.example.mosabbir.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Collections;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(" INITIALIZING RBAC SYSTEM...");

        // Create permissions
        createPermissions();

        // Create roles with permissions
        createRolesWithPermissions();

        // Create default users
        createDefaultUsers();

        System.out.println(" RBAC INITIALIZATION COMPLETED");
    }

    private void createPermissions() {
        String[][] permissionsData = {
                {"PRODUCT_READ", "Read product information"},
                {"PRODUCT_WRITE", "Create new products"},
                {"PRODUCT_UPDATE", "Update existing products"},
                {"PRODUCT_DELETE", "Delete products"}
        };

        for (String[] permissionData : permissionsData) {
            createPermissionIfNotFound(permissionData[0], permissionData[1]);
        }
    }

    private void createRolesWithPermissions() {
        // ROLE_USER - Only read permission
        Role userRole = createRoleIfNotFound("ROLE_USER");
        assignPermissionsToRole(userRole, new String[]{"PRODUCT_READ"});

        // ROLE_ADMIN - All permissions
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        assignPermissionsToRole(adminRole, new String[]{"PRODUCT_READ", "PRODUCT_WRITE", "PRODUCT_UPDATE", "PRODUCT_DELETE"});
    }

    private void createDefaultUsers() {
        // Create admin user
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("System Administrator");
            admin.setAge(30);
            admin.setPhoneNumber("+1234567890");
            admin.setEnabled(true);

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));
            admin.setRoles(Collections.singleton(adminRole));

            userRepository.save(admin);
            System.out.println(" Default admin user created: admin/admin123");
        }

        // Create regular user
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setName("Regular User");
            user.setAge(25);
            user.setPhoneNumber("+1234567891");
            user.setEnabled(true);

            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
            user.setRoles(Collections.singleton(userRole));

            userRepository.save(user);
            System.out.println(" Default regular user created: user/user123");
        }
    }

    private Permission createPermissionIfNotFound(String name, String description) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> {
                    Permission permission = new Permission();
                    permission.setName(name);
                    permission.setDescription(description);
                    Permission saved = permissionRepository.save(permission);
                    System.out.println(" Created permission: " + name);
                    return saved;
                });
    }

    private Role createRoleIfNotFound(String name) {
        return roleRepository.findByName(name).orElseGet(() -> {
            Role role = new Role();
            role.setName(name);
            Role saved = roleRepository.save(role);
            System.out.println(" Created role: " + name);
            return saved;
        });
    }

    private void assignPermissionsToRole(Role role, String[] permissionNames) {
        // Get fresh role from database to avoid detached entity issues
        Role freshRole = roleRepository.findById(role.getId()).orElse(role);
        freshRole.getPermissions().clear();

        for (String permissionName : permissionNames) {
            Permission permission = permissionRepository.findByName(permissionName)
                    .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionName));
            freshRole.getPermissions().add(permission);
        }

        roleRepository.save(freshRole);
        System.out.println(" Assigned permissions to " + freshRole.getName() + ": " + Arrays.toString(permissionNames));
    }
}