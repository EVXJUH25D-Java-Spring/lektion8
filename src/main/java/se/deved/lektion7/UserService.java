package se.deved.lektion7;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

// Detta är en Service - en klass som innehåller affärslogik
// @Service säger till Spring att detta är en service-klass
// @RequiredArgsConstructor skapar automatiskt en konstruktor som tar in alla "final" fält
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    // Dessa tre "beroenden" injiceras automatiskt av Spring
    private final IUserRepository userRepository;  // För att prata med databasen
    private final PasswordEncoder passwordEncoder; // För att kryptera lösenord
    private final JWTService jwtService;          // För att skapa JWT-tokens

    // Skapar en ny användare i databasen
    public User createUser(String username, String password, boolean admin) {
        // Kryptera lösenordet innan vi sparar det (säkerhet!)
        User user = new User(username, passwordEncoder.encode(password), admin);
        // Spara användaren i databasen och returnera den
        return userRepository.save(user);
    }

    public void updatePassword(UUID userId, String updatedPassword) {
        User user = userRepository.findById(userId).get();

        user.setPasswordHash(passwordEncoder.encode(updatedPassword));
        userRepository.save(user);
    }

    // Loggar in en användare och returnerar en JWT-token
    // Returnerar null om inloggningen misslyckas
    public String login(String username, String password) {
        // Försök hitta användaren i databasen
        Optional<User> optional = userRepository.findByUsername(username);
        if (optional.isEmpty()) {
            // Användaren finns inte
            return null;
        }

        User user = optional.get();

        // Kontrollera om lösenordet är rätt
        // passwordEncoder.matches() jämför det inskrivna lösenordet med det krypterade
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            // Fel lösenord
            return null;
        }

        // Inloggningen lyckades! Skapa och returnera en JWT-token
        return jwtService.generateToken(user.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username '" + username + "' not found"));
    }
}
