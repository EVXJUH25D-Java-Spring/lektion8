package se.deved.lektion7;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserService userService;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        try {
            User admin = (User) userService.loadUserByUsername("admin");
            userService.updatePassword(admin.getId(), adminPassword);
        } catch (UsernameNotFoundException ignored) {
            userService.createUser("admin", adminPassword, true);
        }
    }
}
