package boot_security.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import boot_security.models.Role;
import boot_security.models.User;
import boot_security.repositories.RoleRepository;
import boot_security.services.UserService;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final UserService userService;
    private final RoleRepository roleRepository;
    
    @Autowired
    public DataInitializer(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        if (roleAdmin == null) {
            roleAdmin = new Role("ROLE_ADMIN");
            roleRepository.save(roleAdmin);
        }
        
        Role roleUser = roleRepository.findByName("ROLE_USER");
        if (roleUser == null) {
            roleUser = new Role("ROLE_USER");
            roleRepository.save(roleUser);
        }

        // Create or update admin user
        User admin = userService.findByUsername("admin");
        if (admin == null) {
            admin = new User("admin", "admin", "admin", "admin", "admin@mail.ru", 35);
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleAdmin);
            adminRoles.add(roleUser);
            admin.setRoles(adminRoles);
            userService.saveUser(admin);
        } else if (admin.getAge() == null) {
            // Update existing admin with age
            admin.setAge(35);
            userService.updateUser(admin);
        }

        // Create or update user
        User user = userService.findByUsername("user");
        if (user == null) {
            user = new User("user", "user", "user", "user", "user@mail.ru", 30);
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(roleUser);
            user.setRoles(userRoles);
            userService.saveUser(user);
        } else if (user.getAge() == null) {
            // Update existing user with age
            user.setAge(30);
            userService.updateUser(user);
        }
    }
}
